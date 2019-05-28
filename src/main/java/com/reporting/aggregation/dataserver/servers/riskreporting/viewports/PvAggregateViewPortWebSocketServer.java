package com.reporting.aggregation.dataserver.servers.riskreporting.viewports;

import java.util.Map;

import com.google.gson.Gson;
import com.reporting.aggregation.dataserver.mock.ViewPortState;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
public class PvAggregateViewPortWebSocketServer extends AbstractWebSocketHandler {
    private static Map<String, ViewPortState> viewPortStates = null;
    private Gson gson;
    
    public PvAggregateViewPortWebSocketServer() {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }
    
}