package com.reporting.aggregation.dataserver.servers.riskreporting.ignite;

import java.util.List;

public interface IDataCache<K, V> {
    List<V> getData();
    List<V> getDataWindown(int from, int to);
    Boolean deregisterListener(IDataCacheListener<K,V> listener);
}