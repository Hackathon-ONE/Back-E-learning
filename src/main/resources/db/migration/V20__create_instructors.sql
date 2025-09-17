CREATE TABLE instructors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    bio TEXT,
    avatar VARCHAR(500),
    specialty VARCHAR(255),
    linkedin VARCHAR(500),
    github VARCHAR(500),
    twitter VARCHAR(500),
    experience TEXT,
    status VARCHAR(50) DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
