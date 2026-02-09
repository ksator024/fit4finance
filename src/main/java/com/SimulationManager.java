package com;


import java.sql.SQLException;
import java.util.*;

public class SimulationManager {

    private HashMap<UUID, StockManager> simulations = new HashMap<>();


    public UUID newSimulation(int number) {
        // Neue Simulation erstellen
        System.out.println("starte mit erstellen");
        try {
            UUID newSimId = UUID.randomUUID();
            System.out.println("generiere DBManage....");
            //DBManager dbManager = new DBManager(Config.getStocksDbPath());
            DBManager dbManager = new DBManager("stocks.db");

            System.out.println("generiere DBManageNews....");
            DBManagerNews dbManagerNews = new DBManagerNews(Config.getNewsDbPath());
            System.out.println("generiere stockmanager....");
            StockManager stockManager = new StockManager(dbManager, number, dbManagerNews);

            // Initialisierung
            try {
                System.out.println("initialisiere stockmanager....");
                stockManager.init();
            } catch (SQLException e) {
                System.err.println("[ERROR] Fehler beim Initialisieren der Simulation: " + e.getMessage());
                throw new RuntimeException("Simulation konnte nicht erstellt werden", e);
            }

            // Speichern
            System.out.println("speichere stockmanager....");
            simulations.put(newSimId, stockManager);

            System.out.println("[INFO] Neue Simulation erstellt: " + newSimId);
            return newSimId;
        }
        catch (Exception e) {
            System.err.println("[ERROR] Fehler beim Erstellen der Simulation: " + e.getMessage());
        }
        return null;
    }

    /**
     * Ruft einen StockManager über die Simulation-ID ab
     *
     * @param simulationId UUID der Simulation
     * @return StockManager oder null
     */
    public StockManager getSimulation(UUID simulationId) {
        return simulations.get(simulationId);
    }

    /**
     * Löscht eine Simulation
     *
     * @param simulationId UUID der Simulation
     */
    public void deleteSimulation(UUID simulationId) {
        if (simulations.containsKey(simulationId)) {
            simulations.remove(simulationId);
            System.out.println("[INFO] Simulation " + simulationId + " gelöscht");
        } else {
            System.err.println("[ERROR] Simulation " + simulationId + " nicht gefunden");
        }
    }

    public void update(){
        ArrayList<UUID> finishedSimulations = new ArrayList<>();
        for (Map.Entry<UUID, StockManager> entry : simulations.entrySet()) {
            UUID simId = entry.getKey();
            StockManager stockManager = entry.getValue();
            try {
                if(stockManager.getSimulationStatus() == SimulationStatus.RUNNING) {
                    stockManager.update();
                }
                else if(stockManager.getSimulationStatus() == SimulationStatus.FINISHED) {
                    if (stockManager.getFinishTime() + 5000 <= System.currentTimeMillis()) {
                        finishedSimulations.add(simId);
                    }

                }
            } catch (SQLException e) {
                System.err.println("[ERROR] Fehler beim Aktualisieren der Simulation " + simId + ": " + e.getMessage());
            }
        }
        for(UUID simId : finishedSimulations) {
            deleteSimulation(simId);
        }
    }
    public  void cancelOrder(UUID id, int orderId) {
        simulations.get(id).cancelOrder(orderId);
    }


    public void setPause(UUID id, SimulationStatus pause) {
        simulations.get(id).setSimulationStatus(pause);
    }
}

