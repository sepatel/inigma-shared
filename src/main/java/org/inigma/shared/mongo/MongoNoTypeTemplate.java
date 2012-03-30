package org.inigma.shared.mongo;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;

public class MongoNoTypeTemplate extends MongoTemplate {
    public MongoNoTypeTemplate(MongoDbFactory mongoDbFactory) {
        this(mongoDbFactory, null);
    }

    public MongoNoTypeTemplate(MongoDbFactory mongoDbFactory, MongoConverter mongoConverter) {
        super(mongoDbFactory, mongoConverter);
        MongoConverter converter = getConverter();
        if (converter instanceof MappingMongoConverter) {
            ((MappingMongoConverter) converter).setTypeMapper(new DefaultMongoTypeMapper(null));
        }
    }
}
