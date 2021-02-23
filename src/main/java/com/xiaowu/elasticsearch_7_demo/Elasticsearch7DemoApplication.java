package com.xiaowu.elasticsearch_7_demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class Elasticsearch7DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Elasticsearch7DemoApplication.class, args);
    }

}
