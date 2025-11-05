package com.zufang.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zufang.dto.TenantDTO;
import com.zufang.entity.Tenant;

import java.util.List;

/**
 * 租户服务接口
 */
public interface TenantService extends IService<Tenant> {

    /**
     * 分页查询租户列表
     */
    Page<TenantDTO> getTenantPage(Integer current, Integer size, String keyword, String status);

    /**
     * 根据用户ID获取租户信息
     */
    Tenant getTenantByUserId(Long userId);

    /**
     * 获取活跃租户列表
     */
    List<Tenant> getActiveTenants();

    /**
     * 根据状态获取租户列表
     */
    List<Tenant> getTenantsByStatus(String status);

    /**
     * 添加租户
     */
    boolean addTenant(TenantDTO tenant);

    /**
     * 更新租户信息
     */
    boolean updateTenant(Tenant tenant);

    /**
     * 删除租户
     */
    boolean deleteTenant(Long id);

    /**
     * 更新租户状态
     */
    boolean updateTenantStatus(Long id, String status);
}
