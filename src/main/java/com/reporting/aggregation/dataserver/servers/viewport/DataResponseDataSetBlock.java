package com.reporting.aggregation.dataserver.servers.viewport;

import java.util.ArrayList;
import java.util.List;

public class DataResponseDataSetBlock<T> extends DataResponse {
    private ArrayList<T> dataSet;
    public int firstRow;
    public int lastRow;

    public DataResponseDataSetBlock(int firstRow, int lastRow) {
        super(EDataResponseType.DataSetBlock);
        this.dataSet = new ArrayList<>();
        this.firstRow = firstRow;
        this.lastRow = lastRow;
    }

    public void addElement(T element) {
        this.dataSet.add(element);
    }

    public void clear() {
        dataSet.clear();
        firstRow = 0;
        lastRow = 0;
    }

    public List<T> getDataSet() {
        return this.dataSet;
    }

    /**
     * @param dataSet the dataSet to set
     */
    public void setDataSet(ArrayList<T> dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * @return the firstRow
     */
    public int getFirstRow() {
        return firstRow;
    }

    /**
     * @return the lastRow
     */
    public int getLastRow() {
        return lastRow;
    }
}