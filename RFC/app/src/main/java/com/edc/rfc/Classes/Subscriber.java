package com.edc.rfc.Classes;

public class Subscriber {

    private String name, id, enrolledFor;

    public Subscriber() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnrolledFor() {
        return enrolledFor;
    }

    public void setEnrolledFor(String enrolledFor) {
        this.enrolledFor = enrolledFor;
    }
}
