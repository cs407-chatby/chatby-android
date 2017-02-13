package io.github.cs407_chatby.chatby.data.model;

public class User {
    private String name;
    private String email;
    private String picUrl;

    public User(String name, String email, String picUrl) {
        this.name = name;
        this.email = email;
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPicUrl() {
        return picUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + "'" +
                "email='" + email + "'" +
                "picUrl='" + picUrl + "'" +
                "}";
    }
}
