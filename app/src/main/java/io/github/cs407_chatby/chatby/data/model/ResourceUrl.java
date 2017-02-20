package io.github.cs407_chatby.chatby.data.model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;

@Data
public class ResourceUrl {

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
            return Integer.parseInt(matcher.group(2));
        throw new RuntimeException("Failed to match in: " + url);
    }

    public String getResourceType() {
        Matcher matcher = pattern.matcher(url);
        if (matcher.find())
            return matcher.group(1);
        throw new RuntimeException("Failed to match in: " + url);
    }

    public static class Adapter extends TypeAdapter<ResourceUrl> {
        @Override
        public void write(JsonWriter out, ResourceUrl value) throws IOException {
            out.value(value.getUrl());
        }

        @Override
        public ResourceUrl read(JsonReader in) throws IOException {
            return new ResourceUrl(in.nextString());
        }
    }
}
