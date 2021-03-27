package com.mysite25.core.services;


import com.mysite25.core.models.EventDTO;

public interface RenderService {
    String transformObject(EventDTO eventDTO);
}
