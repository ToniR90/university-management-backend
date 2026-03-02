package com.orientation.backend.users.domain.model.query;

public class Pagination {

    private final int page;
    private final int size;

    public Pagination(int page, int size) {
        if(page < 0){
            throw new IllegalArgumentException("El número de pàgina no pot ser negatiu");
        }
        this.page = page;
        if(size < 1 || size > 100){
            throw new IllegalArgumentException("Size ha de ser entre 1 i 100");
        }
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}
