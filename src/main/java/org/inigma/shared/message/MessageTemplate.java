package org.inigma.shared.message;

import java.util.Collection;

import org.inigma.shared.mongo.DBObjectWrapper;
import org.inigma.shared.mongo.MongoDaoTemplate;
import org.inigma.shared.mongo.MongoDataStore;
import org.springframework.beans.factory.annotation.Autowired;

import com.googlecode.ehcache.annotations.Cacheable;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

public class MessageTemplate extends MongoDaoTemplate<Message> {
    public MessageTemplate() {
        // for cache annotations to work
    }

    @Autowired
    public MessageTemplate(MongoDataStore mds) {
        super(mds, "message");
    }

    public MessageTemplate(MongoDataStore mds, String collection) {
        super(mds, collection);
    }

    @Override
    protected Message convert(DBObjectWrapper data) {
        Message message = new Message();
        message.setCode(data.getString("_id"));
        message.setValue(data.getString("value"));
        return message;
    }

    public Message delete(String code) {
        DBObject query = new BasicDBObject("_id", code);
        return convert(getCollection(false).findAndRemove(query));
    }

    @Cacheable(cacheName = "message.all")
    public Collection<Message> find() {
        return super.find();
    }

    @Cacheable(cacheName = "message")
    public Message findById(String code) {
        return super.findById(code);
    }

    public void save(Message message) {
        DBObject query = new BasicDBObject("_id", message.getCode());
        DBObject data = new BasicDBObject("value", message.getValue());
        data.put("modified", message.getModifiedDate());
        DBObject dataset = new BasicDBObject("$set", data);
        WriteResult result = getCollection(false).update(query, dataset, true, false, WriteConcern.SAFE);
        throwOnError(result);
    }
}
