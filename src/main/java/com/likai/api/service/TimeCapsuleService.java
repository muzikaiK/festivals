package com.likai.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.likai.api.domain.TimeCapsule;
import com.likai.api.mapper.TimeCapsuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimeCapsuleService {

    @Autowired
    private TimeCapsuleMapper timeCapsuleMapper;

    /**
     * 获取用户的所有时光胶囊
     * @param userId 用户ID
     * @return 胶囊列表
     */
    public List<TimeCapsule> getUserTimeCapsules(Long userId) {
        QueryWrapper<TimeCapsule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
        return timeCapsuleMapper.selectList(queryWrapper);
    }

    /**
     * 创建新的时光胶囊
     * @param timeCapsule 胶囊对象
     * @return 创建的胶囊
     */
    public TimeCapsule createTimeCapsule(TimeCapsule timeCapsule) {
        timeCapsule.setCreateTime(LocalDate.now());
        timeCapsule.setStatus(0); // 默认未发送
        timeCapsuleMapper.insert(timeCapsule);
        return timeCapsule;
    }

    /**
     * 更新时光胶囊
     * @param timeCapsule 胶囊对象
     * @return 更新后的胶囊
     */
    public TimeCapsule updateTimeCapsule(TimeCapsule timeCapsule) {
        timeCapsuleMapper.updateById(timeCapsule);
        return timeCapsule;
    }

    /**
     * 删除时光胶囊
     * @param capsuleId 胶囊ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    public boolean deleteTimeCapsule(Long capsuleId, Long userId) {
        QueryWrapper<TimeCapsule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", capsuleId).eq("user_id", userId);
        return timeCapsuleMapper.delete(queryWrapper) > 0;
    }

    /**
     * 获取待发送的胶囊（供定时任务使用）
     * @return 待发送的胶囊列表
     */
    public List<TimeCapsule> getPendingCapsules() {
        LocalDate today = LocalDate.now();
        
        QueryWrapper<TimeCapsule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0) // 未发送
                   .le("send_date", today) // 发送日期小于等于今天
                   .orderByAsc("send_date");
        
        return timeCapsuleMapper.selectList(queryWrapper);
    }

    /**
     * 标记胶囊为已发送（供定时任务使用）
     * @param capsuleId 胶囊ID
     * @return 是否标记成功
     */
    public boolean markAsSent(Long capsuleId) {
        UpdateWrapper<TimeCapsule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", capsuleId)
                     .set("status", 1); // 标记为已发送
        
        return timeCapsuleMapper.update(null, updateWrapper) > 0;
    }
}