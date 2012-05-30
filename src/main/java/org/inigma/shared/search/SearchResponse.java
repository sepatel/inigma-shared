package org.inigma.shared.search;

import java.util.List;

public class SearchResponse<T> {
    private SearchCriteria<T> criteria;
    private List<T> results;

    public SearchCriteria<T> getCriteria() {
        return criteria;
    }

    public List<T> getResults() {
        return results;
    }

    public void setCriteria(SearchCriteria<T> criteria) {
        this.criteria = criteria;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
