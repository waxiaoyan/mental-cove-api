package com.mental.cove.entity;

import com.mental.cove.data.MBTITypeExplanation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assessment_result")
public class AssessmentResult extends BaseEntity{
    @Column
    private long userId;
    @Column
    private String assessmentType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column
    private MBTITypeExplanation assessmentResult;
    @Column
    private String assessmentDesc;

}
