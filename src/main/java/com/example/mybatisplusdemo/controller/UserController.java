package com.example.mybatisplusdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mybatisplusdemo.entity.User;
import com.example.mybatisplusdemo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 查询所有
    @GetMapping
    public List<User> list() {
        return userService.list();  // BaseMapper 的 selectList(null)
    }

    // 根据ID查询
    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return userService.getById(id);  // BaseMapper 的 selectById
    }

    // 分页查询（示例）
    @GetMapping("/page")
    public Page<User> page(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "10") int size) {
        return userService.page(new Page<>(current, size));  // 自动分页
    }

    // 条件查询（示例：根据年龄查询）
    @GetMapping("/age/{age}")
    public List<User> listByAge(@PathVariable Integer age) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("age", age);
        return userService.list(wrapper);  // 条件查询
    }

    // 添加
    @PostMapping
    public String add(@RequestBody User user) {
        userService.save(user);  // BaseMapper 的 insert
        return "添加成功";
    }

    // 修改
    @PutMapping
    public String update(@RequestBody User user) {
        userService.updateById(user);  // BaseMapper 的 updateById
        return "修改成功";
    }

    // 删除
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        userService.removeById(id);  // BaseMapper 的 deleteById（逻辑删除）
        return "删除成功";
    }
}