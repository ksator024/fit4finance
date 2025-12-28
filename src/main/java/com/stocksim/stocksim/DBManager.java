package com.stocksim.stocksim;

import java.sql.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

public class DBManager {

    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;

    private long currentTs;
    private String currentSymbol;

    public DBManager(String resourceName) {
        // einfache Persistence.xml mit JDBC LOL :D
        try {
            Class.forName("org.sqlite.JDBC");

            URL url = getClass()
                    .getClassLoader()
                    .getResource(resourceName);

            if (url == null) {
                throw new IllegalStateException("DB nicht gefunden: " + resourceName);
            }

            Path dbPath = Path.of(url.toURI());

            con = DriverManager.getConnection(
                    "jdbc:sqlite:" + dbPath.toAbsolutePath()
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

            // Erstelle Platzhalter für die IN-Klausel (z.B. "?, ?, ?")
            String placeholders = String.join(", ", Collections.nCopies(symbols.size(), "?"));

            String sql = String.format("""
        SELECT *
        FROM stock
        WHERE symbol IN (%s)
          AND ts >= ?
        ORDER BY ts ASC
    """, placeholders);

            stmt = con.prepareStatement(sql);

            // Setze die Symbole als Parameter
            for (int i = 0; i < symbols.size(); i++) {
                stmt.setString(i + 1, symbols.get(i).toUpperCase());
            }

            // Setze den Timestamp als letzten Parameter
            stmt.setLong(symbols.size() + 1, ts);

            rs = stmt.executeQuery();

            if (rs.next()) {
                // Setze currentSymbol und currentTs auf die erste passende Zeile
                currentSymbol = rs.getString("symbol");
                currentTs = rs.getLong("ts");
            } else {
                throw new SQLException(
                        "Kein Eintrag für die Symbole " + symbols + " ab Timestamp " + ts
                );
            }
        }



    public boolean nextTimestamp() throws SQLException {
        if (rs != null && rs.next()) {
            currentTs = rs.getLong("ts");
            return true;
        }
        return false;
    }


    public long getTimestamp() {
        return currentTs;
    }


    public double getValue(String symbol, String column) throws SQLException {

        if (rs == null) {
            throw new SQLException(
                    "Cursor nicht initialisiert. startTimestamp() zuerst aufrufen."
            );
        }

        if (!symbol.equalsIgnoreCase(currentSymbol)) {
            throw new IllegalArgumentException(
                    "Aktueller Cursor ist auf Symbol " + currentSymbol
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
