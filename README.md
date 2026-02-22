# scheduleDemo 動態排程管理系統 

這是一個基於 **Spring Boot 3.5.3+** 開發的輕量級後端管理系統。本專案核心目標是提供一套具備**可擴展性**與**即時性**的排程解決方案，解決傳統 `@Scheduled` 註解無法在不重啟服務的情況下動態調整頻率的痛點。

## 核心特色
- **動態管理**：透過 RESTful API 即時更新資料庫中的 Cron 表達式，並立即重新註冊排程。
- **異步執行緒池**：自定義 `ThreadPoolTaskScheduler`，確保多個排程任務異步執行，互不阻塞。
- **視覺化監控**：整合 **Knife4j (Swagger)**，提供直觀的 API 調試與任務執行狀況監控。
- **完善日誌**：詳實記錄每次任務的執行狀態（RUNNING, SUCCESS, FAILURE）、執行耗時與異常堆棧。
- **高度解耦**：採用模板模式 (Template Method Pattern)，新增任務僅需繼承 `BaseTask` 即可自動納入管理。

## 技術棧
- **Framework**: Java 17 / Spring Boot 3.5.3
- **ORM**: Spring Data JPA / Hibernate
- **Database**: MySQL 8.0.33
- **API Doc**: Knife4j (Swagger) / OpenAPI 3
- **Tools**: Lombok, Maven, Git

---

## 專案結構 (Multi-module Architecture)

本專案採用多模組架構設計，以實現高度解耦與模組化重用：

### 核心模組 (Apps & Builders)
- **apps/scheduleDemo-api-www**: 專案入口啟動層，包含 Spring Boot 啟動類與全域設定。
- **builders/scheduleDemo-api-www-builder**: 負責專案建構與打包配置。

### 務邏輯模組 (Libs)
- **libs/scheduleDemo-api-rest**: Controller 層，定義排程管理的 RESTful API 接口。
- **libs/scheduleDemo-base-service**: Service 層，實作動態排程註冊與任務調度核心邏輯。
- **libs/scheduleDemo-base-repos**: Persistence 層，包含 JPA Entity 與資料庫存取接口 (DAO)。
- **libs/scheduleDemo-base-core**: 定義系統核心介面 (Interface) 與常數。
- **libs/healthCheckTask-service**: 獨立的業務任務模組，實作特定的健康檢查排程邏輯。

### 通用底層 (Common Libs)
- **libs/chin-common-core**
- **libs/chin-common-rest**
- **libs/chin-common-util**
- **libs/scheduleDemo-parent-lib**: 統一管理所有模組的依賴版本 (Dependency Management)。

### 資料庫準備
請先建立名為 `schedule_demo` 的 Schema，並執行以下 SQL 建立排程設定表與執行紀錄表：

```sql
CREATE DATABASE IF NOT EXISTS `schedule_demo`;
USE `schedule_demo`;

-- 排程任務設定表
CREATE TABLE `scheduled_task_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_name` varchar(100) NOT NULL UNIQUE,
  `cron_expression` varchar(100) NOT NULL,
  `frequency_type` varchar(50) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- 任務執行紀錄表 (用於 BaseTask 紀錄執行狀態)
CREATE TABLE IF NOT EXISTS `task_execution_log` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `task_name` VARCHAR(100) NOT NULL,
  `status` VARCHAR(20) NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME DEFAULT NULL,
  `message` TEXT DEFAULT NULL
);

