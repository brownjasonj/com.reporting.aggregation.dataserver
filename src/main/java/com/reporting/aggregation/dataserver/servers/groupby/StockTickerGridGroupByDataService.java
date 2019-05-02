package com.reporting.aggregation.dataserver.servers.groupby;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import com.reporting.aggregation.dataserver.mock.MockDataServer;
import com.reporting.aggregation.dataserver.mock.MockStockTickerAggregationService;
import com.reporting.aggregation.dataserver.model.IRow;
import com.reporting.aggregation.dataserver.model.StockSymbol;
import com.reporting.aggregation.dataserver.servers.groupby.requests.GroupByRequest;

public class StockTickerGridGroupByDataService implements IGridGroupByDataService {
    protected MockDataServer mockDataServer;
    protected MockStockTickerAggregationService aggregationService;
    protected static HashMap<String, GridGroupByState> groupByStates = null;
    private final String name = "/stockticker/groupby";

    public StockTickerGridGroupByDataService(MockDataServer mockDataserver) {
        this.mockDataServer = mockDataserver;
        this.aggregationService = new MockStockTickerAggregationService();

        for (StockSymbol ss : mockDataserver.getAllData()) {
            aggregationService.add(ss);
        }
    }

    @Override
    public IGridGroupByDataConnector getDataConnector() {
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<IRow> getDataRows(GroupByRequest gbr) {
        String[] cols = gbr.getGroupKeys();
        if (cols.length == 0)
            return aggregationService.getRows("/");
        else {
            StringWriter path = new StringWriter();
            for(String col : cols) {
                path.append('/');
                path.write(col);
            }
            return aggregationService.getRows(path.toString());
        }
    }
    
}