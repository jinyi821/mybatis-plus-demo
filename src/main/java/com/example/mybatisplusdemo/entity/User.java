package com.example.mybatisplusdemo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
@TableName("user")  // 指定表名（可选，如果类名与表名一致可省略）
public record User (
        @TableId(type = IdType.ASSIGN_ID)
        Long id,
        String username,
        String password,
        String nickname,
        Integer age,
        @TableField(fill = FieldFill.INSERT)  // 插入时自动填充
        LocalDateTime createTime,

        @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入/更新时自动填充
        LocalDateTime updateTime,

        @TableLogic  // 逻辑删除（deleted=1为删除）
        Integer deleted
) implements Serializable {
}
