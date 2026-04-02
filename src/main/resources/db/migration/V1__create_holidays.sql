CREATE TABLE holidays (
    id BIGSERIAL PRIMARY KEY,
    country_code VARCHAR(2) NOT NULL,
    date DATE NOT NULL,
    name_es VARCHAR(150) NOT NULL,
    name_en VARCHAR(150),
    type VARCHAR(20) NOT NULL,
    is_banking BOOLEAN DEFAULT FALSE,
    region_code VARCHAR(10),
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_holidays_country_year ON holidays (
    country_code,
    EXTRACT(
        YEAR
        FROM date
    )
);