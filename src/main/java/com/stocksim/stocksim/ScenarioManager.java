package com.stocksim.stocksim;

import java.util.ArrayList;
import java.util.Arrays;

public  class ScenarioManager {
   static ArrayList<Scenario> scenarios = new ArrayList<>();

   static Scenario getScenario(int number){

   scenarios.add(new Scenario(1262356200, 1625097600, new ArrayList<>(Arrays.asList("AAPL", "GOOGL")), 100000.0));
   scenarios.add(new Scenario(1262356200, 1625097600, new ArrayList<>(Arrays.asList("AAPL", "GOOGL")), 2));
   scenarios.add(new Scenario(1262356200, 1625097600, new ArrayList<>(Arrays.asList("AAPL", "GOOGL")), 3));
   System.out.println("SCNEARIO NUMBER: " + number);
   return scenarios.get(number-1);
   }



}
