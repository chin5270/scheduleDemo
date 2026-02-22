package com.web.cc.scheduleDemo.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.web.cc.scheduleDemo.base.repos.dao.ScheduledTaskConfigRepository;
import com.web.cc.scheduleDemo.base.repos.dao.TaskExecutionLogRepository;
import com.web.cc.scheduleDemo.base.repos.entity.ScheduledTaskConfigEntity;
import com.web.cc.scheduleDemo.base.repos.entity.TaskExecutionLogEntity;
import com.web.cc.sheduleDemo.base.core.DynamicSchedulerService;
import com.web.cc.sheduleDemo.base.core.FrequencyType;
import com.web.cc.sheduleDemo.base.core.ScheduleConfigService;
import com.web.cc.sheduleDemo.base.core.ScheduledTaskConfig;
import com.web.cc.sheduleDemo.base.core.TaskExecutionLog;
import com.web.cc.sheduleDemo.base.core.vo.AvailableTaskDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScheduleConfigServiceImpl implements ScheduleConfigService {

    @Autowired
    private ScheduledTaskConfigRepository configRepository;

    @Autowired
    private TaskExecutionLogRepository executionLogRepository;

    @Autowired
    private DynamicSchedulerService dynamicSchedulerService;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    /**
     * 查詢所有可用的排程任務（已實作 BaseTask 的 Bean）
     */
    public List<AvailableTaskDto> getAvailableTasks() {

        Map<String, BaseTask> taskBeans =
                applicationContext.getBeansOfType(BaseTask.class);

        List<AvailableTaskDto> result = new ArrayList<>();

        for (Map.Entry<String, BaseTask> entry : taskBeans.entrySet()) {

            BaseTask task = entry.getValue();

            AvailableTaskDto dto = new AvailableTaskDto();
            dto.setTaskName(task.getTaskName());

            Optional<ScheduledTaskConfigEntity> configOpt =
                    configRepository.findByTaskName(task.getTaskName());

            dto.setRegistered(configOpt.isPresent());

            // 如果有註冊，設定排程資訊
            if (configOpt.isPresent()) {
                ScheduledTaskConfigEntity config = configOpt.get();
                dto.setCronExpression(config.getCronExpression());
                dto.setIsActive(config.getIsActive());
            }

            result.add(dto);
        }

        return result;
    }

    /**
     * 查詢所有排程設定
     */
    public List<ScheduledTaskConfig> getAllConfigs() {
    	List<ScheduledTaskConfigEntity> scheduledTaskConfigEntityList = configRepository.findAll();
        return new ArrayList<>(scheduledTaskConfigEntityList); 
    }

    /**
     * 查詢指定排程設定
     */
    public ScheduledTaskConfig getConfigByTaskName(String taskName) {
        return configRepository.findByTaskName(taskName)
            .orElseThrow(() -> new RuntimeException("Task not found: " + taskName));
    }
    
    /**
     * 新增排程設定
     *
     * @param taskName       任務名稱
     * @param frequencyType  排程頻率類
     * @param cronExpression 排程表達式
     * @param isActive       是否啟用
     * @param description    說明
     * @return 
     */
    public ScheduledTaskConfig createConfig(
    		@NotBlank String taskName, 
    		@NotNull FrequencyType frequencyType,
    		@NotBlank String cronExpression,
    		@NotNull Boolean isActive,
    		@NotBlank String description) {
    	
    	 // 檢查是否重複
        if (configRepository.findByTaskName(taskName).isPresent()) {
            throw new IllegalArgumentException("排程任務 [" + taskName + "] 已存在");
        }
        
        ScheduledTaskConfigEntity entity = new ScheduledTaskConfigEntity();
        entity.setTaskName(taskName);
        entity.setFrequencyType(frequencyType);
        entity.setCronExpression(cronExpression);
        entity.setIsActive(isActive);
        entity.setDescription(description);

        ScheduledTaskConfig config = configRepository.save(entity);

        // 若啟用，自動註冊排程
        if (Boolean.TRUE.equals(config.getIsActive())) {
            dynamicSchedulerService.reloadTask(config.getTaskName());
        }

        log.info("已新增排程設定 [{}]", config.getTaskName());
        return config;
    	
    }

    /**
     * 更新排程設定並重新載入排程
     *
     * @param taskName       任務名稱
     * @param frequencyType  排程頻率類型，可為 null
     * @param cronExpression 排程表達式，可為 null
     * @param isActive       是否啟用，可為 null
     * @param description    說明，可為 null
     * @return 更新後的設定，若找不到則丟例外
     */
    public ScheduledTaskConfig updateConfig(
            String taskName,
            FrequencyType frequencyType,
            String cronExpression,
            Boolean isActive,
            String description) {

        ScheduledTaskConfigEntity config = configRepository.findByTaskName(taskName)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskName));

        if (frequencyType != null) {
            config.setFrequencyType(frequencyType);
        }
        if (cronExpression != null) {
            config.setCronExpression(cronExpression);
        }
        if (isActive != null) {
            config.setIsActive(isActive);
        }
        if (description != null) {
            config.setDescription(description);
        }

        ScheduledTaskConfigEntity saved = configRepository.save(config);

        // 重新載入排程
        dynamicSchedulerService.reloadTask(taskName);

        log.info("排程設定 [{}] 已更新並重新載入", taskName);

        return saved; // 直接回傳介面型別 ScheduledTaskConfig
    }

    /**
     * 手動觸發任務（立即執行一次）
     */
    public void triggerTask(String taskName) {
        dynamicSchedulerService.triggerTask(taskName);
    }

    /**
     * 查詢最近的執行記錄（前 10 筆）
     */
    public List<TaskExecutionLog> getRecentLogs() {
    	List<TaskExecutionLogEntity> taskExecutionLogList = executionLogRepository.findTop10ByOrderByCreatedAtDesc();
        return new ArrayList<>(taskExecutionLogList);
    }

    /**
     * 查詢指定任務的執行記錄
     */
    public List<TaskExecutionLog> getTaskLogs(String taskName) {
    	List<TaskExecutionLogEntity> taskExecutionLogList = executionLogRepository.findByTaskNameOrderByCreatedAtDesc(taskName);
        return new ArrayList<>(taskExecutionLogList);
    }
}
