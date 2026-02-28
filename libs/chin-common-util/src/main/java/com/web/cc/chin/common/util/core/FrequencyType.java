package com.web.cc.chin.common.util.core;

/**
 * 排程頻率類型
 */
public enum FrequencyType {
    YEARLY("每年"),
    MONTHLY("每月"),
    DAILY("每天"),
    HOURLY("每小時"),
    MINUTES("每隔幾分鐘");

    private final String description;

    FrequencyType(String description) {
        this.description = description;
    }

	public String getDescription() {
		return description;
	}
    
}
