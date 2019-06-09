package com.reporting.aggregation.dataserver.servers.viewport;

public class DataServerRequestConnectionParameters extends DataServerRequest {
    private String pricingGroupName;
    private String marketIdName;

    DataServerRequestConnectionParameters() {
        super();
    }

    public String getPricingGroupName() {
        return pricingGroupName;
    }

    public String getMarketIdName() {
        return marketIdName;
    }
    
}