package com.likai.api.controller;

import com.likai.api.domain.Event;
import com.likai.api.domain.User;
import com.likai.api.service.EventService;
import com.likai.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private UserService userService;

    /**
     * 获取用户的所有倒计时事件
     * @param openid 用户openid（从Header获取）
     * @return 事件列表
     */
    @GetMapping
    public Map<String, Object> getUserEvents(@RequestHeader(value = "X-User-Openid", required = false) String openid) {
        User user = getUserByOpenid(openid);
        List<Event> events = eventService.getUserEvents(user.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", events);
        result.put("message", "获取事件列表成功");
        return result;
    }

    /**
     * 创建新的倒计时事件
     * @param event 事件对象
     * @param openid 用户openid
     * @return 创建结果
     */
    @PostMapping
    public Map<String, Object> createEvent(@RequestBody Event event,
                                          @RequestHeader(value = "X-User-Openid", required = false) String openid) {
        User user = getUserByOpenid(openid);
        event.setUserId(user.getId());
        Event createdEvent = eventService.createEvent(event);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", createdEvent);
        result.put("message", "创建事件成功");
        return result;
    }

    /**
     * 更新倒计时事件
     * @param eventId 事件ID
     * @param event 更新的事件对象
     * @param openid 用户openid
     * @return 更新结果
     */
    @PutMapping("/{eventId}")
    public Map<String, Object> updateEvent(@PathVariable Long eventId,
                                          @RequestBody Event event,
                                          @RequestHeader(value = "X-User-Openid", required = false) String openid) {
        User user = getUserByOpenid(openid);
        event.setId(eventId);
        event.setUserId(user.getId());
        Event updatedEvent = eventService.updateEvent(event);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", updatedEvent);
        result.put("message", "更新事件成功");
        return result;
    }

    /**
     * 删除倒计时事件
     * @param eventId 事件ID
     * @param openid 用户openid
     * @return 删除结果
     */
    @DeleteMapping("/{eventId}")
    public Map<String, Object> deleteEvent(@PathVariable Long eventId,
                                           @RequestHeader(value = "X-User-Openid", required = false) String openid) {
        User user = getUserByOpenid(openid);
        boolean success = eventService.deleteEvent(eventId, user.getId());
        
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("code", 0);
            result.put("message", "删除事件成功");
        } else {
            result.put("code", 1);
            result.put("message", "删除事件失败");
        }
        return result;
    }

    /**
     * 设置事件置顶状态
     * @param eventId 事件ID
     * @param isTop 置顶状态
     * @param openid 用户openid
     * @return 设置结果
     */
    @PutMapping("/{eventId}/top")
    public Map<String, Object> setEventTop(@PathVariable Long eventId,
                                          @RequestParam Integer isTop,
                                          @RequestHeader(value = "X-User-Openid", required = false) String openid) {
        User user = getUserByOpenid(openid);
        boolean success = eventService.setEventTop(eventId, user.getId(), isTop);
        
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("code", 0);
            result.put("message", "设置置顶状态成功");
        } else {
            result.put("code", 1);
            result.put("message", "设置置顶状态失败");
        }
        return result;
    }

    /**
     * 根据openid获取用户信息
     * @param openid 微信openid
     * @return 用户对象
     */
    private User getUserByOpenid(String openid) {
        if (openid != null && !openid.isEmpty()) {
            return userService.getOrCreateWxUser(openid, "微信用户", "");
        } else {
            return userService.getOrCreateGuestUser();
        }
    }
}