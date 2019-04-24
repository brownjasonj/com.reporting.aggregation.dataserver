package com.reporting.aggregation.dataserver.servers.groupby;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;
import com.reporting.aggregation.dataserver.mock.MockDataServer;
import com.reporting.aggregation.dataserver.model.StockSymbol;
import com.reporting.aggregation.dataserver.servers.viewport.DataResponseDataSetBlock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
public class StockTickerGroupByWebSocketServer extends AbstractWebSocketHandler {
    @Autowired
    MockDataServer mockDataServer;

    protected Gson gson;
    protected static HashMap<String, DataResponseDataSetBlock<StockSymbol>> groupByStates = null;

    @Autowired
    public StockTickerGroupByWebSocketServer(MockDataServer mockDataServer) {
        super();
        if (StockTickerGroupByWebSocketServer.groupByStates == null) {
            StockTickerGroupByWebSocketServer.groupByStates = new HashMap<>();
        }
        this.mockDataServer = mockDataServer;
        this.gson = new Gson();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        if (!StockTickerGroupByWebSocketServer.groupByStates.containsKey(session.getId())) {
            StockTickerGroupByWebSocketServer.groupByStates.put(session.getId(), new DataResponseDataSetBlock<>(0,0));
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);
        if (StockTickerGroupByWebSocketServer.groupByStates.containsKey(session.getId())) {
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        GroupByRequest rbr = this.gson.fromJson(message.getPayload(), GroupByRequest.class);
        List<StockSymbol> stock = this.mockDataServer.getStockSymbolGrouping(rbr.getGroup());
        DataResponseDataSetBlock<StockSymbol> drdsb = new DataResponseDataSetBlock<>(rbr.startRow, stock.size());
        for(int i = rbr.startRow; i < rbr.endRow && i < stock.size(); i++) {
            drdsb.addElement(stock.get(i));
        }
        session.sendMessage(new TextMessage(this.gson.toJson(drdsb)));
    }
    
}