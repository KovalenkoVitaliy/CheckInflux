package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class CheckInflux implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger(CheckInflux.class);

    @Autowired
    private InstanseInflux instanseInflux;


    public CheckInflux() {
        logger.info("StartUP of Application");
    }

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            Thread.sleep(5000);
            instanseInflux.writeMeuseremts();
        }
    }
}
