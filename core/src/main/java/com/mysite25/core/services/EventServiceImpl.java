package com.mysite25.core.services;


import com.mysite25.core.models.EventDTO;
import com.mysite25.core.models.EventModel;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Comparator.*;

@Component(service = EventService.class)
@Designate(ocd = EventServiceConfiguration.class)
public class EventServiceImpl implements EventService {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private EventServiceConfiguration configuration;

    private Map<String, Comparator<EventModel>> comparatorMap;
    private Comparator<EventModel> defaultComparator;

    @Activate
    @Modified
    public void activate(EventServiceConfiguration configuration) {
        this.configuration = configuration;

        comparatorMap = new HashMap<>();
        defaultComparator = comparing(EventModel::getText, String.CASE_INSENSITIVE_ORDER);
        comparatorMap.put("text", defaultComparator);
        comparatorMap.put("date", comparingLong(EventModel::getLongTime));
    }

    @Override
    public EventDTO searchEvents(long page, long pageSize, String sort, boolean isAscendingOrder) {
        page = page < 1 ? 1 : page;
//        final String path = configuration.getEventsContainerPath();
//        final String eventResourceType = configuration.getEventResourceType();
//        pageSize = pageSize > 0 ? pageSize : configuration.getPageSizeValue();

        final String path = "/content/events/jcr:content/parsys";
        final String eventResourceType = "mysite25/components/event";
        pageSize = 10;

        final List<EventModel> allEvents = Optional.ofNullable(resourceResolverFactory)
                .map(ResourceResolverFactory::getThreadResourceResolver)
                .map(resourceResolver -> resourceResolver.getResource(path))
                .map(Resource::listChildren)
                .map(iterator -> Spliterators.spliteratorUnknownSize(iterator, 0))
                .map(spliterator -> StreamSupport.stream(spliterator, false)
                        .filter(resource -> resource.isResourceType(eventResourceType))
                        .map(resource -> resource.adaptTo(EventModel.class))
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);


        final Comparator<EventModel> currentComparator = isAscendingOrder
                ? comparatorMap.getOrDefault(sort, defaultComparator)
                : comparatorMap.getOrDefault(sort, defaultComparator).reversed();

        final List<EventModel> events = allEvents
                .stream()
                .sorted(nullsLast(currentComparator))
                .skip((page - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        final int eventCount = allEvents.size();
        final long pageCount = (eventCount % pageSize == 0) ? eventCount / pageSize : eventCount / pageSize + 1;
        return new EventDTO(events, page, eventCount, pageCount);
    }
}
