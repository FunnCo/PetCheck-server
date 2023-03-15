package com.funnco.petcheckserver.dto.request;

public class ChangeRequestAnimalTypeDto {
    private Long oldTypeId;
    private Long newTypeId;

    public Long getOldTypeId() {
        return oldTypeId;
    }

    public void setOldTypeId(Long oldTypeId) {
        this.oldTypeId = oldTypeId;
    }

    public Long getNewTypeId() {
        return newTypeId;
    }

    public void setNewTypeId(Long newTypeId) {
        this.newTypeId = newTypeId;
    }

    public ChangeRequestAnimalTypeDto(Long oldTypeId, Long newTypeId) {
        this.oldTypeId = oldTypeId;
        this.newTypeId = newTypeId;
    }
}
