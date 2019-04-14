package com.reporting.aggregation.dataserver.controllers;

import com.reporting.aggregation.dataserver.datamodel.StockSymbol;
import com.reporting.aggregation.dataserver.mock.MockDataServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StockSymbolController {
    @Autowired
    MockDataServer mockDataServer;

    @Autowired
    StockSymbolController(MockDataServer mockDataServer) {
        this.mockDataServer = mockDataServer;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "stocksymbols", produces = "application/json; charset=UTF-8")
    public List<StockSymbol> getAllStockSymbols() {
        return this.mockDataServer.getAllData();
    }

    @GetMapping("/start/{period}")
    public Boolean startTicker(@PathVariable int period){
        this.mockDataServer.startTicking(period);
        return true;
    }

    @GetMapping("/stop")
    public Boolean stopTicker() {
        this.mockDataServer.stopTicking();
        return true;
    }
}
