package com.dg.bedservice.infrastructure.messaging.messages;

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
  @Type(value = BookBed.class, name = "bookBed")
})
public abstract class BaseRequest {
    private String aggregateId;

    public abstract void execute(Bed bed);
}