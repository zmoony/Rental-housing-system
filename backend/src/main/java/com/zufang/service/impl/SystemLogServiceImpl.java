package com.zufang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zufang.entity.SystemLog;
import com.zufang.mapper.SystemLogMapper;
import com.zufang.service.SystemLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 系统日志服务实现类
 */
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogService {

    @Override
    public Page<SystemLog> getSystemLogPage(Integer current, Integer size, String keyword) {
        Page<SystemLog> page = new Page<>(current, size);
        LambdaQueryWrapper<SystemLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.like(SystemLog::getAction, keyword)
                  .or()
                  .like(SystemLog::getDescription, keyword)
                  .or()
                  .like(SystemLog::getModule, keyword);
        }

        wrapper.orderByDesc(SystemLog::getCreatedAt);
        return page(page, wrapper);
    }

    @Override
    public List<SystemLog> getSystemLogsByOperationType(String operationType) {
        LambdaQueryWrapper<SystemLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemLog::getAction, operationType);
        wrapper.orderByDesc(SystemLog::getCreatedAt);
        return list(wrapper);
    }

    @Override
    public List<SystemLog> getSystemLogsByOperator(String operator) {
        LambdaQueryWrapper<SystemLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemLog::getAction, operator);
        wrapper.orderByDesc(SystemLog::getCreatedAt);
        return list(wrapper);
    }

    @Override
    @Transactional
    public boolean addSystemLog(SystemLog systemLog) {
        return save(systemLog);
    }

    @Override
    @Transactional
    public boolean deleteSystemLog(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean batchDeleteSystemLogs(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    @Transactional
    public boolean clearSystemLogs() {
        LambdaQueryWrapper<SystemLog> wrapper = new LambdaQueryWrapper<>();
        return remove(wrapper);
    }
}