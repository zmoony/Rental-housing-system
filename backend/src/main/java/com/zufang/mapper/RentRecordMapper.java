package com.zufang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zufang.entity.RentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 租金记录Mapper
 */
@Mapper
public interface RentRecordMapper extends BaseMapper<RentRecord> {

    /**
     * 根据合同和月份获取记录
     */
    @Select("SELECT * FROM rent_records WHERE contract_id = #{contractId} AND rent_month = #{month}")
    RentRecord getRecordByContractAndMonth(@Param("contractId") Long contractId, @Param("month") String month);

    /**
     * 获取逾期的租金记录
     */
    @Select("SELECT * FROM rent_records WHERE status = 'PENDING' AND payment_date < #{date}")
    List<RentRecord> getOverdueRecords(@Param("date") LocalDate date);

    /**
     * 根据合同ID获取租金记录
     */
    @Select("SELECT * FROM rent_records WHERE contract_id = #{contractId} ORDER BY rent_month DESC")
    List<RentRecord> getRecordsByContract(@Param("contractId") Long contractId);

    /**
     * 根据租户ID获取租金记录
     */
    @Select("SELECT * FROM rent_records WHERE tenant_id = #{tenantId} ORDER BY rent_month DESC")
    List<RentRecord> getRecordsByTenant(@Param("tenantId") Long tenantId);
}
