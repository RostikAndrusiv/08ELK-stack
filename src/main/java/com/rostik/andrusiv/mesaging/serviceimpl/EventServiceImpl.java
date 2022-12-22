package com.rostik.andrusiv.mesaging.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rostik.andrusiv.mesaging.exception.ClientException;
import com.rostik.andrusiv.mesaging.exception.EventException;
import com.rostik.andrusiv.mesaging.exception.ServiceException;
import com.rostik.andrusiv.mesaging.helper.Indices;
import com.rostik.andrusiv.mesaging.serviceapi.EventService;
import com.rostik.andrusiv.mesaging.servicedto.model.EventDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.UUIDs;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private static final String CLIENT_EXCEPTION = "client exception";

    private static final String TITLE = "title";
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public EventDto createEvent(EventDto eventDto) {
        if (eventDto.getEventId() == null) {
            final String uuid = UUIDs.randomBase64UUID();
            log.info("Request event id is null. Setting random uuid: " + uuid);
            eventDto.setEventId(UUIDs.randomBase64UUID());
        }
        try {
            log.info("Create new event start...");
            final String eventAsString = mapper.writeValueAsString(eventDto);
            final IndexRequest request = new IndexRequest(Indices.EVENT_INDEX);
            request.id(eventDto.getEventId());
            request.source(eventAsString, XContentType.JSON);

            client.index(request, RequestOptions.DEFAULT);
            log.info("Create new event end. Event is created. event id:" + request.id());
            return eventDto;
        } catch (final Exception e) {
            throw new ClientException(CLIENT_EXCEPTION);
        }
    }

    @Override
    public EventDto deleteEvent(String title) {
        final String BULK_DELETE = "Bulk delete events. ";
        log.info(BULK_DELETE + "Start...");
        // step 1. find all events to delete by title
        QueryBuilder matchSpecificFieldQuery = QueryBuilders
                .matchQuery(TITLE, title);
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .postFilter(matchSpecificFieldQuery);
        SearchRequest request = new SearchRequest(Indices.EVENT_INDEX);
        request.source(builder);

        SearchResponse response = null;
        log.debug(BULK_DELETE + "Calling client to find all events with title" + title);
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ClientException(e.getMessage());
        }
        if (response == null) {
            throw new EventException("response from client is null");
        }
        log.debug(BULK_DELETE + "found " + response.getHits().getHits().length + " events with title " + title);
        SearchHit[] searchHits = response.getHits().getHits();
        //step 2. Extract ids, create deleteRequest for each and add it to bulkRequest
        BulkRequest bulkRequest = new BulkRequest();
        Arrays.stream(searchHits)
                .map(SearchHit::getId)
                .map(id -> new DeleteRequest(Indices.EVENT_INDEX, id))
                .forEach(bulkRequest::add);
        //Step 3. Execute BulkRequest
        log.debug(BULK_DELETE + "Calling client to delete events with title" + title);
        try {
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info(BULK_DELETE + "End SUCCESS");
        } catch (IOException e) {
            throw new ClientException(CLIENT_EXCEPTION);
        }
        throw new EventException();
    }

    @SneakyThrows
    @Override
    public List<EventDto> getAllEvents() {
        log.info("get all events start...");
        SearchRequest searchRequest = new SearchRequest(Indices.EVENT_INDEX);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHits = response.getHits().getHits();
        List<EventDto> eventDtos = Arrays.stream(searchHits)
                .map(this::getEventDto)
                .collect(Collectors.toList());
        log.info("get all events end.");
        return eventDtos;
    }

    @Override
    public List<EventDto> findEventByType(String eventType) {
        log.info("find event by type start...");

        QueryBuilder matchSpecificFieldQuery = QueryBuilders
                .matchQuery("eventType", eventType);
        final SearchSourceBuilder builder = new SearchSourceBuilder()
                .postFilter(matchSpecificFieldQuery);
        final SearchRequest request = new SearchRequest(Indices.EVENT_INDEX);
        request.source(builder);

        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            if (response == null) {
                throw new ServiceException(String.format("can't find events with type %s", eventType));
            }

            SearchHit[] searchHits = response.getHits().getHits();
            List<EventDto> eventDtos = Arrays.stream(searchHits)
                    .map(this::getEventDto)
                    .collect(Collectors.toList());
            log.info("find event by type end...");
            return eventDtos;
        } catch (final Exception e) {
            throw new ClientException(CLIENT_EXCEPTION);
        }
    }

    @Override
    public List<EventDto> findEventByTitle(String eventTitle) {
        log.info("find event by title start...");
        QueryBuilder matchSpecificFieldQuery = QueryBuilders
                .matchQuery(TITLE, eventTitle);
        final SearchSourceBuilder builder = new SearchSourceBuilder()
                .postFilter(matchSpecificFieldQuery);
        final SearchRequest request = new SearchRequest(Indices.EVENT_INDEX);
        request.source(builder);

        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            if (response == null) {
                throw new ServiceException(String.format("can't find events with title %s", eventTitle));
            }
            SearchHit[] searchHits = response.getHits().getHits();
            List<EventDto> eventDtos = Arrays.stream(searchHits)
                    .map(this::getEventDto)
                    .collect(Collectors.toList());
            log.info("find event by title end...");
            return eventDtos;
        } catch (final Exception e) {
            throw new ClientException(CLIENT_EXCEPTION);
        }
    }

    @Override
    public List<EventDto> findEventByDateAndTitle(String eventTitle, String eventDate) {
        log.info("find event by date and title start...");
        QueryBuilder matchSpecificFieldQuery = QueryBuilders
                .matchQuery(TITLE, eventTitle);
        QueryBuilder isAfterQuery = QueryBuilders.rangeQuery("date").gte(eventDate);
        QueryBuilder mustQuery = QueryBuilders.boolQuery().must(matchSpecificFieldQuery).must(isAfterQuery);
        final SearchSourceBuilder builder = new SearchSourceBuilder()
                .postFilter(mustQuery);
        final SearchRequest request = new SearchRequest(Indices.EVENT_INDEX);
        request.source(builder);

        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            if (response == null) {
                throw new ServiceException(String.format("can't find events with title %s and after %s", eventTitle, eventDate));
            }
            SearchHit[] searchHits = response.getHits().getHits();
            List<EventDto> eventDtos = Arrays.stream(searchHits)
                    .map(this::getEventDto)
                    .collect(Collectors.toList());
            log.info("find event by date and title end...");
            return eventDtos;
        } catch (final Exception e) {
            throw new ClientException(CLIENT_EXCEPTION);
        }
    }

    private EventDto getEventDto(SearchHit hit) {
        try {
            return mapper.readValue(hit.getSourceAsString(), EventDto.class);
        } catch (JsonProcessingException e) {
            throw new EventException("Mapping exception");
        }
    }
}
