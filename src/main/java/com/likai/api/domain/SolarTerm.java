package com.likai.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("solar_terms")
public class SolarTerm {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String name;           // 节气名称
    private String startDate;      // 开始日期范围
    private String endDate;        // 结束日期
    private String season;         // 所属季节 (春/夏/秋/冬)
    private String description;    // 核心描述
    private String characteristics; // 气候特点
    private String iconDesc;       // 配图说明
}