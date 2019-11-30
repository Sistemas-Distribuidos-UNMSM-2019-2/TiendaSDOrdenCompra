package com.fisi.sd;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
//@EnableJpaAuditing
public class TiendaSdApplication {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(TiendaSdApplication.class);
        builder.headless(false).run(args);
	}

}
