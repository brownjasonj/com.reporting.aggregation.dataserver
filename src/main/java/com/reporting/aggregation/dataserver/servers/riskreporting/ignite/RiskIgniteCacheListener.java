package com.reporting.aggregation.dataserver.servers.riskreporting.ignite;

import com.reporting.mocks.model.trade.Tcn;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryUpdatedListener;
import java.util.UUID;

public class RiskIgniteCacheListener implements Runnable {
    
    protected Ignite ignite;
    protected UUID marketId;
    protected String Risk;
    protected String cacheName;

    protected IgniteCache<Tcn, Double> cache;


    public RiskIgniteCacheListener(UUID marketId, String pricingGroupName, String book, String Risk) {
        this.marketId = marketId;
        this.Risk = Risk;
        this.ignite = IgniteCacheFactory.getIgnite();
        String risks = "/" + this.marketId.toString() + "/" + pricingGroupName + "/" + book + "/Risks";
        this.cacheName = risks + "/" + this.Risk + "/Tcn";
    }


    public Double getTcns(Tcn tcn) {
        System.out.println("Cache: " + cacheName);
        this.cache = ignite.cache(cacheName);
        if (cache.containsKey(tcn)) {
            Double value = cache.get(tcn);
            System.out.println("Tcn: " + tcn +  " exists in cache with value = " + value);
            return value;
        }
        else {
            System.out.println("Tcn: " + tcn + " does NOT exist in cache");
            return -1.0;
        }
    }

    @Override
    public void run() {
        // Creating a continuous query.
        ContinuousQuery<UUID, Double> qry = new ContinuousQuery<>();


        System.out.println("Attaching to cache : " + cacheName);
        this.cache = ignite.cache(cacheName);

//        qry.setInitialQuery(new ScanQuery<>((k, v) -> true));
//
//        qry.setLocalListener((evts) ->
//                evts.forEach(e -> System.out.println("Tcn =" + e.getKey().toString() + ", value =" + e.getValue())));
//
//        qry.setRemoteFilter(e -> true);


        qry.setInitialQuery(new ScanQuery<>(new IgniteBiPredicate<UUID, Double>() {
            @Override public boolean apply(UUID key, Double val) {
                return true;
            }
        }));

        // Callback that is called locally when update notifications are received.
        qry.setLocalListener(new CacheEntryUpdatedListener<UUID, Double>() {
            @Override public void onUpdated(Iterable<CacheEntryEvent<? extends UUID, ? extends Double>> evts) {
                for (CacheEntryEvent<? extends UUID, ? extends Double> e : evts)
                    System.out.println("Tcn =" + e.getKey().toString() + ", value =" + e.getValue());
            }
        });

        // This filter will be evaluated remotely on all nodes.
        // Entry that pass this filter will be sent to the caller.
        qry.setRemoteFilterFactory(new Factory<CacheEntryEventFilter<UUID, Double>>() {
            @Override
            public CacheEntryEventFilter<UUID, Double> create() {
                return new CacheEntryEventFilter<UUID, Double>() {
                    @Override
                    public boolean evaluate(CacheEntryEvent<? extends UUID, ? extends Double> e) {
                        return true;
                    }
                };
            }
        });


        try (QueryCursor<Cache.Entry<UUID, Double>> cur = cache.query(qry)) {

            for (Cache.Entry<UUID, Double> e : cur) {
                System.out.println("{Market: " + this.marketId.toString() + ", TCN: " + e.getKey().toString() + ", " + this.Risk + ": " + e.getValue() + "}");
            }

            Thread.sleep(Long.MAX_VALUE);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
