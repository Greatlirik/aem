package com.mysite25.core.services;


import com.day.cq.search.QueryBuilder;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import javax.jcr.query.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component(
        service = SearchService.class,
        immediate = true,
        property = {
                "version=5"
        }
)
public class SearchServiceImpl implements SearchService {

    @Reference
    private QueryBuilder queryBuilder;



    @Reference
    private SlingRepository slingRepository;


    private Session session;

    @Activate
    public void activate(ComponentContext context) throws Exception {
//        this.session = Optional
//                .of(resolverFactory.getAdministrativeResourceResolver(null))
//                .map(resolver -> resolver.adaptTo(Session.class))
//                .orElseThrow(IllegalStateException::new);
        this.session = Optional
                .ofNullable(slingRepository.loginAdministrative(slingRepository.getDefaultWorkspace()))
                .orElseThrow(IllegalStateException::new);

    }

    @Deactivate
    public void deactivate() {
        if (session != null) {
            session.logout();
        }
    }

    private QueryManager getQueryManager(Session session) {
        try {
            return Optional
                    .ofNullable(session)
                    .map(Session::getWorkspace)
                    .map(workspace -> {
                        try {
                            return workspace.getQueryManager();
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    })
                    .orElseThrow(IllegalStateException::new);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    @Override
    public List<String> find(String text1, String text2, List<String> paths) {
        List<String> result = new ArrayList<>();
        try {
            String query = String.format(
                    "SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([%s]) and CONTAINS(s.*, '%s') OR CONTAINS(s.*, '%s')",
                    String.join(",", paths),
                    text1,
                    text2
            );

            QueryManager queryManager = getQueryManager(session);
            QueryResult queryResult = queryManager.createQuery(query, Query.JCR_SQL2).execute();
            for (RowIterator it = queryResult.getRows(); it.hasNext(); ) {
                Row row = it.nextRow();
                result.add(row.getPath());
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return result;
    }
}
