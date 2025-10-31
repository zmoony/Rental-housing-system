package com.zufang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zufang.entity.House;
import com.zufang.mapper.HouseMapper;
import com.zufang.service.HouseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 房屋服务实现类
 */
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {

    @Override
    public Page<House> getHousePage(Integer current, Integer size, String keyword) {
        Page<House> page = new Page<>(current, size);
        LambdaQueryWrapper<House> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(House::getHouseName, keyword)
                   .or()
                   .like(House::getAddress, keyword);
        }
        
        wrapper.orderByDesc(House::getCreatedAt);
        return page(page, wrapper);
    }

    @Override
    public List<House> getHousesByLandlord(Long landlordId) {
        return baseMapper.getHousesByLandlord(landlordId);
    }

    @Override
    public List<House> getAvailableHouses() {
        return baseMapper.getAvailableHouses();
    }

    @Override
    public List<House> getHousesByStatus(String status) {
        LambdaQueryWrapper<House> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(House::getStatus, status);
        return list(wrapper);
    }

    @Override
    @Transactional
    public boolean addHouse(House house) {
        return save(house);
    }

    @Override
    @Transactional
    public boolean updateHouse(House house) {
        return updateById(house);
    }

    @Override
    @Transactional
    public boolean deleteHouse(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean updateHouseStatus(Long id, String status) {
        House house = getById(id);
        if (house != null) {
            house.setStatus(status);
            return updateById(house);
        }
        return false;
    }
}