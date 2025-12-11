package com.example.mybatisplusdemo.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

// 标记这是一个配置类，Spring会扫描它来生成Bean
@Configuration 
public class MyBatisPlusConfig {

    // =================================================================
    // 1. MyBatis-Plus 拦截器配置 (分页、乐观锁等)
    // =================================================================

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁插件（如果需要）
        // interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    // =================================================================
    // 2. 自动字段填充处理器 (createTime, updateTime)
    // =================================================================

    @Bean //注解将 MetaObjectHandler 实例注册为 Spring Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            // 匿名内部类实现 MetaObjectHandler 接口

            @Override
            public void insertFill(MetaObject metaObject) {
                // 插入时自动填充 createTime 和 updateTime
                this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                // 更新时自动填充 updateTime
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
            }
        };
    }
}