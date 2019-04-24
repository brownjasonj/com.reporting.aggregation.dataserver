package com.reporting.aggregation.dataserver.servers.viewport;

import java.io.IOException;

public interface IDataChange {
    void change(DataResponseColumnUpdate drcu) throws IOException;
}
