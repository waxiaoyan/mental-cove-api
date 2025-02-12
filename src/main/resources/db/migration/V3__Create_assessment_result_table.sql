CREATE TABLE IF NOT EXISTS assessment_result (
        id SERIAL PRIMARY KEY,
        user_id integer NOT NULL,
        assessment_type VARCHAR(255),
        assessment_result jsonb,
        assessment_desc VARCHAR(255),
        created_time TIMESTAMP NOT NULL DEFAULT now(),
        modified_time TIMESTAMP NOT NULL DEFAULT now()
    );