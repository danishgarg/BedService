package com.dg.bedservice.infrastructure.messaging.configuration;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface KafkaChannels {
    String BEDEVENTSOUT = "bedEventsOutput";
	String BEDEVENTSIN = "bedEventsInput";
	String BEDREQUESTSIN = "bedRequestsInput";
	String BEDEVENTSOUTSTREAM = "bedEventsOutputStream";
	/**
	 * @return output channel
	 */
	@Output(KafkaChannels.BEDEVENTSOUT)
	MessageChannel output();

	@Input(KafkaChannels.BEDEVENTSIN)
	KStream<?,?> bedEventsIn();

	@Input(KafkaChannels.BEDREQUESTSIN)
	KStream<?,?> bedRequestsIn();

	@Output(KafkaChannels.BEDEVENTSOUTSTREAM)
	KStream<?,?> bedEventsOut();
}