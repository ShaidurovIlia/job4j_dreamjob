package ru.job4j.dreamjob;


import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * В классе происходит подключения к БД через jdbc драйвер
 * и создание обьекта пула соединений BasicDataSource.
 * Аннотация указывает Spring загрузить объект BasicDataSource в контекст.
 */
@SpringBootApplication
public class Main {

    /**
     * Подключение к настройкам файла .properties.
     * @return
     */

    private Properties loadDbProperties() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        Main.class.getClassLoader()
                                .getResourceAsStream("db.properties")
                )
        )) {
            cfg.load(io);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return cfg;
    }

    /**
     * Создание объекта BasicDataSource из
     * строк db.properties.
     * Spring загрузит его к себе в контекст.
     * Это нужно, чтобы проинициализировать класс PostDbStore.
     * @return
     */
    @Bean
    public BasicDataSource loadPool() {
        Properties cfg = loadDbProperties();
        BasicDataSource pool = new BasicDataSource();
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
        return pool;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Go to http://localhost:8080/index");
    }
}
