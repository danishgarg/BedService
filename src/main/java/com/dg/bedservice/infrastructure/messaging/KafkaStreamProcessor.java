package com.dg.bedservice.infrastructure.messaging;

import java.util.List;

import com.dg.bedservice.domain.events.BaseEvent;
import com.dg.bedservice.domain.model.Bed;
import com.dg.bedservice.infrastructure.messaging.configuration.KafkaChannels;
import com.dg.bedservice.infrastructure.messaging.messages.BaseRequest;
import com.dg.bedservice.infrastructure.repositories.BedRepository;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.handler.annotation.SendTo;

import lombok.extern.slf4j.Slf4j;

@EnableBinding(KafkaChannels.class)
@Slf4j
public class KafkaStreamProcessor {
    @StreamListener
    @SendTo(KafkaChannels.BEDEVENTSOUTSTREAM)
    public KStream<String, BaseEvent> processEvents(@Input(KafkaChannels.BEDREQUESTSIN) KStream<String, BaseRequest> bedRequests, 
                                @Input(KafkaChannels.BEDEVENTSIN) KStream<String, BaseEvent> events) {
        JsonSerde<Bed> bedSerde = new JsonSerde<>(Bed.class);
        JsonSerde<BaseEvent> eventSerde = new JsonSerde<>(BaseEvent.class);
        JsonSerde<BaseRequest> requestSerde = new JsonSerde<>(BaseRequest.class);
        KTable<String, Bed> bedTable = events
            .groupByKey(Serialized.with(Serdes.String(), eventSerde))
            .<Bed>aggregate(
                Bed::new, 
                (key, value, aggregate) -> value.applyOn(aggregate),
                Materialized.<String, Bed, KeyValueStore<Bytes, byte[]>>as(BedRepository.BEDSTORE)
                        .withKeySerde(Serdes.String())
                        .withValueSerde(bedSerde)
            );
        return bedRequests.<Bed, List<BaseEvent>>join(
            bedTable, 
            (request, bed) -> {
                try {
                    request.execute(bed);
                } catch (Exception ex) {
                    log.error("Bed was not available", ex);
                }
                
                return bed.getDirtyEvents();
            }, 
            Joined.with(Serdes.String(), requestSerde, bedSerde)
        )
        .flatMapValues((dirtyEvents) -> dirtyEvents);
    }
}