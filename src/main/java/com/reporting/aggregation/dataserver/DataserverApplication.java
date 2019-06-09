package com.reporting.aggregation.dataserver;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataserverApplication {
	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
	public DataserverApplication(ApplicationConfig applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

	public static void main(String[] args) {
		SpringApplication.run(DataserverApplication.class, args);
	}
}
