package com.zufang.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 水电费记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("utility_records")
public class UtilityRecord {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("contract_id")
    private Long contractId;
    
    @TableField("room_id")
    private Long roomId;
    
    @TableField("record_date")
    private LocalDate recordDate;
    
    @TableField("water_reading")
    private BigDecimal waterReading;
    
    @TableField("electricity_reading")
    private BigDecimal electricityReading;
    
    @TableField("gas_reading")
    private BigDecimal gasReading;
    
    @TableField("water_usage")
    private BigDecimal waterUsage;
    
    @TableField("electricity_usage")
    private BigDecimal electricityUsage;
    
    @TableField("gas_usage")
    private BigDecimal gasUsage;
    
    @TableField("water_price")
    private BigDecimal waterPrice;
    
    @TableField("electricity_price")
    private BigDecimal electricityPrice;
    
    @TableField("gas_price")
    private BigDecimal gasPrice;
    
    @TableField("water_fee")
    private BigDecimal waterFee;
    
    @TableField("electricity_fee")
    private BigDecimal electricityFee;
    
    @TableField("gas_fee")
    private BigDecimal gasFee;
    
    @TableField("total_fee")
    private BigDecimal totalFee;
    
    @TableField("status")
    private String status;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
