package com.rostik.andrusiv.mesaging.servicerest;


import com.rostik.andrusiv.mesaging.serviceapi.EventService;
import com.rostik.andrusiv.mesaging.serviceapi.IndexService;
import com.rostik.andrusiv.mesaging.servicedto.model.EventDto;
import com.rostik.andrusiv.mesaging.servicerest.api.EventApi;
import com.rostik.andrusiv.mesaging.servicerest.assembler.EventAssembler;
import com.rostik.andrusiv.mesaging.servicerest.model.EventModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class EventController implements EventApi {

    @Autowired
    private EventService service;

    @Autowired
    private IndexService indexService;

    @Autowired
    private EventAssembler assembler;

    //    @PostMapping("/event")
    public EventModel createEvent(EventDto eventDto){
        log.info("creating event " + eventDto.toString());
        return assembler.toModel(service.createEvent(eventDto));
    }

    //    @PutMapping("/createIndex")
    @Override
    public ResponseEntity<Boolean> createIndex() {
        log.info("creating index /event");
        //hardcoded for now
        String indexName = "event";
        Boolean isCreated = indexService.createIndex(indexName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(isCreated);
    }

    @Override
    public List<EventDto> findEventByType(String eventType){
        log.info("trying to find event with type: " + eventType);
        return service.findEventByType(eventType);
    }

    @Override
    public List<EventDto> findEventByTitle(String eventTitle) {
        log.info("trying to find event with title: " + eventTitle);
        return service.findEventByTitle(eventTitle);
    }

    @Override
    public List<EventDto> findEventByDateAndTitle(String eventTitle, String eventDate) {
        log.info("trying to find event with title: " + eventTitle + " and after date: " + eventDate);
        return service.findEventByDateAndTitle(eventTitle,eventDate);
    }

//    @DeleteMapping("/event/{id}")
    public ResponseEntity<Void> deleteEvent(String title){
        log.info("Bulk deleting all events with title" + title);
        service.deleteEvent(title);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/event")
    public List<EventModel> getAllEvents(){
        log.info("Searching for al events");
        return service.getAllEvents().stream()
                .map(eventDto -> assembler.toModel(eventDto))
                .collect(Collectors.toList());
    }
}
