package com.example.demo;

import android.database.Cursor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Utility file with enums for values that can be used throughout the project
 * and methods to extract data from Cursors
 */
public class Utilities {

    /**
     * Enum stores the different tags for challenges
     * Every option has a name in lowercase and a specific score
     *
     * WALK is a walking challenge
     * ELECTRICITY is an electricity conservation challenge
     * WATER is for challenges involving water e.g. water conservation challenges, healthy lifestyle challenges
     * LITTER is for challenges involving picking up litter
     * FOOD is for challenges involving food conservation and food distribution
     */
    public enum ChallengeType {
        WALK(50, "walk"), ELECTRICITY(100, "electricity"), WATER(100, "water"), LITTER(125, "litter"), FOOD(75, "food");

        private int score;
        private String name;

        ChallengeType(int score, String name) {
            this.score = score;
            this.name = name;
        }

        public int getScore() {
            return score;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Enum that stores the different challengeLevels
     * LOCAL is for challenges that will only show in a specific county e.g. County Dublin
     * COUNTRY is for challenges that will only show in a specific country e.g. Ireland
     * Global is fot challenges that will be visible for everybody
     */
    public enum ChallengeLevel {
        LOCAL, COUNTRY, GLOBAL;

        public static ChallengeLevel parseChallengeLevel(String level){
            if(level.toLowerCase().equals("global")){
                return GLOBAL;
            }else if(level.toLowerCase().equals("country")){
                return COUNTRY;
            }else if(level.toLowerCase().equals("local")){
                return LOCAL;
            }
            else return null;
        }
    }


    // this method parses data returned from the getAllChallenges() method in Database.java
    // into an array Challenge objects
    public static ArrayList<Challenge> getChallengesFromCursor(Cursor cursor) {
        ArrayList<Challenge> output = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Challenge challenge = new Challenge(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        Utilities.ChallengeLevel.parseChallengeLevel(cursor.getString(5)),
                        cursor.getInt(6),
                        cursor.getInt(7),
                        LocalDate.parse(cursor.getString(8), DateTimeFormatter.ISO_DATE),
                        cursor.getString(9)
                );
                output.add(challenge);
            } while (cursor.moveToNext());
        }
        return output;
    }

    // retrieves only username and user ID from a Cursor returned by the database's getAllUsers() method
    public static ArrayList<String[]> getUserDataFromCursor(Cursor cursor){
        ArrayList<String[]> output = new ArrayList<String[]>();
        if (cursor.moveToFirst()){
            do{
                String[] user = new String[2];
                user[0] = (String) String.valueOf(cursor.getInt(0)); // userID
                user[1] = (String) cursor.getString(1); // username
                output.add(user);
            } while (cursor.moveToNext());
        }
        return output;
    }

    // parses data returned by the database's getAllCompleted() method
    public static ArrayList<int[]> getLinkDataFromCursor(Cursor cursor){
        // in each String[], [0] is the user ID, [1] is the challenge ID
        ArrayList<int[]> output = new ArrayList<int[]>();
        if (cursor.moveToFirst()){
            do{
                int[] link = new int[2];
                link[0] = cursor.getInt(1); // userID
                link[1] = cursor.getInt(2); // challengeID
                output.add(link);
            } while (cursor.moveToNext());
        }
        return output;
    }

    // method to get a collection of objects linking a completed challenge to the user who completed it
    public static ArrayList<CompletionEvent> buildCompletionObjects(Cursor userCursor, Cursor challengeCursor, Cursor linkCursor) {
        ArrayList<int[]> links = getLinkDataFromCursor(linkCursor);
        ArrayList<Challenge> challenges = getChallengesFromCursor(challengeCursor);
        ArrayList<String[]> userDetails = getUserDataFromCursor(userCursor);
        ArrayList<CompletionEvent> output = new ArrayList<>();

        // looping over user/challenge links from the database's gateway table to create the objects
        for(int i=0; i<links.size(); i++){
            int[] link = links.get(i); // userID, challengeID
            Challenge c = new Challenge(0,"","","","",ChallengeLevel.LOCAL,1,1,null,"");
            int completersID = link[0];
            String completersUsername = "";

            // finding the challenge that matches the current link
            for(int j=0; j<challenges.size(); j++) {
                Challenge mChallenge = challenges.get(j);
                if(mChallenge.getCHALLENGE_ID() == (link[1])){
                    c = mChallenge;
                    break;
                }
            }

            // finding the user that matches the current link
            for(int j=0; j<userDetails.size(); j++) {
                String[] user = userDetails.get(j);
                if(user[0].equals(String.valueOf(link[0]))){
                    completersUsername = user[1];
                    break;
                }
            }

            // if the right challenge and user were found, create a new CompletionEvent out of them
            if(c.getCHALLENGE_ID() != 0){
                CompletionEvent completion = new CompletionEvent(c, completersID, completersUsername);
                output.add(completion);
            }
        }

        return output;
    }
}
