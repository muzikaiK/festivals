package com.likai.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.likai.api.domain.SysImageLibrary;
import com.likai.api.mapper.SysImageLibraryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysImageLibraryService {

    @Autowired
    private SysImageLibraryMapper imageLibraryMapper;

    /**
     * 获取所有图片
     * @return 图片列表
     */
    public List<SysImageLibrary> getAllImages() {
        QueryWrapper<SysImageLibrary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_active", 1).orderByAsc("sort");
        return imageLibraryMapper.selectList(queryWrapper);
    }

    /**
     * 按分类获取图片
     * @param category 分类名
     * @return 图片列表
     */
    public List<SysImageLibrary> getImagesByCategory(String category) {
        QueryWrapper<SysImageLibrary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category)
                    .eq("is_active", 1)
                    .orderByAsc("sort");
        return imageLibraryMapper.selectList(queryWrapper);
    }
}