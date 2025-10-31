package com.zufang.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zufang.entity.FeeConfig;

import java.util.List;

/**
 * 费用配置服务接口
 */
public interface FeeConfigService extends IService<FeeConfig> {

    /**
     * 分页查询费用配置
     */
    Page<FeeConfig> getFeeConfigPage(Integer current, Integer size, String keyword);

    /**
     * 根据类型获取费用配置
     */
    List<FeeConfig> getFeeConfigsByType(String feeType);


    /**
     * 根据状态获取费用配置
     */
    List<FeeConfig> getFeeConfigsByStatus(String status);

    /**
     * 添加费用配置
     */
    boolean addFeeConfig(FeeConfig feeConfig);

    /**
     * 更新费用配置
     */
    boolean updateFeeConfig(FeeConfig feeConfig);

    /**
     * 删除费用配置
     */
    boolean deleteFeeConfig(Long id);

    /**
     * 更新费用配置状态
     */
    boolean updateFeeConfigStatus(Long id, String status);
}
