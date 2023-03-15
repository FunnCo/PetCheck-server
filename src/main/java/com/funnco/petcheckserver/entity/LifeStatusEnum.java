package com.funnco.petcheckserver.entity;

public enum LifeStatusEnum {
    ALIVE("ALIVE"), DEAD("DEAD");
    private final String value;

    public String getValue() {
        return value;
    }

    LifeStatusEnum(String status){
        this.value = status;
    }
}
