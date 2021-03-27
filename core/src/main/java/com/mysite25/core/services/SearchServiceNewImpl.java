package com.mysite25.core.services;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.mysite25.core.models.SearchItem;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import javax.jcr.query.Query;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.day.cq.wcm.api.NameConstants.NT_PAGE;


@Component(
        service = SearchServiceNew.class,
        immediate = true
)
public class SearchServiceNewImpl implements SearchServiceNew {

    private static final String SQL2_QUERY = "SELECT * FROM [cq:Page] AS page WHERE ISDESCENDANTNODE([%s]) and CONTAINS(page.*, '*%s*')";
    private static final String XPATH_QUERY = "/jcr:root%s//element(*, cq:Page)[jcr:contains(., '*%s*')]";
    private static final String PREDICATES = "Predicates";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    public List<SearchItem> performSearch(String path, String searchText, String searchEngine) {
        try {
            final ResourceResolver resolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            Iterator<Resource> resources = Collections.emptyIterator();
            switch (searchEngine) {
                case Query.JCR_SQL2:
                    resources = resolver.findResources(String.format(SQL2_QUERY, path, searchText), searchEngine);
                    break;
                case Query.XPATH:
                    resources = resolver.findResources(String.format(XPATH_QUERY, path, searchText), searchEngine);
                    break;
                case PREDICATES:
                    Map<String, String> map = new HashMap<>();
                    map.put("path", path);
                    map.put("type", NT_PAGE);
                    map.put("fulltext", "*" + searchText + "*");

                    com.day.cq.search.Query query = queryBuilder.createQuery(PredicateGroup.create(map), resolver.adaptTo(Session.class));

                    resources = query.getResult().getResources();
                    break;
            }
            final Iterator<Resource> resourceIterator = resources;
            final Iterable<Resource> iterable = () -> resourceIterator;
            return StreamSupport.stream(iterable.spliterator(), false)
                    .map(resource -> new SearchItem(resource.getName(), resource.getPath()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
