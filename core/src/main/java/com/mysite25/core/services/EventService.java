package com.mysite25.core.services;


import com.mysite25.core.models.EventDTO;

public interface EventService {
    EventDTO searchEvents(long page, long pageSize, String sort, boolean isAscendingOrder);
}
