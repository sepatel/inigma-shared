package org.inigma.shared.message;

import java.util.Collection;

import org.inigma.shared.mongo.DBObjectWrapper;
import org.inigma.shared.mongo.MongoDaoTemplate;
import org.inigma.shared.mongo.MongoDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;

public class MessageDaoTemplate extends MongoDaoTemplate<Message> {
    public MessageDaoTemplate() {
        this("message");
    }

    public MessageDaoTemplate(String collection) {
        super(collection, Message.class);
    }

    @CacheEvict("message")
    public Message delete(String code, String locale) {
        return mongo.findAndRemove(Query.query(null), template, collection);
//        return convert(getCollection(false).findAndRemove(createId(code, locale)));
    }

    @Cacheable("message.all")
    public Collection<Message> find() {
        return super.find();
    }

    @Cacheable("message")
    public Message findById(String code, String locale) {
        DBObject id = createId(code, locale);
        return convert(getCollection(true).findOne(new BasicDBObject("_id", id)));
    }

    public void save(Message message) {
        DBObject query = new BasicDBObject("_id", createId(message.getCode(), message.getLocale()));
        DBObject data = new BasicDBObject("value", message.getValue());
        DBObject dataset = new BasicDBObject("$set", data);
        getCollection(false).update(query, dataset, true, false, WriteConcern.SAFE);
    }

    private DBObject createId(String code, String locale) {
        DBObject id = new BasicDBObject("code", code);
        id.put("locale", locale);
        return id;
    }
}
