package com.zufang.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zufang.entity.House;

import java.util.List;

/**
 * 房屋服务接口
 */
public interface HouseService extends IService<House> {

    /**
     * 分页查询房屋列表
     */
    Page<House> getHousePage(Integer current, Integer size, String keyword);
    
    /**
     * 根据房东ID获取房屋列表
     */
    List<House> getHousesByLandlord(Long landlordId);
    
    /**
     * 获取可用房屋
     */
    List<House> getAvailableHouses();
    
    /**
     * 根据状态获取房屋
     */
    List<House> getHousesByStatus(String status);
    
    /**
     * 添加房屋
     */
    boolean addHouse(House house);
    
    /**
     * 更新房屋信息
     */
    boolean updateHouse(House house);
    
    /**
     * 删除房屋
     */
    boolean deleteHouse(Long id);
    
    /**
     * 更新房屋状态
     */
    boolean updateHouseStatus(Long id, String status);
}