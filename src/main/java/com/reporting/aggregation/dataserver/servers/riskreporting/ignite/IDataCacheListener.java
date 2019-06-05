package com.reporting.aggregation.dataserver.servers.riskreporting.ignite;

public interface IDataCacheListener<K,V> {
    void update(V updatedValue);
}