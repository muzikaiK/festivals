package com.likai.api.task;

import com.likai.api.domain.TimeCapsule;
import com.likai.api.service.TimeCapsuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimeCapsuleTask {

    private static final Logger logger = LoggerFactory.getLogger(TimeCapsuleTask.class);

    @Autowired
    private TimeCapsuleService timeCapsuleService;

    /**
     * 每天早上8点检查待发送的时光胶囊
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkAndSendTimeCapsules() {
        logger.info("开始检查待发送的时光胶囊...");
        
        try {
            List<TimeCapsule> pendingCapsules = timeCapsuleService.getPendingCapsules();
            
            if (pendingCapsules.isEmpty()) {
                logger.info("没有待发送的时光胶囊");
                return;
            }
            
            logger.info("发现 {} 个待发送的时光胶囊", pendingCapsules.size());
            
            for (TimeCapsule capsule : pendingCapsules) {
                try {
                    // TODO: 这里调用微信服务通知API发送消息
                    boolean sendSuccess = sendWechatNotification(capsule);
                    
                    if (sendSuccess) {
                        // 标记为已发送
                        timeCapsuleService.markAsSent(capsule.getId());
                        logger.info("时光胶囊 {} 发送成功", capsule.getId());
                    } else {
                        logger.error("时光胶囊 {} 发送失败", capsule.getId());
                    }
                } catch (Exception e) {
                    logger.error("处理时光胶囊 {} 时发生错误: {}", capsule.getId(), e.getMessage());
                }
            }
            
        } catch (Exception e) {
            logger.error("检查时光胶囊任务执行失败: {}", e.getMessage());
        }
        
        logger.info("时光胶囊检查任务完成");
    }

    /**
     * 发送微信服务通知
     * @param capsule 时光胶囊
     * @return 是否发送成功
     */
    private boolean sendWechatNotification(TimeCapsule capsule) {
        // TODO: 实现微信服务通知逻辑
        // 1. 获取用户的openid
        // 2. 调用微信服务通知API
        // 3. 发送胶囊内容
        
        logger.info("模拟发送微信通知: 用户ID={}, 标题={}, 内容={}", 
                   capsule.getUserId(), capsule.getTitle(), capsule.getContent());
        
        // 暂时返回true，模拟发送成功
        return true;
    }
}