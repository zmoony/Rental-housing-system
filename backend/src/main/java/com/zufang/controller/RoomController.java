package com.zufang.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zufang.annotation.LogOperation;
import com.zufang.common.ApiResponse;
import com.zufang.dto.RoomDTO;
import com.zufang.entity.Room;
import com.zufang.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 房间管理控制器
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    /**
     * 分页获取房间列表
     */
    @GetMapping("/page")
    public ApiResponse getRoomPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String houseId) {
        Page<RoomDTO> page = roomService.getRoomPage(current, size, keyword,houseId);
        return ApiResponse.success(page);
    }

    /**
     * 获取房间详情
     */
    @GetMapping("/{id}")
    public ApiResponse getRoom(@PathVariable Long id) {
        Room room = roomService.getById(id);
        return room != null ? ApiResponse.success(room) : ApiResponse.error("房间不存在");
    }

    /**
     * 根据房屋ID获取房间列表
     */
    @GetMapping("/house/{houseId}")
    public ApiResponse getRoomsByHouseId(@PathVariable Long houseId) {
        List<Room> rooms = roomService.getRoomsByHouseId(houseId);
        return ApiResponse.success(rooms);
    }

    /**
     * 获取可用房间
     */
    @GetMapping("/available")
    public ApiResponse getAvailableRooms() {
        List<Room> rooms = roomService.getAvailableRooms();
        return ApiResponse.success(rooms);
    }

    /**
     * 根据状态获取房间
     */
    @GetMapping("/status/{status}")
    public ApiResponse getRoomsByStatus(@PathVariable String status) {
        List<Room> rooms = roomService.getRoomsByStatus(status);
        return ApiResponse.success(rooms);
    }

    /**
     * 添加房间
     */
    @PostMapping
    @LogOperation(operationType = "新增", operationDesc = "添加房间信息")
    public ApiResponse addRoom(@RequestBody Room room) {
        boolean result = roomService.addRoom(room);
        return result ? ApiResponse.success("添加成功") : ApiResponse.error("添加失败");
    }

    /**
     * 更新房间
     */
    @PutMapping
    @LogOperation(operationType = "修改", operationDesc = "更新房间信息")
    public ApiResponse updateRoom(@RequestBody Room room) {
        boolean result = roomService.updateRoom(room);
        return result ? ApiResponse.success("更新成功") : ApiResponse.error("更新失败");
    }

    /**
     * 删除房间
     */
    @DeleteMapping("/{id}")
    @LogOperation(operationType = "删除", operationDesc = "删除房间信息")
    public ApiResponse deleteRoom(@PathVariable Long id) {
        boolean result = roomService.deleteRoom(id);
        return result ? ApiResponse.success("删除成功") : ApiResponse.error("删除失败");
    }

    /**
     * 更新房间状态
     */
    @PutMapping("/{id}/status/{status}")
    @LogOperation(operationType = "修改", operationDesc = "更新房间状态")
    public ApiResponse updateRoomStatus(@PathVariable Long id, @PathVariable String status) {
        boolean result = roomService.updateRoomStatus(id, status);
        return result ? ApiResponse.success("状态更新成功") : ApiResponse.error("状态更新失败");
    }
}
