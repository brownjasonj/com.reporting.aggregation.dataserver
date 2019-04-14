package com.reporting.aggregation.dataserver.datamodel;

public class DataResponseDataSetSize extends DataResponse {
    private int rowCount;

    public DataResponseDataSetSize(int rowCount) {
        super(EDataResponseType.DataSetSize);
        this.rowCount = rowCount;
    }

    public int getRowCount() {
        return rowCount;
    }
}
