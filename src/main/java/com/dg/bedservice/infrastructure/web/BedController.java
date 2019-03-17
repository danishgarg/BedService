package com.dg.bedservice.infrastructure.web;

import java.util.List;

import com.dg.bedservice.domain.model.Bed;
import com.dg.bedservice.infrastructure.messaging.KafkaAdHocProducer;
import com.dg.bedservice.infrastructure.repositories.BedRepository;
import com.dg.bedservice.infrastructure.web.requests.CreateBedRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/beds")
public class BedController {
    BedRepository repository;
    KafkaAdHocProducer publisher;
    @Autowired
    public BedController(BedRepository repository, KafkaAdHocProducer publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }
    @PostMapping("/create")
    public void createBed(@RequestBody CreateBedRequest request) {
        Bed bed = new Bed(request.getId(), request.getLocation());
        this.publisher.save(bed.getDirtyEvents());
        bed.clearDirtyEvents();
    }

    @GetMapping
    public List<Bed> getMethodName() {
        return repository.findAll();
    }
    
}