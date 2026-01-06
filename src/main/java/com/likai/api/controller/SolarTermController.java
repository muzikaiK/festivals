package com.likai.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.likai.api.common.ApiResult;
import com.likai.api.domain.SolarTerm;
import com.likai.api.service.SolarTermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solar-terms")
public class SolarTermController {

    @Autowired
    private SolarTermService solarTermService;

    // 获取所有节气列表
    @GetMapping("/list")
    public ApiResult getAllTerms() {
        return ApiResult.success(solarTermService.list());
    }

    // 根据季节查询 (例如：查询夏季的所有节气)
    @GetMapping("/season/{season}")
    public ApiResult getTermsBySeason(@PathVariable String season) {
        return ApiResult.success(solarTermService.list(new LambdaQueryWrapper<SolarTerm>().eq(SolarTerm::getSeason, season)));
    }

    // 根据 ID 获取详情
    @GetMapping("/{id}")
    public ApiResult getById(@PathVariable Integer id) {
        return ApiResult.success(solarTermService.getById(id));
    }

    // 新增节气
    @PostMapping("/add")
    public ApiResult save(@RequestBody SolarTerm solarTerm) {
        return ApiResult.success(solarTermService.save(solarTerm));
    }
}