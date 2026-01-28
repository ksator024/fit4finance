package com;

import io.javalin.Javalin;

import java.util.HashMap;
import java.util.UUID;

public class Main {

    static SimulationManager simManager = new SimulationManager();
    public static void main(String[] args) {

        System.out.println("Hello World!");
        Javalin app = Javalin.create().start(8080);
        app.post("/simulations", ctx -> {
            System.out.println("test");
            UUID simulationId = simManager.newSimulation(Integer.parseInt(ctx.queryParam("number")));

            HashMap<String, String> response = new HashMap<>();
            response.put("simulationId", simulationId.toString());

            ctx.json(response);
        });



    }



    private UUID parseSimulationId(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            System.err.println("[ERROR] Ungültige Simulation ID: " + id);
            return null;
        }
    }
}
