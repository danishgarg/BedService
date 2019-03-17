package com.dg.bedservice.infrastructure.messaging.messages;

import com.dg.bedservice.domain.model.Bed;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BookBed extends BaseRequest {
    private String occupantId;
    public BookBed(String id, String occupantId) {
        this.setAggregateId(id);
        this.occupantId = occupantId;
    }

    @Override
    public void execute(Bed bed) {
        bed.book(this.occupantId);
    }
}