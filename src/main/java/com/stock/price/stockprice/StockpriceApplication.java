package com.stock.price.stockprice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.stock.price")
public class StockpriceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockpriceApplication.class, args);
	}

}
