package com.rostik.andrusiv.mesaging.servicerest.api;

import com.rostik.andrusiv.mesaging.servicedto.model.EventDto;
import com.rostik.andrusiv.mesaging.servicerest.model.EventModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Event management API")
@RequestMapping("/api/v1/event")
public interface EventApi {

    @ApiOperation("Get all events")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<EventModel> getAllEvents();

    @ApiOperation("Create event")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    EventModel createEvent(@RequestBody EventDto eventDto);

    @ApiOperation("Create index")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(path = "/createIndex")
    ResponseEntity<Boolean> createIndex();


    @ApiOperation("Find events by type")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
            value = "/findEventByType")
    public List<EventDto> findEventByType(@RequestParam String eventType);


    @ApiOperation("Find events by title")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
            value = "/findEventByTitle")
    public List<EventDto> findEventByTitle(@RequestParam String eventTitle);


    @ApiOperation("Find events by date and type")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
            value = "/findEventByDateAndTitle")
    public List<EventDto> findEventByDateAndTitle(@RequestParam String eventTitle, @RequestParam String eventDate);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", paramType = "path", required = true, value = "Event title"),
    })
    @ApiOperation("bulk delete events by title")
    @DeleteMapping(value = "/{title}")
    ResponseEntity<Void> deleteEvent(@PathVariable String title);
}
