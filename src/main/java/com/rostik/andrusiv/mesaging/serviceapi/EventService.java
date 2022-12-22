package com.rostik.andrusiv.mesaging.serviceapi;

import com.rostik.andrusiv.mesaging.servicedto.model.EventDto;

import java.util.List;

public interface EventService {
    EventDto createEvent(EventDto eventDto);

    EventDto deleteEvent(String id);

    List<EventDto> getAllEvents();

    List<EventDto> findEventByType(String eventType);

    List<EventDto> findEventByTitle(String eventType);

    List<EventDto> findEventByDateAndTitle(String eventType, String eventDate);

}
