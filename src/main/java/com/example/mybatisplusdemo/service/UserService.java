package com.example.mybatisplusdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mybatisplusdemo.entity.User;

public interface UserService extends IService<User> {
    // 可扩展自定义方法
}