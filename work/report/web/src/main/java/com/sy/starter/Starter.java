package com.sy.starter;

import com.sy.constant.AppConstant;
import com.sy.core.netty.tcp.NettyServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@ComponentScan(basePackages = {"com.sy"})
@SpringBootApplication
@EnableJpaRepositories("com.sy.dao")
@EntityScan("com.sy.entity")
@EnableScheduling
@MapperScan("com.sy.dao")
public class Starter implements CommandLineRunner {

    @Resource
    private NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        nettyServer.run(90);
    }
}
