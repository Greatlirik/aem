package com.mysite25.core.servlets;


import com.mysite25.core.models.EventDTO;
import com.mysite25.core.services.EventService;
import com.mysite25.core.services.RenderService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


import javax.annotation.Nonnull;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.Optional;


import static org.apache.sling.api.servlets.HttpConstants.METHOD_GET;

@Component(service = Servlet.class, immediate = true)
@SlingServletPaths({"/bin/data"})
@SlingServletResourceTypes(
        resourceTypes = "mysite25/components/structure/page",
        extensions = "json,xml",
        methods = METHOD_GET
)
public class EventServlet extends SlingAllMethodsServlet {

    @Reference
    private EventService eventService;

    @Reference
    private RenderService renderService;

    @Override
    protected void doGet(@Nonnull SlingHttpServletRequest request, @Nonnull SlingHttpServletResponse response) throws IOException {

        final ResourceResolver resourceResolver = request.getResourceResolver();

        final long pageSize = Optional.ofNullable(request.getParameter("size"))
                .map(Long::parseLong)
                .orElse(0L);

        final long page = Optional.ofNullable(request.getParameter("page"))
                .map(Long::parseLong)
                .orElse(1L);

        final String sort = Optional.ofNullable(request.getParameter("sort"))
                .orElse("text");

        final boolean ascendingOrder = Optional.ofNullable(request.getParameter("ascendingOrder"))
                .map(Boolean::parseBoolean)
                .orElse(true);

        final String extension = request.getRequestPathInfo().getExtension();

        EventDTO eventDTO = eventService.searchEvents(page, pageSize, sort, ascendingOrder);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(renderService.transformObject(eventDTO));

    }

}
