-- ============================================
-- Database Schema for Schedule Demo
-- ============================================

-- 1. 建立資料庫
CREATE DATABASE IF NOT EXISTS `schedule_demo` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `schedule_demo`;

-- 2. 排程任務設定表
CREATE TABLE IF NOT EXISTS scheduled_task_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_name VARCHAR(255) NOT NULL UNIQUE,
    frequency_type VARCHAR(50) NOT NULL,
    cron_expression VARCHAR(255) NOT NULL,
    is_active TINYINT(1) NOT NULL,
    description VARCHAR(255)
) ENGINE=InnoDB;

-- 3. 排程任務執行記錄表
CREATE TABLE IF NOT EXISTS task_execution_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    start_time DATETIME(6) NOT NULL,
    end_time DATETIME(6),
    message TEXT,
    created_at DATETIME(6) NOT NULL
) ENGINE=InnoDB;
