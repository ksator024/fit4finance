package com;

import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    static SimulationManager simManager = new SimulationManager();
    static Logger logger = LogManager.getLogger(Main.class);


    public static void main(String[] args) {
        updater();
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = "/dist";
                staticFiles.hostedPath = "/";
            });
        }).start(8080);
        logger.info("Server gestartet auf Port 8080");

        // API Routes
        app.post("/simulations", Main::handleCreateSimulation);
        app.get("/{id}/update", Main::handleGetUpdate);
        app.post("/{id}/pause", Main::handlePause);
        app.post("/{id}/buy", Main::handleBuy);
        app.post("/{id}/sell", Main::handleSell);
        app.post("/{id}/cancel", Main::handleCancel);
        app.post("/{id}/delete", Main::handleDelete);

        // Fallback für SPA: Alle nicht gefundenen Routes liefern index.html
        app.error(404, ctx -> {
            ctx.contentType("text/html");
            ctx.result(Main.class.getResourceAsStream("/dist/index.html"));
        });

        // Direkter / Route Handler als Fallback
        app.get("/", ctx -> {
            ctx.contentType("text/html");
            ctx.result(Main.class.getResourceAsStream("/dist/index.html"));
        });


    }


    private static void handleCreateSimulation(Context ctx) {
        try {
            int number = Integer.parseInt(ctx.queryParam("number"));
            UUID simulationId = simManager.newSimulation(number);
            String clientIp = ctx.ip();
            String userAgent = ctx.header("User-Agent");

            HashMap<String, String> response = new HashMap<>();
            response.put("simulationId", simulationId.toString());
            logger.info("New simulation ID: " + simulationId + " Nr: " + number + " IP: " + clientIp + " User-Agent: " + userAgent);
            ctx.json(response);
        }
        catch (Exception e) {
            logger.error("Fehler beim Initialisieren der Simulation: " + e.getMessage());
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

        System.out.println("Buy Order ausgeführt");
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
        System.out.println("Sell Order ausgeführt");
        stockManager.sell(sellOrder);
        ctx.result("Sell Order verarbeitet");
    }

    private static void handleCancel(Context ctx) {
        String id = ctx.pathParam("id");
        HashMap<String, Integer> request = ctx.bodyAsClass(HashMap.class);
        Integer bodyId = request.get("id");

        System.out.println("Cancel Order ausgeführt");

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
        logger.info("Simulation " + uuid + " gelöscht");
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
                System.err.println("[ERROR] Fehler beim Aktualisieren der Simulationen: " + e.getMessage());
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
