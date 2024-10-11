package com.kjeldsen.db.migration;

import org.springframework.boot.SpringApplication;
import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = "com.kjeldsen")
@EnableMongock
public class MigrationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MigrationApplication.class, args);
        context.close();
    }
}