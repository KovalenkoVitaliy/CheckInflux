package com.example.demo;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Scope("singleton")
@PropertySource("classpath:influx.properties")
public class InstanseInflux {
    private static Logger logger = LoggerFactory.getLogger(CheckInflux.class);

    private String URL;
    private InfluxDB influxDB;
    private String databaseName;

    public InstanseInflux(@Value("${InfluxURL}") String URL, @Value("${DBname}") String databaseName){
        logger.info("Instanse of Influx is created");
        this.URL = URL;
        influxDB = InfluxDBFactory.connect(this.URL);
        this.databaseName = databaseName;
//        influxDB.query(new Query("CREATE DATABASE " + this.databaseName));
        influxDB.setDatabase(this.databaseName);
    }

    public void writeMeuseremts(){
        Pong response = influxDB.ping();
        QueryResult queryResult = influxDB.query(new Query(" SELECT count(*) FROM StatusOMS"));
        System.out.println(queryResult.getResults().get(0).getSeries().get(0).getValues().get(0).get(1));
        influxDB.write(Point.measurement("StatusOMS")
                .time(System.currentTimeMillis(), TimeUnit.NANOSECONDS)
                .tag("statusPing", String.valueOf(response.isGood()))
                .addField("ResponseTime", System.currentTimeMillis())
                .build());

        if (response.getVersion().equalsIgnoreCase("unknown")) {
            logger.info("INFLUX Error pinging server, STATUS = " + response.isGood());
        }
    }

    public String getURL() {
        return URL;
    }

    public InfluxDB getInfluxDB() {
        return influxDB;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
