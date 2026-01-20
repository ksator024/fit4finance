package com.stocksim.stocksim;


import com.stocksim.stocksim.DTOs.UpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RestController
public class TraderController
{
    @Autowired
    private SimulationManager simulationManager;

    /**
     * Erstellt eine neue Simulation
     */
    @PostMapping("/simulations")
    public ResponseEntity<HashMap<String, String>> createSimulation(@RequestParam String number) {
        UUID simulationId = simulationManager.newSimulation(Integer.parseInt(number));

        HashMap<String, String> response = new HashMap<>();
        response.put("simulationId", simulationId.toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Löscht eine Simulation
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteSimulation(
            @PathVariable String id) {

        UUID simulationId = null;

        try {
            simulationId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            System.err.println("[ERROR] Ungültige Simulation ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Simulation nicht gefunden");
        }

        if (simulationManager.getSimulation(simulationId) == null) {
            System.err.println("[ERROR] Simulation ID '" + id + "' nicht gefunden");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Simulation nicht gefunden");
        }

        simulationManager.deleteSimulation(simulationId);
        return ResponseEntity.ok("Simulation gelöscht");
    }

    @PostMapping("/{id}/buy")
    public ResponseEntity<String> buy(@PathVariable String id, @RequestBody BuyOrder buyOrder) {
        UUID simulationId = parseSimulationId(id);
        if (simulationId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Simulation nicht gefunden");
        }

        StockManager stockManager = simulationManager.getSimulation(simulationId);
        if (stockManager == null) {
            System.err.println("[ERROR] Simulation ID '" + id + "' nicht gefunden");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Simulation nicht gefunden");
        }

        stockManager.buy(buyOrder);
        return ResponseEntity.ok("Buy Order verarbeitet");
    }

    @GetMapping("/{id}/update")
    public ResponseEntity<?> update(@PathVariable String id) {
        UUID uuid = parseSimulationId(id);

        StockManager stockManager = simulationManager.getSimulation(uuid);

        if (stockManager == null) {
            System.err.println("[ERROR] Simulation ID '" + id + "' nicht gefunden");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Simulation nicht gefunden");
        }

        return ResponseEntity.ok(stockManager.getUpdateDTO());
    }

    @PostMapping("/{id}/pause")
    public void pause(@PathVariable String id, @RequestParam String action){
        UUID simulationId = parseSimulationId(id);
        simulationManager.setPause(simulationId, action);

    }


    @PostMapping("/{id}/sell")
    public ResponseEntity<String> sell(@PathVariable String id, @RequestBody SellOrder sellOrder) {
        UUID simulationId = parseSimulationId(id);
        if (simulationId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Simulation nicht gefunden");
        }

        StockManager stockManager = simulationManager.getSimulation(simulationId);
        if (stockManager == null) {
            System.err.println("[ERROR] Simulation ID '" + id + "' nicht gefunden");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Simulation nicht gefunden");
        }

        stockManager.sell(sellOrder);
        return ResponseEntity.ok("Sell Order verarbeitet");
    }

    @PostMapping("/{id}/cancel")
    public void cancelSimulation(@PathVariable String id, @RequestBody HashMap<String, Integer> request) {
        Integer bodyId = request.get("id");
        System.out.println("Cancel Simulation called with path id: " + id + " and body id: " + bodyId);
        UUID simulationId = parseSimulationId(id);
        simulationManager.cancelOrder(simulationId,(int) bodyId);
        // TODO: use id (path) and bodyId (payload) to cancel the simulation
    }

    /**
     * Hilfsmethode zum Parsen der Simulation-ID
     */
    private UUID parseSimulationId(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            System.err.println("[ERROR] Ungültige Simulation ID: " + id);
            return null;
        }
    }


}
