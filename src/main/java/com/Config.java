package com;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();


    public static String getStocksDbPath() {
        String path = dotenv.get("STOCKS_DB_PATH");
        return path != null ? path : "stocks.db";
    }

    public static String getNewsDbPath() {
        String path = dotenv.get("NEWS_DB_PATH");
        return path != null ? path : "news.db";
    }
}
