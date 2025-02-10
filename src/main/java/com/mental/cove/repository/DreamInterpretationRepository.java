package com.mental.cove.repository;

import com.mental.cove.entity.DreamInterpretation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DreamInterpretationRepository extends JpaRepository<DreamInterpretation, Long> {

    @Query("SELECT COUNT(di) FROM DreamInterpretation di JOIN di.user u WHERE u.openId = :openId AND di.createdTime >= CURRENT_DATE")
    int countByUserOpenIdAndCreatedTimeToday(@Param("openId") String openId);

}
