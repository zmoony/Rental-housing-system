package com.zufang.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zufang.annotation.LogOperation;
import com.zufang.common.ApiResponse;
import com.zufang.entity.House;
import com.zufang.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 房屋管理控制器
 */
@RestController
@RequestMapping("/houses")
public class HouseController {

    @Autowired
    private HouseService houseService;

    /**
     * 分页获取房屋列表
     */
    @GetMapping("/page")
    public ApiResponse getHousePage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<House> page = houseService.getHousePage(current, size, keyword);
        return ApiResponse.success(page);
    }

    /**
     * 获取房屋详情
     */
    @GetMapping("/{id}")
    public ApiResponse getHouse(@PathVariable Long id) {
        House house = houseService.getById(id);
        return house != null ? ApiResponse.success(house) : ApiResponse.error("房屋不存在");
    }

    /**
     * 根据房东ID获取房屋列表
     */
    @GetMapping("/landlord/{landlordId}")
    public ApiResponse getHousesByLandlord(@PathVariable Long landlordId) {
        List<House> houses = houseService.getHousesByLandlord(landlordId);
        return ApiResponse.success(houses);
    }

    /**
     * 获取可用房屋
     */
    @GetMapping("/available")
    public ApiResponse getAvailableHouses() {
        List<House> houses = houseService.getAvailableHouses();
        return ApiResponse.success(houses);
    }

    /**
     * 根据状态获取房屋
     */
    @GetMapping("/status/{status}")
    public ApiResponse getHousesByStatus(@PathVariable String status) {
        List<House> houses = houseService.getHousesByStatus(status);
        return ApiResponse.success(houses);
    }

    /**
     * 添加房屋
     */
    @PostMapping
    @LogOperation(operationType = "新增", operationDesc = "添加房屋信息")
    public ApiResponse addHouse(@RequestBody House house) {
        boolean result = houseService.addHouse(house);
        return result ? ApiResponse.success("添加成功") : ApiResponse.error("添加失败");
    }

    /**
     * 更新房屋
     */
    @PutMapping
    @LogOperation(operationType = "修改", operationDesc = "更新房屋信息")
    public ApiResponse updateHouse(@RequestBody House house) {
        boolean result = houseService.updateHouse(house);
        return result ? ApiResponse.success("更新成功") : ApiResponse.error("更新失败");
    }

    /**
     * 删除房屋
     */
    @DeleteMapping("/{id}")
    @LogOperation(operationType = "删除", operationDesc = "删除房屋信息")
    public ApiResponse deleteHouse(@PathVariable Long id) {
        boolean result = houseService.deleteHouse(id);
        return result ? ApiResponse.success("删除成功") : ApiResponse.error("删除失败");
    }

    /**
     * 更新房屋状态
     */
    @PutMapping("/{id}/status/{status}")
    @LogOperation(operationType = "修改", operationDesc = "更新房屋状态")
    public ApiResponse updateHouseStatus(@PathVariable Long id, @PathVariable String status) {
        boolean result = houseService.updateHouseStatus(id, status);
        return result ? ApiResponse.success("状态更新成功") : ApiResponse.error("状态更新失败");
    }
}