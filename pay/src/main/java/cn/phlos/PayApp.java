package cn.phlos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"cn.phlos.mapper","cn.phlos.order.mapper"})
public class PayApp  {

    public static void main(String[] args) {
        SpringApplication.run(PayApp.class, args);
    }

}
