package top.sheepyu.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("top.sheepyu.module")
@EnableCaching
public class SheepyuServer {
    public static void main(String[] args) {
        SpringApplication.run(SheepyuServer.class, args);
        System.out.println("项目启动成功!");
    }
}
