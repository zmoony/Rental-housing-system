package com.zufang.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房屋实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("houses")
public class House {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("house_name")
    private String houseName;
    
    @TableField("address")
    private String address;
    
    @TableField("total_rooms")
    private Integer totalRooms;
    
    @TableField("total_area")
    private BigDecimal totalArea;
    
    @TableField("house_type")
    private String houseType;
    
    @TableField("description")
    private String description;
    
    @TableField("facilities")
    private String[] facilities;
    
    @TableField("images")
    private String[] images;
    
    @TableField("landlord_id")
    private Long landlordId;
    
    @TableField("status")
    private String status;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
