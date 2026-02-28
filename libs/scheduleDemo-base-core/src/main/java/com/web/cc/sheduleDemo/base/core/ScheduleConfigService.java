package com.web.cc.sheduleDemo.base.core;

import java.util.List;

import com.web.cc.chin.common.util.core.FrequencyType;
import com.web.cc.sheduleDemo.base.core.vo.AvailableTaskDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface ScheduleConfigService {
	

    /**
     * 查詢所有可用的排程任務（已實作 BaseTask 的 Bean）
     * <p>
     * 回傳每個任務的 Bean 名稱、類別名稱、是否已註冊排程。
     */
    public List<AvailableTaskDto> getAvailableTasks();
	
	/**
     * 查詢所有排程設定
     */
    public List<ScheduledTaskConfig> getAllConfigs();

    /**
     * 查詢指定排程設定
     */
    public ScheduledTaskConfig getConfigByTaskName(String taskName);
    
    /**
     * 新增排程設定
     *
     * @param taskName       任務名稱
     * @param frequencyType  排程頻率類
     * @param month          月份
     * @param dayOfMonth     日期
     * @param hour           小時
     * @param minute         分鐘
     * @param interval       間隔分鐘數
     * @param cronExpression 排程表達式
     * @param isActive       是否啟用
     * @param description    說明
     * @return 
     */
    public ScheduledTaskConfig createConfig(
    		@NotBlank String taskName, 
    		@NotNull FrequencyType frequencyType,
    		Integer month,
    		Integer dayOfMonth,
    		Integer hour,
    		Integer minute,
    		Integer interval,
    		@NotNull Boolean isActive,
    		@NotBlank String description);

    /**
     * 更新排程設定並重新載入排程
     *
     * @param taskName       任務名稱
     * @param frequencyType  排程頻率類型，可為 null
     * @param month          月份
     * @param dayOfMonth     日期
     * @param hour           小時
     * @param minute         分鐘
     * @param interval       間隔分鐘數
     * @param isActive       是否啟用，可為 null
     * @param description    說明，可為 null
     * @return 更新後的設定，若找不到則丟例外
     */
    public ScheduledTaskConfig updateConfig( 
    		String taskName,
            FrequencyType frequencyType,
        	Integer month,
    		Integer dayOfMonth,
    		Integer hour,
    		Integer minute,
    		Integer interval,
            Boolean isActive,
            String description);

    /**
     * 手動觸發任務（立即執行一次）
     */
    public void triggerTask(String taskName);

    /**
     * 查詢最近的執行記錄（前 10 筆）
     */
    public List<TaskExecutionLog> getRecentLogs();

    /**
     * 查詢指定任務的執行記錄
     */
    public List<TaskExecutionLog> getTaskLogs(String taskName);
}
