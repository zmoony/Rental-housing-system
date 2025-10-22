package com.zufang.service;

import com.zufang.entity.User;
import com.zufang.mapper.UserMapper;
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

    /**
     * 用户登录
     */
    public Map<String, Object> login(String username, String password) {
        // 查找用户
        User user = userMapper.getUserByUsername(username);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new RuntimeException("用户状态异常");
        }
        
        // 验证密码（这里简化处理，实际应该使用BCrypt）
        if (!password.equals("admin123")) {
            throw new RuntimeException("密码错误");
        }
        
        // 生成JWT token（这里简化处理）
        String token = "mock-jwt-token-" + System.currentTimeMillis();
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        
        log.info("用户 {} 登录成功", username);
        
        return result;
    }
}
