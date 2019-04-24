package com.reporting.aggregation.dataserver;

import com.reporting.aggregation.dataserver.mock.MockDataServer;
import com.reporting.aggregation.dataserver.servers.groupby.StockTickerGroupByWebSocketServer;
import com.reporting.aggregation.dataserver.websockethandlers.StockTicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    MockDataServer mockDataServer;

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxBinaryMessageBufferSize(1024000);
        return container;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new StockTicker(mockDataServer), "/stockticker/viewport").setAllowedOrigins("*");
        registry.addHandler(new StockTickerGroupByWebSocketServer(mockDataServer), "/stockticker/groupby").setAllowedOrigins("*");
    }
}
