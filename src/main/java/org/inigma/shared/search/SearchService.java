package org.inigma.shared.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Component
public class SearchService {
    private static Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private MongoOperations mongo;

    public Criteria convertDbObject(DBObject dbo) {
        Criteria criteria = new Criteria();
        for (String key : dbo.keySet()) {
            Object value = dbo.get(key);
            if (value instanceof DBObject) {
                DBObject sub = (DBObject) value;
                if (sub.get("$regex") != null) {
                    criteria.and(key).regex((String) sub.get("$regex"), (String) sub.get("$options"));
                } else if (sub.get("$gt") != null) {
                    criteria.and(key).gt(sub.get("$gt"));
                } else {
                    criteria.and(key).andOperator(convertDbObject(sub));
                }
            } else {
                criteria.and(key).is(value);
            }
        }
        return criteria;
    }

    public <T> SearchResponse<T> search(SearchCriteria criteria, Class<T> clazz) {
        DBObject query = new BasicDBObject();
        DBObject fields = new BasicDBObject();
        DBObject sort = new BasicDBObject();
        if (criteria.getQuery() != null) {
            query = (DBObject) JSON.parse(criteria.getQuery());
        }

        Criteria c = convertDbObject(query);
        Query q = Query.query(c);
        /*
        Field f = q.fields();
        for (String key : fields.keySet()) {
            // TODO: 0 = hide, 1 = show
        }
        */
        q.skip(criteria.getPage() * criteria.getRows());
        q.limit(criteria.getRows());

        SearchResponse<T> response = new SearchResponse<T>(criteria);
        response.setResults(mongo.find(q, clazz));
        return response;
    }

    public void setMongo(MongoOperations mongo) {
        this.mongo = mongo;
    }
}
