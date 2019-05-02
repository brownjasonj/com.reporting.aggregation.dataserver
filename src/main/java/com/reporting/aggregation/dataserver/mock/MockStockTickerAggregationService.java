package com.reporting.aggregation.dataserver.mock;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.reporting.aggregation.dataserver.model.IRow;
import com.reporting.aggregation.dataserver.model.StockSymbol;
import com.reporting.aggregation.dataserver.model.StockSymbolRow;
import com.reporting.aggregation.dataserver.servers.groupby.GridGroupByDescriptor;

public class MockStockTickerAggregationService {
    private GridGroupByDescriptor groupDescriptor;
    private HashMap<String, LinkedHashMap<String, StockSymbolRow>> groupSets = null;

    public MockStockTickerAggregationService() {
        this.groupSets = new HashMap<>();
        this.groupDescriptor = StockSymbolRow.getGroupByDescriptor();
    }

    public GridGroupByDescriptor getGroupByDescriptor() {
        return this.groupDescriptor;
    }

    public List<IRow> getRows(String path) {
        if (groupSets.containsKey(path))
            return groupSets.get(path).values().stream().collect(Collectors.toList());
        else 
            return new ArrayList<>();
    }
    
    public void add(StockSymbol ss) {
        StockSymbolRow stockSymbolRow = new StockSymbolRow(ss);
        Object[] groupByColumnValues = stockSymbolRow.getGroupByColumnValues();

        if (groupByColumnValues.length == 0) {
            if (!groupSets.containsKey("/")) {
                LinkedHashMap<String, StockSymbolRow> ssGroupTopList = new LinkedHashMap<>();
                ssGroupTopList.put(ss.getExchange(), stockSymbolRow);
                groupSets.put("/", ssGroupTopList);
            }
            else {
                LinkedHashMap<String, StockSymbolRow> group = groupSets.get("/");
                if (!group.containsKey(ss.getExchange())) {
                    group.put(ss.getExchange(), stockSymbolRow);
                }
            }
        }
        else {
            // There is at least one group 
            if (!groupSets.containsKey("/")) {
                StockSymbolRow gssr = new StockSymbolRow();
                gssr.setColumnValue(groupDescriptor.getGroupColumns()[0], groupByColumnValues[0]);
                LinkedHashMap<String, StockSymbolRow> ssGroupTopList = new LinkedHashMap<>();
                ssGroupTopList.put(groupByColumnValues[0].toString(), gssr);
                groupSets.put("/", ssGroupTopList);
            }
            else {
                LinkedHashMap<String, StockSymbolRow> group = groupSets.get("/");
                if (!group.containsKey(groupByColumnValues[0].toString())) {
                    StockSymbolRow gssr = new StockSymbolRow();
                    gssr.setColumnValue(groupDescriptor.getGroupColumns()[0], groupByColumnValues[0]);
                    group.put(groupByColumnValues[0].toString(), gssr);
                }
            }

            StringWriter path = new StringWriter();

            for(int i = 1; i <= groupByColumnValues.length; i++) {
                path.append('/');
                path.write(groupByColumnValues[i-1].toString()); 
                String currentPath = path.toString();

                LinkedHashMap<String, StockSymbolRow> group;
                if (!groupSets.containsKey(currentPath)) {
                    group = new LinkedHashMap<>();
                    groupSets.put(currentPath, group);
                }
                else {
                    group = groupSets.get(currentPath);
                }

                // if we readhed the last group
                if (i == groupByColumnValues.length) {
                    group.put(ss.getCode(), stockSymbolRow);
                }
                else {
                    if (!group.containsKey(groupByColumnValues[i].toString())) {
                        StockSymbolRow gssr = new StockSymbolRow();
                        for(int j = 0; j <= i; j++) {
                            gssr.setColumnValue(groupDescriptor.getGroupColumns()[j], groupByColumnValues[j]);
                        }
                        group.put(groupByColumnValues[i].toString(), gssr);
                    }
                }
            }
        }
    }
}