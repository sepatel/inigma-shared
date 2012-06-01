package org.inigma.shared.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Sort;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Component
public class SearchService {
    private static Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private MongoOperations mongo;

    public <T> SearchResponse<T> search(SearchCriteria criteria, Class<T> clazz) {
        DBObject query = new BasicDBObject();
        DBObject fields = new BasicDBObject();
        DBObject sort = new BasicDBObject();
        if (criteria.getQuery() != null) {
            query = (DBObject) JSON.parse(criteria.getQuery());
        }
        if (criteria.getFields() != null) {
            fields = (DBObject) JSON.parse(criteria.getFields());
        }
        if (criteria.getSort() != null) {
            sort = (DBObject) JSON.parse(criteria.getSort());
        }

        Criteria c = new Criteria();
        for (String key : query.keySet()) {
            c.and(key).is(query.get(key));
        }
        Query q = Query.query(c);
        Field f = q.fields();
        for (String key : fields.keySet()) {
            if (fields.get(key).equals(1)) {
                f.include(key);
            } else if (fields.get(key).equals(0)) {
                f.exclude(key);
            } else {
                logger.error("Not sure what field {} is exactly but think it is {}", fields.get(key), fields.get(key)
                        .getClass());
            }
        }
        Sort s = q.sort();
        for (String key : sort.keySet()) {
            Integer direction = (Integer) sort.get(key);
            if (direction < 0) {
                s.on(key, Order.DESCENDING);
            } else if (direction > 0) {
                s.on(key, Order.ASCENDING);
            }
        }
        q.skip(criteria.getPage() * criteria.getRows());
        q.limit(criteria.getRows());

        SearchResponse<T> response = new SearchResponse<T>(criteria);
        response.setCount(mongo.count(q, clazz));
        response.setResults(mongo.find(q, clazz));
        return response;
    }

    public void setMongo(MongoOperations mongo) {
        this.mongo = mongo;
    }
}
