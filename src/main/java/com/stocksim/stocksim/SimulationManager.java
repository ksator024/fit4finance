package com.stocksim.stocksim;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
public class SimulationManager {

    private HashMap<UUID, StockManager> simulations = new HashMap<>();


    public UUID newSimulation(int number) {
        // Neue Simulation erstellen
        UUID newSimId = UUID.randomUUID();
        DBManager dbManager = new DBManager("testDB.db");
        DBManagerNews dbManagerNews = new DBManagerNews("news.db");
        StockManager stockManager = new StockManager(dbManager,number,dbManagerNews);

        // Initialisierung
        try {
            stockManager.init();
        } catch (SQLException e) {
            System.err.println("[ERROR] Fehler beim Initialisieren der Simulation: " + e.getMessage());
            throw new RuntimeException("Simulation konnte nicht erstellt werden", e);
        }

        // Speichern
        simulations.put(newSimId, stockManager);

        System.out.println("[INFO] Neue Simulation erstellt: " + newSimId);
        return newSimId;
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

    @Scheduled(fixedRate = 1000)
    public void update(){
        for (Map.Entry<UUID, StockManager> entry : simulations.entrySet()) {
            UUID simId = entry.getKey();
            StockManager stockManager = entry.getValue();
            try {
                if(!stockManager.isPaused()) {
                    stockManager.update();
                }
            } catch (SQLException e) {
                System.err.println("[ERROR] Fehler beim Aktualisieren der Simulation " + simId + ": " + e.getMessage());
            }
        }
    }
    public  void cancelOrder(UUID id, int orderId) {
        simulations.get(id).cancelOrder(orderId);
    }


    public void setPause(UUID id, String pause) {
        simulations.get(id).setPaused(pause);
    }
}

