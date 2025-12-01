package com.stocksim.stocksim;


import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OrderBook {


    private ArrayList<BuyOrder> buyOrders = new ArrayList<BuyOrder>();
    private ArrayList<SellOrder> sellOrders = new ArrayList<SellOrder>();
    private int quantity;
    private double currentPrice = 175.5;
    private double capital;


    public void setOrder(Order order){

        if(order.getClass().equals(BuyOrder.class)){
            buyOrders.add((BuyOrder)order);
        }
        else{
            sellOrders.add((SellOrder)order);
        }
    }

   public int getQuantity() {
        return quantity;
    }


    OrderBook(){

    }

    public void update(){
        ArrayList<Order> temp = new ArrayList<Order>();
                for(BuyOrder buyOrder : buyOrders) {
                    if(currentPrice <= buyOrder.getPrice()) {
                        quantity += buyOrder.getQuantity();
                        capital -= buyOrder.getPrice() * buyOrder.getQuantity();
                        temp.add(buyOrder);
                    }
                }
                for(SellOrder sellOrder : sellOrders) {
                    if (currentPrice >= sellOrder.getPrice()) {
                        quantity -= sellOrder.getQuantity();
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

    public double getCapital() {
        return capital;
    }
}
