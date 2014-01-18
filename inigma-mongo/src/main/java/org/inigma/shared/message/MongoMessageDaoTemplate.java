package org.inigma.shared.message;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongoMessageDaoTemplate implements MessageDaoTemplate {
    @Autowired
    protected MongoOperations mongo;
    private String collection;

    public MongoMessageDaoTemplate() {
        this("message");
    }

    public MongoMessageDaoTemplate(String collection) {
        this.collection = collection;
    }

    @CacheEvict("message")
    public Message delete(String code, String locale) {
        return mongo.findAndRemove(Query.query(getIdCriteria(code, locale)), Message.class, collection);
    }

    @Cacheable("message.all")
    public Collection<Message> find() {
        return mongo.findAll(Message.class);
    }

    @Cacheable("message")
    public Message findById(String code, String locale) {
        return mongo.findOne(Query.query(getIdCriteria(code, locale)), Message.class, collection);
    }

    public void save(Message message) {
        mongo.save(message, collection); // TODO: Write concern journal safe
    }

    private Criteria getIdCriteria(String code, String locale) {
        return Criteria.where("code").is(code).and("locale").is(locale);
    }
}
