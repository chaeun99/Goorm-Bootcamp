package com.example.bookmark.bookmark_manager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

public class Bookmark {
    private final Long id;
    private final String url;
    private final String title;
    private final String description;
    private final List<String> tags;
    private final LocalDateTime createdAt;

    public Bookmark(Long id, String url, String title, String description, List<String> tags) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getTagsAsString() {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        StringJoiner stringJoiners =  new StringJoiner(", ");
        for (String tag : tags) {
            stringJoiners.add(tag);
        }
        return stringJoiners.toString();
    }
}