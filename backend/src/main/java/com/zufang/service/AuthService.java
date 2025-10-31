package com.zufang.service;

import com.zufang.entity.User;
import com.zufang.exception.BusinessException;
import com.zufang.mapper.UserMapper;
import com.zufang.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    public Map<String, Object> login(String username, String password) {
        // 查找用户
        User user = userMapper.getUserByUsername(username);

        if (user == null) {
            throw new BusinessException("USER_NOT_FOUND", "用户不存在");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BusinessException("USER_INACTIVE", "用户状态异常");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("INVALID_PASSWORD", "密码错误");
        }

        // 生成JWT token
        String token = jwtUtil.generateToken(username);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);

        log.info("用户 {} 登录成功", username);

        return result;
    }
}
