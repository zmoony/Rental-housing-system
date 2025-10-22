package com.zufang.service;

import com.zufang.entity.Contract;
import com.zufang.entity.RentRecord;
import com.zufang.entity.UtilityRecord;
import com.zufang.mapper.ContractMapper;
import com.zufang.mapper.RentRecordMapper;
import com.zufang.mapper.UtilityRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 定时任务服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final ContractService contractService;
    private final UtilityCalculationService utilityCalculationService;
    private final ContractMapper contractMapper;
    private final RentRecordMapper rentRecordMapper;
    private final UtilityRecordMapper utilityRecordMapper;

    /**
     * 每天凌晨1点检查合同到期
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void checkExpiredContracts() {
        log.info("开始检查过期合同");
        try {
            contractService.checkExpiredContracts();
            log.info("合同到期检查完成");
        } catch (Exception e) {
            log.error("检查过期合同失败", e);
        }
    }

    /**
     * 每月1号生成水电费账单
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void generateMonthlyUtilityBills() {
        log.info("开始生成月度水电费账单");
        try {
            utilityCalculationService.generateMonthlyBills();
            log.info("月度水电费账单生成完成");
        } catch (Exception e) {
            log.error("生成月度水电费账单失败", e);
        }
    }

    /**
     * 每月1号生成租金账单
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void generateMonthlyRentBills() {
        log.info("开始生成月度租金账单");
        try {
            generateRentBills();
            log.info("月度租金账单生成完成");
        } catch (Exception e) {
            log.error("生成月度租金账单失败", e);
        }
    }

    /**
     * 每天检查逾期账单
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void checkOverdueBills() {
        log.info("开始检查逾期账单");
        try {
            checkOverdueRentBills();
            checkOverdueUtilityBills();
            log.info("逾期账单检查完成");
        } catch (Exception e) {
            log.error("检查逾期账单失败", e);
        }
    }

    /**
     * 生成租金账单
     */
    @Transactional
    private void generateRentBills() {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastMonth = currentDate.minusMonths(1);
        String rentMonth = lastMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // 获取所有活跃合同
        List<Contract> activeContracts = contractMapper.getActiveContracts();

        for (Contract contract : activeContracts) {
            try {
                // 检查是否已生成该月租金账单
                RentRecord existingRecord = rentRecordMapper.getRecordByContractAndMonth(
                    contract.getId(), rentMonth);

                if (existingRecord == null) {
                    // 生成租金账单
                    RentRecord rentRecord = new RentRecord();
                    rentRecord.setContractId(contract.getId());
                    rentRecord.setTenantId(contract.getTenantId());
                    rentRecord.setRoomId(contract.getRoomId());
                    rentRecord.setRentMonth(rentMonth);
                    rentRecord.setRentAmount(contract.getMonthlyRent());

                    // 计算水电费
                    BigDecimal utilityFee = calculateUtilityFeeForMonth(contract.getId(), rentMonth);
                    rentRecord.setUtilityFee(utilityFee);

                    // 计算总金额
                    BigDecimal totalAmount = contract.getMonthlyRent().add(utilityFee);
                    rentRecord.setTotalAmount(totalAmount);
                    rentRecord.setStatus("PENDING");

                    rentRecordMapper.insert(rentRecord);

                    log.info("生成租金账单 - 合同ID: {}, 月份: {}, 金额: {}", 
                            contract.getId(), rentMonth, totalAmount);
                }
            } catch (Exception e) {
                log.error("生成合同 {} 的租金账单失败: {}", contract.getId(), e.getMessage());
            }
        }
    }

    /**
     * 计算指定月份的水电费
     */
    private BigDecimal calculateUtilityFeeForMonth(Long contractId, String rentMonth) {
        UtilityRecord utilityRecord = utilityRecordMapper.getRecordByContractAndMonth(
            contractId, rentMonth);
        return utilityRecord != null ? utilityRecord.getTotalFee() : BigDecimal.ZERO;
    }

    /**
     * 检查逾期租金账单
     */
    private void checkOverdueRentBills() {
        LocalDate today = LocalDate.now();
        List<RentRecord> overdueRecords = rentRecordMapper.getOverdueRecords(today);

        for (RentRecord record : overdueRecords) {
            record.setStatus("OVERDUE");
            rentRecordMapper.updateById(record);
            log.info("租金账单逾期 - 记录ID: {}, 租户ID: {}, 金额: {}", 
                    record.getId(), record.getTenantId(), record.getTotalAmount());
        }
    }

    /**
     * 检查逾期水电费账单
     */
    private void checkOverdueUtilityBills() {
        LocalDate today = LocalDate.now();
        List<UtilityRecord> overdueRecords = utilityRecordMapper.getOverdueRecords(today);

        for (UtilityRecord record : overdueRecords) {
            record.setStatus("OVERDUE");
            utilityRecordMapper.updateById(record);
            log.info("水电费账单逾期 - 记录ID: {}, 合同ID: {}, 金额: {}", 
                    record.getId(), record.getContractId(), record.getTotalFee());
        }
    }
}
