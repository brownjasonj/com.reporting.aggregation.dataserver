package com.reporting.aggregation.dataserver.servers.viewport;

public enum EDataResponseType {
    CalculationContextChangeEvent("CalculationContextChangeEvent"),
    RowUpdate("RowUpdate"),
    DataSetSize("DataSetSize"),
    ColumnUpdate("ColumnUpdate"),
    DataSet("DataSet"),
    DataSetBlock("DataSetBlock");

    private String name;

    EDataResponseType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
