package com.web.cc.chin.common.util;

import static com.cronutils.model.field.expression.FieldExpressionFactory.always;
import static com.cronutils.model.field.expression.FieldExpressionFactory.every;
import static com.cronutils.model.field.expression.FieldExpressionFactory.on;
import static com.cronutils.model.field.expression.FieldExpressionFactory.questionMark;

import java.time.YearMonth;

import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.web.cc.chin.common.util.core.FrequencyType;

/**
 * Cron 表達式產生器
 * Spring cron 格式：秒 分 時 日 月 星期（6 位）
 */
public class CronExpressionBuilder {

    private CronExpressionBuilder() {
        // utility class
    }

    /**
     * 根據頻率類型和時間參數產生 cron 表達式
     * <p>
     * 參數由大到小排列：月 → 日 → 時 → 分 → 間隔
     *
     * @param frequencyType 頻率類型
     * @param month         月份 (1-12)，YEARLY 使用
     * @param dayOfMonth    日期 (1-31)，MONTHLY / YEARLY 使用
     * @param hour          小時 (0-23)，DAILY / MONTHLY / YEARLY 使用
     * @param minute        分鐘 (0-59)，DAILY / MONTHLY / YEARLY / HOURLY 使用
     * @param interval      間隔分鐘數 (1-59)，MINUTES 使用
     * @return Spring 格式的 cron 表達式
     * @throws IllegalArgumentException 參數不合法時拋出
     */
    public static String buildCronExpression(FrequencyType frequencyType,
            Integer month,
            Integer dayOfMonth,
            Integer hour,
            Integer minute,
            Integer interval) {
        if (frequencyType == null) {
            throw new IllegalArgumentException("frequencyType 不可為空");
        }

        return switch (frequencyType) {
            case YEARLY -> buildYearly(month, dayOfMonth, hour, minute);
            case MONTHLY -> buildMonthly(dayOfMonth, hour, minute);
            case DAILY -> buildDaily(hour, minute);
            case HOURLY -> buildHourly(minute);
            case MINUTES -> buildMinutes(interval);
        };
    }

    /**
     * YEARLY: 每年某月某日某時執行
     * 需要: month (1-12), dayOfMonth (1-31), hour (0-23), minute (0-59)
     */
    private static String buildYearly(Integer month, Integer dayOfMonth, Integer hour, Integer minute) {
        validateRequired(month, "month", 1, 12);
        validateRequired(dayOfMonth, "dayOfMonth", 1, 31);
        validateMonthDay(month, dayOfMonth);
        validateRequired(hour, "hour", 0, 23);
        validateRequired(minute, "minute", 0, 59);

        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING))
                .withSecond(on(0))
                .withMinute(on(minute))
                .withHour(on(hour))
                .withDoM(on(dayOfMonth))
                .withMonth(on(month))
                .withDoW(questionMark())
                .instance();

        return cron.asString();
    }

    /**
     * MONTHLY: 每月某日某時執行
     * 需要: dayOfMonth (1-31), hour (0-23), minute (0-59)
     */
    private static String buildMonthly(Integer dayOfMonth, Integer hour, Integer minute) {
        validateRequired(dayOfMonth, "dayOfMonth", 1, 31);
        validateRequired(hour, "hour", 0, 23);
        validateRequired(minute, "minute", 0, 59);

        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING))
                .withSecond(on(0))
                .withMinute(on(minute))
                .withHour(on(hour))
                .withDoM(on(dayOfMonth))
                .withMonth(always())
                .withDoW(questionMark())
                .instance();

        return cron.asString();
    }

    /**
     * DAILY: 每天某時執行
     * 需要: hour (0-23), minute (0-59)
     */
    private static String buildDaily(Integer hour, Integer minute) {
        validateRequired(hour, "hour", 0, 23);
        validateRequired(minute, "minute", 0, 59);

        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING))
                .withSecond(on(0))
                .withMinute(on(minute))
                .withHour(on(hour))
                .withDoM(always())
                .withMonth(always())
                .withDoW(questionMark())
                .instance();

        return cron.asString();
    }

    /**
     * HOURLY: 每小時幾分執行
     * 需要: minute (0-59)
     */
    private static String buildHourly(Integer minute) {
        validateRequired(minute, "minute", 0, 59);

        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING))
                .withSecond(on(0))
                .withMinute(on(minute))
                .withHour(always())
                .withDoM(always())
                .withMonth(always())
                .withDoW(questionMark())
                .instance();

        return cron.asString();
    }

    /**
     * MINUTES: 每隔幾分鐘執行
     * 需要: interval (1-59)
     */
    private static String buildMinutes(Integer interval) {
        validateRequired(interval, "interval", 1, 59);

        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING))
                .withSecond(on(0))
                .withMinute(every(interval))
                .withHour(always())
                .withDoM(always())
                .withMonth(always())
                .withDoW(questionMark())
                .instance();

        return cron.asString();
    }

    /**
     * 驗證必填參數及範圍
     */
    private static void validateRequired(Integer value, String fieldName, int min, int max) {
        if (value == null) {
            throw new IllegalArgumentException(
                    String.format("頻率類型需要 %s 參數", fieldName));
        }
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    String.format("%s 必須在 %d ~ %d 之間，目前值: %d", fieldName, min, max, value));
        }
    }

    /**
     * 驗證月份與日期的組合是否合理（以閏年為基準）
     */
    private static void validateMonthDay(int month, int dayOfMonth) {
        int maxDay = YearMonth.of(2024, month).lengthOfMonth();
        if (dayOfMonth > maxDay) {
            throw new IllegalArgumentException(
                    String.format("%d 月最多只有 %d 天，不可設定第 %d 天", month, maxDay, dayOfMonth));
        }
    }

    public static void main(String[] args) {
        System.out.println("=== CronExpressionBuilder Demo ===\n");

        // YEARLY: 每年 6/15 08:30 執行
        String yearly = buildCronExpression(FrequencyType.YEARLY, 6, 15, 8, 30, null);
        System.out.println("YEARLY  (每年 6/15 08:30): " + yearly);

        // MONTHLY: 每月 1 號 09:00 執行
        String monthly = buildCronExpression(FrequencyType.MONTHLY, null, 1, 9, 0, null);
        System.out.println("MONTHLY (每月 1 號 09:00): " + monthly);

        // DAILY: 每天 08:30 執行
        String daily = buildCronExpression(FrequencyType.DAILY, null, null, 8, 30, null);
        System.out.println("DAILY   (每天 08:30): " + daily);

        // HOURLY: 每小時第 15 分執行
        String hourly = buildCronExpression(FrequencyType.HOURLY, null, null, null, 15, null);
        System.out.println("HOURLY  (每小時第 15 分): " + hourly);

        // MINUTES: 每 5 分鐘執行
        String minutes = buildCronExpression(FrequencyType.MINUTES, null, null, null, null, 5);
        System.out.println("MINUTES (每 5 分鐘): " + minutes);
    }
}