package com.likai.api.controller;

import com.likai.api.domain.SysImageLibrary;
import com.likai.api.service.SysImageLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class SysImageLibraryController {

    @Autowired
    private SysImageLibraryService imageLibraryService;

    /**
     * 获取所有背景图片
     * @return 图片列表
     */
    @GetMapping("/list")
    public Map<String, Object> getAllImages() {
        List<SysImageLibrary> images = imageLibraryService.getAllImages();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", images);
        result.put("message", "获取图片列表成功");
        return result;
    }

    /**
     * 按分类获取背景图片
     * @param category 分类
     * @return 图片列表
     */
    @GetMapping("/listByCategory")
    public Map<String, Object> getImagesByCategory(@RequestParam String category) {
        List<SysImageLibrary> images = imageLibraryService.getImagesByCategory(category);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", images);
        result.put("message", "按分类获取图片列表成功");
        return result;
    }
}