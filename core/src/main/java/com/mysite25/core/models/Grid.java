package com.mysite25.core.models;

import com.mysite25.core.epam.Cell;
import com.mysite25.core.epam.Row;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Model(adaptables = {Resource.class})
public class Grid {

    @Inject
    @Named("./columns")
    private int columnsCount;

    @Inject
    @Named("./rows")
    private int rowsCount;

    private List<Row> rows;

    public List<Row> getRows() {
        return rows;
    }

    @PostConstruct
    protected void init() {
        AtomicInteger counter = new AtomicInteger(0);
        this.rows = IntStream.range(0, rowsCount)
                .boxed()
                .map(rowIndex -> IntStream.range(0, columnsCount)
                        .boxed()
                        .map(columnIndex -> new Cell(counter.getAndIncrement()))
                        .collect(Collectors.toList()))
                .map(Row::new)
                .collect(Collectors.toList());
    }

}
