package com.mysite25.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Model(adaptables = SlingHttpServletRequest.class)
public class Breadcrumb {

    @ScriptVariable
    private Page currentPage;

    private List<com.mysite25.core.models.BreadcrumbItem> items;

    public List<com.mysite25.core.models.BreadcrumbItem> getItems() {
        return items;
    }

    @PostConstruct
    private void init() {
        this.items = IntStream.range(0, currentPage.getDepth())
                .boxed()
                .map(currentPage::getAbsoluteParent)
                .filter(Objects::nonNull)
                .filter(this::checkToHidePage)
                .map(page -> new com.mysite25.core.models.BreadcrumbItem(page.getPath(), getPageTitle(page)))
                .collect(Collectors.toList());
    }

    private String getPageTitle(Page page) {
        return StringUtils.defaultIfEmpty(page.getNavigationTitle(), StringUtils.defaultIfEmpty(page.getPageTitle(), page.getTitle()));
    }

    private boolean checkToHidePage(Page page) {
        return !page.isHideInNav() || page.equals(currentPage);
    }
}
