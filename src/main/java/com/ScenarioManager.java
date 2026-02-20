package com;

import java.util.ArrayList;
import java.util.Arrays;

public  class ScenarioManager {
   static ArrayList<Scenario> scenarios = new ArrayList<>();

   static Scenario getScenario(int number){
   scenarios.add(new Scenario(915200940, 1031750940 , new ArrayList<>(Arrays.asList("CSCO", "INTC","AMZN", "KO", "MSFT")), 1000));
   scenarios.add(new Scenario(1577888940, 1640960940+86400 , new ArrayList<>(Arrays.asList("DIS", "XOM","NVDA", "JPM", "BAC")), 1500));
   scenarios.add(new Scenario(1167661740, 1236608940+86400 , new ArrayList<>(Arrays.asList("BAC", "JPM","AAPL", "MSFT", "JNJ", "UNH")), 1000));
   return scenarios.get(number-1);
   }



}
