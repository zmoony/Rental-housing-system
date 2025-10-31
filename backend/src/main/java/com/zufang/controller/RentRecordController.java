package com.zufang.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zufang.annotation.LogOperation;
import com.zufang.common.ApiResponse;
import com.zufang.entity.RentRecord;
import com.zufang.service.RentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租金记录管理控制器
 */
@RestController
@RequestMapping("/rent-records")
public class RentRecordController {

    @Autowired
    private RentRecordService rentRecordService;

    /**
     * 分页获取租金记录列表
     */
    @GetMapping("/page")
    public ApiResponse getRentRecordPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<RentRecord> page = rentRecordService.getRentRecordPage(current, size, keyword);
        return ApiResponse.success(page);
    }

    /**
     * 获取租金记录详情
     */
    @GetMapping("/{id}")
    public ApiResponse getRentRecord(@PathVariable Long id) {
        RentRecord rentRecord = rentRecordService.getById(id);
        return rentRecord != null ? ApiResponse.success(rentRecord) : ApiResponse.error("租金记录不存在");
    }

    /**
     * 根据合同ID获取租金记录
     */
    @GetMapping("/contract/{contractId}")
    public ApiResponse getRentRecordsByContractId(@PathVariable Long contractId) {
        List<RentRecord> records = rentRecordService.getRentRecordsByContractId(contractId);
        return ApiResponse.success(records);
    }

    /**
     * 根据租户ID获取租金记录
     */
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse getRentRecordsByTenantId(@PathVariable Long tenantId) {
        List<RentRecord> records = rentRecordService.getRentRecordsByTenantId(tenantId);
        return ApiResponse.success(records);
    }

    /**
     * 根据房间ID获取租金记录
     */
    @GetMapping("/room/{roomId}")
    public ApiResponse getRentRecordsByRoomId(@PathVariable Long roomId) {
        List<RentRecord> records = rentRecordService.getRentRecordsByRoomId(roomId);
        return ApiResponse.success(records);
    }

    /**
     * 根据月份获取租金记录
     */
    @GetMapping("/month/{rentMonth}")
    public ApiResponse getRentRecordsByMonth(@PathVariable String rentMonth) {
        List<RentRecord> records = rentRecordService.getRentRecordsByMonth(rentMonth);
        return ApiResponse.success(records);
    }

    /**
     * 根据状态获取租金记录
     */
    @GetMapping("/status/{status}")
    public ApiResponse getRentRecordsByStatus(@PathVariable String status) {
        List<RentRecord> records = rentRecordService.getRentRecordsByStatus(status);
        return ApiResponse.success(records);
    }

    /**
     * 添加租金记录
     */
    @PostMapping
    @LogOperation(operationType = "新增", operationDesc = "添加租金记录信息")
    public ApiResponse addRentRecord(@RequestBody RentRecord rentRecord) {
        boolean result = rentRecordService.addRentRecord(rentRecord);
        return result ? ApiResponse.success("添加成功") : ApiResponse.error("添加失败");
    }

    /**
     * 更新租金记录
     */
    @PutMapping
    @LogOperation(operationType = "修改", operationDesc = "更新租金记录信息")
    public ApiResponse updateRentRecord(@RequestBody RentRecord rentRecord) {
        boolean result = rentRecordService.updateRentRecord(rentRecord);
        return result ? ApiResponse.success("更新成功") : ApiResponse.error("更新失败");
    }

    /**
     * 删除租金记录
     */
    @DeleteMapping("/{id}")
    @LogOperation(operationType = "删除", operationDesc = "删除租金记录信息")
    public ApiResponse deleteRentRecord(@PathVariable Long id) {
        boolean result = rentRecordService.deleteRentRecord(id);
        return result ? ApiResponse.success("删除成功") : ApiResponse.error("删除失败");
    }

    /**
     * 更新租金记录状态
     */
    @PutMapping("/{id}/status/{status}")
    @LogOperation(operationType = "修改", operationDesc = "更新租金记录状态")
    public ApiResponse updateRentRecordStatus(@PathVariable Long id, @PathVariable String status) {
        boolean result = rentRecordService.updateRentRecordStatus(id, status);
        return result ? ApiResponse.success("状态更新成功") : ApiResponse.error("状态更新失败");
    }

    /**
     * 计算租金记录总金额
     */
    @PutMapping("/{id}/calculate")
    @LogOperation(operationType = "修改", operationDesc = "计算租金记录总金额")
    public ApiResponse calculateTotalAmount(@PathVariable Long id) {
        boolean result = rentRecordService.calculateTotalAmount(id);
        return result ? ApiResponse.success("计算成功") : ApiResponse.error("计算失败");
    }
}