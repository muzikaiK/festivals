package com.likai.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.likai.api.domain.SolarTerm;
import com.likai.api.mapper.SolarTermMapper;
import com.likai.api.service.SolarTermService;
import org.springframework.stereotype.Service;

@Service
public class SolarTermServiceImpl extends ServiceImpl<SolarTermMapper, SolarTerm> implements SolarTermService {
}