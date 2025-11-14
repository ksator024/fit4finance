package com.stocksim.stocksim;

public class Bot implements Trader {

    private int id;
    private String name;

    public Bot(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
