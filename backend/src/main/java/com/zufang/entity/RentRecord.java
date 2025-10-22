package com.zufang.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 租金记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("rent_records")
public class RentRecord {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("contract_id")
    private Long contractId;
    
    @TableField("tenant_id")
    private Long tenantId;
    
    @TableField("room_id")
    private Long roomId;
    
    @TableField("rent_month")
    private String rentMonth;
    
    @TableField("rent_amount")
    private BigDecimal rentAmount;
    
    @TableField("utility_fee")
    private BigDecimal utilityFee;
    
    @TableField("other_fee")
    private BigDecimal otherFee;
    
    @TableField("total_amount")
    private BigDecimal totalAmount;
    
    @TableField("payment_date")
    private LocalDate paymentDate;
    
    @TableField("payment_method")
    private String paymentMethod;
    
    @TableField("status")
    private String status;
    
    @TableField("notes")
    private String notes;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
