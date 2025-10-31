package com.zufang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zufang.entity.Room;
import com.zufang.mapper.RoomMapper;
import com.zufang.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 房间服务实现类
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Override
    public Page<Room> getRoomPage(Integer current, Integer size, String keyword) {
        Page<Room> page = new Page<>(current, size);
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Room::getRoomNumber, keyword);
        }
        
        wrapper.orderByDesc(Room::getCreatedAt);
        return page(page, wrapper);
    }

    @Override
    public List<Room> getRoomsByHouseId(Long houseId) {
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Room::getHouseId, houseId);
        return list(wrapper);
    }

    @Override
    public List<Room> getAvailableRooms() {
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Room::getStatus, "AVAILABLE");
        return list(wrapper);
    }

    @Override
    public List<Room> getRoomsByStatus(String status) {
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Room::getStatus, status);
        return list(wrapper);
    }

    @Override
    @Transactional
    public boolean addRoom(Room room) {
        return save(room);
    }

    @Override
    @Transactional
    public boolean updateRoom(Room room) {
        return updateById(room);
    }

    @Override
    @Transactional
    public boolean deleteRoom(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean updateRoomStatus(Long id, String status) {
        Room room = getById(id);
        if (room != null) {
            room.setStatus(status);
            return updateById(room);
        }
        return false;
    }
}