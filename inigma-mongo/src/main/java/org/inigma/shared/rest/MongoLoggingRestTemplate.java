package org.inigma.shared.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 8/8/13 1:50 PM
 */
public class MongoLoggingRestTemplate extends LoggingRestTemplate implements WebServiceLogHandler {
    private MongoOperations mongo;

    @Autowired
    public MongoLoggingRestTemplate(MongoOperations mongo) {
        this.mongo = mongo;
        setHandler(this);
    }

    @Override
    public void onWebServiceLog(WebServiceLog log) {
        mongo.save(log);
    }

    public void setMongo(MongoOperations mongo) {
        this.mongo = mongo;
    }
}
