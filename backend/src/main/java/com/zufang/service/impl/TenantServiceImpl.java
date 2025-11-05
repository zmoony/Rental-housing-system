package com.zufang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zufang.common.Constants;
import com.zufang.dto.TenantDTO;
import com.zufang.entity.Tenant;
import com.zufang.entity.User;
import com.zufang.exception.BusinessException;
import com.zufang.mapper.TenantMapper;
import com.zufang.service.TenantService;
import com.zufang.service.UsersService;
import com.zufang.utils.ChineseToPinYinUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 租户服务实现类
 */
@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    private final UsersService userService;

    @Override
    public Page<TenantDTO> getTenantPage(Integer current, Integer size, String keyword, String status) {
        Page<Tenant> page = new Page<>(current, size);
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();

        // 如果有关键字，可以根据姓名/电话进行查询 status根据状态搜索
        // 这里需要自定义SQL或者使用多表关联查询

        wrapper.orderByDesc(Tenant::getCreatedAt);
        Page<Tenant> tenantPage = page(page, wrapper);
        return ;
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
    @Transactional(rollbackFor = Exception.class)
    public boolean addTenant(TenantDTO tenant) {
        //保存用户
        User user = new User();
        user.setUsername(ChineseToPinYinUtil.getFullNamePinyin(tenant.getName()));
        user.setRealName(tenant.getName());
        user.setPhone(tenant.getPhone());
        user.setIdCard(tenant.getIdCard());
        user.setPassword(Constants.PASSWORD_DEFAULT);
        boolean userSaved = userService.save(user);
        if(!userSaved){
            throw new BusinessException("保存用户失败");
        }
        tenant.setUserId(user.getId());
        boolean tenantSaved = save(tenant);
        if(!tenantSaved){
            throw new BusinessException("保存租户失败");
        }
        return true;
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
