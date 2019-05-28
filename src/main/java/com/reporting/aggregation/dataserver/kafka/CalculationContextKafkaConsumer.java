package com.reporting.aggregation.dataserver.kafka;

import com.google.gson.Gson;
import com.reporting.aggregation.dataserver.ApplicationConfig;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.risks.Pv;
import com.reporting.mocks.model.risks.aggregate.DeltaAggregate;
import com.reporting.mocks.model.risks.aggregate.PvAggregate;
import com.reporting.mocks.model.risks.aggregate.RiskAggregate;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

@Component
public class CalculationContextKafkaConsumer implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(CalculationContextKafkaConsumer.class.getName());
    
    private String BOOTSTRAPSERVER;
    private String CALCULATIONCONTEXT;
    private Properties kafkaProperties;
    private KafkaConsumer<UUID, String> consumer;
    private Map<String, List<ICalculationContextListener>> listeners;



    @Autowired
    public CalculationContextKafkaConsumer(ApplicationConfig applicationConfig) {
        this.BOOTSTRAPSERVER = applicationConfig.getKafkaServer();
        this.CALCULATIONCONTEXT = applicationConfig.getCalculationContextTopic();

        this.kafkaProperties = new Properties();

        this.kafkaProperties.put("bootstrap.servers", this.BOOTSTRAPSERVER);
        this.kafkaProperties.put("group.id", "test1");
        this.kafkaProperties.put("key.deserializer", "com.reporting.kafka.serialization.UUIDDeserializer");
        this.kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        this.consumer = new KafkaConsumer<UUID, String>(this.kafkaProperties);
        this.consumer.subscribe(Collections.singletonList(this.CALCULATIONCONTEXT));

        this.listeners = new HashMap<>();
    }

    public void addListener(String pricingGroupName, ICalculationContextListener listener) {
        List<ICalculationContextListener> pricingGroupListeners;
        if (this.listeners.containsKey(pricingGroupName)) {
            pricingGroupListeners = this.listeners.get(pricingGroupName);
        }
        else {
            pricingGroupListeners = new LinkedList<>();
            this.listeners.put(pricingGroupName, pricingGroupListeners);
        }
        pricingGroupListeners.add(listener);
    }
    
    public void consume() {
        Gson gson = new Gson();
        try {
            while (true) {
                ConsumerRecords<UUID, String> records = this.consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<UUID, String> record : records)
                {
                    CalculationContext calculationContext = gson.fromJson(record.value(), CalculationContext.class);
                    String pricingGroupName = calculationContext.getPricingGroup().getName();
                    if (this.listeners.containsKey(pricingGroupName)) {
                        List<ICalculationContextListener> listeners = this.listeners.get(pricingGroupName);
                        if (listeners != null) {
                            for(ICalculationContextListener listener : listeners) {
                                listener.update(calculationContext);
                            }
                        }
                    }
                    
                }
            }
        } finally {
            consumer.close();
        }
    }
    @Override
    public void run() {
        System.out.println("DataServer: CalculationContext Kafka Consumer Started");
        consume();
    }
}
