package com.reporting.aggregation.dataserver.servers.riskreporting;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.reporting.aggregation.dataserver.kafka.CalculationContextKafkaConsumer;
import com.reporting.aggregation.dataserver.kafka.ICalculationContextListener;
import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.RiskResultIgniteCache;
import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches.IDataCacheListener;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;

public class PricingGroupDataServer implements ICalculationContextListener, IDataCacheListener<UUID, RiskResult<? extends Risk>> {
    private final static Logger LOGGER = Logger.getLogger(PricingGroupDataServer.class.getName());

    private CalculationContextKafkaConsumer calculationContextKafkaConsumer;
    private RiskResultIgniteCache riskResultIgniteCache;
    private Gson gson;

    private String pricingGroupName;

    public PricingGroupDataServer(String pricingGroupName, CalculationContextKafkaConsumer calculationContextKafkaConsumer) {
        this.calculationContextKafkaConsumer = calculationContextKafkaConsumer;
        this.pricingGroupName = pricingGroupName;
        this.riskResultIgniteCache = new RiskResultIgniteCache(pricingGroupName);
        this.calculationContextKafkaConsumer.addListener(pricingGroupName, this);
        this.riskResultIgniteCache = new RiskResultIgniteCache(pricingGroupName);
        this.gson = new Gson();
    }

    @Override
    public void update(CalculationContext calculationContext) {
        MarketEnvId marketEnvId = calculationContext.get(RiskType.PV);
        
        List<String> cacheNames = riskResultIgniteCache.getAvailableCaches(marketEnvId);
     
        if (!cacheNames.isEmpty()) {
            String cacheName = cacheNames.get(0);
            this.riskResultIgniteCache.registerListener(cacheName, this);
            LOGGER.info("Listening to Cache: " + cacheName);
        }
    }

    @Override
    public void update(RiskResult<? extends Risk> updatedValue) {
        LOGGER.info(this.gson.toJson(updatedValue));
    }
}