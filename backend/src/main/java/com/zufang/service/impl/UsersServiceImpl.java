package com.zufang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zufang.entity.User;
import com.zufang.mapper.UserMapper;
import com.zufang.service.UsersService;
import org.springframework.stereotype.Service;

/**
* @author yuez
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-11-05 11:50:29
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UserMapper, User> implements UsersService {

}




