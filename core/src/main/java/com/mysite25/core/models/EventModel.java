package com.mysite25.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EventModel implements Serializable {

    private static final String DATE_PATTERN = "dd MMMM yyyy HH:mm";

    private final transient SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);

    @Inject
    @Named("./date")
    private Date date;

    @Inject
    private String text;

    public String getDate() {
        return simpleDateFormat.format(date);
    }

    public long getLongTime() {
        return date.getTime();
    }

    public String getText() {
        return text;
    }

    @PostConstruct
    private void init() {
        date = Optional.ofNullable(date).orElse(new Date());
    }
}
