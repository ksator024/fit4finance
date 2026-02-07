package com;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    static SimulationManager simManager = new SimulationManager();

    public static void main(String[] args) {
        updater();
        System.out.println("Hello World!");
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
        }).start(8080);

        app.post("/simulations",    Main::handleCreateSimulation);
        app.get("/{id}/update",     Main::handleGetUpdate);
        app.post("/{id}/pause",     Main::handlePause);
        app.post("/{id}/buy",       Main::handleBuy);
        app.post("/{id}/sell",      Main::handleSell);
        app.post("/{id}/cancel",    Main::handleCancel);
        app.post("/{id}/delete",    Main::handleDelete);

    }


    private static void handleCreateSimulation(Context ctx) {
        try {
            UUID simulationId = simManager.newSimulation(Integer.parseInt(ctx.queryParam("number")));

            HashMap<String, String> response = new HashMap<>();
            response.put("simulationId", simulationId.toString());

            ctx.json(response);
        }
        catch (Exception e) {
            System.out.println("[ERROR] Fehler beim Erstellen der Simulation: " + e.getMessage());
        }
    }

    private static void handleGetUpdate(Context ctx) {
        UUID uuid = parseSimulationId(ctx.pathParam("id"));
        if (uuid == null) {
            ctx.status(400).result("Ungültige Simulation ID");
            return;
        }

        StockManager stockManager = simManager.getSimulation(uuid);
        if (stockManager == null) {
            System.err.println("[ERROR] Simulation ID '" + ctx.pathParam("id") + "' nicht gefunden");
            ctx.status(404).result("Simulation nicht gefunden");
            return;
        }

        ctx.json(stockManager.getUpdateDTO());
    }

    private static void handlePause(Context ctx) {
        UUID uuid = parseSimulationId(ctx.pathParam("id"));
        if (uuid == null) {
            ctx.status(400).result("Ungültige Simulation ID");
            return;
        }

        String action = ctx.queryParam("action");
        SimulationStatus simStat = SimulationStatus.RUNNING;
        if ("pause".equals(action)) {
            simStat = SimulationStatus.PAUSED;
        } else if ("resume".equals(action)) {
            simStat = SimulationStatus.RUNNING;
        }
        simManager.setPause(uuid, simStat);
    }

    private static void handleBuy(Context ctx) {
        UUID uuid = parseSimulationId(ctx.pathParam("id"));
        System.out.println("test");
        if (uuid == null) {
            ctx.status(400).result("Ungültige Simulation ID");
            return;
        }

        StockManager stockManager = simManager.getSimulation(uuid);
        if (stockManager == null) {
            System.err.println("[ERROR] Simulation ID '" + ctx.pathParam("id") + "' nicht gefunden");
            ctx.status(404).result("Simulation nicht gefunden");
            return;
        }
            BuyOrder buyOrder = ctx.bodyAsClass(BuyOrder.class);


        stockManager.buy(buyOrder);
        ctx.result("Buy Order verarbeitet");
    }

    private static void handleSell(Context ctx) {
        UUID uuid = parseSimulationId(ctx.pathParam("id"));
        if (uuid == null) {
            ctx.status(400).result("Ungültige Simulation ID");
            return;
        }

        StockManager stockManager = simManager.getSimulation(uuid);
        if (stockManager == null) {
            System.err.println("[ERROR] Simulation ID '" + ctx.pathParam("id") + "' nicht gefunden");
            ctx.status(404).result("Simulation nicht gefunden");
            return;
        }

        SellOrder sellOrder = ctx.bodyAsClass(SellOrder.class);
        stockManager.sell(sellOrder);
        ctx.result("Sell Order verarbeitet");
    }

    private static void handleCancel(Context ctx) {
        String id = ctx.pathParam("id");
        HashMap<String, Integer> request = ctx.bodyAsClass(HashMap.class);
        Integer bodyId = request.get("id");

        System.out.println("Cancel order called with path id: " + id + " and body id: " + bodyId);

        UUID simulationId = parseSimulationId(id);
        if (simulationId == null) {
            ctx.status(400).result("Ungültige Simulation ID");
            return;
        }

        simManager.cancelOrder(simulationId, bodyId);
        ctx.result("Order storniert");
    }

    private static void handleDelete(Context ctx) {
        UUID uuid = parseSimulationId(ctx.pathParam("id"));
        if (uuid == null) {
            ctx.status(400).result("Ungültige Simulation ID");
            return;
        }

        simManager.deleteSimulation(uuid);
        ctx.result("Simulation gelöscht");
    }

    private static void updater(){
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                simManager.update();
            }
            catch (Exception e) {
                //System.err.println("[ERROR] Fehler beim Aktualisieren der Simulationen: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.SECONDS);

    }



    private static UUID parseSimulationId(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            System.err.println("[ERROR] Ungültige Simulation ID: " + id);
            return null;
        }
    }
}
