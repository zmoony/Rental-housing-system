package com.zufang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zufang.entity.House;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 房屋Mapper
 */
@Mapper
public interface HouseMapper extends BaseMapper<House> {

    /**
     * 根据房东ID获取房屋列表
     */
    @Select("SELECT * FROM houses WHERE landlord_id = #{landlordId} ORDER BY created_at DESC")
    List<House> getHousesByLandlord(@Param("landlordId") Long landlordId);

    /**
     * 获取可用房屋
     */
    @Select("SELECT * FROM houses WHERE status = 'AVAILABLE' ORDER BY created_at DESC")
    List<House> getAvailableHouses();

    /**
     * 根据状态获取房屋
     */
    @Select("SELECT * FROM houses WHERE status = #{status} ORDER BY created_at DESC")
    List<House> getHousesByStatus(@Param("status") String status);
}
