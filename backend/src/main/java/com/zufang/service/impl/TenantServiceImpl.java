package com.zufang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zufang.entity.Tenant;
import com.zufang.mapper.TenantMapper;
import com.zufang.service.TenantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 租户服务实现类
 */
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    @Override
    public Page<Tenant> getTenantPage(Integer current, Integer size, String keyword) {
        Page<Tenant> page = new Page<>(current, size);
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        
        // 如果有关键字，可以根据关联的用户信息进行查询
        // 这里需要自定义SQL或者使用多表关联查询
        
        wrapper.orderByDesc(Tenant::getCreatedAt);
        return page(page, wrapper);
    }

    @Override
    public Tenant getTenantByUserId(Long userId) {
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tenant::getUserId, userId);
        return getOne(wrapper);
    }

    @Override
    public List<Tenant> getActiveTenants() {
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tenant::getStatus, "ACTIVE");
        return list(wrapper);
    }

    @Override
    public List<Tenant> getTenantsByStatus(String status) {
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tenant::getStatus, status);
        return list(wrapper);
    }

    @Override
    @Transactional
    public boolean addTenant(Tenant tenant) {
        return save(tenant);
    }

    @Override
    @Transactional
    public boolean updateTenant(Tenant tenant) {
        return updateById(tenant);
    }

    @Override
    @Transactional
    public boolean deleteTenant(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean updateTenantStatus(Long id, String status) {
        Tenant tenant = getById(id);
        if (tenant != null) {
            tenant.setStatus(status);
            return updateById(tenant);
        }
        return false;
    }
}