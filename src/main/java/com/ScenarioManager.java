package com;

import java.util.ArrayList;
import java.util.Arrays;

public  class ScenarioManager {
   static ArrayList<Scenario> scenarios = new ArrayList<>();

   static Scenario getScenario(int number){
   //scenarios.add(new Scenario(915200940, 1031750940 , new ArrayList<>(Arrays.asList("CSCO", "INTC","AMZN")), 100000.0));

   scenarios.add(new Scenario(915200940, 915200940+864000 , new ArrayList<>(Arrays.asList("CSCO", "INTC","AMZN")), 100000.0));
   scenarios.add(new Scenario(1577888940, 1640960940+86400 , new ArrayList<>(Arrays.asList("DIS", "XOM","NVDA")), 3));
   scenarios.add(new Scenario(1167661740, 1236608940+86400 , new ArrayList<>(Arrays.asList("BAC", "JPM","AAPL")), 2));
   System.out.println("SCNEARIO NUMBER: " + number);
   return scenarios.get(number-1);
   }



}
