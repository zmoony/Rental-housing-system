package com.zufang.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 租户实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tenants")
public class Tenant {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("emergency_contact")
    private String emergencyContact;
    
    @TableField("emergency_phone")
    private String emergencyPhone;
    
    @TableField("occupation")
    private String occupation;
    
    @TableField("company")
    private String company;
    
    @TableField("move_in_date")
    private LocalDate moveInDate;
    
    @TableField("move_out_date")
    private LocalDate moveOutDate;
    
    @TableField("status")
    private String status;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
