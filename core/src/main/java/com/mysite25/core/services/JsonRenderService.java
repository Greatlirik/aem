package com.mysite25.core.services;


import com.google.gson.Gson;
import com.mysite25.core.models.EventDTO;
import org.osgi.service.component.annotations.Component;

@Component(service = RenderService.class)
public class JsonRenderService implements RenderService {
    @Override
    public String transformObject(EventDTO eventDTO) {
        return new Gson().toJson(eventDTO);
    }
}
