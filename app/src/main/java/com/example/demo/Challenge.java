package com.example.demo;

import java.time.LocalDate;

/**
 * Class for the usage and creation of Challenge objects
 * contain all the information of a challenge
 */
public class Challenge {

    private int CHALLENGE_ID;
    private String CHALLENGE_TITLE;
    private String CHALLENGE_TAG;
    private String CHALLENGE_DESCRIPTION;
    private String CHALLENGE_COORDINATES;
    private Utilities.ChallengeLevel CHALLENGE_LEVEL;
    private int CHALLENGE_SCORE;
    private int CHALLENGE_CREATOR_ID;
    private LocalDate CHALLENGE_DEADLINE;
    private String CHALLENGE_HEX;

    public Challenge(int CHALLENGE_ID, String CHALLENGE_TITLE, String CHALLENGE_TAG, String CHALLENGE_DESCRIPTION, String CHALLENGE_COORDINATES, Utilities.ChallengeLevel CHALLENGE_LEVEL, int CHALLENGE_SCORE, int CHALLENGE_CREATOR_ID, LocalDate CHALLENGE_DEADLINE, String CHALLENGE_HEX) {
        this.CHALLENGE_ID = CHALLENGE_ID;
        this.CHALLENGE_TITLE = CHALLENGE_TITLE;
        this.CHALLENGE_TAG = CHALLENGE_TAG;
        this.CHALLENGE_DESCRIPTION = CHALLENGE_DESCRIPTION;
        this.CHALLENGE_COORDINATES = CHALLENGE_COORDINATES;
        this.CHALLENGE_LEVEL = CHALLENGE_LEVEL;
        this.CHALLENGE_SCORE = CHALLENGE_SCORE;
        this.CHALLENGE_CREATOR_ID = CHALLENGE_CREATOR_ID;
        this.CHALLENGE_DEADLINE = CHALLENGE_DEADLINE;
        this.CHALLENGE_HEX = CHALLENGE_HEX;
    }

    public Challenge(String CHALLENGE_TITLE, String CHALLENGE_TAG, String CHALLENGE_DESCRIPTION, String CHALLENGE_COORDINATES, Utilities.ChallengeLevel CHALLENGE_LEVEL, int CHALLENGE_SCORE, int CHALLENGE_CREATOR_ID, LocalDate CHALLENGE_DEADLINE, String CHALLENGE_HEX) {
        this.CHALLENGE_TITLE = CHALLENGE_TITLE;
        this.CHALLENGE_TAG = CHALLENGE_TAG;
        this.CHALLENGE_DESCRIPTION = CHALLENGE_DESCRIPTION;
        this.CHALLENGE_COORDINATES = CHALLENGE_COORDINATES;
        this.CHALLENGE_LEVEL = CHALLENGE_LEVEL;
        this.CHALLENGE_SCORE = CHALLENGE_SCORE;
        this.CHALLENGE_CREATOR_ID = CHALLENGE_CREATOR_ID;
        this.CHALLENGE_DEADLINE = CHALLENGE_DEADLINE;
        this.CHALLENGE_HEX = CHALLENGE_HEX;
    }

    public int getCHALLENGE_ID() {
        return CHALLENGE_ID;
    }

    public void setCHALLENGE_ID(int CHALLENGE_ID) {
        this.CHALLENGE_ID = CHALLENGE_ID;
    }

    public String getCHALLENGE_TITLE() {
        return CHALLENGE_TITLE;
    }

    public void setCHALLENGE_TITLE(String CHALLENGE_TITLE) {
        this.CHALLENGE_TITLE = CHALLENGE_TITLE;
    }

    public String getCHALLENGE_TAG() {
        return CHALLENGE_TAG;
    }

    public void setCHALLENGE_TAG(String CHALLENGE_TAG) {
        this.CHALLENGE_TAG = CHALLENGE_TAG;
    }

    public String getCHALLENGE_DESCRIPTION() {
        return CHALLENGE_DESCRIPTION;
    }

    public void setCHALLENGE_DESCRIPTION(String CHALLENGE_DESCRIPTION) {
        this.CHALLENGE_DESCRIPTION = CHALLENGE_DESCRIPTION;
    }

    public String getCHALLENGE_COORDINATES() {
        return CHALLENGE_COORDINATES;
    }

    public void setCHALLENGE_COORDINATES(String CHALLENGE_COORDINATES) {
        this.CHALLENGE_COORDINATES = CHALLENGE_COORDINATES;
    }

    public Utilities.ChallengeLevel getCHALLENGE_LEVEL() {
        return CHALLENGE_LEVEL;
    }

    public void setCHALLENGE_LEVEL(Utilities.ChallengeLevel CHALLENGE_LEVEL) {
        this.CHALLENGE_LEVEL = CHALLENGE_LEVEL;
    }

    public int getCHALLENGE_SCORE() {
        return CHALLENGE_SCORE;
    }

    public void setCHALLENGE_SCORE(int CHALLENGE_SCORE) {
        this.CHALLENGE_SCORE = CHALLENGE_SCORE;
    }

    public int getCHALLENGE_CREATOR_ID() {
        return CHALLENGE_CREATOR_ID;
    }

    public void setCHALLENGE_CREATOR_ID(int CHALLENGE_CREATOR_ID) {
        this.CHALLENGE_CREATOR_ID = CHALLENGE_CREATOR_ID;
    }

    public LocalDate getCHALLENGE_DEADLINE() {
        return CHALLENGE_DEADLINE;
    }

    public void setCHALLENGE_DEADLINE(LocalDate CHALLENGE_DEADLINE) {
        this.CHALLENGE_DEADLINE = CHALLENGE_DEADLINE;
    }

    public String getCHALLENGE_HEX() {
        return CHALLENGE_HEX;
    }

    public void setCHALLENGE_HEX(String CHALLENGE_HEX) {
        this.CHALLENGE_HEX = CHALLENGE_HEX;
    }
}
