package com.mysite25.core.epam;

import java.util.List;

public class Row {

    private List<Cell> cells;

    public Row(List<Cell> cells) {
        this.cells = cells;
    }

    public List<Cell> getCells() {
        return cells;
    }
}
