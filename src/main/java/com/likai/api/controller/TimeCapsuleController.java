package com.likai.api.controller;

import com.likai.api.domain.TimeCapsule;
import com.likai.api.domain.User;
import com.likai.api.service.TimeCapsuleService;
import com.likai.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timeCapsules")
public class TimeCapsuleController {

    @Autowired
    private TimeCapsuleService timeCapsuleService;
    
    @Autowired
    private UserService userService;

    /**
     * 获取用户的所有时光胶囊
     * @param openid 用户openid
     * @return 胶囊列表
     */
    @GetMapping
    public Map<String, Object> getUserTimeCapsules(@RequestHeader(value = "X-User-Openid", required = false) String openid) {
        User user = getUserByOpenid(openid);
        List<TimeCapsule> capsules = timeCapsuleService.getUserTimeCapsules(user.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", capsules);
        result.put("message", "获取时光胶囊列表成功");
        return result;
    }

    /**
     * 创建新的时光胶囊
     * @param timeCapsule 胶囊对象
     * @param openid 用户openid
     * @return 创建结果
     */
    @PostMapping
    public Map<String, Object> createTimeCapsule(@RequestBody TimeCapsule timeCapsule,
                                                  @RequestHeader(value = "X-User-Openid", required = false) String openid) {
        User user = getUserByOpenid(openid);
        timeCapsule.setUserId(user.getId());
        TimeCapsule createdCapsule = timeCapsuleService.createTimeCapsule(timeCapsule);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", createdCapsule);
        result.put("message", "创建时光胶囊成功");
        return result;
    }

    /**
     * 更新时光胶囊
     * @param capsuleId 胶囊ID
     * @param timeCapsule 更新的胶囊对象
     * @param openid 用户openid
     * @return 更新结果
     */
    @PutMapping("/{capsuleId}")
    public Map<String, Object> updateTimeCapsule(@PathVariable Long capsuleId,
                                                 @RequestBody TimeCapsule timeCapsule,
                                                 @RequestHeader(value = "X-User-Openid", required = false) String openid) {
        User user = getUserByOpenid(openid);
        timeCapsule.setId(capsuleId);
        timeCapsule.setUserId(user.getId());
        TimeCapsule updatedCapsule = timeCapsuleService.updateTimeCapsule(timeCapsule);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", updatedCapsule);
        result.put("message", "更新时光胶囊成功");
        return result;
    }

    /**
     * 删除时光胶囊
     * @param capsuleId 胶囊ID
     * @param openid 用户openid
     * @return 删除结果
     */
    @DeleteMapping("/{capsuleId}")
    public Map<String, Object> deleteTimeCapsule(@PathVariable Long capsuleId,
                                                 @RequestHeader(value = "X-User-Openid", required = false) String openid) {
        User user = getUserByOpenid(openid);
        boolean success = timeCapsuleService.deleteTimeCapsule(capsuleId, user.getId());
        
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("code", 0);
            result.put("message", "删除时光胶囊成功");
        } else {
            result.put("code", 1);
            result.put("message", "删除时光胶囊失败");
        }
        return result;
    }

    /**
     * 获取待发送的胶囊（供定时任务使用）
     * @return 待发送的胶囊列表
     */
    @GetMapping("/pending")
    public Map<String, Object> getPendingCapsules() {
        List<TimeCapsule> pendingCapsules = timeCapsuleService.getPendingCapsules();
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", pendingCapsules);
        result.put("message", "获取待发送胶囊成功");
        return result;
    }

    /**
     * 标记胶囊为已发送（供定时任务使用）
     * @param capsuleId 胶囊ID
     * @return 标记结果
     */
    @PutMapping("/{capsuleId}/send")
    public Map<String, Object> markAsSent(@PathVariable Long capsuleId) {
        boolean success = timeCapsuleService.markAsSent(capsuleId);
        
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("code", 0);
            result.put("message", "标记为已发送成功");
        } else {
            result.put("code", 1);
            result.put("message", "标记为已发送失败");
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