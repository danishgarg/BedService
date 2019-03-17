package com.dg.bedservice.domain.events;

import java.time.Instant;

import com.dg.bedservice.domain.model.Bed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BedCreated extends BaseEvent {
    private String location;

    public BedCreated(String id, String location) {
        this.setAggregateId(id);
        this.location = location;
        this.setEventTimestamp(Instant.now().toString());
    }

    @Override
    public Bed applyOn(Bed bed) {
        bed.apply(this);
        return bed;
    }
}