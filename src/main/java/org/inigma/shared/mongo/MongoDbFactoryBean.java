package org.inigma.shared.mongo;

import org.springframework.beans.factory.FactoryBean;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;

public class MongoDbFactoryBean implements FactoryBean<DB> {
    private MongoURI mongoUri;

    @Override
    public DB getObject() throws Exception {
        return new Mongo(mongoUri).getDB(mongoUri.getDatabase());
    }

    @Override
    public Class<?> getObjectType() {
        return Mongo.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
