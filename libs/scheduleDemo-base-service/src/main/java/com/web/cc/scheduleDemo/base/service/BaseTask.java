package com.web.cc.scheduleDemo.base.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.web.cc.scheduleDemo.base.repos.dao.TaskExecutionLogRepository;
import com.web.cc.scheduleDemo.base.repos.entity.TaskExecutionLogEntity;
import com.web.cc.sheduleDemo.base.core.TaskStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * 排程任務基礎抽象類別 (Template Method Pattern)
 * <p>
 * 所有排程任務必須繼承此類別並實作 {@link #execute()} 方法。
 * 框架自動處理：
 * <ul>
 * <li>執行前後的日誌記錄 (Logging)</li>
 * <li>執行狀態追蹤與寫入資料庫 (TaskExecutionLog)</li>
 * <li>例外捕捉與錯誤記錄</li>
 * </ul>
 */
@Slf4j
public abstract class BaseTask {

    @Autowired
    private TaskExecutionLogRepository executionLogRepository;

    /**
     * 取得任務名稱，子類別可覆寫
     */
    public String getTaskName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 排程觸發的進入點（由 DynamicSchedulerService 呼叫）
     * 自動處理日誌記錄與狀態追蹤
     */
    public void run() {
        String taskName = getTaskName();
        LocalDateTime startTime = LocalDateTime.now();

        log.info("========== [{}] 排程任務開始執行 ==========", taskName);

        // 建立執行記錄 (RUNNING)
        TaskExecutionLogEntity executionLog = TaskExecutionLogEntity.builder()
                .taskName(taskName)
                .status(TaskStatus.RUNNING)
                .startTime(startTime)
                .build();
        executionLog = executionLogRepository.save(executionLog);

        try {
            // 呼叫子類別的業務邏輯
            String resultMessage = execute();

            // 更新為成功
            executionLog.setStatus(TaskStatus.SUCCESS);
            executionLog.setEndTime(LocalDateTime.now());
            executionLog.setMessage(resultMessage);

            log.info("[{}] 排程任務執行成功。訊息: {}", taskName, resultMessage);

        } catch (Exception e) {
            // 更新為失敗
            executionLog.setStatus(TaskStatus.FAILURE);
            executionLog.setEndTime(LocalDateTime.now());
            executionLog.setMessage("執行失敗: " + e.getMessage());

            log.error("[{}] 排程任務執行失敗！", taskName, e);

        } finally {
            executionLogRepository.save(executionLog);
            log.info("========== [{}] 排程任務結束 (耗時: {}ms) ==========",
                    taskName,
                    java.time.Duration.between(startTime, LocalDateTime.now()).toMillis());
        }
    }

    /**
     * 執行排程任務的核心業務邏輯
     *
     * @return 執行結果摘要訊息
     */
    protected abstract String execute();
}
