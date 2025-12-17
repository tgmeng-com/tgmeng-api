package com.tgmeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tgmeng.model.domain.User;
import com.tgmeng.service.UserService;
import com.tgmeng.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author bjsttlp317
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-12-11 11:11:08
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




