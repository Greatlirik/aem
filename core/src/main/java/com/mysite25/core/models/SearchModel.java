package com.mysite25.core.models;

import com.mysite25.core.services.SearchService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import java.util.Arrays;
import java.util.List;


@Model(adaptables = Resource.class)
public class SearchModel {

    @OSGiService
    private SearchService searchService;

    public List<String> getResults() {
        List<String> paths = Arrays.asList("/content/we-retail", "/content/dam");
        String text1 = "industry leadership";
        String text2 = "successful product";

        return searchService.find(text1, text2, paths);
    }
}
