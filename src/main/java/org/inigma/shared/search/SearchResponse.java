package org.inigma.shared.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchResponse {
    private final SearchCriteria criteria;
    private List<Map<?, ?>> results = new ArrayList<Map<?,?>>();

    public SearchResponse(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    public SearchCriteria getCriteria() {
        return criteria;
    }

    public List<Map<?, ?>> getResults() {
        return results;
    }

    public void addResult(Map<?, ?> result) {
        this.results.add(result);
    }

    public void setResults(List<Map<?, ?>> results) {
        this.results = results;
    }
}
