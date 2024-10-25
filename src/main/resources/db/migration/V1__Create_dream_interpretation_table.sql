CREATE TABLE IF NOT EXISTS dream_interpretation (
    id SERIAL PRIMARY KEY,
    user_id integer not null,
    dream_content TEXT NOT NULL,
    interpretation TEXT NOT NULL,
    ip varchar(50),
    created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modified_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
