package com.web.cc.sheduleDemo.base.core;

public interface DynamicSchedulerService {
	
	/**
     * 註冊所有啟用的排程任務
     **/
    public void registerAllTasks();

    /**
     * 註冊單一排程任務
     **/
    public void registerTask(ScheduledTaskConfig config);
    
    /**
     * 取消排程任務
     **/
    public void cancelTask(String taskName);
    
    /**
     * 重新載入指定任務（用於 API 更新頻率後呼叫）
     **/
    public void reloadTask(String taskName);

    /**
     * 手動觸發任務（立即執行一次）
     **/
    public void triggerTask(String taskName);
}
