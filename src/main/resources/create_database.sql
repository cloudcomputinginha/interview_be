create database cloudComputing_db;

USE cloudComputing_db;

CREATE TABLE member
(
    member_id       BIGINT                   NOT NULL AUTO_INCREMENT,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    job_type        VARCHAR(10),
    phone           VARCHAR(15),
    email           VARCHAR(30)              NOT NULL,
    name            VARCHAR(30)              NOT NULL,
    provider_id     VARCHAR(100)             NOT NULL,
    introduction    TEXT,
    refresh_token   TEXT,
    social_provider ENUM ('GOOGLE', 'KAKAO') NOT NULL,
    PRIMARY KEY (member_id)
) ENGINE = InnoDB;

CREATE TABLE resume
(
    resume_id  BIGINT       NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    file_size  BIGINT       NOT NULL,
    member_id  BIGINT       NOT NULL,
    file_name  VARCHAR(100) NOT NULL,
    file_url   VARCHAR(255) NOT NULL,
    file_type  ENUM ('PDF') NOT NULL,
    PRIMARY KEY (resume_id)
) ENGINE = InnoDB;

CREATE TABLE coverletter
(
    coverletter_id BIGINT NOT NULL AUTO_INCREMENT,
    created_at     DATETIME(6),
    updated_at     DATETIME(6),
    member_id      BIGINT NOT NULL,
    job_name       VARCHAR(20),
    corporate_name VARCHAR(100),
    PRIMARY KEY (coverletter_id)
) ENGINE = InnoDB;

CREATE TABLE interview_option
(
    id               BIGINT NOT NULL AUTO_INCREMENT,
    created_at       DATETIME(6),
    updated_at       DATETIME(6),
    answer_time      INT,
    question_number  INT,
    interview_format VARCHAR(20),
    interview_type   VARCHAR(20),
    voice_type       VARCHAR(20),
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE interview
(
    interview_id         BIGINT      NOT NULL AUTO_INCREMENT,
    current_participants INT         NOT NULL,
    is_open              BIT,
    max_participants     INT,
    created_at           DATETIME(6),
    updated_at           DATETIME(6),
    started_at           DATETIME(6),
    ended_at             DATETIME(6),
    host_id              BIGINT,
    interview_option_id  BIGINT      NOT NULL,
    corporate_name       VARCHAR(20) NOT NULL,
    job_name             VARCHAR(20) NOT NULL,
    name                 VARCHAR(20) NOT NULL,
    description          TEXT,
    session_name         VARCHAR(255),
    start_type           VARCHAR(20),
    PRIMARY KEY (interview_id)
) ENGINE = InnoDB;

CREATE TABLE member_interview
(
    member_interview_id BIGINT                                            NOT NULL AUTO_INCREMENT,
    created_at          DATETIME(6),
    updated_at          DATETIME(6),
    interview_id        BIGINT                                            NOT NULL,
    member_id           BIGINT                                            NOT NULL,
    resume_id           BIGINT,
    coverletter_id      BIGINT,
    status              ENUM ('DONE','IN_PROGRESS','NO_SHOW','SCHEDULED') NOT NULL,
    PRIMARY KEY (member_interview_id)
) ENGINE = InnoDB;

CREATE TABLE notification
(
    notification_id BIGINT                                                                                                 NOT NULL AUTO_INCREMENT,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    member_id       BIGINT                                                                                                 NOT NULL,
    message         VARCHAR(100)                                                                                           NOT NULL,
    related_url     VARCHAR(255)                                                                                           NOT NULL,
    type            ENUM ('FEEDBACK_RECEIVED','INTERVIEW_REMINDER_1D','INTERVIEW_REMINDER_30M','ROOM_ENTRY','ROOM_INVITE') NOT NULL,
    PRIMARY KEY (notification_id)
) ENGINE = InnoDB;

CREATE TABLE qna
(
    qna_id         BIGINT NOT NULL AUTO_INCREMENT,
    created_at     DATETIME(6),
    updated_at     DATETIME(6),
    coverletter_id BIGINT NOT NULL,
    question       TEXT   NOT NULL,
    answer         TEXT   NOT NULL,
    PRIMARY KEY (qna_id)
) ENGINE = InnoDB;
