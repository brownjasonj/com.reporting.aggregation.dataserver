package com.reporting.aggregation.dataserver.servers.groupby;

import java.io.IOException;
import java.util.LinkedHashSet;

import com.google.gson.Gson;
import com.reporting.aggregation.dataserver.servers.groupby.requests.GroupByRequest;
import com.reporting.aggregation.dataserver.servers.viewport.DataResponseColumnUpdate;
import com.reporting.aggregation.dataserver.servers.viewport.IDataChange;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class GridGroupByState implements IDataChange {
    private WebSocketSession session;
    private Gson gson;
    private LinkedHashSet<String> groups;

    public GridGroupByState(WebSocketSession session) {
        this.groups = new LinkedHashSet<>();
        this.session = session;
        this.gson = new Gson();
    }

    public void addRequest(GroupByRequest gbr) {
        groups.add(gbr.getGroup());
    }

    @Override
    public void change(DataResponseColumnUpdate drcu) throws IOException {
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(gson.toJson(drcu)));
        }
    }
}