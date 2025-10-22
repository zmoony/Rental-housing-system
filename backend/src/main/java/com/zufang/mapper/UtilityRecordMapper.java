package com.zufang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zufang.entity.UtilityRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 水电费记录Mapper
 */
@Mapper
public interface UtilityRecordMapper extends BaseMapper<UtilityRecord> {

    /**
     * 获取房间的最后一条记录
     */
    @Select("SELECT * FROM utility_records WHERE room_id = #{roomId} ORDER BY record_date DESC LIMIT 1")
    UtilityRecord getLastRecordByRoom(@Param("roomId") Long roomId);

    /**
     * 根据合同和月份获取记录
     */
    @Select("SELECT * FROM utility_records WHERE contract_id = #{contractId} AND TO_CHAR(record_date, 'YYYY-MM') = #{month}")
    UtilityRecord getRecordByContractAndMonth(@Param("contractId") Long contractId, @Param("month") String month);

    /**
     * 获取逾期的水电费记录
     */
    @Select("SELECT * FROM utility_records WHERE status = 'PENDING' AND record_date < #{date}")
    List<UtilityRecord> getOverdueRecords(@Param("date") LocalDate date);

    /**
     * 根据房间ID获取水电费记录
     */
    @Select("SELECT * FROM utility_records WHERE room_id = #{roomId} ORDER BY record_date DESC")
    List<UtilityRecord> getRecordsByRoom(@Param("roomId") Long roomId);

    /**
     * 根据合同ID获取水电费记录
     */
    @Select("SELECT * FROM utility_records WHERE contract_id = #{contractId} ORDER BY record_date DESC")
    List<UtilityRecord> getRecordsByContract(@Param("contractId") Long contractId);
}
