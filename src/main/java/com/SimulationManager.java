package com;


import java.sql.SQLException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimulationManager {

    private HashMap<UUID, StockManager> simulations = new HashMap<>();
    private static Logger logger = LogManager.getLogger(SimulationManager.class);


    public UUID newSimulation(int number) {
        // Neue Simulation erstellen
        try {
            UUID newSimId = UUID.randomUUID();
            //DBManager dbManager = new DBManager(Config.getStocksDbPath());
            DBManager dbManager = new DBManager("stocks.db");

            DBManagerNews dbManagerNews = new DBManagerNews(Config.getNewsDbPath());
            StockManager stockManager = new StockManager(dbManager, number, dbManagerNews);

            // Initialisierung
            try {
                stockManager.init();
            } catch (SQLException e) {
                logger.error("Fehler beim Initialisieren der Simulation: " + e.getMessage());
                throw new RuntimeException("Simulation konnte nicht erstellt werden", e);
            }

            // Speichern
            simulations.put(newSimId, stockManager);

            return newSimId;
        }
        catch (Exception e) {
            logger.error("Fehler beim Erstellen der Simulation: " + e.getMessage());
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
           StockManager sim = simulations.get(simulationId);
            sim.getDb().close();
            sim.getDbNews().close();
            simulations.remove(simulationId);

        } else {
            logger.error("Simulation " + simulationId + " nicht gefunden");
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
                logger.error("Fehler beim Aktualisieren der Simulation " + simId + ": " + e.getMessage());
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

