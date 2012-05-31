package org.inigma.shared.search;

import java.util.List;

public class SearchResponse<T> {
    private final SearchCriteria criteria;
    private List<T> results;

    public SearchResponse(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    public SearchCriteria getCriteria() {
        return criteria;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
