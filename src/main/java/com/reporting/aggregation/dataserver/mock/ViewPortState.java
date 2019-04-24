package com.reporting.aggregation.dataserver.mock;

import com.google.gson.Gson;
import com.reporting.aggregation.dataserver.servers.viewport.DataResponseColumnUpdate;
import com.reporting.aggregation.dataserver.servers.viewport.IDataChange;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class ViewPortState implements IDataChange {
    WebSocketSession session;
    private int firstRow;
    private int lastRow;
    private Gson gson;

    public ViewPortState(WebSocketSession session) {
        this.firstRow = 0;
        this.lastRow = 0;
        this.session = session;
        this.gson = new Gson();
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public Boolean isVisible(int row) {
        return (row >= this.firstRow) && (this.lastRow >= row);
    }

    @Override
    public void change(DataResponseColumnUpdate drcu) throws IOException {
        if (isVisible(drcu.getRowIndex())) {
            if (session.isOpen())
                session.sendMessage(new TextMessage(gson.toJson(drcu)));
        }
    }
}
