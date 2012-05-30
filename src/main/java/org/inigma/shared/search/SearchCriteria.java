package org.inigma.shared.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchCriteria<T> {
    private int rows;
    private int page;
    private T criteria;
    private List<SearchField> fields = new ArrayList<SearchField>();

    public SearchCriteria() {
    }

    public SearchCriteria(T criteria, String... fields) {
        this.criteria = criteria;
        for (String field : fields) {
            addField(field);
        }
    }

    public void addField(String key) {
        SearchField field = new SearchField();
        field.setKey(key);
        this.fields.add(field);
    }

    public T getCriteria() {
        return criteria;
    }

    public SearchField getField(String key) {
        for (SearchField field : fields) {
            if (field.getKey().equals(key)) {
                return field;
            }
        }
        return null;
    }

    public List<SearchField> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public int getPage() {
        return page;
    }

    public int getRows() {
        return rows;
    }

    public void setCriteria(T criteria) {
        this.criteria = criteria;
    }

    public void setFields(List<SearchField> fields) {
        this.fields = fields;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
