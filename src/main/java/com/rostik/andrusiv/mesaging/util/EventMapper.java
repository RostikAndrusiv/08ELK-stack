package com.rostik.andrusiv.mesaging.util;

import com.rostik.andrusiv.mesaging.servicedto.model.EventDto;
import com.rostik.andrusiv.mesaging.servicedto.model.EventEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventDto eventModelToDto(EventEntity eventEntity);

    EventEntity dtoToEventModel(EventDto eventDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerFromDto(EventDto dto, @MappingTarget EventEntity entity);
}
