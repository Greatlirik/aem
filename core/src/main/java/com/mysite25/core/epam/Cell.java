package com.mysite25.core.epam;

public class Cell {

    public static final String PREFIX_ID = Cell.class.getSimpleName().toLowerCase();
    private String id;

    public Cell(int row) {
        this.id = String.format("%s_%d", PREFIX_ID, row);
    }

    public String getId() {
        return id;
    }
}
