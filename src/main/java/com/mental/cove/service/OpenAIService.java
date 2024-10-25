package com.mental.cove.service;

import com.mental.cove.auth.SecurityService;
import com.mental.cove.constant.PromptConstant;
import com.mental.cove.data.ChatGPTRequest;
import com.mental.cove.data.ChatGPTResponse;
import com.mental.cove.data.Message;
import com.mental.cove.entity.DreamInterpretation;
import com.mental.cove.entity.User;
import com.mental.cove.repository.DreamInterpretationRepository;
import com.mental.cove.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OpenAIService {
    @Resource
    private SecurityService securityService;
    @Resource
    private DreamInterpretationRepository dreamInterpretationRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private RestTemplate restTemplate;

    private final String model = "gpt-4o";

    @Value("${openai.api.url}")
    private String openAIUrl;

    public String chat(String prompt) {
        Message systemMessage = new Message("system", PromptConstant.DREAM_PROMPT );
        ChatGPTRequest chatGPTRequest = new ChatGPTRequest(model, prompt);
        chatGPTRequest.getMessages().add(systemMessage);
        chatGPTRequest.setMax_tokens(200);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIUrl + "/chat/completions", chatGPTRequest, ChatGPTResponse.class);
        assert chatGPTResponse != null;
        String content = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        String openId = securityService.getCurrentUserOpenId();
        User user = userRepository.findByName(openId);
        DreamInterpretation dreamInterpretation = DreamInterpretation.builder()
                .userId(user.getId())
                .dreamContent(prompt)
                .interpretation(content)
                .ip(securityService.getCurrentRequestUserIp())
                .build();
        dreamInterpretationRepository.save(dreamInterpretation);
        return content;
    }
}
