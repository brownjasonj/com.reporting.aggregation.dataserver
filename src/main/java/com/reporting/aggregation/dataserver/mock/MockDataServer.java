package com.reporting.aggregation.dataserver.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import com.reporting.aggregation.dataserver.model.StockSymbol;
import com.reporting.aggregation.dataserver.servers.viewport.DataResponseColumnUpdate;
import com.reporting.aggregation.dataserver.servers.viewport.IDataChange;

import org.springframework.stereotype.Component;

@Component
public class MockDataServer {
    private static CopyOnWriteArrayList<StockSymbol> stockSymbols; // {new StockSymbol("fred", "fred")};
    private static CopyOnWriteArrayList<IDataChange> listners;
    private static Timer stockTickTimer;
    private static TimerTask stockTickTimerTask;
    private static LinkedBlockingQueue<DataResponseColumnUpdate> tickerQueue;
    private static Thread thread;



    public MockDataServer() {
        if (MockDataServer.listners == null)
            MockDataServer.listners = new CopyOnWriteArrayList<>();
        if (MockDataServer.stockTickTimer == null) {
            MockDataServer.stockTickTimer = new Timer();
        }
        if (MockDataServer.tickerQueue == null)
            MockDataServer.tickerQueue = new LinkedBlockingQueue<>();
        if (MockDataServer.thread == null) {
            MockDataServer.thread = new Thread() {
                public void run() {
                    System.out.println("Stock Ticker Dispatcher Thread Started");
                    while (true) {
                        try {
                            DataResponseColumnUpdate update = MockDataServer.tickerQueue.take();
                            System.out.println("Processing Tick " + update.toString());
                            for (IDataChange listner : MockDataServer.listners) {
                                listner.change(update);
                            }
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };
            MockDataServer.thread.start();
        }


        if (MockDataServer.stockSymbols == null) {
            MockDataServer.stockSymbols = new CopyOnWriteArrayList<>();

            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD1.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD2.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD3.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD4.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD5.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD6.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD7.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD8.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD9.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD10.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD11.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD12.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD13.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD14.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD15.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD16.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD17.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD18.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Food", "FRD19.L", "Fred"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM2.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM3.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM4.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM5.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM6.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM7.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM8.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM9.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM10.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM11.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM12.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM13.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM14.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM15.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM16.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM17.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM18.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM19.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM20.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM21.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM21.L", "Jim"));
            addStockSymbol(new StockSymbol("FTSE", "Technology", "JIM23.L", "Jim"));
        }
    }

    private void addStockSymbol(StockSymbol ss) {
        MockDataServer.stockSymbols.add(ss);
    }

    
    public void startTicking(long period) {
        if (MockDataServer.stockTickTimerTask == null) {
            MockDataServer.stockTickTimerTask = new TimerTask() {
                public void run() {
                    MockDataServer.tickerQueue.add(MockDataServer.makeSomeVolumeChanges());
                    MockDataServer.tickerQueue.addAll(MockDataServer.makeSomePriceChanges());
                }
            };
        }
        if (MockDataServer.stockTickTimer == null)
            MockDataServer.stockTickTimer = new Timer("StockSymbol Ticker");
        MockDataServer.stockTickTimer.schedule(MockDataServer.stockTickTimerTask, 0, period);
    }

    public void stopTicking() {
        MockDataServer.stockTickTimer.cancel();
        MockDataServer.stockTickTimerTask = null;
        MockDataServer.stockTickTimer = null;
    }


    public List<StockSymbol> getAllData() {
        return MockDataServer.stockSymbols.stream().collect(Collectors.toList());
    }

    public Boolean registerListener(IDataChange idc) {
        return MockDataServer.listners.add(idc);
    }

    public Boolean deregisterListner(IDataChange idc) {
        return MockDataServer.listners.remove(idc);
    }

    /*
        when called picks a stock symbol at random and makes a random up/down move of +/- 5 of the volume.
    */
    public static DataResponseColumnUpdate makeSomeVolumeChanges() {
        Random r = new Random();
        int index = r.nextInt(MockDataServer.stockSymbols.size());
        StockSymbol ss = MockDataServer.stockSymbols.get(index);
        // change by a value between -5 and 5
        int move =  r.nextInt((5 - (-5)) + 1) - 5;
        ss.setVolume(ss.getVolume() + move);

        return new DataResponseColumnUpdate(index, "volume", ss.getVolume());
    }

    /*
        when called picks a stock symbol at ramdom and randomly moves the mid/bid/ask.
    */
    public static List<DataResponseColumnUpdate> makeSomePriceChanges() {
        List<DataResponseColumnUpdate> response = new ArrayList<>();
        Random r = new Random();
        int index = r.nextInt(MockDataServer.stockSymbols.size());
        StockSymbol ss = MockDataServer.stockSymbols.get(index);
        double move = (Math.floor(30 * Math.random()))/10 - 1;
        ss.setMid(ss.getMid() + move);
        setBidAndAsk(ss);

        response.add(new DataResponseColumnUpdate(index, "mid", ss.getMid()));
        response.add(new DataResponseColumnUpdate(index, "ask", ss.getAsk()));
        response.add(new DataResponseColumnUpdate(index, "bid", ss.getBid()));
        return response;
    }

    public static void setBidAndAsk(StockSymbol ss) {
        ss.setAsk(ss.getMid() * 0.98);
        ss.setBid(ss.getMid() * 1.92);
    }
}
