package com.reporting.aggregation.dataserver.servers.riskreporting.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.springframework.stereotype.Component;

public class IgniteCacheFactory {
    protected static Ignite ignite = null;

    public static void connect() {
        if (ignite == null) {
            Ignition.setClientMode(true);
            IgniteCacheFactory.ignite = Ignition.start("examples/config/example-ignite.xml");
        }
    }

    public static Ignite getIgnite() {
        connect();
        return IgniteCacheFactory.ignite;
    }
}
