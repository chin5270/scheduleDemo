package com.web.cc.scheduleDemo.api.rest.vo;

import com.web.cc.sheduleDemo.base.core.FrequencyType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
	@Schema(description = "頻率類型 ")
	private FrequencyType frequencyType;
	
	/** 
     * Cron 表達式 
     */
	@Schema(description = "Cron 表達式")
	private String cronExpression;
	
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
