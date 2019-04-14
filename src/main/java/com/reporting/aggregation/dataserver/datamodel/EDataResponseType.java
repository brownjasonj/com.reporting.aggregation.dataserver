package com.reporting.aggregation.dataserver.datamodel;

public enum EDataResponseType {
    DataSetSize("DataSetSize"),
    ColumnUpdate("ColumnUpdate"),
    DataSet("DataSet");

    private String name;

    EDataResponseType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
