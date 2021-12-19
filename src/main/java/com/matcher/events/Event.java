package com.matcher.events;

public class Event {
    String event;

    public Event(String msg) {
        this.event = msg;
    }

    public void printEvent() {
        System.out.println(this.event);
    }

    public String getEvent() {
        return this.event;
    }
}
