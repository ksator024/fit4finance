package com;

import java.sql.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DBManager {

    private Connection con;
    private PreparedStatement stmt;
    private HashMap<String, ResultSet> resultSets;

    private long currentTs;

    public DBManager(String dbPath) {
        // einfache Persistence.xml mit JDBC LOL :D
        try {
            /*
            Class.forName("org.sqlite.JDBC");

            Path path = Path.of(dbPath);

            // Prüfe, ob es ein Resource ist
            if (!Files.exists(path)) {
                URL url = getClass()
                        .getClassLoader()
                        .getResource(dbPath);

                if (url == null) {
                    throw new IllegalStateException("DB nicht gefunden: " + dbPath);
                }

                path = Path.of(url.toURI());
            }
            */
            Path tempdb = Files.createTempFile("stocks", ".db");
            try (var inputStream = getClass().getResourceAsStream("/stocks.db")) {
                Files.copy(inputStream, tempdb, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
            con = DriverManager.getConnection(
                    "jdbc:sqlite:" + tempdb
            );

        } catch (Exception e) {
            throw new IllegalStateException(
                    "DBManager konnte nicht initialisiert werden", e
            );
        }
    }


        public void startTimestamp(ArrayList<String> symbols, long ts) throws SQLException {
        if (symbols == null || symbols.isEmpty()) {
            throw new IllegalArgumentException("Symbol-Liste darf nicht null oder leer sein.");
        }

        resultSets = new HashMap<>();
        currentTs = ts;

        String sql = """
            SELECT *
            FROM stock
            WHERE symbol = ?
              AND ts >= ?
            ORDER BY ts ASC
        """;

        // Für jedes Symbol eine separate Query durchführen
        for (String symbol : symbols) {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, symbol.toUpperCase());
            stmt.setLong(2, ts);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println(stmt.toString());
                throw new SQLException(
                        "Kein Eintrag für Symbol " + symbol + " ab Timestamp " + ts
                );
            }

            // Speichere das ResultSet in der HashMap
            resultSets.put(symbol.toUpperCase(), rs);
            
            // Setze currentTs auf die erste passende Zeile (vom ersten Symbol)
            if (currentTs == ts) {
                currentTs = rs.getLong("ts");
            }
        }
    }



    public boolean nextTimestamp() throws SQLException {
        if (resultSets == null || resultSets.isEmpty()) {
            return false;
        }

        // Bewege alle ResultSets zur nächsten Zeile
        boolean hasNext = true;
        for (Map.Entry<String, ResultSet> entry : resultSets.entrySet()) {
            if (!entry.getValue().next()) {
                hasNext = false;
                break;
            }
        }

        if (hasNext) {
            // Alle haben eine neue Zeile, updatet currentTs vom ersten ResultSet
            ResultSet firstRs = resultSets.values().iterator().next();
            currentTs = firstRs.getLong("ts");
            return true;
        }

        return false;
    }


    public long getTimestamp() {
        return currentTs;
    }


    public double getValue(String symbol, String column) throws SQLException {
        if (resultSets == null || resultSets.isEmpty()) {
            throw new SQLException(
                    "Cursor nicht initialisiert. startTimestamp() zuerst aufrufen."
            );
        }

        ResultSet rs = resultSets.get(symbol.toUpperCase());
        if (rs == null) {
            throw new IllegalArgumentException(
                    "Symbol " + symbol + " nicht in der Symbolliste vorhanden."
            );
        }

        return switch (column.toUpperCase()) {
            case "OPEN"  -> rs.getDouble("open");
            case "HIGH"  -> rs.getDouble("high");
            case "LOW"   -> rs.getDouble("low");
            case "CLOSE" -> rs.getDouble("close");
            case "VOLUME"-> rs.getLong("volume");
            default -> throw new IllegalArgumentException(
                    "Unbekannte Spalte: " + column
            );
        };
    }
}
