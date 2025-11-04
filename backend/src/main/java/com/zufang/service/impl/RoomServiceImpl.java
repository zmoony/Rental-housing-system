package com.zufang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zufang.dto.RoomDTO;
import com.zufang.entity.House;
import com.zufang.entity.Room;
import com.zufang.mapper.RoomMapper;
import com.zufang.service.HouseService;
import com.zufang.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 房间服务实现类
 */
@Service
@RequiredArgsConstructor
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {
    private final HouseService houseService;

    @Override
    public Page<RoomDTO> getRoomPage(Integer current, Integer size, String keyword,String houseId) {
        Page<Room> page = new Page<>(current, size);
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(houseId)){
            wrapper.eq(Room::getHouseId, Long.valueOf(houseId));
        }

        if (StringUtils.hasText(keyword)) {

            wrapper.and(w -> {
                w.like(Room::getRoomNumber, keyword);
                w.or().like(Room::getRoomType, keyword);
            });
        }
        wrapper.orderByDesc(Room::getCreatedAt);
        Page<Room> roomPage = this.page(page, wrapper);
        Page<RoomDTO> roomDTOPage = new Page<>();
        BeanUtils.copyProperties(roomPage, roomDTOPage, "records");

        if (!CollectionUtils.isEmpty(roomPage.getRecords())) {
            List<Room> rooms = roomPage.getRecords();
            List<Long> houseIds = rooms.stream().map(Room::getHouseId).distinct().collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(houseIds)) {
                Map<Long, String> houseIdNameMap = houseService.listByIds(houseIds).stream()
                        .collect(Collectors.toMap(House::getId, House::getHouseName));

                List<RoomDTO> roomDTOs = rooms.stream().map(room -> {
                    RoomDTO roomDTO = new RoomDTO();
                    BeanUtils.copyProperties(room, roomDTO);
                    roomDTO.setHouseName(houseIdNameMap.get(room.getHouseId()));
                    return roomDTO;
                }).collect(Collectors.toList());
                roomDTOPage.setRecords(roomDTOs);
            }
        }
        return roomDTOPage;
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
