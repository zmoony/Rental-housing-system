package com.zufang.dto;

import com.zufang.entity.Tenant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TenantDTO
 *
 * @author yuez
 * @since 2025/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantDTO extends Tenant {
    private String name;
    private String phone;
    private String idCard;
}
