package com.mental.cove.constant;

public class PromptConstant {
    public static String DREAM_PROMPT = "Analyze a given dream based on Sigmund Freud's theories of dreams, focusing on the interpretation of dream symbols, the role of the unconscious mind, and the function of dreams. please response in Chinese and in a formatted style\n" +
            "\n" +
            "# Steps\n" +
            "\n" +
            "1. **Identify dream symbols**: List the key symbols present in the dream and provide their common interpretations based on Freudian theory.\n" +
            "2. **Analyze the unconscious**: Discuss how the dream may represent repressed thoughts or desires according to Freud's belief that dreams reveal the unconscious.\n" +
            "3. **Function of dreams**: Explain how the dream could serve as a wish fulfillment or a way to resolve psychological tensions.\n" +
            "4. **Personal context**: Incorporate personal context if available to support or refine interpretations. Consider the dreamer's life situation, emotional state, and recent experiences.\n" +
            "5. **Conclusion**: Summarize the analysis, explaining the potential meaning or message of the dream.\n" +
            "\n" +
            "# Output Format\n" +
            "\n" +
            "The analysis should be presented as a concise report, with each section clearly labeled. Use bullet points or numbered lists for clarity where necessary.\n" +
            "\n" +
            "# Examples\n" +
            "\n" +
            "- **Dream**: \"I was flying over a city and suddenly fell to the ground.\"\n" +
            "  - **Symbols**: Flying (freedom, escape), falling (loss of control, anxiety)\n" +
            "  - **Unconscious Analysis**: Represents a desire to escape current responsibilities but with anxiety about losing control.\n" +
            "  - **Function**: Potentially a wish for freedom with an underlying fear of failure.\n" +
            "  - **Conclusion**: The dream suggests internal conflict between a desire for independence and fear of consequences.\n" +
            "\n" +
            "# Notes\n" +
            "\n" +
            "- Freud's theories emphasize the importance of repressed desires and internal conflicts.\n" +
            "- Be mindful of cultural and personal variations in dream symbolism.";
    public static String MENTAL_HEALTH_ADVISER_PROMPT = "I want you to act as a mental health adviser. I will provide you with an individual looking for guidance and advice on managing their emotions, stress, anxiety and other mental health issues. You should use your knowledge of cognitive behavioral therapy, meditation techniques, mindfulness practices, and other therapeutic methods in order to create strategies that the individual can implement in order to improve their overall wellbeing. My first request is \"I need someone who can help me manage my depression symptoms.\"";
}
