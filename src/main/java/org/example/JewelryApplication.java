package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JewelryApplication {

    public static void main(String[] args) {

        SpringApplication.run(JewelryApplication.class, args);
    }
}
