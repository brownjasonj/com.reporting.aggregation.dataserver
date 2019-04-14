package com.reporting.aggregation.dataserver.datamodel;

public class DataResponseColumnUpdate extends DataResponse {

    private int rowIndex;
    private String columnName;
    private Object value;

    public DataResponseColumnUpdate(int rowIndex, String columnName, Object value) {
        super(EDataResponseType.ColumnUpdate);
        this.rowIndex = rowIndex;
        this.columnName = columnName;
        this.value = value;
    }

    
    public int getRowIndex() {
        return rowIndex;
    }

    public String getColumnName() {
        return columnName;
    }

    public Object getValue() {
        return value;
    }

    public String toString() {
        return "Row: " + this.rowIndex + " Column: " + this.columnName + " Value: " + this.value;
    }
}
