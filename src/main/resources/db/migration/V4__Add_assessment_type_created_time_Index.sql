CREATE INDEX idx_assessment_user_type_time
    ON assessment_result (user_id, assessment_type, created_time DESC);