CREATE TABLE users (
   id SERIAL PRIMARY KEY,
   user_name VARCHAR(255),
   open_id VARCHAR(255),
   created_time TIMESTAMP NOT NULL DEFAULT now(),
   modified_time TIMESTAMP NOT NULL DEFAULT now()
);
