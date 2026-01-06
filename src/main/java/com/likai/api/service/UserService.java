package com.likai.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.likai.api.domain.User;
import com.likai.api.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取或创建默认游客用户
     * @return 游客用户实例
     */
    public User getOrCreateGuestUser() {
        // 定义一个固定的标识，比如一个特殊的openid
        String guestOpenid = "guest_user_default_openid";
        
        // 查询是否已存在该游客用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", guestOpenid);
        User guestUser = userMapper.selectOne(queryWrapper);

        // 如果不存在，则创建一个
        if (guestUser == null) {
            guestUser = new User();
            guestUser.setOpenid(guestOpenid);
            guestUser.setNickname("游客");
            guestUser.setAvatar("/static/default_avatar.png"); // 可以放一个默认头像路径
            guestUser.setCreateTime(LocalDate.now());
            userMapper.insert(guestUser);
        }
        
        return guestUser;
    }

    /**
     * 通过微信openid获取或创建用户
     * @param openid 微信openid
     * @param nickname 微信昵称
     * @param avatar 微信头像
     * @return 用户实例
     */
    public User getOrCreateWxUser(String openid, String nickname, String avatar) {
        // 查询是否已存在该微信用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        User wxUser = userMapper.selectOne(queryWrapper);

        // 如果用户不存在，则新注册
        if (wxUser == null) {
            wxUser = new User();
            wxUser.setOpenid(openid);
            wxUser.setNickname(nickname);
            wxUser.setAvatar(avatar);
            wxUser.setCreateTime(LocalDate.now());
            userMapper.insert(wxUser);
        } else {
            // 如果用户已存在，可以考虑更新昵称和头像（如果它们变了）
            // 这里为了简单起见，先不更新，但可以扩展
        }

        return wxUser;
    }
}