package com.reporting.aggregation.dataserver.datamodel;

import java.io.IOException;

public interface IDataChange {
    void change(DataResponseColumnUpdate drcu) throws IOException;
}
