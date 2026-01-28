package com;

import java.util.ArrayList;

public class Scenario {

    private long startTime;
    private long endTime;
    private ArrayList<String> stockName = new ArrayList<>();
    private double capital;

    public Scenario(long startTime, long endTime, ArrayList<String> stockName, double capital) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.stockName = stockName;
        this.capital = capital;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public ArrayList<String> getStockName() {
        return stockName;
    }

    public double getCapital() {
        return capital;
    }
}
