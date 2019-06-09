package com.reporting.aggregation.dataserver.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.reporting.aggregation.dataserver.kafka.CalculationContextKafkaConsumer;
import com.reporting.aggregation.dataserver.servers.riskreporting.PricingGroupDataServer;
import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches.PricingGroupDataCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PricingGroupCacheController {
    @Autowired
    private CalculationContextKafkaConsumer calculationContextKafkaConsumer;
    private PricingGroupDataCache pgDataCache;

    @Autowired
    public PricingGroupCacheController(CalculationContextKafkaConsumer calculationContextKafkaConsumer) {
        this.calculationContextKafkaConsumer = calculationContextKafkaConsumer;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/pricinggroupmarketcaches/{pricinggroup}")
    public List<String> getMarketIds(@PathVariable String pricinggroup) {
        List<String> results;
        if (this.pgDataCache == null) {
            this.pgDataCache = new PricingGroupDataCache(pricinggroup);
        }
        results = pgDataCache.getKeys();
        return results;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/pricinggroupcaches/{pricinggroup}")
    public HashSet<String> getMarketIdCaches(@PathVariable String pricinggroup, @RequestParam("marketid")UUID marketid) {
        HashSet<String> results;
        if (this.pgDataCache == null) {
            this.pgDataCache = new PricingGroupDataCache(pricinggroup);
        }
        results = pgDataCache.getValue(marketid.toString());
        return results;
    }

}