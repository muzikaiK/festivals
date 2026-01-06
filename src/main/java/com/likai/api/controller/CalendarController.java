package com.likai.api.controller;

import com.likai.api.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    /**
     * 获取即将到来的最近的节气和节假日
     * @return 包含最近节气和最近节假日的Map
     */
    @GetMapping("/upcoming")
    public Map<String, Object> getUpcomingEvents() {
        List<Map<String, Object>> solarTerms = calendarService.getCalendarEvents(Collections.singletonList(1));
        List<Map<String, Object>> holidays = calendarService.getCalendarEvents(Arrays.asList(2, 3));

        Optional<Map<String, Object>> nearestSolarTerm = findNearestEvent(solarTerms);
        Optional<Map<String, Object>> nearestHoliday = findNearestEvent(holidays);

        Map<String, Object> data = new HashMap<>();
        data.put("nearestSolarTerm", nearestSolarTerm.orElse(null));
        data.put("nearestHoliday", nearestHoliday.orElse(null));

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", data);
        result.put("message", "获取成功");
        return result;
    }

    /**
     * 获取节气信息（最近的节气和节气列表）
     * @return 包含最近节气和完整列表的Map
     */
    @GetMapping("/solar-terms")
    public Map<String, Object> getSolarTerms() {
        List<Map<String, Object>> solarTerms = calendarService.getCalendarEvents(Collections.singletonList(1));
        return buildResponse(solarTerms);
    }

    /**
     * 获取节假日信息（最近的节假日和节假日列表）
     * @return 包含最近节假日和完整列表的Map
     */
    @GetMapping("/holidays")
    public Map<String, Object> getHolidays() {
        List<Map<String, Object>> holidays = calendarService.getCalendarEvents(Arrays.asList(2, 3));
        return buildResponse(holidays);
    }

    /**
     * 构建统一的API响应结构
     * @param eventList 事件列表
     * @return 包含最近事件和完整列表的Map
     */
    private Map<String, Object> buildResponse(List<Map<String, Object>> eventList) {
        Map<String, Object> result = new HashMap<>();
        if (eventList == null || eventList.isEmpty()) {
            result.put("code", 1);
            result.put("message", "暂无相关事件信息");
            return result;
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("nearest", findNearestEvent(eventList).orElse(null));
        data.put("list", eventList);

        result.put("code", 0);
        result.put("data", data);
        result.put("message", "获取成功");
        return result;
    }

    /**
     * 从事件列表中找出“最近”的事件
     * “最近”定义为：第一个未来的事件；如果没有未来的事件，则是最后一个过去的事件。
     * @param eventList 经过排序的事件列表
     * @return 最近事件的Optional
     */
    private Optional<Map<String, Object>> findNearestEvent(List<Map<String, Object>> eventList) {
        if (eventList == null || eventList.isEmpty()) {
            return Optional.empty();
        }

        // 优先寻找第一个未来的（或今天的）事件
        Optional<Map<String, Object>> upcomingEvent = eventList.stream()
                .filter(e -> (long) e.get("daysDifference") >= 0)
                .findFirst();

        // 如果有未来的事件，则直接返回
        if (upcomingEvent.isPresent()) {
            return upcomingEvent;
        }

        // 如果没有未来的事件，则返回列表中的最后一个（即最近过去的）
        return Optional.of(eventList.get(eventList.size() - 1));
    }
}