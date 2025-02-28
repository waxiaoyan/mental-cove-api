package com.mental.cove.repository;

import com.mental.cove.entity.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {

        AssessmentResult findFirstByUserIdOrderByCreatedTimeDesc(long userId);
        @Query(value = """
            SELECT a1.* FROM assessment_result a1
            WHERE a1.created_time = (
                SELECT MAX(a2.created_time) FROM assessment_result a2 
                                            WHERE a2.assessment_type = a1.assessment_type
            ) and a1.user_id = :userId
            """, nativeQuery = true)
        List<AssessmentResult> findLatestAssessmentsByUserId(@Param("userId") Long userId);
}
