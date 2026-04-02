CREATE TABLE api_usage (
    api_key_hash    VARCHAR(64)  PRIMARY KEY,
    plan            VARCHAR(20)  NOT NULL DEFAULT 'FREE',
    requests_today  INT          NOT NULL DEFAULT 0,
    requests_month  INT          NOT NULL DEFAULT 0,
    last_request_at TIMESTAMP,
    reset_day_at    DATE         NOT NULL DEFAULT CURRENT_DATE,
    reset_month_at  DATE         NOT NULL DEFAULT DATE_TRUNC('month', CURRENT_DATE)
);