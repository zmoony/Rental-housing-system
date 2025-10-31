package com.zufang.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zufang.common.ApiResponse;
import com.zufang.entity.SystemLog;
import com.zufang.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统日志管理控制器
 */
@RestController
@RequestMapping("/system-logs")
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    /**
     * 分页获取系统日志列表
     */
    @GetMapping("/page")
    public ApiResponse getSystemLogPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<SystemLog> page = systemLogService.getSystemLogPage(current, size, keyword);
        return ApiResponse.success(page);
    }

    /**
     * 获取系统日志详情
     */
    @GetMapping("/{id}")
    public ApiResponse getSystemLog(@PathVariable Long id) {
        SystemLog systemLog = systemLogService.getById(id);
        return systemLog != null ? ApiResponse.success(systemLog) : ApiResponse.error("系统日志不存在");
    }

    /**
     * 根据操作类型获取系统日志
     */
    @GetMapping("/type/{operationType}")
    public ApiResponse getSystemLogsByOperationType(@PathVariable String operationType) {
        List<SystemLog> logs = systemLogService.getSystemLogsByOperationType(operationType);
        return ApiResponse.success(logs);
    }

    /**
     * 根据操作用户获取系统日志
     */
    @GetMapping("/operator/{operator}")
    public ApiResponse getSystemLogsByOperator(@PathVariable String operator) {
        List<SystemLog> logs = systemLogService.getSystemLogsByOperator(operator);
        return ApiResponse.success(logs);
    }

    /**
     * 添加系统日志
     */
    @PostMapping
    public ApiResponse addSystemLog(@RequestBody SystemLog systemLog) {
        boolean result = systemLogService.addSystemLog(systemLog);
        return result ? ApiResponse.success("添加成功") : ApiResponse.error("添加失败");
    }

    /**
     * 删除系统日志
     */
    @DeleteMapping("/{id}")
    public ApiResponse deleteSystemLog(@PathVariable Long id) {
        boolean result = systemLogService.deleteSystemLog(id);
        return result ? ApiResponse.success("删除成功") : ApiResponse.error("删除失败");
    }

    /**
     * 批量删除系统日志
     */
    @DeleteMapping("/batch")
    public ApiResponse batchDeleteSystemLogs(@RequestBody List<Long> ids) {
        boolean result = systemLogService.batchDeleteSystemLogs(ids);
        return result ? ApiResponse.success("批量删除成功") : ApiResponse.error("批量删除失败");
    }

    /**
     * 清空系统日志
     */
    @DeleteMapping("/clear")
    public ApiResponse clearSystemLogs() {
        boolean result = systemLogService.clearSystemLogs();
        return result ? ApiResponse.success("清空成功") : ApiResponse.error("清空失败");
    }
}