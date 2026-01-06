package com.likai.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("event")
public class Event {
    /**
     * 事件ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件时间
     */
    private LocalDate eventTime;

    /**
     * 事件描述
     */
    private String description;

    /**
     * 是否置顶 (0-否, 1-是)
     */
    private Integer isTop;

    /**
     * 创建时间
     */
    private LocalDate createTime;
}