package top.bielai.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author bielai
 */
@MapperScan("top.bielai.shop.dao")
@SpringBootApplication
public class XxShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(XxShopApplication.class, args);
    }

}
