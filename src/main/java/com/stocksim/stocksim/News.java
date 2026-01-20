package com.stocksim.stocksim;

public class News {
    private final long timestamp;
    private final String headline;
    private final String news;

    public News(long timestamp, String headline, String news) {
        this.timestamp = timestamp;
        this.headline = headline;
        this.news = news;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getHeadline() {
        return headline;
    }

    public String getNews() {
        return news;
    }




}
