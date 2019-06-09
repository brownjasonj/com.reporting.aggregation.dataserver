package com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches;

import java.util.UUID;

import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.risks.Risk;

public class RiskResultDataCache extends DataCache<UUID, RiskResult<? extends Risk>> {

    public RiskResultDataCache(String cacheName) {
        super(cacheName);
    }
    
}