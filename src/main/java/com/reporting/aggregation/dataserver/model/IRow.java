package com.reporting.aggregation.dataserver.model;

import java.util.Map;

public interface IRow {
    String[] getColumns();
    Object[] getColumnValues();
    Object[] getGroupByColumnValues();
    Object getColumnValue(String columnName);
    void setColumnValue(String columnName, Object columnValue);
    Map<String,Object> getKvps();
}