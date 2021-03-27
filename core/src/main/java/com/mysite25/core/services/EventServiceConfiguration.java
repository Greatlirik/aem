package com.mysite25.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Event Service Configuration", description = "Event Service Configuration")
public @interface EventServiceConfiguration {

    @AttributeDefinition(name = "Page Size", description = "Page Size value", defaultValue = "10", type = AttributeType.LONG)
    long getPageSizeValue();

    @AttributeDefinition(name = "Events container path", description = "Events container path", defaultValue = "/content/events", type = AttributeType.STRING)
    String getEventsContainerPath();

    @AttributeDefinition(name = "Events resource type", description = "Events resource type", defaultValue = "mysite25/components/event", type = AttributeType.STRING)
    String getEventResourceType();
}
