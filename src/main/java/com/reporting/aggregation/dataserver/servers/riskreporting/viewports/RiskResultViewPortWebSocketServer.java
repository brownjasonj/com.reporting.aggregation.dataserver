package com.reporting.aggregation.dataserver.servers.riskreporting.viewports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.reporting.aggregation.dataserver.kafka.CalculationContextKafkaConsumer;
import com.reporting.aggregation.dataserver.kafka.ICalculationContextListener;
import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches.IDataCacheListener;
import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches.PricingGroupDataCache;
import com.reporting.aggregation.dataserver.servers.riskreporting.ignite.datacaches.RiskResultDataCache;
import com.reporting.aggregation.dataserver.servers.viewport.DataResponseCalculationContextChangeEvent;
import com.reporting.aggregation.dataserver.servers.viewport.DataResponseDataSet;
import com.reporting.aggregation.dataserver.servers.viewport.DataResponseDataSetSize;
import com.reporting.aggregation.dataserver.servers.viewport.DataServerRequest;
import com.reporting.aggregation.dataserver.servers.viewport.DataServerRequestConnectionParameters;
import com.reporting.aggregation.dataserver.servers.viewport.DataServerRequestWindowChange;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.risks.Risk;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class RiskResultViewPortWebSocketServer extends AbstractWebSocketHandler implements IDataCacheListener<UUID, RiskResult<? extends Risk>>, ICalculationContextListener {
    private final static Logger LOGGER = Logger.getLogger(RiskResultViewPortWebSocketServer.class.getName());
    private CalculationContextKafkaConsumer calculationContextKafkaConsumer;
    private Gson gson;
    private HashMap<String, PricingGroupDataCache> pricingGroupCaches;
    private HashMap<String, RiskResultViewPortState> sessionRiskResultCaches;
    private HashMap<String, List<WebSocketSession>> pricingGroupSessionList;


    public RiskResultViewPortWebSocketServer(CalculationContextKafkaConsumer calculationContextKafkaConsumer) {
        this.gson = new Gson();
        this.pricingGroupCaches = new HashMap<>();
        this.sessionRiskResultCaches = new HashMap<>();
        this.calculationContextKafkaConsumer = calculationContextKafkaConsumer;
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
                    session.sendMessage(
                            new TextMessage(gson.toJson(new DataResponseDataSetSize(rrdc.getData().size()))));
                }
            }
        }
            break;
        case WindowChangeSize: {
            DataServerRequestWindowChange dsrwc = gson.fromJson(message.getPayload(),
                    DataServerRequestWindowChange.class);
            RiskResultViewPortState pgdc = this.getViewPortStateBySession(session);
            if (pgdc != null) {
                HashSet<String> cacheNames = pgdc.getPricingGroupDataCache().getValue(pgdc.getMarketIdName());
                if (cacheNames.size() > 0) {
                    List<String> caches = new ArrayList<String>(cacheNames);
                    RiskResultDataCache rrdc = new RiskResultDataCache(caches.get(0));
                    DataResponseDataSet<RiskResult<? extends Risk>> rsrds = new DataResponseDataSet<>();
                    int rowsInResponse = dsrwc.firstRow;
                    for (RiskResult<? extends Risk> rr : rrdc.getData()) {
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
            RiskResultViewPortState rrvps = this.getOrCreateViewPortState(session, dsrcp);
            this.sessionRiskResultCaches.put(session.getId(), rrvps);
            break;
        }
    }

    private RiskResultViewPortState getOrCreateViewPortState(WebSocketSession session, DataServerRequestConnectionParameters dsrcp) {
        RiskResultViewPortState returnResult;
        PricingGroupDataCache pgdc;

        if (this.sessionRiskResultCaches.containsKey(session.getId())) {
            returnResult = this.sessionRiskResultCaches.get(session.getId());
        }
        else {
            if (this.pricingGroupCaches.containsKey(dsrcp.getPricingGroupName())) {
                pgdc = this.pricingGroupCaches.get(dsrcp.getPricingGroupName());
            } else {
                // need to create a ner pricing group data cache
                pgdc = new PricingGroupDataCache(dsrcp.getPricingGroupName());
                this.pricingGroupCaches.put(dsrcp.getPricingGroupName(), pgdc);

                // given this is a new pricing group, set up a calculation context listener for the pricing group
                this.calculationContextKafkaConsumer.addListener(dsrcp.getPricingGroupName(), this);
            }
            returnResult = new RiskResultViewPortState(session, dsrcp.getPricingGroupName(), dsrcp.getMarketIdName(), pgdc);
        }
        return returnResult;
    }

    private RiskResultViewPortState getViewPortStateBySession(WebSocketSession session) {
        RiskResultViewPortState returnResult = null;
        if (this.sessionRiskResultCaches.containsKey(session.getId())) {
            returnResult = this.sessionRiskResultCaches.get(session.getId());
        }
        return returnResult;
    }

    @Override
    public void update(RiskResult<? extends Risk> updatedValue) {

    }

    @Override
    public void update(CalculationContext calculationContext) {
        // each time the calculation context changes for a pricing group, find all the sessions associated with that 
        // pricing group and then send them a calculationcontext change event
        if (this.pricingGroupSessionList.containsKey(calculationContext.getPricingGroup().getName())) {
            for(WebSocketSession session: this.pricingGroupSessionList.get(calculationContext.getPricingGroup().getName())) {
                try {
                    session.sendMessage(new TextMessage(gson.toJson(new DataResponseCalculationContextChangeEvent(calculationContext))));
                }
                catch (IOException ioe) {
                    LOGGER.severe(ioe.toString());
                }
            }
        }
    }
}