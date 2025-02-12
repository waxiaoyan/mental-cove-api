package com.mental.cove.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mental.cove.auth.SecurityService;
import com.mental.cove.data.AssessmentResultData;
import com.mental.cove.data.MBTIQuestionnaire;
import com.mental.cove.data.MBTITypeExplanation;
import com.mental.cove.data.MBTITypeExplanationList;
import com.mental.cove.entity.AssessmentResult;
import com.mental.cove.entity.User;
import com.mental.cove.enums.AssessmentTypeEnum;
import com.mental.cove.exception.CustomBusinessException;
import com.mental.cove.repository.AssessmentResultRepository;
import com.mental.cove.repository.UserRepository;
import com.mental.cove.utils.DateUtils;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class QuestionnaireService {

    @Resource
    private AssessmentResultRepository mbtiResultRepository;
    @Resource
    private SecurityService securityService;
    @Resource
    private UserRepository userRepository;

    public MBTIQuestionnaire loadMBTIQuestions() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new ClassPathResource("questionnaire/mbti_questions.json").getInputStream(), MBTIQuestionnaire.class);
    }

    public MBTITypeExplanation calculateMBTIResult(Map<String, String> mbtiAnswers) {
        String JSON_FILE_PATH = "/questionnaire/mbti_questions.json";
        try (InputStream is = QuestionnaireService.class.getResourceAsStream(JSON_FILE_PATH)) {
            if (is == null) {
                throw new FileNotFoundException("File not found: " + JSON_FILE_PATH);
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(is);
            JsonNode questionsNode = rootNode.path("questions");
            Map<String, Integer> scores = new HashMap<>();
            String[] types = {"E", "I", "S", "N", "T", "F", "J", "P"};
            for (String type : types) {
                scores.put(type, 0);
            }
            if (questionsNode.isArray()) {
                for (JsonNode questionNode : questionsNode) {
                    String id = questionNode.path("id").asText();
                    String answer = mbtiAnswers.get(id);
                    if (scores.containsKey(answer)) {
                        scores.put(answer, scores.get(answer) + 1);
                    } else {
                        scores.put(answer, 1);
                    }
                }
                String mbtiType = calculateMBTIType(scores);
                //calculate each type's percentage
                Map<String, Integer> percentage = new HashMap<>();
                scores.forEach((key, value) -> percentage.put(key, value * 100 / questionsNode.size()));
                MBTITypeExplanation mbtiTypeExplanation = getMBTITypeExplanation(mbtiType);
                mbtiTypeExplanation.setPercentages(calculatePercentages(scores, mbtiType));
                saveMBTIType(mbtiTypeExplanation);
                return mbtiTypeExplanation;
            } else {
                throw new CustomBusinessException("No questions found in the questionnaire");
            }
        } catch (Exception e) {
            log.error("Failed to calculate MBTI result", e);
            throw new CustomBusinessException("Failed to calculate MBTI result");
        }
    }

    private MBTITypeExplanation getMBTITypeExplanation(String mbtiType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("questionnaire/mbti_type/mbti_personality_desc.json");
        MBTITypeExplanationList typeList = mapper.readValue(resource.getInputStream(), MBTITypeExplanationList.class);
        Map<String, MBTITypeExplanation> mbtiMap = typeList.getTypes().stream().collect(Collectors.toMap(MBTITypeExplanation::getType, type -> type));
        return mbtiMap.get(mbtiType);
    }

    private void saveMBTIType(MBTITypeExplanation result) {
        String openId = securityService.getCurrentUserOpenId();
        User user = userRepository.findByOpenId(openId);
        AssessmentResult mbtiResult = AssessmentResult.builder()
                .userId(user.getId())
                .assessmentResult(result)
                .assessmentType(AssessmentTypeEnum.MBTI.getValue())
                .assessmentDesc(AssessmentTypeEnum.MBTI.getName())
                .build();
        mbtiResultRepository.save(mbtiResult);
    }

    private static String calculateMBTIType(Map<String, Integer> scores) {
        return (scores.get("E") > scores.get("I") ? "E" : "I") +
                (scores.get("S") > scores.get("N") ? "S" : "N") +
                (scores.get("T") > scores.get("F") ? "T" : "F") +
                (scores.get("J") > scores.get("P") ? "J" : "P");
    }

    private static List<MBTITypeExplanation.Percentage> calculatePercentages(Map<String, Integer> scores, String type) {
        List<MBTITypeExplanation.Percentage> percentages = new ArrayList<>();
        int totalEandI = scores.get("E") + scores.get("I");
        int totalSandN = scores.get("S") + scores.get("N");
        int totalTandF = scores.get("T") + scores.get("F");
        int totalJandP = scores.get("J") + scores.get("P");

        // Use the type to determine which label to use
        percentages.add(new MBTITypeExplanation.Percentage(
                type.charAt(0) == 'E' ? "E-外倾型" : "I-内倾型",
                calculatePercentage(scores.get(String.valueOf(type.charAt(0))), totalEandI),
                type.charAt(0) == 'E' ? "I-内倾型" : "E-外倾型"
        ));
        percentages.add(new MBTITypeExplanation.Percentage(
                type.charAt(1) == 'S' ? "S-感觉型" : "N-直觉型",
                calculatePercentage(scores.get(String.valueOf(type.charAt(1))), totalSandN),
                type.charAt(1) == 'S' ? "N-直觉型" : "S-感觉型"
        ));
        percentages.add(new MBTITypeExplanation.Percentage(
                type.charAt(2) == 'T' ? "T-思维型" : "F-情感型",
                calculatePercentage(scores.get(String.valueOf(type.charAt(2))), totalTandF),
                type.charAt(2) == 'T' ? "F-情感型" : "T-思维型"
        ));
        percentages.add(new MBTITypeExplanation.Percentage(
                type.charAt(3) == 'J' ? "J-判断型" : "P-知觉型",
                calculatePercentage(scores.get(String.valueOf(type.charAt(3))), totalJandP),
                type.charAt(3) == 'J' ? "P-知觉型" : "J-判断型"
        ));
        return percentages;
    }
    private static Integer calculatePercentage(int score, int total) {
        return (int) Math.round((double) score / total * 100);
    }

    public List<AssessmentResultData> getAssessmentResults(long userId) {
        AssessmentResult mbtiResult = mbtiResultRepository.findFirstByUserIdOrderByCreatedTimeDesc(userId);
        if (mbtiResult == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(AssessmentResultData.builder()
                .userId(String.valueOf(userId))
                .assessmentResult(mbtiResult.getAssessmentResult())
                .assessmentDesc(mbtiResult.getAssessmentDesc())
                .assessmentType(AssessmentTypeEnum.MBTI.getValue())
                .assessmentTime(DateUtils.formatLocalDateTime(mbtiResult.getCreatedTime()))
                .build());
    }
}
