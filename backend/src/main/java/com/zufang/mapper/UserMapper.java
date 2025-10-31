package com.zufang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zufang.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名获取用户
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    User getUserByUsername(@Param("username") String username);

    /**
     * 根据角色获取用户列表
     */
    @Select("SELECT * FROM users WHERE role = #{role} AND status = 'ACTIVE' ORDER BY created_at DESC")
    List<User> getUsersByRole(@Param("role") String role);

    /**
     * 获取活跃用户
     */
    @Select("SELECT * FROM users WHERE status = 'ACTIVE' ORDER BY created_at DESC")
    List<User> getActiveUsers();

}
