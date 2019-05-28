package com.reporting.aggregation.dataserver.servers.riskreporting.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.lang.IgniteBiPredicate;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryUpdatedListener;
import java.util.UUID;

public class RiskAggregateIgniteCacheListener implements Runnable {
    protected Ignite ignite;
    protected UUID marketId;
    protected String cacheName;

    protected IgniteCache<String, Double> cache;

    public RiskAggregateIgniteCacheListener(UUID marketId, String pricingGroupName, String book, String Risk) {
        this.marketId = marketId;
        this.ignite = IgniteCacheFactory.getIgnite();
        this.cacheName = "/" + this.marketId.toString() + "/" + pricingGroupName + "/" + book + "/Risks";
    }

    @Override
    public void run() {
        // Creating a continuous query.
        ContinuousQuery<String, Double> qry = new ContinuousQuery<>();


        System.out.println("Attaching to cache : " + cacheName);
        this.cache = ignite.cache(cacheName);

        qry.setInitialQuery(new ScanQuery<>(new IgniteBiPredicate<String, Double>() {
            @Override public boolean apply(String key, Double val) {
                return true;
            }
        }));

        // Callback that is called locally when update notifications are received.
        qry.setLocalListener(new CacheEntryUpdatedListener<String, Double>() {
            @Override public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends Double>> evts) {
                for (CacheEntryEvent<? extends String, ? extends Double> e : evts)
                    System.out.println("{" + e.getKey() + ": " + e.getValue() + "}");
            }
        });

        // This filter will be evaluated remotely on all nodes.
        // Entry that pass this filter will be sent to the caller.
        qry.setRemoteFilterFactory(new Factory<CacheEntryEventFilter<String, Double>>() {
            @Override
            public CacheEntryEventFilter<String, Double> create() {
                return new CacheEntryEventFilter<String, Double>() {
                    @Override
                    public boolean evaluate(CacheEntryEvent<? extends String, ? extends Double> e) {
                        return true;
                    }
                };
            }
        });


        try (QueryCursor<Cache.Entry<String, Double>> cur = cache.query(qry)) {

            for (Cache.Entry<String, Double> e : cur) {
                System.out.println("{" + e.getKey() + ": " + e.getValue() + "}");
            }

            Thread.sleep(Long.MAX_VALUE);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
