package com.zufang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zufang.entity.Contract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 合同Mapper
 */
@Mapper
public interface ContractMapper extends BaseMapper<Contract> {

    /**
     * 获取所有活跃的合同
     */
    @Select("SELECT * FROM contracts WHERE status = 'ACTIVE'")
    List<Contract> getActiveContracts();

    /**
     * 获取过期的合同
     */
    @Select("SELECT * FROM contracts WHERE status = 'ACTIVE' AND end_date < #{date}")
    List<Contract> getExpiredContracts(@Param("date") LocalDate date);

    /**
     * 根据租户ID获取合同
     */
    @Select("SELECT * FROM contracts WHERE tenant_id = #{tenantId} ORDER BY created_at DESC")
    List<Contract> getContractsByTenant(@Param("tenantId") Long tenantId);

    /**
     * 根据房东ID获取合同
     */
    @Select("SELECT * FROM contracts WHERE landlord_id = #{landlordId} ORDER BY created_at DESC")
    List<Contract> getContractsByLandlord(@Param("landlordId") Long landlordId);

    /**
     * 根据房间ID获取合同
     */
    @Select("SELECT * FROM contracts WHERE room_id = #{roomId} AND status = 'ACTIVE'")
    Contract getActiveContractByRoom(@Param("roomId") Long roomId);
}
