package com.zufang.controller;

import com.zufang.common.ApiResponse;
import com.zufang.entity.User;
import com.zufang.exception.BusinessException;
import com.zufang.mapper.UserMapper;
import com.zufang.service.AuthService;
import com.zufang.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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
  private final JwtUtil jwtUtil;
  private final UserMapper userMapper;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Map<String, Object> result = authService.login(username, password);
        return ResponseEntity.ok(ApiResponse.success(result));
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
    public ResponseEntity<ApiResponse<User>> getUserInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException("未提供有效的认证token");
        }

        String token = authHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
