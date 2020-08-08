package com.udacity.jwdnd.course1.cloudstorage.entity;

public class Credentials {
    private int id;
    private String url;
    private String username;
    private String key;
    private String password;
    private User user;

    public String getUrl() {
        return url;
    }

    public Credentials() {}

    public Credentials(User user, String url) {
        this.user = user;
        this.key = user.getSalt();
        this.url = url;
        this.password = user.getPassword();
        this.username = user.getUsername();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
