package com.reporting.aggregation.dataserver.servers.viewport;

public class DataResponseRowUpdate<T> extends DataResponse {
    private T row;

    protected DataResponseRowUpdate(T row) {
        super(EDataResponseType.RowUpdate);
        this.row = row;
    }

    public T getRow() {
        return row;
    }
}