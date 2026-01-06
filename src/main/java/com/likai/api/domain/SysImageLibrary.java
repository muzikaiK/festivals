package com.likai.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_image_library")
public class SysImageLibrary {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 图片URL
     */
    private String imgUrl;

    /**
     * 分类
     */
    private String category;

    /**
     * 是否激活
     */
    private Integer isActive;

    /**
     * 排序
     */
    private Integer sort;
}