package com.reporting.aggregation.dataserver.servers.groupby;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.reporting.aggregation.dataserver.model.IRow;
import com.reporting.aggregation.dataserver.servers.groupby.requests.GroupByRequest;
import com.reporting.aggregation.dataserver.servers.viewport.DataResponseDataSetBlock;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class StockTickerGroupByWebSocketServer extends AbstractWebSocketHandler {
    protected IGridGroupByDataService groupByDataService;
    protected Gson gson;
    protected static HashMap<String, GridGroupByState> groupByStates = null;

    public StockTickerGroupByWebSocketServer(IGridGroupByDataService groupByDataService) {
        super();
        if (StockTickerGroupByWebSocketServer.groupByStates == null) {
            StockTickerGroupByWebSocketServer.groupByStates = new HashMap<>();
        }
        this.groupByDataService = groupByDataService;
        this.gson = new Gson();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        if (!StockTickerGroupByWebSocketServer.groupByStates.containsKey(session.getId())) {
            GridGroupByState gbs = StockTickerGroupByWebSocketServer.groupByStates.get(session.getId());
            StockTickerGroupByWebSocketServer.groupByStates.remove(session.getId());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        if (!StockTickerGroupByWebSocketServer.groupByStates.containsKey(session.getId())) {
            GridGroupByState gps = new GridGroupByState(session);
            StockTickerGroupByWebSocketServer.groupByStates.put(session.getId(), gps);
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
        List<IRow> rows = this.groupByDataService.getDataRows(rbr);
        DataResponseDataSetBlock<Map<String,Object>> drdsb = new DataResponseDataSetBlock<>(rbr.getStartRow(), rows.size());
        for(int i = rbr.getStartRow(); i < rbr.getEndRow() && i < rows.size(); i++) {
            drdsb.addElement(rows.get(i).getKvps());
        }
        String msg = this.gson.toJson(drdsb);
        session.sendMessage(new TextMessage(msg));
    }
    
}