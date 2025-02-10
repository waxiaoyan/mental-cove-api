package com.mental.cove.entity;

import jakarta.persistence.*;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dream_interpretation")
public class DreamInterpretation extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(columnDefinition = "TEXT")
    private String dreamContent;
    @Column(columnDefinition = "TEXT")
    private String interpretation;
    private String ip;
}
