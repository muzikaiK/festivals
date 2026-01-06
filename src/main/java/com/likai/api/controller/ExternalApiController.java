package com.likai.api.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.apache.tomcat.util.json.JSONFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class ExternalApiController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.ipgeolocation.key}")
    private String ipGeolocationApiKey;

    /**
     * 历史上的今天
     * @param month 月份
     * @param day 日期
     * @return 历史事件
     */
    @GetMapping("/history/today")
    public Map<String, Object> getHistoryToday(@RequestParam int month, @RequestParam int day) {
        try {
            String url = "https://api.52vmy.cn/api/wl/today?type=text";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("data", response);
            result.put("message", "获取历史事件成功");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 1);
            result.put("message", "获取历史事件失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 获取所有星座列表
     * @return 星座列表
     */
    @GetMapping("/constellations")
    public Map<String, Object> getConstellations() {
        List<Map<String, String>> constellations = new ArrayList<>();
        constellations.add(createConstellation("白羊座", "Aries", "3/21 - 4/19"));
        constellations.add(createConstellation("金牛座", "Taurus", "4/20 - 5/20"));
        constellations.add(createConstellation("双子座", "Gemini", "5/21 - 6/21"));
        constellations.add(createConstellation("巨蟹座", "Cancer", "6/22 - 7/22"));
        constellations.add(createConstellation("狮子座", "Leo", "7/23 - 8/22"));
        constellations.add(createConstellation("处女座", "Virgo", "8/23 - 9/22"));
        constellations.add(createConstellation("天秤座", "Libra", "9/23 - 10/23"));
        constellations.add(createConstellation("天蝎座", "Scorpio", "10/24 - 11/22"));
        constellations.add(createConstellation("射手座", "Sagittarius", "11/23 - 12/21"));
        constellations.add(createConstellation("摩羯座", "Capricorn", "12/22 - 1/19"));
        constellations.add(createConstellation("水瓶座", "Aquarius", "1/20 - 2/18"));
        constellations.add(createConstellation("双鱼座", "Pisces", "2/19 - 3/20"));

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", constellations);
        result.put("message", "获取星座列表成功");
        return result;
    }

    private Map<String, String> createConstellation(String name, String englishName, String dateRange) {
        Map<String, String> constellation = new HashMap<>();
        constellation.put("name", name);
        constellation.put("englishName", englishName);
        constellation.put("dateRange", dateRange);
        return constellation;
    }

    /**
     * 今日星座运势
     * @param constellation 星座名称 (如: scorpio, aries等)
     * @return 星座运势
     */
    @GetMapping("/constellation/today")
    public Map<String, Object> getConstellationToday(@RequestParam String constellation) {
        try {
            String url = "http://api.suxun.site/api/constellation?type=" + constellation + "&time=today";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("data", response);
            result.put("message", "获取星座运势成功");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 1);
            result.put("message", "获取星座运势失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 随机一言
     * @return 一言内容
     */
    @GetMapping("/yiyan")
    public Map<String, Object> getYiyan() {
        try {
//            // 随机选择一个一言API
//            String[] apis = {
//                "https://v.api.aa1.cn/api/yiyan/index.php",
//                "https://api.xygeng.cn/one",
//                "https://v1.jinrishici.com/renshu.txt"
//            };
//
//            Random random = new Random();
//            String selectedApi = apis[random.nextInt(apis.length)];
            String selectedApi = "https://api.52vmy.cn/api/wl/yan/yiyan";
            String response = restTemplate.getForObject(selectedApi, String.class);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("data", JSONUtil.parseObj(response).getJSONObject("data").getStr("hitokoto"));
            result.put("message", "获取一言成功");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 1);
            result.put("message", "获取一言失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 60s看世界新闻
     * @return 新闻图片URL
     */
    @GetMapping("/news/60s")
    public Map<String, Object> getNews60s() {
        try {
            String url = "https://60s.lylme.com/";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("data", response);
            result.put("message", "获取60s新闻成功");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 1);
            result.put("message", "获取60s新闻失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 月相与日出日落信息
     * @param lat 纬度
     * @param lon 经度
     * @return 天文信息
     */
    @GetMapping("/astronomy/today")
    public Map<String, Object> getAstronomyToday(
            @RequestParam(defaultValue = "39.9042") String lat,
            @RequestParam(defaultValue = "116.4074") String lon) {
        try {
            if ("YOUR_IPGEOLOCATION_API_KEY".equals(ipGeolocationApiKey)) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 2);
                result.put("message", "获取天文信息失败: API Key未配置");
                return result;
            }
            String url = String.format(
                    "https://api.ipgeolocation.io/astronomy?apiKey=%s&lat=%s&long=%s",
                    ipGeolocationApiKey, lat, lon);
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("data", response);
            result.put("message", "获取天文信息成功");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 1);
            result.put("message", "获取天文信息失败: " + e.getMessage());
            return result;
        }
    }
}