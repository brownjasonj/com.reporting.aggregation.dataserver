package com.reporting.aggregation.dataserver.datamodel;


public class DataServerRequestWindowChange extends DataServerRequest {
    public int firstRow;
    public int lastRow;

    DataServerRequestWindowChange() {
        super();
    }
}
