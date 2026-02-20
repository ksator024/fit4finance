package com;


import java.util.ArrayList;
import java.util.HashMap;

public class OrderBook {


    private ArrayList<BuyOrder> buyOrders = new ArrayList<BuyOrder>();
    private ArrayList<SellOrder> sellOrders = new ArrayList<SellOrder>();
    private HashMap<String,Integer> quantities = new HashMap<>();
    private HashMap<String,Double> prices = new HashMap<>();

    private double capital;


    public void setOrder(Order order){

        if(order.getClass().equals(BuyOrder.class)){
            buyOrders.add((BuyOrder)order);
        }
        else{
            sellOrders.add((SellOrder)order);
        }
    }

   public HashMap<String,Integer> getQuantities() {
        return quantities;
    }





    public OrderBook(ArrayList<String> stockNames){

        for (String s : stockNames) {
            quantities.put(s, 0); // Jeder Key bekommt den Wert 0
            prices.put(s, 0.);
        }

    }




    public void update(){
        ArrayList<Order> temp = new ArrayList<Order>();
                for(BuyOrder buyOrder : buyOrders) {
                    String name = buyOrder.getName();
                    double currentPrice = prices.get(name);
                    if(buyOrder.getPrice() == -1.0) {
                        buyOrder.setPrice(currentPrice);
                    }
                    if(currentPrice <= buyOrder.getPrice() && capital >= buyOrder.getPrice() * buyOrder.getQuantity()) {
                        quantities.put(name,quantities.get(name) +buyOrder.getQuantity());
                        capital -= currentPrice * buyOrder.getQuantity();
                        temp.add(buyOrder);
                    }
                }
                for(SellOrder sellOrder : sellOrders) {

                    if(sellOrder.getPrice() == -1.0) {
                        sellOrder.setPrice(prices.get(sellOrder.getName()));
                    }


                    String name = sellOrder.getName();
                    double currentPrice = prices.get(name);
                    if (currentPrice >= sellOrder.getPrice() && quantities.get(name) >= sellOrder.getQuantity()) {
                        quantities.put(name,quantities.get(name) -sellOrder.getQuantity());
                        capital += currentPrice * sellOrder.getQuantity();
                        temp.add(sellOrder);
                    }
                }
        for(Order order : temp) {
            if(order instanceof BuyOrder) {
                buyOrders.remove(order);
            }
            else if(order instanceof SellOrder) {
                sellOrders.remove(order);
            }
        }
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public void setCurrentPrice(double currentPrice, String symbol) {
        prices.put(symbol,currentPrice);
    }

    public double getCapital() {
        return capital;
    }

    public ArrayList<BuyOrder> getBuyOrders() {
        return buyOrders;
    }

    public ArrayList<SellOrder> getSellOrders() {
        return sellOrders;
    }
    public HashMap<String,Double> getCurrentPrice() {
        return prices;
    }

    public void cancelOrder(int id) {
        buyOrders.removeIf(order -> order.getId() == id);
        sellOrders.removeIf(order -> order.getId() == id);
    }
}
