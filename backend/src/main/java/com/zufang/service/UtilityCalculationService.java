package com.zufang.service;

import com.zufang.entity.UtilityRecord;
import com.zufang.entity.FeeConfig;
import com.zufang.mapper.UtilityRecordMapper;
import com.zufang.mapper.FeeConfigMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * 水电费计算服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UtilityCalculationService {

    private final UtilityRecordMapper utilityRecordMapper;
    private final FeeConfigMapper feeConfigMapper;

    /**
     * 计算水电费
     */
    @Transactional
    public UtilityRecord calculateUtilityFee(Long contractId, Long roomId, 
                                           BigDecimal waterReading, BigDecimal electricityReading, 
                                           BigDecimal gasReading, LocalDate recordDate) {
        
        // 获取上期读数
        UtilityRecord lastRecord = utilityRecordMapper.getLastRecordByRoom(roomId);
        
        // 计算用量
        BigDecimal waterUsage = calculateUsage(lastRecord != null ? lastRecord.getWaterReading() : BigDecimal.ZERO, waterReading);
        BigDecimal electricityUsage = calculateUsage(lastRecord != null ? lastRecord.getElectricityReading() : BigDecimal.ZERO, electricityReading);
        BigDecimal gasUsage = calculateUsage(lastRecord != null ? lastRecord.getGasReading() : BigDecimal.ZERO, gasReading);
        
        // 获取当前费用标准
        FeeConfig waterConfig = feeConfigMapper.getActiveConfigByType("WATER");
        FeeConfig electricityConfig = feeConfigMapper.getActiveConfigByType("ELECTRICITY");
        FeeConfig gasConfig = feeConfigMapper.getActiveConfigByType("GAS");
        
        // 计算费用
        BigDecimal waterPrice = waterConfig != null ? waterConfig.getPrice() : new BigDecimal("3.5");
        BigDecimal electricityPrice = electricityConfig != null ? electricityConfig.getPrice() : new BigDecimal("0.6");
        BigDecimal gasPrice = gasConfig != null ? gasConfig.getPrice() : new BigDecimal("2.5");
        
        BigDecimal waterFee = waterUsage.multiply(waterPrice).setScale(2, RoundingMode.HALF_UP);
        BigDecimal electricityFee = electricityUsage.multiply(electricityPrice).setScale(2, RoundingMode.HALF_UP);
        BigDecimal gasFee = gasUsage.multiply(gasPrice).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalFee = waterFee.add(electricityFee).add(gasFee);
        
        // 创建记录
        UtilityRecord record = new UtilityRecord();
        record.setContractId(contractId);
        record.setRoomId(roomId);
        record.setRecordDate(recordDate);
        record.setWaterReading(waterReading);
        record.setElectricityReading(electricityReading);
        record.setGasReading(gasReading);
        record.setWaterUsage(waterUsage);
        record.setElectricityUsage(electricityUsage);
        record.setGasUsage(gasUsage);
        record.setWaterPrice(waterPrice);
        record.setElectricityPrice(electricityPrice);
        record.setGasPrice(gasPrice);
        record.setWaterFee(waterFee);
        record.setElectricityFee(electricityFee);
        record.setGasFee(gasFee);
        record.setTotalFee(totalFee);
        record.setStatus("PENDING");
        
        utilityRecordMapper.insert(record);
        
        log.info("水电费计算完成 - 合同ID: {}, 房间ID: {}, 总费用: {}", contractId, roomId, totalFee);
        
        return record;
    }

    /**
     * 计算用量
     */
    private BigDecimal calculateUsage(BigDecimal lastReading, BigDecimal currentReading) {
        if (lastReading == null) lastReading = BigDecimal.ZERO;
        if (currentReading == null) currentReading = BigDecimal.ZERO;
        
        BigDecimal usage = currentReading.subtract(lastReading);
        return usage.compareTo(BigDecimal.ZERO) >= 0 ? usage : BigDecimal.ZERO;
    }

    /**
     * 自动生成月度水电费账单
     */
    @Transactional
    public void generateMonthlyBills() {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastMonth = currentDate.minusMonths(1);
        
        // 获取所有活跃合同
        List<Contract> activeContracts = contractMapper.getActiveContracts();
        
        for (Contract contract : activeContracts) {
            try {
                // 检查是否已生成该月账单
                UtilityRecord existingRecord = utilityRecordMapper.getRecordByContractAndMonth(
                    contract.getId(), lastMonth.getYear(), lastMonth.getMonthValue());
                
                if (existingRecord == null) {
                    // 生成水电费账单（这里需要实际读取表数据）
                    generateBillForContract(contract, lastMonth);
                }
            } catch (Exception e) {
                log.error("生成合同 {} 的月度账单失败: {}", contract.getId(), e.getMessage());
            }
        }
    }

    /**
     * 为指定合同生成账单
     */
    private void generateBillForContract(Contract contract, LocalDate month) {
        // 这里需要根据实际情况读取水电表数据
        // 暂时使用模拟数据
        BigDecimal waterReading = BigDecimal.valueOf(100 + Math.random() * 50);
        BigDecimal electricityReading = BigDecimal.valueOf(200 + Math.random() * 100);
        BigDecimal gasReading = BigDecimal.valueOf(50 + Math.random() * 30);
        
        calculateUtilityFee(contract.getId(), contract.getRoomId(), 
                          waterReading, electricityReading, gasReading, month);
    }
}
