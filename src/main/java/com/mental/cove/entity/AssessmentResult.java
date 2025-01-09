package com.mental.cove.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assessment_result")
public class AssessmentResult extends BaseEntity{
    private long userId;
    private String assessmentType;
    private String assessmentResult;
    private String assessmentDesc;
}
