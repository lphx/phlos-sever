package cn.phlos;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSwagger2Doc
@MapperScan({"cn.phlos.mapper","cn.phlos.order.mapper","cn.phlos.product.mapper"})
public class PayApp  {

    public static void main(String[] args) {
        SpringApplication.run(PayApp.class, args);
    }

}
