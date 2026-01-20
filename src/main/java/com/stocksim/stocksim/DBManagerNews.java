package com.stocksim.stocksim;

import java.net.URL;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManagerNews {

    private final Connection con;
    private ResultSet newsResultSet;

    public DBManagerNews(String resourceName) {
        try {
            Class.forName("org.sqlite.JDBC");

            URL url = getClass().getClassLoader().getResource(resourceName);
            if (url == null) {
                throw new IllegalStateException("DB nicht gefunden: " + resourceName);
            }

            Path dbPath = Path.of(url.toURI());
            con = DriverManager.getConnection("jdbc:sqlite:" + dbPath.toAbsolutePath());
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
            PreparedStatement stmt = con.prepareStatement(sql);
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

}
