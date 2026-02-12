package com;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBManagerNews implements AutoCloseable {

    private final Connection con;
    private ResultSet newsResultSet;
    private ArrayList<PreparedStatement> statements;

    public DBManagerNews(String dbPath) {
        try {
            statements = new ArrayList<>();
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

        // Alte ResultSet und Statements schließen
        closeResultSet();

        String sql = """
                SELECT *
                FROM NEWS
                WHERE Timestamp BETWEEN ? AND ?
                ORDER BY Timestamp ASC
                """;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            statements.add(stmt);
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
                return null; // keine weitere Zeile
            }

            long ts = newsResultSet.getLong("Timestamp");
            String headline = newsResultSet.getString("Headline");
            String news = newsResultSet.getString("News");

            return new News(ts, headline, news);
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Lesen der nächsten News", e);
        }
    }

    /**
     * Schließt das aktuelle ResultSet.
     */
    private void closeResultSet() {
        if (newsResultSet != null) {
            try {
                newsResultSet.close();
            } catch (SQLException e) {
                System.err.println("Fehler beim Schließen von ResultSet: " + e.getMessage());
            }
            newsResultSet = null;
        }
    }

    /**
     * Schließt alle PreparedStatements.
     */
    private void closeStatements() {
        if (statements != null) {
            for (PreparedStatement ps : statements) {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        System.err.println("Fehler beim Schließen von PreparedStatement: " + e.getMessage());
                    }
                }
            }
            statements.clear();
        }
    }

    /**
     * Schließt die Datenbankverbindung und gibt alle Ressourcen frei.
     */
    @Override
    public void close() throws SQLException {
        try {
            closeResultSet();
            closeStatements();
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Schließen des DBManagerNews: " + e.getMessage());
            throw e;
        }
    }
}
