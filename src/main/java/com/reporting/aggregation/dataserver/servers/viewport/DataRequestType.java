package com.reporting.aggregation.dataserver.servers.viewport;

public enum DataRequestType {
    WindowChangeSize("WindowChangeSize"),
    AllData("AllData");

    private String name;

    DataRequestType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
