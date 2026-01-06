package com.likai.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.likai.api.domain.SolarTerm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SolarTermMapper extends BaseMapper<SolarTerm> {
    // 如需自定义 SQL 可在此添加
}