package com.dg.bedservice.domain.events;

import java.time.Instant;

import com.dg.bedservice.domain.model.Bed;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BedBooked extends BaseEvent {
    private String occupantId;
    public BedBooked(String id, String occupantId) {
        this.setAggregateId(id);
        this.occupantId = occupantId;
        this.setEventTimestamp(Instant.now().toString());
    }

    @Override
    public Bed applyOn(Bed bed) {
        bed.apply(this);
        return bed;
    }
}