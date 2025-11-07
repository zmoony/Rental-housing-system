package com.zufang.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zufang.annotation.LogOperation;
import com.zufang.common.ApiResponse;
import com.zufang.dto.TenantDTO;
import com.zufang.entity.Tenant;
import com.zufang.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户管理控制器
 */
@RestController
@RequestMapping("/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    /**
     * 分页获取租户列表
     */
    @GetMapping("/page")
    public ApiResponse getTenantPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        IPage<TenantDTO> page = tenantService.getTenantPage(current, size, keyword, status);
        return ApiResponse.success(page);
    }

    /**
     * 获取租户详情
     */
    @GetMapping("/{id}")
    public ApiResponse getTenant(@PathVariable Long id) {
        Tenant tenant = tenantService.getById(id);
        return tenant != null ? ApiResponse.success(tenant) : ApiResponse.error("租户不存在");
    }

    /**
     * 根据用户ID获取租户信息
     */
    @GetMapping("/user/{userId}")
    public ApiResponse getTenantByUserId(@PathVariable Long userId) {
        Tenant tenant = tenantService.getTenantByUserId(userId);
        return tenant != null ? ApiResponse.success(tenant) : ApiResponse.error("租户不存在");
    }

    /**
     * 获取活跃租户列表
     */
    @GetMapping("/active")
    public ApiResponse getActiveTenants() {
        List<Tenant> tenants = tenantService.getActiveTenants();
        return ApiResponse.success(tenants);
    }

    /**
     * 根据状态获取租户列表
     */
    @GetMapping("/status/{status}")
    public ApiResponse getTenantsByStatus(@PathVariable String status) {
        List<Tenant> tenants = tenantService.getTenantsByStatus(status);
        return ApiResponse.success(tenants);
    }

    /**
     * 添加租户
     */
    @PostMapping
    @LogOperation(operationType = "新增", operationDesc = "添加租户信息")
    public ApiResponse addTenant(@RequestBody TenantDTO tenant) {
        boolean result = tenantService.addTenant(tenant);
        return result ? ApiResponse.success("添加成功") : ApiResponse.error("添加失败");
    }

    /**
     * 更新租户
     */
    @PutMapping
    @LogOperation(operationType = "修改", operationDesc = "更新租户信息")
    public ApiResponse updateTenant(@RequestBody Tenant tenant) {
        boolean result = tenantService.updateTenant(tenant);
        return result ? ApiResponse.success("更新成功") : ApiResponse.error("更新失败");
    }

    /**
     * 删除租户
     */
    @DeleteMapping("/{id}")
    @LogOperation(operationType = "删除", operationDesc = "删除租户信息")
    public ApiResponse deleteTenant(@PathVariable Long id) {
        boolean result = tenantService.deleteTenant(id);
        return result ? ApiResponse.success("删除成功") : ApiResponse.error("删除失败");
    }

    /**
     * 更新租户状态
     */
    @PutMapping("/{id}/status/{status}")
    @LogOperation(operationType = "修改", operationDesc = "更新租户状态")
    public ApiResponse updateTenantStatus(@PathVariable Long id, @PathVariable String status) {
        boolean result = tenantService.updateTenantStatus(id, status);
        return result ? ApiResponse.success("状态更新成功") : ApiResponse.error("状态更新失败");
    }
}
