package com.reporting.aggregation.dataserver.servers.groupby;

import java.util.List;

import com.reporting.aggregation.dataserver.model.IRow;
import com.reporting.aggregation.dataserver.servers.groupby.requests.GroupByRequest;

public interface IGridGroupByDataService {
    String getName();
    IGridGroupByDataConnector getDataConnector();
    List<IRow> getDataRows(GroupByRequest gbr);
}