package com.example.demo;
/**
 * Exception thrown if there are problems with the creation of a challenge e.g. missing title, description, location, ...
 */
public class ChallengeException extends Exception {
    public ChallengeException(String message) {
        super(message);
    }
}
