package com.zufang.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zufang.annotation.LogOperation;
import com.zufang.common.ApiResponse;
import com.zufang.entity.FeeConfig;
import com.zufang.service.FeeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 费用配置管理控制器
 */
@RestController
@RequestMapping("/fee-configs")
public class FeeConfigController {

    @Autowired
    private FeeConfigService feeConfigService;

    /**
     * 分页获取费用配置列表
     */
    @GetMapping("/page")
    public ApiResponse getFeeConfigPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<FeeConfig> page = feeConfigService.getFeeConfigPage(current, size, keyword);
        return ApiResponse.success(page);
    }

    /**
     * 获取费用配置详情
     */
    @GetMapping("/{id}")
    public ApiResponse getFeeConfig(@PathVariable Long id) {
        FeeConfig feeConfig = feeConfigService.getById(id);
        return feeConfig != null ? ApiResponse.success(feeConfig) : ApiResponse.error("费用配置不存在");
    }

    /**
     * 根据类型获取费用配置
     */
    @GetMapping("/type/{feeType}")
    public ApiResponse getFeeConfigsByType(@PathVariable String feeType) {
        List<FeeConfig> configs = feeConfigService.getFeeConfigsByType(feeType);
        return ApiResponse.success(configs);
    }

    /**
     * 根据房屋ID获取费用配置
     */
    @GetMapping("/house/{houseId}")
    public ApiResponse getFeeConfigsByHouseId(@PathVariable Long houseId) {
//        List<FeeConfig> configs = feeConfigService.getFeeConfigsByHouseId(houseId);
        return ApiResponse.success(null);
    }

    /**
     * 根据状态获取费用配置
     */
    @GetMapping("/status/{status}")
    public ApiResponse getFeeConfigsByStatus(@PathVariable String status) {
        List<FeeConfig> configs = feeConfigService.getFeeConfigsByStatus(status);
        return ApiResponse.success(configs);
    }

    /**
     * 添加费用配置
     */
    @PostMapping
    @LogOperation(operationType = "新增", operationDesc = "添加费用配置信息")
    public ApiResponse addFeeConfig(@RequestBody FeeConfig feeConfig) {
        boolean result = feeConfigService.addFeeConfig(feeConfig);
        return result ? ApiResponse.success("添加成功") : ApiResponse.error("添加失败");
    }

    /**
     * 更新费用配置
     */
    @PutMapping
    @LogOperation(operationType = "修改", operationDesc = "更新费用配置信息")
    public ApiResponse updateFeeConfig(@RequestBody FeeConfig feeConfig) {
        boolean result = feeConfigService.updateFeeConfig(feeConfig);
        return result ? ApiResponse.success("更新成功") : ApiResponse.error("更新失败");
    }

    /**
     * 删除费用配置
     */
    @DeleteMapping("/{id}")
    @LogOperation(operationType = "删除", operationDesc = "删除费用配置信息")
    public ApiResponse deleteFeeConfig(@PathVariable Long id) {
        boolean result = feeConfigService.deleteFeeConfig(id);
        return result ? ApiResponse.success("删除成功") : ApiResponse.error("删除失败");
    }

    /**
     * 更新费用配置状态
     */
    @PutMapping("/{id}/status/{status}")
    @LogOperation(operationType = "修改", operationDesc = "更新费用配置状态")
    public ApiResponse updateFeeConfigStatus(@PathVariable Long id, @PathVariable String status) {
        boolean result = feeConfigService.updateFeeConfigStatus(id, status);
        return result ? ApiResponse.success("状态更新成功") : ApiResponse.error("状态更新失败");
    }
}
