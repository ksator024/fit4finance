package com.stocksim.stocksim;

import java.util.ArrayList;
import java.util.Arrays;

public  class ScenarioManager {
   static ArrayList<Scenario> scenarios = new ArrayList<>();

   static Scenario getScenario(int number){

   scenarios.add(new Scenario(915200940, 1034170140+86400 , new ArrayList<>(Arrays.asList("CSCO", "INTC","AMZN","MSFT")), 300.0));
   scenarios.add(new Scenario(1577888940, 1640960940+86400 , new ArrayList<>(Arrays.asList("DIS", "XOM","JPM","BAC","AAPL","MSFT","NVDA")), 300.0));
   scenarios.add(new Scenario(1167661740, 1236608940+86400 , new ArrayList<>(Arrays.asList("BAC", "JPM","AAPL","MSFT","JNJ","PG","UNH")), 300.0));
   System.out.println("SCNEARIO NUMBER: " + number);
   return scenarios.get(number-1);
   }



}
