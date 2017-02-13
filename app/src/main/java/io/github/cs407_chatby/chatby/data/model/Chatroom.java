package io.github.cs407_chatby.chatby.data.model;

import java.util.Date;

public class Chatroom {
    private String name;
    private String creatorId;
    private String picUrl;
    private Date start;
    private Date end;
    private Float radius;
    private double latitude;
    private double longitude;

    public Chatroom(String name, String creatorId, String picUrl, Date start, Date end, Float radius, double latitude, double longitude) {
        this.name = name;
        this.creatorId = creatorId;
        this.picUrl = picUrl;
        this.start = start;
        this.end = end;
        this.radius = radius;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public Float getRadius() {
        return radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "name='" + name + "'" +
                "creatorId=" + creatorId + "'" +
                "picUrl='" + picUrl + "'" +
                "start='" + start + "'" +
                "end='" + end + "'" +
                "radius'" + radius + "'" +
                "latitude='" + latitude + "'" +
                "longitude='" + longitude + "'" +
                "}";
    }
}
