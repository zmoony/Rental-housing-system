package com.zufang.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("contracts")
public class Contract {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("contract_number")
    private String contractNumber;
    
    @TableField("house_id")
    private Long houseId;
    
    @TableField("room_id")
    private Long roomId;
    
    @TableField("tenant_id")
    private Long tenantId;
    
    @TableField("landlord_id")
    private Long landlordId;
    
    @TableField("start_date")
    private LocalDate startDate;
    
    @TableField("end_date")
    private LocalDate endDate;
    
    @TableField("monthly_rent")
    private BigDecimal monthlyRent;
    
    @TableField("deposit")
    private BigDecimal deposit;
    
    @TableField("payment_day")
    private Integer paymentDay;
    
    @TableField("contract_terms")
    private String contractTerms;
    
    @TableField("status")
    private String status;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
