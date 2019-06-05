package com.reporting.aggregation.dataserver;

import com.reporting.aggregation.dataserver.kafka.CalculationContextKafkaConsumer;
import com.reporting.aggregation.dataserver.servers.riskreporting.PricingGroupDataServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataserverApplication {
	@Autowired
	private ApplicationConfig applicationConfig;

	@Autowired
	private CalculationContextKafkaConsumer calculationContextKafkaConsumer;

	private PricingGroupDataServer pricingGroupDataServer;
	
	@Autowired
	public DataserverApplication(ApplicationConfig applicationConfig, CalculationContextKafkaConsumer calculationContextKafkaConsumer) {
		this.applicationConfig = applicationConfig;
		this.calculationContextKafkaConsumer = calculationContextKafkaConsumer;
		this.pricingGroupDataServer = new PricingGroupDataServer("fxoptiondesk", calculationContextKafkaConsumer);
		this.calculationContextKafkaConsumer.run();
	}

	public static void main(String[] args) {
		SpringApplication.run(DataserverApplication.class, args);
	}

}
