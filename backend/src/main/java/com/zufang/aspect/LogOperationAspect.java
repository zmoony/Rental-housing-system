package com.zufang.aspect;

import com.zufang.annotation.LogOperation;
import com.zufang.entity.SystemLog;
import com.zufang.mapper.UserMapper;
import com.zufang.service.SystemLogService;
import com.zufang.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 操作日志切面
 */
@Aspect
@Component
public class LogOperationAspect {

    @Autowired
    private SystemLogService systemLogService;
    @Autowired
    private  UserMapper userMapper;

    /**
     * 定义切点 - 所有带有LogOperation注解的方法
     */
    @Pointcut("@annotation(com.zufang.annotation.LogOperation)")
    public void logPointCut() {
    }

    /**
     * 在方法执行后记录日志
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint) {
        try {
            // 获取当前请求对象
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            // 获取注解信息
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            LogOperation logOperation = method.getAnnotation(LogOperation.class);

            // 获取当前登录用户ID
            String userName = SecurityUtils.getCurrentUsername();
            Long userId = userMapper.getUserByUsername(userName).getId();

            // 创建系统日志对象
            SystemLog systemLog = new SystemLog();
            systemLog.setUserId(userId);
            systemLog.setAction(logOperation.operationType());
            systemLog.setDescription(logOperation.operationDesc());
            systemLog.setModule(signature.getDeclaringTypeName());
            systemLog.setIpAddress(getIpAddress(request));
            systemLog.setUserAgent(request.getHeader("User-Agent"));

            // 保存系统日志
            systemLogService.addSystemLog(systemLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取请求IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}