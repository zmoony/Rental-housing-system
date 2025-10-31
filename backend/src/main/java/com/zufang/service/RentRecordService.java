package com.zufang.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zufang.entity.RentRecord;

import java.util.List;

/**
 * 租金记录服务接口
 */
public interface RentRecordService extends IService<RentRecord> {

    /**
     * 分页查询租金记录
     */
    Page<RentRecord> getRentRecordPage(Integer current, Integer size, String keyword);
    
    /**
     * 根据合同ID获取租金记录
     */
    List<RentRecord> getRentRecordsByContractId(Long contractId);
    
    /**
     * 根据租户ID获取租金记录
     */
    List<RentRecord> getRentRecordsByTenantId(Long tenantId);
    
    /**
     * 根据房间ID获取租金记录
     */
    List<RentRecord> getRentRecordsByRoomId(Long roomId);
    
    /**
     * 根据月份获取租金记录
     */
    List<RentRecord> getRentRecordsByMonth(String rentMonth);
    
    /**
     * 根据状态获取租金记录
     */
    List<RentRecord> getRentRecordsByStatus(String status);
    
    /**
     * 添加租金记录
     */
    boolean addRentRecord(RentRecord rentRecord);
    
    /**
     * 更新租金记录
     */
    boolean updateRentRecord(RentRecord rentRecord);
    
    /**
     * 删除租金记录
     */
    boolean deleteRentRecord(Long id);
    
    /**
     * 更新租金记录状态
     */
    boolean updateRentRecordStatus(Long id, String status);
    
    /**
     * 自动计算租金记录总金额
     */
    boolean calculateTotalAmount(Long id);
}