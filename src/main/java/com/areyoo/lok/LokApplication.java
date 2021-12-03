package com.areyoo.lok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xusong
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.areyoo.lok"})
public class LokApplication {
	public static void main(String[] args) {
		SpringApplication.run(LokApplication.class, args);
	}
}
