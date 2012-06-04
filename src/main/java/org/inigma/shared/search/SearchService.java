package org.inigma.shared.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.AssociationHandler;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Sort;
import org.springframework.data.util.TypeInformation;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Component
public class SearchService {
    private static Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private MongoOperations mongo;

    private static class ScanningPropertyHandler implements PropertyHandler<MongoPersistentProperty>,
            AssociationHandler<MongoPersistentProperty> {
        private MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;
        private String prefix;
        private Collection<String> storage;

        public ScanningPropertyHandler(MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> map,
                Collection<String> storage) {
            this.mappingContext = map;
            this.storage = storage;
            this.prefix = "\"";
        }

        private ScanningPropertyHandler(
                MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> map, String propertyPrefix,
                Collection<String> storage) {
            this.mappingContext = map;
            this.storage = storage;
            this.prefix = propertyPrefix;
        }

        @Override
        public void doWithAssociation(Association<MongoPersistentProperty> association) {
            MongoPersistentProperty inverse = association.getInverse();
            if (inverse != null) {
                MongoPersistentEntity<?> entity = mappingContext.getPersistentEntity(inverse.getType());
                ScanningPropertyHandler propertyHandler = new ScanningPropertyHandler(mappingContext, prefix + inverse.getFieldName() + ".", storage);
                entity.doWithProperties(propertyHandler);
                entity.doWithAssociations(propertyHandler);
            }
        }

        @Override
        public void doWithPersistentProperty(MongoPersistentProperty persistentProperty) {
            boolean nested = false;
            String fieldName = persistentProperty.getFieldName();
            Iterable<? extends TypeInformation<?>> entityType = persistentProperty.getPersistentEntityType();
            for (TypeInformation<?> info : entityType) {
                MongoPersistentEntity<?> entity = mappingContext.getPersistentEntity(info.getType());
                ScanningPropertyHandler propertyHandler = new ScanningPropertyHandler(this.mappingContext, this.prefix
                        + fieldName + ".", storage);
                entity.doWithProperties(propertyHandler);
                entity.doWithAssociations(propertyHandler);
                nested = true;
            }
            if (!nested) {
                storage.add(prefix + fieldName + "\": 1");
            }
        }
    }

    public SearchCriteria createCriteria(Class<?> clazz) {
        List<String> fields = new ArrayList<String>();
        SearchCriteria criteria = new SearchCriteria();
        final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = mongo
                .getConverter().getMappingContext();

        MongoPersistentEntity<?> entity = mappingContext.getPersistentEntity(clazz);
        ScanningPropertyHandler propertyHandler = new ScanningPropertyHandler(mappingContext, fields);
        entity.doWithProperties(propertyHandler);
        entity.doWithAssociations(propertyHandler);
        criteria.setFields("{" + StringUtils.collectionToCommaDelimitedString(fields) + "}");

        return criteria;
    }

    public <T> SearchResponse<T> search(SearchCriteria criteria, Class<T> clazz) {
        DBObject sort = new BasicDBObject();
        if (criteria.getSort() != null) {
            sort = (DBObject) JSON.parse(criteria.getSort());
        }

        BasicQuery query = new BasicQuery(criteria.getQuery(), criteria.getFields());

        Sort s = query.sort();
        for (String key : sort.keySet()) {
            Integer direction = (Integer) sort.get(key);
            if (direction < 0) {
                s.on(key, Order.DESCENDING);
            } else if (direction > 0) {
                s.on(key, Order.ASCENDING);
            }
        }
        query.skip(criteria.getPage() * criteria.getRows());
        query.limit(criteria.getRows());

        SearchResponse<T> response = new SearchResponse<T>(criteria);
        response.setCount(mongo.count(query, clazz));
        response.setResults(mongo.find(query, clazz));
        return response;
    }

    public void setMongo(MongoOperations mongo) {
        this.mongo = mongo;
    }
}
