package com.mysite25.core.services;


import com.mysite25.core.models.EventDTO;
import org.osgi.service.component.annotations.Component;

@Component(service = RenderService.class)
public class XmlRenderService implements RenderService {
    @Override
    public String transformObject(EventDTO eventDTO) {
        return "<xml/>";
    }
}
