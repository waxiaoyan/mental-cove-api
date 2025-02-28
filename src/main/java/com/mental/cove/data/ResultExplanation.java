package com.mental.cove.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mental.cove.enums.AssessmentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = MBTITypeExplanation.class // 如果缺少 type 字段，默认反序列化为 MBTITypeExplanation
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MBTITypeExplanation.class, name = "MBTI"),
        @JsonSubTypes.Type(value = SdsResultExplanation.class, name = "SDS")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ResultExplanation implements Serializable {
    private String type;
}
