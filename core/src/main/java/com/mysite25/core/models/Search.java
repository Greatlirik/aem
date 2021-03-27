package com.mysite25.core.models;

import com.day.cq.wcm.api.Page;
import com.mysite25.core.services.SearchServiceNew;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Search {

    @OSGiService
    private SearchServiceNew searchService;

    @Inject
    @Named("./searchEngine")
    @Default(values = "xpath")
    @Via("resource")
    private String searchEngine;


    @SlingObject
    private SlingHttpServletRequest slingHttpServletRequest;

    @ScriptVariable
    private Page currentPage;

    public String getSearchEngine() {
        return searchEngine;
    }

    public List<SearchItem> getItems() {
        return searchService.performSearch(currentPage.getPath(), slingHttpServletRequest.getParameter("search"), searchEngine);
    }

}
