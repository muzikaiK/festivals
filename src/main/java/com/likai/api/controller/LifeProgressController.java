package com.likai.api.controller;

import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LifeProgressController {

    private static final int LIFE_EXPECTANCY_YEARS = 80; // 预期寿命（年）

    @GetMapping("/api/life/progress")
    public Map<String, Object> getLifeProgress(@RequestParam String birthdate) {
        try {
            LocalDate birthDate = LocalDate.parse(birthdate);
            LocalDateTime now = LocalDateTime.now();
            LocalDate today = now.toLocalDate();

            if (birthDate.isAfter(today)) {
                throw new IllegalArgumentException("出生日期不能晚于当前日期");
            }

            // === 1. 已活总秒数（精确到秒）===
            long totalDays = ChronoUnit.DAYS.between(birthDate, today);
            long secondsTodaySoFar = LocalTime.from(now).toSecondOfDay();
            double totalSecondsLived = totalDays * 24.0 * 3600.0 + secondsTodaySoFar;

            // === 2. 预期寿命总秒数（80年 × 365天，简化处理）===
            double lifeExpectancySeconds = LIFE_EXPECTANCY_YEARS * 365.0 * 24.0 * 3600.0;
            double lifeProgress = Math.min(100.0, totalSecondsLived / lifeExpectancySeconds * 100.0);

            // === 3. 基础统计 ===
            long meals = totalDays * 3;
            long nights = totalDays;
            long weekends = countWeekendCount(birthDate, today); // 整数

            // === 4. 今日进度 & 已过小时数（带小数）===
            double secondsInDay = 24.0 * 3600.0;
            double secondsToday = LocalTime.from(now).toSecondOfDay();
            double todayProgress = secondsToday / secondsInDay * 100.0;
            double hoursPassedToday = secondsToday / 3600.0; // 如 18.74 小时

            // === 5. 本周进度 & 已过天数（从周一 00:00 开始）===
            LocalDateTime startOfWeek = now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
            double secondsInWeek = 7.0 * 24.0 * 3600.0;
            double secondsThisWeek = Duration.between(startOfWeek, now).getSeconds();
            double weekProgress = Math.min(100.0, secondsThisWeek / secondsInWeek * 100.0);
            double daysPassedThisWeek = secondsThisWeek / (24.0 * 3600.0);

            // === 6. 本月进度 & 已过天数 ===
            LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
            LocalDateTime endOfMonth = startOfMonth.plusMonths(1);
            double secondsInMonth = Duration.between(startOfMonth, endOfMonth).getSeconds();
            double secondsThisMonth = Duration.between(startOfMonth, now).getSeconds();
            double monthProgress = Math.min(100.0, secondsThisMonth / secondsInMonth * 100.0);
            double daysPassedThisMonth = secondsThisMonth / (24.0 * 3600.0);

            // === 7. 本年进度 & 已过天数 ===
            LocalDateTime startOfYear = now.withDayOfYear(1).toLocalDate().atStartOfDay();
            LocalDateTime endOfYear = startOfYear.plusYears(1);
            double secondsInYear = Duration.between(startOfYear, endOfYear).getSeconds();
            double secondsThisYear = Duration.between(startOfYear, now).getSeconds();
            double yearProgress = Math.min(100.0, secondsThisYear / secondsInYear * 100.0);
            double daysPassedThisYear = secondsThisYear / (24.0 * 3600.0);

            // === 构建返回数据（全部保留1位小数，除了整数字段）===
            Map<String, Object> data = new HashMap<>();
            data.put("years", (int) ChronoUnit.YEARS.between(birthDate, today));
            data.put("days", totalDays);
            data.put("progress", String.format("%.1f", lifeProgress));
            data.put("meals", meals);
            data.put("weekends", weekends); // 整数
            data.put("nights", nights);     // 整数

            // 进度百分比（1位小数）
            data.put("todayProgress", String.format("%.1f", todayProgress));
            data.put("weekProgress", String.format("%.1f", weekProgress));
            data.put("monthProgress", String.format("%.1f", monthProgress));
            data.put("yearProgress", String.format("%.1f", yearProgress));

            // 已过去的时间量（1位小数）
            data.put("hoursPassedToday", String.format("%.1f", hoursPassedToday));
            data.put("daysPassedThisWeek", String.format("%.1f", daysPassedThisWeek));
            data.put("daysPassedThisMonth", String.format("%.1f", daysPassedThisMonth));
            data.put("daysPassedThisYear", String.format("%.1f", daysPassedThisYear));

            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("data", data);
            result.put("message", "获取人生进度成功");
            return result;

        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 1);
            result.put("message", "获取人生进度失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 计算完整周末数量（每个周六+周日对 = 1 个周末）
     */
    private long countWeekendCount(LocalDate start, LocalDate end) {
        if (!start.isBefore(end)) {
            return 0;
        }

        long fullWeeks = ChronoUnit.WEEKS.between(start, end);
        long weekendCount = fullWeeks;

        // 检查剩余天数是否构成额外完整周末
        LocalDate current = start.plusWeeks(fullWeeks);
        boolean hasSat = false, hasSun = false;
        while (current.isBefore(end)) {
            DayOfWeek dow = current.getDayOfWeek();
            if (dow == DayOfWeek.SATURDAY) {
                hasSat = true;
            }
            if (dow == DayOfWeek.SUNDAY) {
                hasSun = true;
            }
            current = current.plusDays(1);
        }
        if (hasSat && hasSun) {
            weekendCount++;
        }
        return weekendCount;
    }
}