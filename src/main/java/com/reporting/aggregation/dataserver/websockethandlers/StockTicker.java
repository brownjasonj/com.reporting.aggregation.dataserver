package com.reporting.aggregation.dataserver.websockethandlers;

import com.google.gson.Gson;
import com.reporting.aggregation.dataserver.mock.MockDataServer;
import com.reporting.aggregation.dataserver.mock.ViewPortState;
import com.reporting.aggregation.dataserver.model.StockSymbol;
import com.reporting.aggregation.dataserver.servers.viewport.DataResponseDataSet;
import com.reporting.aggregation.dataserver.servers.viewport.DataResponseDataSetSize;
import com.reporting.aggregation.dataserver.servers.viewport.DataServerRequest;
import com.reporting.aggregation.dataserver.servers.viewport.DataServerRequestWindowChange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StockTicker extends AbstractWebSocketHandler {
    @Autowired
    MockDataServer mockDataServer;

    private static Map<String, ViewPortState> viewPortStates = null;

    private Gson gson;

    public StockTicker(MockDataServer mockDataServer) {
        super();
        if (StockTicker.viewPortStates == null) {
            StockTicker.viewPortStates = new ConcurrentHashMap<>();
        }
        this.gson = new Gson();
        this.mockDataServer = mockDataServer;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        if (!StockTicker.viewPortStates.containsKey(session.getId())) {
            ViewPortState vpc = new ViewPortState(session);
            StockTicker.viewPortStates.put(session.getId(), vpc);
            mockDataServer.registerListener(vpc);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        super.afterConnectionClosed(session, status);
        if (!StockTicker.viewPortStates.containsKey(sessionId)) {
            ViewPortState vpc = StockTicker.viewPortStates.get(sessionId);
            mockDataServer.deregisterListner(vpc);
            StockTicker.viewPortStates.remove(sessionId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        DataServerRequest dsr = gson.fromJson(message.getPayload(), DataServerRequest.class);
        switch (dsr.requestType) {
            case AllData:
                session.sendMessage(new TextMessage(gson.toJson(new DataResponseDataSetSize(mockDataServer.getAllData().size()))));
                break;
            case WindowChangeSize:
                DataServerRequestWindowChange dsrwc = gson.fromJson(message.getPayload(), DataServerRequestWindowChange.class);
                ViewPortState vps = StockTicker.viewPortStates.get(session.getId());
                vps.setFirstRow(dsrwc.firstRow);
                vps.setLastRow(dsrwc.lastRow);
                List<StockSymbol> dataSet = mockDataServer.getAllData();
                DataResponseDataSet<StockSymbol> rsrds = new DataResponseDataSet<>();
                for(int i = dsrwc.firstRow; i < dsrwc.lastRow; i++) {
                    rsrds.addElement(dataSet.get(i));
                }
                session.sendMessage(new TextMessage(gson.toJson(rsrds)));
                break;
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        System.out.println("New Binary Message Received");
        session.sendMessage(message);
    }
}
