package com.web.cc.scheduleDemo.base.repos.entity;

import com.web.cc.sheduleDemo.base.core.FrequencyType;
import com.web.cc.sheduleDemo.base.core.ScheduledTaskConfig;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * 排程任務設定（可配置頻率）
 */
@Entity
@Table(name = "scheduled_task_config")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduledTaskConfigEntity implements ScheduledTaskConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 
     * 任務名稱（唯一識別） 
     */
    @Column(nullable = false, unique = true)
    private String taskName;

    /** 
     * 頻率類型 
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private FrequencyType frequencyType;

    /** 
     * Cron 表達式 
     */
    @Column(nullable = false)
    private String cronExpression;

    /** 
     * 是否啟用 
     */
    @Column(nullable = false)
    private Boolean isActive;

    /** 
     * 任務描述 
     */
    private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public FrequencyType getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(FrequencyType frequencyType) {
		this.frequencyType = frequencyType;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}
