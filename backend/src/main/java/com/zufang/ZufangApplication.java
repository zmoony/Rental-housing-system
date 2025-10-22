package com.zufang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 租房管理系统主应用类
 */
@SpringBootApplication
@MapperScan("com.zufang.mapper")
public class ZufangApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZufangApplication.class, args);
    }
}
