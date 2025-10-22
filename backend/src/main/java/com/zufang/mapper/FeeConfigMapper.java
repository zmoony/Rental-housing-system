package com.zufang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zufang.entity.FeeConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 费用配置Mapper
 */
@Mapper
public interface FeeConfigMapper extends BaseMapper<FeeConfig> {

    /**
     * 根据类型获取活跃的配置
     */
    @Select("SELECT * FROM fee_configs WHERE config_type = #{type} AND is_active = true ORDER BY effective_date DESC LIMIT 1")
    FeeConfig getActiveConfigByType(@Param("type") String type);

    /**
     * 获取所有活跃的配置
     */
    @Select("SELECT * FROM fee_configs WHERE is_active = true ORDER BY config_type, effective_date DESC")
    List<FeeConfig> getAllActiveConfigs();
}
