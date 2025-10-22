package com.zufang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zufang.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 房间Mapper
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room> {

    /**
     * 根据房屋ID获取房间列表
     */
    @Select("SELECT * FROM rooms WHERE house_id = #{houseId} ORDER BY room_number")
    List<Room> getRoomsByHouse(@Param("houseId") Long houseId);

    /**
     * 获取可用房间
     */
    @Select("SELECT * FROM rooms WHERE status = 'AVAILABLE' ORDER BY house_id, room_number")
    List<Room> getAvailableRooms();

    /**
     * 根据房屋ID获取可用房间
     */
    @Select("SELECT * FROM rooms WHERE house_id = #{houseId} AND status = 'AVAILABLE' ORDER BY room_number")
    List<Room> getAvailableRoomsByHouse(@Param("houseId") Long houseId);
}
