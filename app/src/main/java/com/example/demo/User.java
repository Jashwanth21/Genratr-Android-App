package com.example.demo;

/**
 * Class for the usage and creation of User objects
 * contain all the user information
 */
public class User {

    private int USER_ID;
    private String USER_USERNAME;
    private String USER_EMAIL;
    private String USER_PASSWORD;
    private String USER_SCORE;
    private String USER_HEX;
    private String USER_LOCATION;
    private String USER_IMAGE_PATH;

    public User(int USER_ID, String USER_USERNAME, String USER_EMAIL, String USER_PASSWORD, String USER_SCORE, String USER_HEX, String USER_LOCATION, String USER_IMAGE_PATH) {
        this.USER_ID = USER_ID;
        this.USER_USERNAME = USER_USERNAME;
        this.USER_EMAIL = USER_EMAIL;
        this.USER_PASSWORD = USER_PASSWORD;
        this.USER_SCORE = USER_SCORE;
        this.USER_HEX = USER_HEX;
        this.USER_LOCATION = USER_LOCATION;
        this.USER_IMAGE_PATH = USER_IMAGE_PATH;
    }

    public User(String USER_USERNAME, String USER_EMAIL, String USER_PASSWORD, String USER_SCORE, String USER_HEX, String USER_LOCATION, String USER_IMAGE_PATH) {
        this.USER_USERNAME = USER_USERNAME;
        this.USER_EMAIL = USER_EMAIL;
        this.USER_PASSWORD = USER_PASSWORD;
        this.USER_SCORE = USER_SCORE;
        this.USER_HEX = USER_HEX;
        this.USER_LOCATION = USER_LOCATION;
        this.USER_IMAGE_PATH = USER_IMAGE_PATH;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getUSER_USERNAME() {
        return USER_USERNAME;
    }

    public void setUSER_USERNAME(String USER_USERNAME) {
        this.USER_USERNAME = USER_USERNAME;
    }

    public String getUSER_EMAIL() {
        return USER_EMAIL;
    }

    public void setUSER_EMAIL(String USER_EMAIL) {
        this.USER_EMAIL = USER_EMAIL;
    }

    public String getUSER_PASSWORD() {
        return USER_PASSWORD;
    }

    public void setUSER_PASSWORD(String USER_PASSWORD) {
        this.USER_PASSWORD = USER_PASSWORD;
    }

    public String getUSER_SCORE() {
        return USER_SCORE;
    }

    public void setUSER_SCORE(String USER_SCORE) {
        this.USER_SCORE = USER_SCORE;
    }

    public String getUSER_HEX() {
        return USER_HEX;
    }

    public void setUSER_HEX(String USER_HEX) {
        this.USER_HEX = USER_HEX;
    }

    public String getUSER_LOCATION() {
        return USER_LOCATION;
    }

    public void setUSER_LOCATION(String USER_LOCATION) {
        this.USER_LOCATION = USER_LOCATION;
    }

    public String getUSER_IMAGE_PATH() {
        return USER_IMAGE_PATH;
    }

    public void setUSER_IMAGE_PATH(String USER_IMAGE_PATH) {
        this.USER_IMAGE_PATH = USER_IMAGE_PATH;
    }
}
