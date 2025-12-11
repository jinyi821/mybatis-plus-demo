package com.example.mybatisplusdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mybatisplusdemo.entity.User;

// @Mapper 可省略，如果主类有 @MapperScan
public interface UserMapper extends BaseMapper<User> {
    // 无需写任何方法！BaseMapper 已提供：selectList, selectById, insert, updateById, deleteById 等
    // 如需自定义SQL，可加注解或XML
}