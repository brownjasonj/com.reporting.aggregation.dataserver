package com.reporting.aggregation.dataserver.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reporting.aggregation.dataserver.servers.groupby.GridGroupByDescriptor;

public class StockSymbolRow implements IRow {
    protected static GridGroupByDescriptor gridGroupByDescriptor;
    protected Map<String, Object> kvps;

    static {
        StockSymbolRow.gridGroupByDescriptor = new GridGroupByDescriptor("stocksymbol", new String[] { "exchange", "sector" },
                new String[] { "code", "name", "volume", "mid", "bid", "ask" });
    }

    public static GridGroupByDescriptor getGroupByDescriptor() {
        return StockSymbolRow.gridGroupByDescriptor;
    }

    public StockSymbolRow() {
        kvps = new HashMap<>();
        for (String gcn : StockSymbolRow.gridGroupByDescriptor.getGroupColumns()) {
            kvps.put(gcn, null);
        }
        for (String vcn : StockSymbolRow.gridGroupByDescriptor.getValueColumns()) {
            kvps.put(vcn, null);
        }
    }

    public StockSymbolRow(StockSymbol ss) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.kvps = objectMapper.convertValue(ss, Map.class);
    }

    @Override
    public Map<String,Object> getKvps() {
        return this.kvps;
    }

    @Override
    public Object getColumnValue(String columnName) {
        if (kvps.containsKey(columnName))
            return kvps.get(columnName);
        else
            return null;
    }

    @Override
    public Object[] getColumnValues() {
        return kvps.values().toArray();
    }

    @Override
    public String[] getColumns() {
        return kvps.keySet().toArray(new String[0]);
    }

    @Override
    public void setColumnValue(String columnName, Object columnValue) {
        kvps.put(columnName, columnValue);
    }

    @Override
    public Object[] getGroupByColumnValues() {
        String[] colNames = StockSymbolRow.gridGroupByDescriptor.getGroupColumns();
        Object[] gbcv = new Object[colNames.length];
        for(int i = 0; i < colNames.length; i++) {
            gbcv[i] = kvps.get(colNames[i]);
        }
        return gbcv;
    }
}