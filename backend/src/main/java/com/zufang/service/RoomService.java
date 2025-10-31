package com.zufang.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zufang.entity.Room;

import java.util.List;

/**
 * 房间服务接口
 */
public interface RoomService extends IService<Room> {

    /**
     * 分页查询房间列表
     */
    Page<Room> getRoomPage(Integer current, Integer size, String keyword);
    
    /**
     * 根据房屋ID获取房间列表
     */
    List<Room> getRoomsByHouseId(Long houseId);
    
    /**
     * 获取可用房间
     */
    List<Room> getAvailableRooms();
    
    /**
     * 根据状态获取房间
     */
    List<Room> getRoomsByStatus(String status);
    
    /**
     * 添加房间
     */
    boolean addRoom(Room room);
    
    /**
     * 更新房间信息
     */
    boolean updateRoom(Room room);
    
    /**
     * 删除房间
     */
    boolean deleteRoom(Long id);
    
    /**
     * 更新房间状态
     */
    boolean updateRoomStatus(Long id, String status);
}