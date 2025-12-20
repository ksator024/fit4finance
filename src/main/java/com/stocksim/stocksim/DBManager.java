package com.stocksim.stocksim;

import java.sql.*;

public class DBManager {

    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;

    private long currentTs;

    public DBManager(String dbPath) throws SQLException {
        con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    public void startTimestamp(long ts) throws SQLException {
        String sql = """
            SELECT * FROM stock
            WHERE ts >= ?
            ORDER BY ts ASC
        """;

        stmt = con.prepareStatement(sql);
        stmt.setLong(1, ts);
        rs = stmt.executeQuery();

        if (rs.next()) {
            currentTs = rs.getLong("ts");
        } else {
            throw new SQLException("Kein Eintrag ab diesem Timestamp");
        }
    }

    public boolean nextTime() throws SQLException {
        if (rs != null && rs.next()) {
            currentTs = rs.getLong("ts");
            return true;
        }
        return false;
    }

    public long getTimestamp() throws SQLException {
        return currentTs;
    }

    public double getValue(String symbol) throws SQLException {

        if (rs == null) {
            throw new SQLException("Cursor nicht initialisiert. startTimestamp() zuerst aufrufen.");
        }

        return switch (symbol.toUpperCase()) {
            case "OPEN"     -> rs.getDouble("open");
            case "HIGH"     -> rs.getDouble("high");
            case "LOW"      -> rs.getDouble("low");
            case "CLOSE"    -> rs.getDouble("close");
            case "ADJCLOSE" -> rs.getDouble("adjclose");
            case "VOLUME"   -> rs.getLong("volume");
            default -> throw new IllegalArgumentException(
                    "Unbekannter Wert-Typ: " + symbol
            );
        };
    }


}
