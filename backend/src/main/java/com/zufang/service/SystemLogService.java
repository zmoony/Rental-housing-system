package com.zufang.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zufang.entity.SystemLog;

import java.util.List;

/**
 * 系统日志服务接口
 */
public interface SystemLogService extends IService<SystemLog> {

    /**
     * 分页查询系统日志
     */
    Page<SystemLog> getSystemLogPage(Integer current, Integer size, String keyword);
    
    /**
     * 根据操作类型获取系统日志
     */
    List<SystemLog> getSystemLogsByOperationType(String operationType);
    
    /**
     * 根据操作用户获取系统日志
     */
    List<SystemLog> getSystemLogsByOperator(String operator);
    
    /**
     * 添加系统日志
     */
    boolean addSystemLog(SystemLog systemLog);
    
    /**
     * 删除系统日志
     */
    boolean deleteSystemLog(Long id);
    
    /**
     * 批量删除系统日志
     */
    boolean batchDeleteSystemLogs(List<Long> ids);
    
    /**
     * 清空系统日志
     */
    boolean clearSystemLogs();
}