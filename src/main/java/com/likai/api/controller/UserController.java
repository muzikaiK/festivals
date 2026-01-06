package com.likai.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likai.api.domain.User;
import com.likai.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.wechat.appid}")
    private String wechatAppid;

    @Value("${api.wechat.secret}")
    private String wechatSecret;

    /**
     * 获取当前用户信息
     * 前端在每次请求时，可以在Header中携带一个自定义的token（如openid）来标识用户。
     * 如果没有携带，则视为游客。
     * @param openid 微信openid (从Header或请求参数中获取)
     * @return 用户信息
     */
    @GetMapping("/current")
    public Map<String, Object> getCurrentUser(@RequestHeader(value = "X-User-Openid", required = false) String openid) {
        User user;
        if (openid != null && !openid.isEmpty()) {
            user = userService.getOrCreateWxUser(openid, "微信用户", ""); 
        } else {
            user = userService.getOrCreateGuestUser();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", user);
        result.put("message", "获取用户信息成功");
        return result;
    }

    /**
     * 微信小程序登录接口
     * 前端通过wx.login()获取code，发送到后端
     * 后端通过code向微信服务器换取openid和session_key
     * @param code 微信登录凭证
     * @return 包含自定义登录态（token）和用户信息
     */
    @PostMapping("/wxLogin")
    public Map<String, Object> wxLogin(@RequestParam String code) {
        Map<String, Object> result = new HashMap<>();
        try {
            if ("YOUR_WECHAT_APPID".equals(wechatAppid) || "YOUR_WECHAT_SECRET".equals(wechatSecret)) {
                result.put("code", 2);
                result.put("message", "微信登录失败: AppID 或 Secret 未配置");
                return result;
            }

            String url = String.format(
                    "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    wechatAppid, wechatSecret, code);
            
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);

            if (responseMap == null || responseMap.get("openid") == null) {
                result.put("code", 1);
                result.put("message", "微信登录失败: " + responseMap.get("errmsg"));
                return result;
            }

            String openid = (String) responseMap.get("openid");
            // 默认昵称和头像，后续可提供接口让用户更新
            String defaultNickname = "微信用户";
            String defaultAvatar = "https://path/to/default/avatar.png";

            User user = userService.getOrCreateWxUser(openid, defaultNickname, defaultAvatar);

            String token = openid; // 简化处理，直接用openid当token

            result.put("code", 0);
            result.put("message", "登录成功");
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userInfo", user);
            result.put("data", data);
            return result;

        } catch (Exception e) {
            result.put("code", 1);
            result.put("message", "微信登录异常: " + e.getMessage());
            return result;
        }
    }
}