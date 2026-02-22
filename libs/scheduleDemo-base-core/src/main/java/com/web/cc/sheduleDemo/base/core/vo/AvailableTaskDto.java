package com.web.cc.sheduleDemo.base.core.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 可用排程任務資訊 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTaskDto {

    /** 
     * 任務名稱（對應 DB taskName） 
     */
    private String taskName;

    /** 
     * 是否已在 DB 註冊排程 
     */
    private boolean registered;

    /** 
     * Cron 表達式（已註冊時才有值） 
     */
    private String cronExpression;

    /** 
     * 是否啟用（已註冊時才有值） 
     */
    private Boolean isActive;
}
