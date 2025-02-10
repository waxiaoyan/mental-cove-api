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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

    @Value("${openai.api.url}")
    private String openAIUrl;

    @Value("${deepseek.api.url}")
    private String deepSeekUrl;

    @Value("${dream_interpretation_limited_enable}")
    private boolean enableDreamInterpretationLimited;

    private final RestTemplate openAIRestTemplate;
    private final RestTemplate deepSeekRestTemplate;

    public OpenAIService(@Qualifier("openAIRestTemplate") RestTemplate openAIRestTemplate,
                         @Qualifier("deepSeekRestTemplate") RestTemplate deepSeekRestTemplate) {
        this.openAIRestTemplate = openAIRestTemplate;
        this.deepSeekRestTemplate = deepSeekRestTemplate;
    }

    public String chat(String dreamContent) {
        String openId = securityService.getCurrentUserOpenId();
        int totalInterpretations = dreamInterpretationRepository.countByUserOpenIdAndCreatedTimeToday(openId);
        if(enableDreamInterpretationLimited && totalInterpretations > 1) {
            throw new CustomBusinessException("You have reached the daily limit of dream interpretations");
        }
        String content;
        try{
            content = openAIChat(dreamContent);
        } catch (Exception e) {
            log.error("OpenAI chat error: {0}", e);
            content = deepSeekChat(dreamContent);
        }
        User user = userRepository.findByOpenId(openId);
        DreamInterpretation dreamInterpretation = DreamInterpretation.builder()
                .user(user)
                .dreamContent(dreamContent)
                .interpretation(content)
                .ip(securityService.getCurrentRequestUserIp())
                .build();
        dreamInterpretationRepository.save(dreamInterpretation);
        return deepSeekChat(dreamContent);
    }

    private String openAIChat(String dreamContent) {
        ChatGPTMessage systemMessage = new ChatGPTMessage("system", PromptConstant.DREAM_PROMPT );
        ChatGPTRequest chatGPTRequest = new ChatGPTRequest(model, dreamContent);
        chatGPTRequest.getMessages().add(systemMessage);
        chatGPTRequest.setMax_tokens(1000);
        ChatGPTResponse chatGPTResponse = openAIRestTemplate.postForObject(openAIUrl + "/chat/completions", chatGPTRequest, ChatGPTResponse.class);
        assert chatGPTResponse != null;
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    public String deepSeekChat(String dreamConent) {
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("model", "deepseek-chat");
        requestBodyMap.put("frequency_penalty", 0);
        requestBodyMap.put("max_tokens", 2048);
        requestBodyMap.put("presence_penalty", 0);
        requestBodyMap.put("response_format", Map.of("type", "text"));
        requestBodyMap.put("stop", null);
        requestBodyMap.put("stream", false);
        requestBodyMap.put("temperature", 1);
        requestBodyMap.put("top_p", 1);
        requestBodyMap.put("tools", null);
        requestBodyMap.put("tool_choice", "none");
        requestBodyMap.put("logprobs", false);
        requestBodyMap.put("top_logprobs", null);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> systemMessageMap = new HashMap<>();
        systemMessageMap.put("content", PromptConstant.DREAM_PROMPT);
        systemMessageMap.put("role", "system");

        Map<String, String> userMessageMap = new HashMap<>();
        userMessageMap.put("content", dreamConent);
        userMessageMap.put("role", "user");
        messages.add(systemMessageMap);
        messages.add(userMessageMap);
        requestBodyMap.put("messages", messages);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBodyMap);
        try{
            ChatGPTResponse chatGPTResponse = deepSeekRestTemplate.postForObject(deepSeekUrl + "/chat/completions", requestEntity, ChatGPTResponse.class);
            assert chatGPTResponse != null;
            return chatGPTResponse.getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            log.error("DeepSeek chat error: {0}", e);
            throw new CustomBusinessException("DeepSeek chat error");
        }
    }
}
