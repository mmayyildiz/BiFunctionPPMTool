package com.bifunction.ppmtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PpmToolFullStackApplication {

	@Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(); // after creation of bean, we can autowire it
    }

    public static void main(String[] args) {
        SpringApplication.run(PpmToolFullStackApplication.class, args);
    }

}
