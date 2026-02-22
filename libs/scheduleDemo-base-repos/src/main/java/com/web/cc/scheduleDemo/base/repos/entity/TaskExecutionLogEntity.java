package com.web.cc.scheduleDemo.base.repos.entity;

import java.time.LocalDateTime;

import com.web.cc.sheduleDemo.base.core.TaskExecutionLog;
import com.web.cc.sheduleDemo.base.core.TaskStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * 排程任務執行記錄
 */
@Entity
@Table(name = "task_execution_log")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskExecutionLogEntity implements TaskExecutionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 
     * 任務名稱
     **/
    @Column(nullable = false)
    private String taskName;

    /** 
     * 執行狀態
     **/
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    /**
     * 執行開始時間
     **/
    @Column(nullable = false)
    private LocalDateTime startTime;

    /** 
     * 執行結束時間 
     **/
    private LocalDateTime endTime;

    /** 
     * 執行訊息（成功摘要或錯誤訊息） 
     **/
    @Column(columnDefinition = "TEXT")
    private String message;

    /** 
     * 記錄建立時間 
     **/
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

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

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
}
