package com.stocksim.stocksim;

import java.sql.*;
import java.net.URL;
import java.nio.file.Path;

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


    public void startTimestamp(String symbol, long ts) throws SQLException {

        String sql = """
            SELECT *
            FROM stock
            WHERE symbol = ?
              AND ts >= ?
            ORDER BY ts ASC
        """;

        stmt = con.prepareStatement(sql);
        stmt.setString(1, symbol.toUpperCase());
        stmt.setLong(2, ts);

        rs = stmt.executeQuery();

        if (rs.next()) {
            currentSymbol = symbol.toUpperCase();
            currentTs = rs.getLong("ts");
        } else {
            throw new SQLException(
                    "Kein Eintrag für Symbol " + symbol + " ab Timestamp " + ts
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
