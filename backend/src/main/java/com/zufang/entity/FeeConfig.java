package com.zufang.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 费用配置实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fee_configs")
public class FeeConfig {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("config_name")
    private String configName;
    
    @TableField("config_type")
    private String configType;
    
    @TableField("price")
    private BigDecimal price;
    
    @TableField("unit")
    private String unit;
    
    @TableField("effective_date")
    private LocalDate effectiveDate;
    
    @TableField("expire_date")
    private LocalDate expireDate;
    
    @TableField("description")
    private String description;
    
    @TableField("is_active")
    private Boolean isActive;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
