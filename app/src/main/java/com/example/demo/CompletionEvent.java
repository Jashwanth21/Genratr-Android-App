package com.example.demo;

/**
 * POJO containing data to display in FeedActivity.java
 * Represents a given challenge and information about a user who has completed it
 */
public class CompletionEvent {

    private Challenge challenge;
    private int completersUserId;
    private String completersUserName;


    public CompletionEvent(Challenge c, int completersId, String userName){
        this.challenge = c;
        this.completersUserId = completersId;
        this.completersUserName = userName;
    }


    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public int getCompletersUserId() {
        return completersUserId;
    }

    public void setCompletersUserId(int completersUserId) {
        this.completersUserId = completersUserId;
    }

    public String getCompletersUserName() {
        return completersUserName;
    }

    public void setCompletersUserName(String completersUserName) {
        this.completersUserName = completersUserName;
    }
}
