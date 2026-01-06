package com.likai.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.likai.api.domain.Event;
import com.likai.api.mapper.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventMapper eventMapper;

    /**
     * 获取用户的所有倒计时事件，按事件时间和置顶状态排序
     * @param userId 用户ID
     * @return 事件列表
     */
    public List<Event> getUserEvents(Long userId) {
        QueryWrapper<Event> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        // 先按置顶状态降序（置顶的在前），再按事件时间升序
        queryWrapper.orderByDesc("is_top").orderByAsc("event_time");
        return eventMapper.selectList(queryWrapper);
    }

    /**
     * 创建新的倒计时事件
     * @param event 事件对象
     * @return 创建的事件
     */
    public Event createEvent(Event event) {
        event.setCreateTime(LocalDate.now());
        event.setIsTop(0); // 默认不置顶
        eventMapper.insert(event);
        return event;
    }

    /**
     * 更新倒计时事件
     * @param event 事件对象
     * @return 更新后的事件
     */
    public Event updateEvent(Event event) {
        eventMapper.updateById(event);
        return event;
    }

    /**
     * 删除倒计时事件
     * @param eventId 事件ID
     * @param userId 用户ID（确保只能删除自己的事件）
     * @return 是否删除成功
     */
    public boolean deleteEvent(Long eventId, Long userId) {
        QueryWrapper<Event> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", eventId).eq("user_id", userId);
        return eventMapper.delete(queryWrapper) > 0;
    }

    /**
     * 设置事件置顶状态
     * @param eventId 事件ID
     * @param userId 用户ID
     * @param isTop 置顶状态
     * @return 是否设置成功
     */
    public boolean setEventTop(Long eventId, Long userId, Integer isTop) {
        UpdateWrapper<Event> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", eventId).eq("user_id", userId)
                     .set("is_top", isTop);
        return eventMapper.update(null, updateWrapper) > 0;
    }
}