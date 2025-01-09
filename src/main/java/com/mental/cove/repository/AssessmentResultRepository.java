package com.mental.cove.repository;

import com.mental.cove.entity.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {

        AssessmentResult findFirstByUserIdOrderByCreatedTimeDesc(long userId);

}
