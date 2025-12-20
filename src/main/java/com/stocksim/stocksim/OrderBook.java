package com.stocksim.stocksim;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class OrderBook {


    private ArrayList<BuyOrder> buyOrders = new ArrayList<BuyOrder>();
    private ArrayList<SellOrder> sellOrders = new ArrayList<SellOrder>();
    private HashMap<String,Integer> quantities = new HashMap<>();
    private double currentPrice = 1;
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
        }

    }




    public void update(){
        ArrayList<Order> temp = new ArrayList<Order>();
       // System.out.println(capital);
        System.out.println(currentPrice);

                for(BuyOrder buyOrder : buyOrders) {
                    String name = buyOrder.getName();
                    if(currentPrice <= buyOrder.getPrice()) {
                        quantities.put(name,quantities.get(name) +buyOrder.getQuantity());
                        capital -= buyOrder.getPrice() * buyOrder.getQuantity();
                        temp.add(buyOrder);
                    }
                }
                for(SellOrder sellOrder : sellOrders) {
                    String name = sellOrder.getName();
                    if (currentPrice >= sellOrder.getPrice()) {
                        quantities.put(name,quantities.get(name) -sellOrder.getQuantity());
                        capital += sellOrder.getPrice() * sellOrder.getQuantity();
                        temp.add(sellOrder);
                        System.out.println("test");
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

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
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
    public double getCurrentPrice() {
        return currentPrice;
    }

}
