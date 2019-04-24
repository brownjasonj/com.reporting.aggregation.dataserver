package com.reporting.aggregation.dataserver.servers.viewport;

import java.util.ArrayList;
import java.util.List;

public class DataResponseDataSet<T> extends DataResponse {
    private ArrayList<T> dataSet;

    public DataResponseDataSet() {
        super(EDataResponseType.DataSet);
        this.dataSet = new ArrayList<>();
    }

    public void addElement(T element) {
        this.dataSet.add(element);
    }

    public List<T> getDataSet() {
        return this.dataSet;
    }
}