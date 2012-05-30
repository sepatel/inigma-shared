package org.inigma.shared.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Sort;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Component
public class SearchService {
    private static Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private MongoOperations mongo;

    public <T> SearchResponse<T> search(SearchCriteria<T> searchCriteria) {
        BasicDBObject sink = new BasicDBObject();
        MongoConverter converter = mongo.getConverter();
        converter.write(searchCriteria.getCriteria(), sink);

        Query query = new Query();
        if (searchCriteria.getRows() > 0) {
            query.limit(searchCriteria.getRows());
            query.skip(searchCriteria.getPage() * searchCriteria.getRows());
        }

        Field fields = query.fields();
        Sort sort = query.sort();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria);

        for (SearchField field : searchCriteria.getFields()) {
            String key = field.getKey();
            if (field.getSorting() != 0) {
                Order order = Order.ASCENDING;
                if (field.getSorting() < 0) {
                    order = Order.DESCENDING;
                }
                sort.on(key, order);
            }
            if (field.isVisible()) {
                fields.include(key);
            } else {
                fields.exclude(key);
            }

            Object value = getValue(sink, key);
            if (value != null) {
                switch (field.getOperation()) {
                case UNUSED:
                    break;
                case IS:
                    criteria.and(key).is(value);
                    break;
                case GT:
                    criteria.and(key).gt(value);
                    break;
                case GTE:
                    criteria.and(key).gte(value);
                    break;
                case LT:
                    criteria.and(key).lt(value);
                    break;
                case LTE:
                    criteria.and(key).lte(value);
                    break;
                case REGEX:
                    criteria.and(key).regex((String) value);
                    break;
                }
            }
        }

        SearchResponse<T> response = new SearchResponse<T>();
        response.setCriteria(searchCriteria);
        Class<T> clazz = (Class<T>) searchCriteria.getCriteria().getClass();
        response.setResults(mongo.find(query, clazz));
        return response;
    }
    
    public Object getValue(DBObject o, String key) {
        int dot = key.indexOf(".");
        if (dot > 0) {
            return getValue((DBObject) o.get(key.substring(0, dot)), key.substring(dot + 1));
        }
        return o.get(key);
    }

    public void setMongo(MongoOperations mongo) {
        this.mongo = mongo;
    }
}
