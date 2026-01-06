package com.likai.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likai.api.domain.SysCalendarConfig;
import com.likai.api.mapper.SysCalendarConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    @Autowired
    private SysCalendarConfigMapper calendarInfoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 获取指定类型的日历事件（节气/节假日），并计算天数差异
     * @param types 事件类型列表 (1-节气, 2-传统节日, 3-法定假)
     * @return 包含天数差异的事件列表
     */
    public List<Map<String, Object>> getCalendarEvents(List<Integer> types) {
        LocalDate today = LocalDate.now();
        QueryWrapper<SysCalendarConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("type", types)
                    .orderByAsc("full_date"); 

        List<SysCalendarConfig> events = calendarInfoMapper.selectList(queryWrapper);

        return events.stream().map(event -> {
            long daysDifference = ChronoUnit.DAYS.between(today, event.getFullDate());
            Map<String, Object> eventMap = objectMapper.convertValue(event, Map.class);
            eventMap.put("daysDifference", daysDifference);
            return eventMap;
        }).collect(Collectors.toList());
    }
}