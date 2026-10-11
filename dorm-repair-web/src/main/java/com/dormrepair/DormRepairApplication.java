package com.dormrepair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 宿舍报修管理系统 —— 后端启动入口
 *
 * @SpringBootApplication 是组合注解，等价于：
 *   @SpringBootConfiguration 标记配置类
 *   @EnableAutoConfiguration  开启自动配置
 *   @ComponentScan            扫描本包及子包下的所有组件
 */
@SpringBootApplication
public class DormRepairApplication {

    public static void main(String[] args) {
        SpringApplication.run(DormRepairApplication.class, args);
    }
}
