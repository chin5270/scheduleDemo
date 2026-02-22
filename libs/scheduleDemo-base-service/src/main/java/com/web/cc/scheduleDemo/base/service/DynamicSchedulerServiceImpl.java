package com.web.cc.scheduleDemo.base.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.web.cc.scheduleDemo.base.repos.dao.ScheduledTaskConfigRepository;
import com.web.cc.scheduleDemo.base.repos.entity.ScheduledTaskConfigEntity;
import com.web.cc.sheduleDemo.base.core.DynamicSchedulerService;
import com.web.cc.sheduleDemo.base.core.ScheduledTaskConfig;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 動態排程管理服務
 * <p>
 * 從資料庫讀取排程設定，動態註冊/取消排程任務。
 * 支援執行期間更新排程頻率。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicSchedulerServiceImpl implements DynamicSchedulerService {

	@Autowired
    private ScheduledTaskConfigRepository scheduledTaskConfigRepository;
    
	@Autowired
	private ApplicationContext applicationContext;

    private TaskScheduler taskScheduler;
    
    private final Map<String, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // 建立排程執行緒池
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("schedule-task-");
        scheduler.initialize();
        this.taskScheduler = scheduler;

        // 載入所有啟用的排程任務
        registerAllTasks();
    }

    /**
     * 註冊所有啟用的排程任務
     */
    public void registerAllTasks() {
        List<ScheduledTaskConfigEntity> configs = scheduledTaskConfigRepository.findByIsActive(Boolean.TRUE);
        log.info("正在載入 {} 個排程任務設定...", configs.size());

        for (ScheduledTaskConfigEntity config : configs) {
            registerTask(config);
        }
    }

    /**
     * 註冊單一排程任務
     */
    public void registerTask(ScheduledTaskConfig config) {
        String taskName = config.getTaskName();

        // 先取消舊的排程（如果存在）
        cancelTask(taskName);

        try {
            // 從 Spring Context 中取得對應的 BaseTask Bean
            BaseTask task = applicationContext.getBean(taskName, BaseTask.class);

            // 使用 cron 表達式建立排程
            ScheduledFuture<?> future = taskScheduler.schedule(
                    () -> task.run(),
                    new CronTrigger(config.getCronExpression()));

            scheduledFutures.put(taskName, future);
            log.info("已註冊排程任務 [{}] - 頻率: {} - Cron: {}",
                    taskName, config.getFrequencyType(), config.getCronExpression());

        } catch (Exception e) {
            log.error("註冊排程任務 [{}] 失敗", taskName, e);
        }
    }

    /**
     * 取消排程任務
     */
    public void cancelTask(String taskName) {
        ScheduledFuture<?> future = scheduledFutures.remove(taskName);
        if (future != null) {
            future.cancel(false);
            log.info("已取消排程任務 [{}]", taskName);
        }
    }

    /**
     * 重新載入指定任務（用於 API 更新頻率後呼叫）
     */
    public void reloadTask(String taskName) {
    	scheduledTaskConfigRepository.findByTaskName(taskName).ifPresentOrElse(
                config -> {
                    if (config.getIsActive()) {
                        registerTask(config);
                    } else {
                        cancelTask(taskName);
                        log.info("排程任務 [{}] 已停用", taskName);
                    }
                },
                () -> log.warn("找不到排程任務設定: {}", taskName));
    }

    /**
     * 手動觸發任務（立即執行一次）
     */
    public void triggerTask(String taskName) {
        try {
            BaseTask task = applicationContext.getBean(taskName, BaseTask.class);
            log.info("手動觸發排程任務 [{}]", taskName);
            task.run();
        } catch (Exception e) {
            log.error("手動觸發排程任務 [{}] 失敗", taskName, e);
            throw new RuntimeException("找不到排程任務: " + taskName, e);
        }
    }
}
