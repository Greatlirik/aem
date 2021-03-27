package com.mysite25.core.models;

import java.util.List;

public class EventDTO {
    private long elementCount;

    private long pageCount;

    private long page;

    private List<com.mysite25.core.models.EventModel> events;

    public EventDTO(List<com.mysite25.core.models.EventModel> events, long page, long elementCount, long pageCount) {
        this.page = page;
        this.events = events;
        this.elementCount = elementCount;
        this.pageCount = pageCount;
    }

    public List<com.mysite25.core.models.EventModel> getEvents() {
        return events;
    }

    public long getElementCount() {
        return elementCount;
    }

    public long getPage() {
        return page;
    }

    public long getPageCount() {
        return pageCount;
    }
}
