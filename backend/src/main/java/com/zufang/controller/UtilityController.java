package com.zufang.controller;

import com.zufang.entity.UtilityRecord;
import com.zufang.service.UtilityCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 水电费管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/utilities")
@RequiredArgsConstructor
public class UtilityController {

    private final UtilityCalculationService utilityCalculationService;

    /**
     * 记录水电费读数
     */
    @PostMapping("/record")
    public ResponseEntity<UtilityRecord> recordUtilityReading(@Valid @RequestBody UtilityRecordRequest request) {
        UtilityRecord record = utilityCalculationService.calculateUtilityFee(
            request.getContractId(),
            request.getRoomId(),
            request.getWaterReading(),
            request.getElectricityReading(),
            request.getGasReading(),
            request.getRecordDate()
        );
        return ResponseEntity.ok(record);
    }

    /**
     * 获取房间水电费记录
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<UtilityRecord>> getRoomUtilityRecords(@PathVariable Long roomId) {
        // 实现获取房间水电费记录的逻辑
        return ResponseEntity.ok(List.of());
    }

    /**
     * 获取合同水电费记录
     */
    @GetMapping("/contract/{contractId}")
    public ResponseEntity<List<UtilityRecord>> getContractUtilityRecords(@PathVariable Long contractId) {
        // 实现获取合同水电费记录的逻辑
        return ResponseEntity.ok(List.of());
    }

    /**
     * 生成月度水电费账单
     */
    @PostMapping("/generate-monthly-bills")
    public ResponseEntity<Void> generateMonthlyBills() {
        utilityCalculationService.generateMonthlyBills();
        return ResponseEntity.ok().build();
    }

    /**
     * 水电费记录请求DTO
     */
    public static class UtilityRecordRequest {
        private Long contractId;
        private Long roomId;
        private BigDecimal waterReading;
        private BigDecimal electricityReading;
        private BigDecimal gasReading;
        private LocalDate recordDate;

        // Getters and Setters
        public Long getContractId() { return contractId; }
        public void setContractId(Long contractId) { this.contractId = contractId; }
        
        public Long getRoomId() { return roomId; }
        public void setRoomId(Long roomId) { this.roomId = roomId; }
        
        public BigDecimal getWaterReading() { return waterReading; }
        public void setWaterReading(BigDecimal waterReading) { this.waterReading = waterReading; }
        
        public BigDecimal getElectricityReading() { return electricityReading; }
        public void setElectricityReading(BigDecimal electricityReading) { this.electricityReading = electricityReading; }
        
        public BigDecimal getGasReading() { return gasReading; }
        public void setGasReading(BigDecimal gasReading) { this.gasReading = gasReading; }
        
        public LocalDate getRecordDate() { return recordDate; }
        public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }
    }
}
