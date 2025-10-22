package com.zufang.controller;

import com.zufang.entity.User;
import com.zufang.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        Map<String, Object> result = authService.login(username, password);
        return ResponseEntity.ok(result);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // 这里可以实现登出逻辑，比如将token加入黑名单
        return ResponseEntity.ok().build();
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/user-info")
    public ResponseEntity<User> getUserInfo() {
        // 这里应该从JWT token中获取用户信息
        // 暂时返回模拟数据
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setRealName("系统管理员");
        user.setRole("ADMIN");
        user.setStatus("ACTIVE");
        
        return ResponseEntity.ok(user);
    }
}
