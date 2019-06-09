package com.reporting.aggregation.dataserver.servers.riskreporting.ignite;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.cache.Cache;

import com.reporting.mocks.model.risks.Risk;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.lang.IgniteBiPredicate;

import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches.IDataCache;
import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches.DataCache;
import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches.IDataCacheListener;
import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches.IgniteCacheFactory;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.id.MarketEnvId;

public class RiskResultIgniteCache {
    private String pricingGroupName;
    // map between cachenames and datacache objects
    private Map<String, DataCache<UUID,RiskResult<? extends Risk>>> dataCaches;
    protected Ignite ignite;
    protected IgniteCache<String, HashSet<String>> pricingGroupCache;

    public RiskResultIgniteCache(String pricingGroupName) {
        this.pricingGroupName = pricingGroupName;
        this.dataCaches = new HashMap<>();
        this.ignite = IgniteCacheFactory.getIgnite();
        this.pricingGroupCache = ignite.getOrCreateCache(this.pricingGroupName);
    }

    public List<String> getAvailableCaches(MarketEnvId marketEnvId) {
        String marketIdName = marketEnvId.getId().toString();
        List<String> cacheSet = new LinkedList<>();
        IgniteBiPredicate<String, HashSet<String>> filter = new IgniteBiPredicate<String, HashSet<String>>() {
			private static final long serialVersionUID = 1L;
            @Override
            public boolean apply(String e1, HashSet<String> e2) {
                // return e1.equals(marketIdName);
                return true;
            }
        };

        QueryCursor<Cache.Entry<String, HashSet<String>>> cursor = this.pricingGroupCache.query(new ScanQuery<>(filter));

        for(Cache.Entry<String, HashSet<String>> entry : cursor) {
            HashSet<String> caches = entry.getValue();
            cacheSet.addAll(caches);
        }

        return cacheSet;
    }

    public IDataCache<UUID, RiskResult<? extends Risk>> registerListener(String cacheName, IDataCacheListener<UUID, RiskResult<? extends Risk>> listener) {
        DataCache<UUID, RiskResult<? extends Risk>> dataCache = null;
        if (dataCaches.containsKey(cacheName)) {
            dataCache = dataCaches.get(cacheName);
        }
        else {
            dataCache = new DataCache<>(cacheName);
            dataCaches.put(cacheName, dataCache);
            dataCache.startCacheQuery();
        }
        dataCache.addUpdateListener(listener);
        return dataCache;
    }
}
