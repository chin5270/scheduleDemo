package com.web.cc.healthCheckTask.service;

import org.springframework.stereotype.Component;

import com.web.cc.scheduleDemo.base.service.BaseTask;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("healthCheckTask")
@RequiredArgsConstructor
public class HealthCheckTask extends BaseTask {
	
	@Override
	public String getTaskName() {
        return "healthCheckTask";
    }

    @Override
    protected String execute() {
        log.info("排程檢查執行中...");
        return "系統正常運作中";
    }

}
