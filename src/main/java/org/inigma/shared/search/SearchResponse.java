package org.inigma.shared.search;

import java.util.List;

public class SearchResponse<T> {
    private final SearchCriteria criteria;
    private List<T> results;
    private long count;

    public SearchResponse(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    public long getCount() {
        return count;
    }

    public SearchCriteria getCriteria() {
        return criteria;
    }

    public List<T> getResults() {
        return results;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
