package com;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManagerNews {

    private Connection con;
    private PreparedStatement stmt;
    private ResultSet newsResultSet;

    public DBManagerNews(String dbPath) {
        try {

            Path tempdb = Files.createTempFile("news", ".db");

            try (var inputStream = getClass().getResourceAsStream("/news.db")) {
                Files.copy(inputStream, tempdb, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
            con = DriverManager.getConnection("jdbc:sqlite:" + tempdb);
        } catch (Exception e) {
            throw new IllegalStateException("DBManagerNews konnte nicht initialisiert werden", e);
        }
    }

    public void startTimestamp(long startTimestamp, long endTimestamp) {
        if (startTimestamp > endTimestamp) {
            throw new IllegalArgumentException("startTimestamp darf nicht nach endTimestamp liegen");
        }

        String sql = """
                SELECT *
                FROM NEWS
                WHERE Timestamp BETWEEN ? AND ?
                ORDER BY Timestamp ASC
                """;

        try {
            // Falls bereits eine vorherige Abfrage offen ist: Ressourcen schließen
            if (newsResultSet != null && !newsResultSet.isClosed()) {
                newsResultSet.close();
            }
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }

            stmt = con.prepareStatement(sql);
            stmt.setLong(1, startTimestamp);
            stmt.setLong(2, endTimestamp);

            newsResultSet = stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Laden der News", e);
        }
    }

    public News next() {
        if (newsResultSet == null) {
            throw new IllegalStateException("startTimestamp muss zuerst aufgerufen werden");
        }

        try {
            if (!newsResultSet.next()) {
                // keine weitere Zeile -> Ressourcen freigeben
                if (!newsResultSet.isClosed()) {
                    newsResultSet.close();
                }
                if (stmt != null && !stmt.isClosed()) {
                    stmt.close();
                }
                newsResultSet = null;
                stmt = null;
                return null;
            }

            long ts = newsResultSet.getLong("Timestamp");
            String headline = newsResultSet.getString("Headline");
            String news = newsResultSet.getString("News");

            return new News(ts, headline, news);
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Lesen der nächsten News", e);
        }
    }

    public void close() {
        try {
            System.out.println("DBManagerNews wird geschlossen...");
            if (newsResultSet != null && !newsResultSet.isClosed()) {
                newsResultSet.close();
            }
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
