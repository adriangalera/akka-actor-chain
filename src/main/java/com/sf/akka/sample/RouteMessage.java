package com.sf.akka.sample;

/**
 * Created by gal on 5/12/17.
 */
public class RouteMessage {
    private final String message;

    public RouteMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}