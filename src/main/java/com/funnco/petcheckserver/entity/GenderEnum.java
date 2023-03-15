package com.funnco.petcheckserver.entity;

public enum GenderEnum {
    MALE("MALE"), FEMALE("FEMALE"), OTHER("OTHER");
    private final String value;

    public String getValue() {
        return value;
    }

    GenderEnum(String gender){
        this.value = gender;
    }
}
