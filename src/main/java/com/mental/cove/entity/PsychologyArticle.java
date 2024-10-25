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
@Table(name = "psychology_articles")
public class PsychologyArticle extends BaseEntity{
    private String title;
    private String content;
    private String author;
}
