package org.inigma.shared.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Component
public class SearchService {
    private static Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private MongoOperations mongo;

    public SearchResponse search(SearchCriteria criteria, Class<?> clazz) {
        DBCollection collection = mongo.getCollection(mongo.getCollectionName(clazz));
        DBObject query = new BasicDBObject();
        DBObject fields = new BasicDBObject();
        DBObject sort = new BasicDBObject();
        if (criteria.getQuery() != null) {
           query = (DBObject) JSON.parse(criteria.getQuery());
        }
        DBCursor cursor = collection.find(query, fields).limit(criteria.getRows())
                .skip(criteria.getPage() * criteria.getRows()).sort(sort);
        SearchResponse response = new SearchResponse(criteria);
        for (DBObject result : cursor) {
            response.addResult(result.toMap());
        }
        return response;
    }

    public void setMongo(MongoOperations mongo) {
        this.mongo = mongo;
    }
}
