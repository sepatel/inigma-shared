package org.inigma.shared.mongo;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;

public class MongoNoTypeTemplate extends MongoTemplate {
    public MongoNoTypeTemplate(MongoDbFactory mongoDbFactory) {
        super(mongoDbFactory, null);
        MongoConverter converter = getConverter();
        if (converter instanceof MappingMongoConverter) {
            ((MappingMongoConverter) converter).setTypeMapper(new DefaultMongoTypeMapper(null));
        }
    }
}
