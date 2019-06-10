package com.reporting.aggregation.dataserver.servers.riskreporting.viewports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches.PricingGroupDataCache;
import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches.RiskResultDataCache;
import com.reporting.aggregation.dataserver.servers.viewport.DataResponseDataSet;
import com.reporting.aggregation.dataserver.servers.viewport.DataResponseDataSetSize;
import com.reporting.aggregation.dataserver.servers.viewport.DataServerRequest;
import com.reporting.aggregation.dataserver.servers.viewport.DataServerRequestConnectionParameters;
import com.reporting.aggregation.dataserver.servers.viewport.DataServerRequestWindowChange;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.risks.Risk;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class RiskResultViewPortWebSocketServer extends AbstractWebSocketHandler {
    private Gson gson;
    private HashMap<String, RiskResultViewPortState> pricingGroupCaches;

    public RiskResultViewPortWebSocketServer() {
        this.gson = new Gson();
        this.pricingGroupCaches = new HashMap<>();
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
        DataServerRequest dsr = gson.fromJson(message.getPayload(), DataServerRequest.class);
        switch (dsr.requestType) {
            case AllData: {
                    RiskResultViewPortState pgdc = this.getViewPortStateBySession(session);
                    if (pgdc != null) {
                        HashSet<String> cacheNames = pgdc.getPricingGroupDataCache().getValue(pgdc.getMarketIdName());
                        if (cacheNames.size() > 0) {
                            List<String> caches = new ArrayList<String>(cacheNames);
                            RiskResultDataCache rrdc = new RiskResultDataCache(caches.get(0));
                            session.sendMessage(new TextMessage(gson.toJson(new DataResponseDataSetSize(rrdc.getData().size()))));
                        }
                    }
                }
                break;
            case WindowChangeSize: {
                    DataServerRequestWindowChange dsrwc = gson.fromJson(message.getPayload(), DataServerRequestWindowChange.class);
                    RiskResultViewPortState pgdc = this.getViewPortStateBySession(session);
                    if (pgdc != null) {
                        HashSet<String> cacheNames = pgdc.getPricingGroupDataCache().getValue(pgdc.getMarketIdName());
                        if (cacheNames.size() > 0) {
                            List<String> caches = new ArrayList<String>(cacheNames);
                            RiskResultDataCache rrdc = new RiskResultDataCache(caches.get(0));
                            DataResponseDataSet<RiskResult<? extends Risk>> rsrds = new DataResponseDataSet<>();
                            int rowsInResponse = dsrwc.firstRow;
                            for(RiskResult<? extends Risk> rr: rrdc.getData()) {
                                rsrds.addElement(rr);
                                rowsInResponse++;
                            }
                            pgdc.setFirstRow(dsrwc.firstRow);
                            pgdc.setLastRow(rowsInResponse);
                            session.sendMessage(new TextMessage(gson.toJson(rsrds)));
                        }
                    }
                }
                break;
            case ConnectionParameters:
                DataServerRequestConnectionParameters dsrcp = gson.fromJson(message.getPayload(), DataServerRequestConnectionParameters.class);
                this.getOrCreateViewPortState(session, dsrcp);
                break;
        }
    }

    private RiskResultViewPortState getOrCreateViewPortState(WebSocketSession session, DataServerRequestConnectionParameters dsrcp) {
        RiskResultViewPortState returnResult;
        if (this.pricingGroupCaches.containsKey(session.getId())) {
            returnResult = this.pricingGroupCaches.get(session.getId());
        }
        else {
            PricingGroupDataCache pgdc = new PricingGroupDataCache(dsrcp.getPricingGroupName());
            returnResult = new RiskResultViewPortState(session, dsrcp.getPricingGroupName(), dsrcp.getMarketIdName(), pgdc);
            this.pricingGroupCaches.put(session.getId(), returnResult);
        }
        return returnResult;
    }

    private RiskResultViewPortState getViewPortStateBySession(WebSocketSession session) {
        RiskResultViewPortState returnResult = null;
        if (this.pricingGroupCaches.containsKey(session.getId())) {
            returnResult = this.pricingGroupCaches.get(session.getId());
        }
        return returnResult;
    }
}