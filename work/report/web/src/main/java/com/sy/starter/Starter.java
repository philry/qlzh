package com.sy.starter;


import com.sy.core.netty.tcp.NettyServer;
import com.sy.service.WorkService;
import com.sy.utils.DateUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;


@ComponentScan(basePackages = {"com.sy"})
@SpringBootApplication
@EnableJpaRepositories("com.sy.dao")
@EntityScan("com.sy.entity")
@EnableScheduling
@MapperScan("com.sy.dao")
public class Starter implements CommandLineRunner {

    @Autowired
    WorkService workService;

    @Resource
    private NettyServer nettyServer;

    public static void main(String[] args) {
       SpringApplication.run(Starter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /*Date date = new Date();
        System.out.println("--------------"+date+"----------------");
        System.out.println("------------");*/

        /*Date openTime = workService.getLastOpenTimeByMachine(28);
        System.out.println("--------------"+openTime+"----------------");*/

        nettyServer.run(95);
    }
}