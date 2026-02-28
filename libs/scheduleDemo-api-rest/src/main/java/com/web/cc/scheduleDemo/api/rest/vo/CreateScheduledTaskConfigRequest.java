package com.web.cc.scheduleDemo.api.rest.vo;

import com.web.cc.chin.common.util.core.FrequencyType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 更新排程頻率並重新載入-請求物件
 *
 */
@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateScheduledTaskConfigRequest {

	/** 
     * 任務名稱（唯一識別） 
     */
	@Schema(description = "任務名稱")
	@NotBlank(message = "任務名稱不能空白")
	private String taskName;
	
	/** 
     * 頻率類型 
     */
	@Schema(description = "頻率類型")
	@NotNull(message = "任務名稱不能空白")
	private FrequencyType frequencyType;
	
	/** 
     * 月份 (1-12)，YEARLY 使用 
     */
	@Schema(description = "月份 (1-12)，YEARLY 使用")
    private Integer month;
	
    /** 
     * 日期 (1-31)，MONTHLY / YEARLY 使用 
     */
	@Schema(description = "日期 (1-31)，MONTHLY / YEARLY 使用")
    private Integer dayOfMonth;

    /** 
     * 小時 (0-23)，DAILY / MONTHLY / YEARLY 使用 
     */
	@Schema(description = "小時 (0-23)，DAILY / MONTHLY / YEARLY 使用")
    private Integer hour;

    /** 
     * 分鐘 (0-59)，DAILY / MONTHLY / YEARLY / HOURLY 使用 
     */
	@Schema(description = "分鐘 (0-59)，DAILY / MONTHLY / YEARLY / HOURLY 使用")
    private Integer minute;

    /** 
     * 間隔分鐘數 (1-59)，MINUTES 使用 
     */
	@Schema(description = "間隔分鐘數 (1-59)，MINUTES 使用")
    private Integer interval;
	
	/** 
     * 是否啟用 
     */
	@Schema(description = "是否啟用")
	private Boolean isActive;
	
	/**
     * 任務描述 
     */
	@Schema(description = "任務描述 ")
	private String description;
}
