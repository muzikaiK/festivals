package com.likai.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("sys_calendar_config")
public class SysCalendarConfig {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 节气或节日名
     */
    private String name;

    /**
     * 1-24节气, 2-传统节日, 3-法定假
     */
    private Integer type;

    /**
     * 2026年的具体公历日期
     */
    private LocalDate fullDate;

    /**
     * 年份: 如 2026
     */
    private Integer cYear;

    /**
     * 月份: 1-12
     */
    private Integer cMonth;

    /**
     * 日期: 1-31
     */
    private Integer cDay;

    /**
     * 是否为农历(0-公历, 1-农历)
     */
    private Integer isLunar;

    /**
     * 简短介绍
     */
    private String intro;

    /**
     * 详细故事由来
     */
    private String story;

    /**
     * 详情页背景图
     */
    private String bgImageUrl;
}