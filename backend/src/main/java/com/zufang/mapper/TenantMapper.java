package com.zufang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zufang.dto.TenantDTO;
import com.zufang.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 租户Mapper
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {

    /**
     * 根据用户ID获取租户信息
     */
    IPage<TenantDTO> getTenantAndUser(Page<TenantDTO> page,
                                      @Param("keyword") String keyword,
                                      @Param("status") String status);

    /**
     * 获取活跃的租户
     */
    @Select("SELECT * FROM tenants WHERE status = 'ACTIVE' ORDER BY created_at DESC")
    List<Tenant> getActiveTenants();

    /**
     * 根据房间ID获取当前租户
     */
    @Select("SELECT t.* FROM tenants t JOIN contracts c ON t.id = c.tenant_id WHERE c.room_id = #{roomId} AND c.status = 'ACTIVE'")
    Tenant getCurrentTenantByRoom(@Param("roomId") Long roomId);
}
