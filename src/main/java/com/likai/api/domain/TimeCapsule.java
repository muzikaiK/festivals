package com.likai.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("time_capsule")
public class TimeCapsule {
    /**
     * 胶囊ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 胶囊标题
     */
    private String title;

    /**
     * 胶囊内容
     */
    private String content;

    /**
     * 发送日期
     */
    private LocalDate sendDate;

    /**
     * 发送状态 (0-未发送, 1-已发送)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDate createTime;
}