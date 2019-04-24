package com.reporting.aggregation.dataserver.model;

public class StockSymbol {
    private String exchange;
    private String sector;
    private String code;
    private String name;
    private int volume;
    private Double mid;
    private Double bid;
    private Double ask;

    public StockSymbol(String exchange, String sector, String code, String name) {
        this.exchange = exchange;
        this.sector = sector;
        this.code = code;
        this.name = name;
        this.volume = 0;
        this.mid = 0.0;
        this.bid = 0.0;
        this.ask = 0.0;
    }

    public String getExchange() {
        return exchange;
    }

    public String getSector() {
        return sector;
    }
    
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Double getMid() {
        return mid;
    }

    public void setMid(Double mid) {
        this.mid = mid;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Double getAsk() {
        return ask;
    }

    public void setAsk(Double ask) {
        this.ask = ask;
    }
}
