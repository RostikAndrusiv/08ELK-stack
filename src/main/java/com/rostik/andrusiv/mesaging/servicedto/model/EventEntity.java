package com.rostik.andrusiv.mesaging.servicedto.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "event")
@Setting(settingPath = "static/es-settings.json")
public class EventEntity {
    //mappings and index are configured manually. Annotations here is just for example in case of
    //spring-data-elastic-search repository implementation will be added (nope :) )
    @Id
    @Field(type = FieldType.Keyword)
    private String eventId;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String place;
    @Field(type = FieldType.Text)
    private String speaker;
    @Field(type = FieldType.Text)
    private EventType eventType;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Nested, includeInParent = true)
    private List<String> subTopics;
    @Field(
            type = FieldType.Date,
            store = true,
            format = DateFormat.date_hour_minute_second
    )
    private LocalDateTime date;
}
