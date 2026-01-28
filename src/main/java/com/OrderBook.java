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

        if(order.getPrice()== -1.0){
            order.setPrice(prices.get(order.getName()));
        }

        if(order.getClass().equals(BuyOrder.class)){


            buyOrders.add((BuyOrder)order);
            System.out.println("🛒 BUY ORDER GESETZT - Stock: " + order.getName() +
                             ", Menge: " + order.getQuantity() +
                             ", Preis: " + order.getPrice() + "€");
        }
        else{
            sellOrders.add((SellOrder)order);
            System.out.println("📤 SELL ORDER GESETZT - Stock: " + order.getName() +
                             ", Menge: " + order.getQuantity() +
                             ", Preis: " + order.getPrice() + "€");
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
                    if(currentPrice <= buyOrder.getPrice()) {
                        quantities.put(name,quantities.get(name) +buyOrder.getQuantity());
                        capital -= buyOrder.getPrice() * buyOrder.getQuantity();
                        temp.add(buyOrder);
                        System.out.println("✅ BUY ORDER AUSGEFÜHRT - Stock: " + name +
                                         ", Menge: " + buyOrder.getQuantity() +
                                         ", Preis: " + buyOrder.getPrice() + "€" +
                                         ", Kosten: " + (buyOrder.getPrice() * buyOrder.getQuantity()) + "€" +
                                         ", Verbleibender Kapital: " + capital + "€");
                    }
                }
                for(SellOrder sellOrder : sellOrders) {
                    String name = sellOrder.getName();
                    double currentPrice = prices.get(name);
                    if (currentPrice >= sellOrder.getPrice()) {
                        quantities.put(name,quantities.get(name) -sellOrder.getQuantity());
                        capital += sellOrder.getPrice() * sellOrder.getQuantity();
                        temp.add(sellOrder);
                        System.out.println("✅ SELL ORDER AUSGEFÜHRT - Stock: " + name +
                                         ", Menge: " + sellOrder.getQuantity() +
                                         ", Preis: " + sellOrder.getPrice() + "€" +
                                         ", Wert: " + (sellOrder.getPrice() * sellOrder.getQuantity()) + "€" +
                                         ", Verbleibender Kapital: " + capital + "€");
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
