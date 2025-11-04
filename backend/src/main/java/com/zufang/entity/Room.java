package com.zufang.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房间实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("rooms")
public class Room {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("house_id")
    private Long houseId;

    @TableField("room_number")
    private String roomNumber;

    @TableField("room_type")
    private String roomType;

    @TableField("area")
    private BigDecimal area;

    @TableField("monthly_rent")
    private BigDecimal monthlyRent;

    @TableField("deposit")
    private BigDecimal deposit;

    @TableField("facilities")
    private String facilities;

    @TableField("images")
    private String images;

    @TableField("status")
    private String status;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
