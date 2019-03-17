package com.dg.bedservice.infrastructure.messaging;

import java.util.List;

import com.dg.bedservice.domain.events.BaseEvent;
import com.dg.bedservice.infrastructure.messaging.configuration.KafkaChannels;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;

@Component
@EnableBinding(KafkaChannels.class)
public class KafkaAdHocProducer {
    private KafkaChannels channels;

    @Autowired
    public KafkaAdHocProducer(KafkaChannels channels) {
        this.channels = channels;
    }
    public void save(List<BaseEvent> events) {
        for (BaseEvent event : events) {
            channels.output().send(MessageBuilder.withPayload(event)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, event.getAggregateId()).build());
        }

    }
}