package com.zufang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zufang.entity.FeeConfig;
import com.zufang.mapper.FeeConfigMapper;
import com.zufang.service.FeeConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 费用配置服务实现类
 */
@Service
public class FeeConfigServiceImpl extends ServiceImpl<FeeConfigMapper, FeeConfig> implements FeeConfigService {

    @Override
    public Page<FeeConfig> getFeeConfigPage(Integer current, Integer size, String keyword) {
        Page<FeeConfig> page = new Page<>(current, size);
        LambdaQueryWrapper<FeeConfig> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.like(FeeConfig::getConfigName, keyword)
                  .or()
                  .like(FeeConfig::getConfigType, keyword);
        }

        wrapper.orderByDesc(FeeConfig::getCreatedAt);
        return page(page, wrapper);
    }

    @Override
    public List<FeeConfig> getFeeConfigsByType(String feeType) {
        LambdaQueryWrapper<FeeConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeeConfig::getConfigType, feeType);
        return list(wrapper);
    }



    @Override
    public List<FeeConfig> getFeeConfigsByStatus(String status) {
        LambdaQueryWrapper<FeeConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeeConfig::getIsActive, status);
        return list(wrapper);
    }

    @Override
    @Transactional
    public boolean addFeeConfig(FeeConfig feeConfig) {
        return save(feeConfig);
    }

    @Override
    @Transactional
    public boolean updateFeeConfig(FeeConfig feeConfig) {
        return updateById(feeConfig);
    }

    @Override
    @Transactional
    public boolean deleteFeeConfig(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean updateFeeConfigStatus(Long id, String status) {
        FeeConfig feeConfig = getById(id);
        if (feeConfig != null) {
            feeConfig.setIsActive(Boolean.getBoolean(status));
            return updateById(feeConfig);
        }
        return false;
    }
}
