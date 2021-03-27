package com.mysite25.core.servlets;


import com.mysite25.core.epam.Cell;
import com.mysite25.core.models.Grid;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.Nonnull;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.apache.sling.api.servlets.HttpConstants.METHOD_POST;

@Component(service = Servlet.class)
@SlingServletPaths({"/bin/grid/refresh"})
@SlingServletResourceTypes(
        resourceTypes = "mysite25/components/structure/page",
        methods = METHOD_POST
)
public class GridServlet extends SlingAllMethodsServlet {

    private final Logger logger = LoggerFactory.getLogger(GridServlet.class);

    @Override
    protected void doPost(@Nonnull SlingHttpServletRequest request, @Nonnull SlingHttpServletResponse response) throws ServletException, IOException {

        final ResourceResolver resourceResolver = request.getResourceResolver();

        final String path = Optional.ofNullable(request.getRequestParameter("path"))
                .map(RequestParameter::getString)
                .orElse("/content");

        final Resource gridResource = resourceResolver.getResource(path);
        Optional.ofNullable(gridResource)
                .map(resource -> resource.adaptTo(Grid.class))
                .ifPresent(grid -> {
                    final List<String> cells = grid.getRows().stream()
                            .flatMap(row -> row.getCells().stream())
                            .map(Cell::getId)
                            .collect(Collectors.toList());

                    final Iterable<Resource> iterable = gridResource::listChildren;
                    StreamSupport.stream(iterable.spliterator(), false)
                            .filter(resource -> !cells.contains(resource.getName()))
                              .forEach( resource -> {
                                  try {
                                      resourceResolver.delete(resource);
                                  } catch (PersistenceException e) {
                                      e.printStackTrace();
                                  }
                              });
                });

        resourceResolver.commit();

    }

}
