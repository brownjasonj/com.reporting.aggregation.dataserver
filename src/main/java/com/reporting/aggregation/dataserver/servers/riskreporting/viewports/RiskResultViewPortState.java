package com.reporting.aggregation.dataserver.servers.riskreporting.viewports;

import com.reporting.aggregation.dataserver.mock.ViewPortState;
import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches.PricingGroupDataCache;

import org.springframework.web.socket.WebSocketSession;

public class RiskResultViewPortState extends ViewPortState {
    private String pricingGroupName;
    private String marketIdName;
    private PricingGroupDataCache pricingGroupDataCache;

    public RiskResultViewPortState(WebSocketSession session, String pricingGroupName, String marketIdName, PricingGroupDataCache pricingGroupDataCache) {
        super(session);
        this.pricingGroupName = pricingGroupName;
        this.marketIdName = marketIdName;
        this.pricingGroupDataCache = pricingGroupDataCache;
    }

    public String getPricingGroupName() {
        return pricingGroupName;
    }

    public void setPricingGroupName(String pricingGroupName) {
        this.pricingGroupName = pricingGroupName;
    }

    public String getMarketIdName() {
        return marketIdName;
    }

    public void setMarketIdName(String marketIdName) {
        this.marketIdName = marketIdName;
    }

    public PricingGroupDataCache getPricingGroupDataCache() {
        return pricingGroupDataCache;
    }

    public void setPricingGroupDataCache(PricingGroupDataCache pricingGroupDataCache) {
        this.pricingGroupDataCache = pricingGroupDataCache;
    }

    
    
}