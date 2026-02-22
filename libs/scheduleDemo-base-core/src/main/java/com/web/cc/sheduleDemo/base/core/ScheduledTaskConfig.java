package com.web.cc.sheduleDemo.base.core;

/**
 * 排程任務設定介面
 */
public interface ScheduledTaskConfig {
	
    Long getId();
    
    /** 
     * 任務名稱（唯一識別） 
     */
    String getTaskName();

    /** 
     * 頻率類型 
     */
    FrequencyType getFrequencyType();

    /** 
     * Cron 表達式 
     */
    String getCronExpression();

    /** 
     * 是否啟用 
     */
    Boolean getIsActive();

    /** 
     * 任務描述 
     */
    String getDescription();
}
