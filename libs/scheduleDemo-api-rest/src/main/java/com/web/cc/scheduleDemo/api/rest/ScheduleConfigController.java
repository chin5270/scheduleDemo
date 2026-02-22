package com.web.cc.scheduleDemo.api.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.cc.scheduleDemo.api.rest.vo.CreateScheduledTaskConfigRequest;
import com.web.cc.scheduleDemo.api.rest.vo.UpdateScheduledTaskConfigRequest;
import com.web.cc.sheduleDemo.base.core.ScheduleConfigService;
import com.web.cc.sheduleDemo.base.core.ScheduledTaskConfig;
import com.web.cc.sheduleDemo.base.core.TaskExecutionLog;
import com.web.cc.sheduleDemo.base.core.vo.AvailableTaskDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * 排程設定管理 API
 */
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleConfigController {

    @Autowired
    private ScheduleConfigService scheduleConfigService;
    

    /**
     * 查詢所有可註冊的排程任務
     */
    @GetMapping("/available-tasks")
    @Operation(summary = "[A001]查詢所有可註冊的排程任務", tags = { "排程設定" })
    public ResponseEntity<List<AvailableTaskDto>> getAvailableTasks() {
        return ResponseEntity.ok(scheduleConfigService.getAvailableTasks());
    }

    /**
     * 查詢所有排程設定
     */
    @GetMapping
    @Operation(summary = "[A002]查詢所有排程設定", tags = { "排程設定" })
    public ResponseEntity<List<ScheduledTaskConfig>> getAllConfigs() {
        return ResponseEntity.ok(scheduleConfigService.getAllConfigs());
    }
    
    /**
     * 查詢指定排程設定
     */
    @GetMapping("/{taskName}")
    @Operation(summary = "[A003]查詢指定排程設定", tags = { "排程設定" })
    public ResponseEntity<ScheduledTaskConfig> getConfig(@PathVariable String taskName) {
    	return ResponseEntity.ok(scheduleConfigService.getConfigByTaskName(taskName)); 
    }

    /**
     * 新增排程設定
     */
    @PostMapping("/createConfig")
    @Operation(summary = "[A004]新增排程設定", tags = { "排程設定" })
    public ResponseEntity<?> createConfig(@RequestBody CreateScheduledTaskConfigRequest request) {
        try {
            ScheduledTaskConfig config = scheduleConfigService.createConfig(
            		request.getTaskName(),
            		request.getFrequencyType(),
            		request.getCronExpression(),
            		request.getIsActive(),
            		request.getDescription());
            return ResponseEntity.status(201).body(config);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /**
     * 更新排程頻率並重新載入
     */
    @PostMapping("/updateConfig")
    @Operation(summary = "[A005]更新排程頻率並重新載入", tags = { "排程設定" })
    public ResponseEntity<ScheduledTaskConfig> updateConfig(@RequestBody UpdateScheduledTaskConfigRequest request) {
        ScheduledTaskConfig updatedConfig = scheduleConfigService.updateConfig(
                request.getTaskName(),
                request.getFrequencyType(),
                request.getCronExpression(),
                request.getIsActive(),    
                request.getDescription()   
        );

        return ResponseEntity.ok(updatedConfig);
    }

    /**
     * 手動觸發任務（立即執行一次）
     */
    @PostMapping("/{taskName}/trigger")
    @Operation(summary = "[A006]手動觸發任務（立即執行一次）", tags = { "排程設定" })
    public ResponseEntity<String> triggerTask(@PathVariable("taskName") String taskName) {
        scheduleConfigService.triggerTask(taskName);
        return ResponseEntity.ok("任務 [" + taskName + "] 已觸發執行");
    }

    /**
     * 查詢最近的執行記錄
     */
    @GetMapping("/logs")
    @Operation(summary = "[A007]查詢最近的執行記錄", tags = { "排程設定" })
    public ResponseEntity<List<TaskExecutionLog>> getRecentLogs() {
        return ResponseEntity.ok(scheduleConfigService.getRecentLogs());
    }

    /**
     * 查詢指定任務的執行記錄
     */
    @GetMapping("/{taskName}/logs")
    @Operation(summary = "[A008]查詢指定任務的執行記錄", tags = { "排程設定" })
    public ResponseEntity<List<TaskExecutionLog>> getTaskLogs(@PathVariable("taskName") String taskName) {
        return ResponseEntity.ok(scheduleConfigService.getTaskLogs(taskName));
    }
}
