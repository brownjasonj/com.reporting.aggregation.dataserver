package com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches;

public interface IDataCacheListener<K,V> {
    void update(V updatedValue);
}