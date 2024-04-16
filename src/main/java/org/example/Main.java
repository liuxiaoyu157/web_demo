package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value={"org.example.ddd"})
@ServletComponentScan
public class Main {
    public static void main(String[] args) {
        ApplicationContext run = SpringApplication.run(Main.class);
    }
}