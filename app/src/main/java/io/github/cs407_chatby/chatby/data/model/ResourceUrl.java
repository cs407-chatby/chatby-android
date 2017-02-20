package io.github.cs407_chatby.chatby.data.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;

@Data
public class ResourceUrl<T> {

    private static final Pattern pattern = Pattern.compile("/([a-zA-Z\\-]+)/([0-9]+)/$");

    private final String url;

    public ResourceUrl(String url) {
        this.url = url;
    }

    public ResourceUrl(String baseUrl, String resourceType, String id) {
        this(String.format("%s/%s/%s/", baseUrl, resourceType, id));
    }

    public ResourceUrl(String resourceType, String id) {
        this("/api", resourceType, id);
    }

    public Integer getId() {
        Matcher matcher = pattern.matcher(url);
        if (matcher.find())
            return Integer.parseInt(matcher.group(0));
        throw new RuntimeException("Failed to match in: " + url);
    }

    public String getResourceType() {
        Matcher matcher = pattern.matcher(url);
        if (matcher.find())
            return matcher.group(1);
        throw new RuntimeException("Failed to match in: " + url);
    }
}
