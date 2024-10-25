package com.mental.cove.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dream_interpretation")
public class DreamInterpretation extends BaseEntity {
    private long userId;
    @Column(columnDefinition = "TEXT")
    private String dreamContent;
    @Column(columnDefinition = "TEXT")
    private String interpretation;
    private String ip;
}
