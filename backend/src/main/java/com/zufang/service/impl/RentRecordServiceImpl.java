package com.zufang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zufang.entity.RentRecord;
import com.zufang.mapper.RentRecordMapper;
import com.zufang.service.RentRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 租金记录服务实现类
 */
@Service
public class RentRecordServiceImpl extends ServiceImpl<RentRecordMapper, RentRecord> implements RentRecordService {

    @Override
    public Page<RentRecord> getRentRecordPage(Integer current, Integer size, String keyword) {
        Page<RentRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<RentRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(RentRecord::getRentMonth, keyword);
        }
        
        wrapper.orderByDesc(RentRecord::getCreatedAt);
        return page(page, wrapper);
    }

    @Override
    public List<RentRecord> getRentRecordsByContractId(Long contractId) {
        LambdaQueryWrapper<RentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RentRecord::getContractId, contractId);
        wrapper.orderByDesc(RentRecord::getRentMonth);
        return list(wrapper);
    }

    @Override
    public List<RentRecord> getRentRecordsByTenantId(Long tenantId) {
        LambdaQueryWrapper<RentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RentRecord::getTenantId, tenantId);
        wrapper.orderByDesc(RentRecord::getRentMonth);
        return list(wrapper);
    }

    @Override
    public List<RentRecord> getRentRecordsByRoomId(Long roomId) {
        LambdaQueryWrapper<RentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RentRecord::getRoomId, roomId);
        wrapper.orderByDesc(RentRecord::getRentMonth);
        return list(wrapper);
    }

    @Override
    public List<RentRecord> getRentRecordsByMonth(String rentMonth) {
        LambdaQueryWrapper<RentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RentRecord::getRentMonth, rentMonth);
        return list(wrapper);
    }

    @Override
    public List<RentRecord> getRentRecordsByStatus(String status) {
        LambdaQueryWrapper<RentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RentRecord::getStatus, status);
        return list(wrapper);
    }

    @Override
    @Transactional
    public boolean addRentRecord(RentRecord rentRecord) {
        // 自动计算总金额
        calculateTotalAmount(rentRecord);
        return save(rentRecord);
    }

    @Override
    @Transactional
    public boolean updateRentRecord(RentRecord rentRecord) {
        // 自动计算总金额
        calculateTotalAmount(rentRecord);
        return updateById(rentRecord);
    }

    @Override
    @Transactional
    public boolean deleteRentRecord(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean updateRentRecordStatus(Long id, String status) {
        RentRecord rentRecord = getById(id);
        if (rentRecord != null) {
            rentRecord.setStatus(status);
            return updateById(rentRecord);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean calculateTotalAmount(Long id) {
        RentRecord rentRecord = getById(id);
        if (rentRecord != null) {
            calculateTotalAmount(rentRecord);
            return updateById(rentRecord);
        }
        return false;
    }
    
    /**
     * 计算总金额
     */
    private void calculateTotalAmount(RentRecord rentRecord) {
        BigDecimal rentAmount = rentRecord.getRentAmount() != null ? rentRecord.getRentAmount() : BigDecimal.ZERO;
        BigDecimal utilityFee = rentRecord.getUtilityFee() != null ? rentRecord.getUtilityFee() : BigDecimal.ZERO;
        BigDecimal otherFee = rentRecord.getOtherFee() != null ? rentRecord.getOtherFee() : BigDecimal.ZERO;
        
        BigDecimal totalAmount = rentAmount.add(utilityFee).add(otherFee);
        rentRecord.setTotalAmount(totalAmount);
    }
}