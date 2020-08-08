package com.udacity.jwdnd.course1.cloudstorage.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;

public class User {
    private int userId;
    private String username;

    @JsonIgnore
    private String salt;

    @NotBlank
    private String firstName;
    private String lastName;
    private String password;

    private String confirmPassword;

//    @AssertTrue
//    private boolean passwordMatches() {
//        return password.matches(confirmPassword);
//    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
