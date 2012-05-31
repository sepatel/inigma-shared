package org.inigma.shared.search;

public class SearchCriteria {
    private int rows = 100;
    private int page = 0;
    private String query = "{}";
    private String fields = "{}";
    private String sort = "{}";

    public String getFields() {
        return fields;
    }

    public int getPage() {
        return page;
    }

    public String getQuery() {
        return query;
    }

    public int getRows() {
        return rows;
    }

    public String getSort() {
        return sort;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
