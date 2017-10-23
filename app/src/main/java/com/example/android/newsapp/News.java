package com.example.android.newsapp;

/**
 * Created by Adam on 25.06.2017.
 */

public class News {

    private String title;
    private String author;
    private String date;
    private String url;
    private String category;


    public News (String title, String author, String date, String category, String url)
    {
        this.title = title;
        this.author = author;
        this.date = date;
        this.category = category;
        this.url = url;
    }


    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
