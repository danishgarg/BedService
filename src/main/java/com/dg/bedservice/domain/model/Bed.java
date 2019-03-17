package com.dg.bedservice.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.dg.bedservice.domain.events.BaseEvent;
import com.dg.bedservice.domain.events.BedBooked;
import com.dg.bedservice.domain.events.BedCreated;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bed {
    private String id;
    private String status;
    private String location;
    private String occupantId;
    @JsonIgnore
    private List<BaseEvent> dirtyEvents = new ArrayList<>();
    
    public Bed(String id, String location) {
        dirtyEvents.add(new BedCreated(id, location));
    }

    public void clearDirtyEvents() {
        this.dirtyEvents.clear();
    }

    public Bed apply(BedCreated event) {
        this.status = "Available";
        this.location = event.getLocation();
        this.id = event.getAggregateId();
        return this;
    }

    public void book(String occupantId) {
        if (!"Available".equals(this.status)) {
            throw new IllegalStateException(String.format("Bed is not available. BedId: %s. Status: %s", this.id, this.status));
        }
        dirtyEvents.add(new BedBooked(this.id, occupantId));
    }
	public Bed apply(BedBooked bedBooked) {
        this.status = "Booked";
        this.occupantId = bedBooked.getOccupantId();
        return this;
	}
}