package com.dg.bedservice.domain.events;

import com.dg.bedservice.domain.model.Bed;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME, 
  include = JsonTypeInfo.As.PROPERTY, 
  property = "type")
@JsonSubTypes({ 
  @Type(value = BedCreated.class, name = "bedCreated"),
  @Type(value = BedBooked.class, name = "bedBooked")
})
public abstract class BaseEvent {
    private String aggregateId;
    private String eventTimestamp;

    public abstract Bed applyOn(Bed bed);
}