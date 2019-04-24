package com.reporting.aggregation.dataserver.servers.viewport;


public class DataServerRequestWindowChange extends DataServerRequest {
    public int firstRow;
    public int lastRow;

    DataServerRequestWindowChange() {
        super();
    }
}
