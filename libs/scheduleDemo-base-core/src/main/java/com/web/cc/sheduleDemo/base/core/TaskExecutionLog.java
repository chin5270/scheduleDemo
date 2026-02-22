package com.web.cc.sheduleDemo.base.core;

import java.time.LocalDateTime;

/**
 * 排程任務執行記錄介面
 */
public interface TaskExecutionLog {
    Long getId();

    /** 
     * 任務名稱
     **/
    String getTaskName();

    /** 
     * 執行狀態
     **/
    TaskStatus getStatus();

    /**
     * 執行開始時間
     **/
    LocalDateTime getStartTime();
    
    /** 
     * 執行結束時間 
     **/
    LocalDateTime getEndTime();

    /** 
     * 執行訊息（成功摘要或錯誤訊息） 
     **/
    String getMessage();

    /** 
     * 記錄建立時間 
     **/
    LocalDateTime getCreatedAt();
}
