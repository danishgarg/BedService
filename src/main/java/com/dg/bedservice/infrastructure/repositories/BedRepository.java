package com.dg.bedservice.infrastructure.repositories;

import java.util.ArrayList;
import java.util.List;

import com.dg.bedservice.domain.model.Bed;

import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Repository;

@Repository
public class BedRepository {
    private InteractiveQueryService interactiveQueryService;
    private ReadOnlyKeyValueStore<String, Bed> bedStore;
    public static final String BEDSTORE = "beds";

    @Autowired
    public BedRepository(InteractiveQueryService interactiveQueryService) {
        this.interactiveQueryService = interactiveQueryService;
    }

    public List<Bed> findAll() {
        List<Bed> beds = new ArrayList<>();
        if (this.bedStore == null) {
            this.bedStore = this.interactiveQueryService.getQueryableStore(BEDSTORE,
                    QueryableStoreTypes.keyValueStore());
        }
        this.bedStore.all().forEachRemaining((keyValue) -> beds.add(keyValue.value));
        return beds;
    }
}