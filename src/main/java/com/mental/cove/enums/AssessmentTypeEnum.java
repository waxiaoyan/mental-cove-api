package com.mental.cove.enums;

public enum AssessmentTypeEnum {
    MBTI("MBTI","MBTI职业性格测试"),
    SDS("SDS","抑郁自评量表SDS");

    private final String value;
    private final String name;
    AssessmentTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
