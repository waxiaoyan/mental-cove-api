package com.mental.cove.service;

import com.mental.cove.auth.SecurityService;
import com.mental.cove.constant.PromptConstant;
import com.mental.cove.data.ChatGPTMessage;
import com.mental.cove.data.ChatGPTRequest;
import com.mental.cove.data.ChatGPTResponse;
import com.mental.cove.entity.DreamInterpretation;
import com.mental.cove.entity.User;
import com.mental.cove.exception.CustomBusinessException;
import com.mental.cove.repository.DreamInterpretationRepository;
import com.mental.cove.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OpenAIService {
    @Resource
    private SecurityService securityService;
    @Resource
    private DreamInterpretationRepository dreamInterpretationRepository;
    @Resource
    private UserRepository userRepository;

    private final String model = "gpt-4o";

//    @Value("${openai.api.url}")
    private String openAIUrl;

    @Value("${deepseek.api.url}")
    private String deepSeekUrl;

    @Value("${dream_interpretation_limited_enable}")
    private boolean enableDreamInterpretationLimited;

//    private final RestTemplate openAIRestTemplate;
    private final RestTemplate deepSeekRestTemplate;

    @Resource
    private GeminiService geminiService;

//    public OpenAIService(@Qualifier("openAIRestTemplate") RestTemplate openAIRestTemplate,
//                         @Qualifier("deepSeekRestTemplate") RestTemplate deepSeekRestTemplate) {
//        this.openAIRestTemplate = openAIRestTemplate;
//        this.deepSeekRestTemplate = deepSeekRestTemplate;
//    }

    public OpenAIService(@Qualifier("deepSeekRestTemplate") RestTemplate deepSeekRestTemplate) {
        this.deepSeekRestTemplate = deepSeekRestTemplate;
    }

    public int getInterpretationCount() {
        String openId = securityService.getCurrentUserOpenId();
        return dreamInterpretationRepository.countByUserOpenIdAndCreatedTimeToday(openId);
    }

    public String chat(String dreamContent) {
        String openId = securityService.getCurrentUserOpenId();
        User user = userRepository.findByOpenId(openId);
        int totalInterpretations = dreamInterpretationRepository.countByUserOpenIdAndCreatedTimeToday(openId);
        if(enableDreamInterpretationLimited && totalInterpretations > 1) {
//            List<DreamInterpretation> results = dreamInterpretationRepository.findLatestByUserAndToday(user);
//            DreamInterpretation dreamInterpretation = results.isEmpty() ? null : results.get(0);
//            return dreamInterpretation.getInterpretation();
            throw new CustomBusinessException("You have reached the daily limit of dream interpretations");
        }
        String content = tryDeepSeekChat(dreamContent);
//        if (content == null) {
//            content = tryOpenAIChat(dreamContent);
//        }
//        if (content == null) {
//            content = tryGeminiChat(dreamContent);
//        }
        if (content == null) {
            throw new CustomBusinessException("All chat services failed");
        }
        DreamInterpretation dreamInterpretation = DreamInterpretation.builder()
                .user(user)
                .dreamContent(dreamContent)
                .interpretation(content)
                .ip(securityService.getCurrentRequestUserIp())
                .build();
        dreamInterpretationRepository.save(dreamInterpretation);
        return content;
    }
//    private String tryOpenAIChat(String dreamContent) {
//        try {
//            return openAIChat(dreamContent);
//        } catch (Exception e) {
//            log.error("OpenAI chat error: {0}", e);
//            return null;
//        }
//    }

    private String tryDeepSeekChat(String dreamContent) {
        try {
            return deepSeekChat(dreamContent);
        } catch (Exception e) {
            log.error("DeepSeek chat error: {0}", e);
            return null;
        }
    }

    private String tryGeminiChat(String dreamContent) {
        try {
            return geminiService.chat(dreamContent);
        } catch (Exception e) {
            log.error("Gemini chat error: {0}", e);
            return null;
        }
    }

//    private String openAIChat(String dreamContent) {
//        ChatGPTMessage systemMessage = new ChatGPTMessage("system", PromptConstant.DREAM_PROMPT );
//        ChatGPTRequest chatGPTRequest = new ChatGPTRequest(model, dreamContent);
//        chatGPTRequest.getMessages().add(systemMessage);
//        chatGPTRequest.setMax_tokens(1000);
//        ChatGPTResponse chatGPTResponse = openAIRestTemplate.postForObject(openAIUrl + "/chat/completions", chatGPTRequest, ChatGPTResponse.class);
//        assert chatGPTResponse != null;
//        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
//    }

    public String deepSeekChat(String dreamConent) {
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", PromptConstant.DREAM_PROMPT);
        Map<String, String> userMessageMap = new HashMap<>();
        userMessageMap.put("role", "user");
        userMessageMap.put("content", dreamConent);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("messages", Arrays.asList(systemMessage, userMessageMap));
        requestBody.put("stream", false);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody);
        try{
            ChatGPTResponse responseEntity = deepSeekRestTemplate.postForObject(deepSeekUrl + "/chat/completions", requestEntity, ChatGPTResponse.class);
            return responseEntity.getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            log.error("DeepSeek chat error: {0}", e);
            throw new CustomBusinessException("DeepSeek chat error");
        }
    }
}
