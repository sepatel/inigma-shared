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

public class MessageDaoTemplate extends MongoDaoTemplate<Message> {
    public MessageDaoTemplate() {
        // for cache annotations to work
    }

    @Autowired
    public MessageDaoTemplate(MongoDataStore mds) {
        super(mds, "message");
    }

    public MessageDaoTemplate(MongoDataStore mds, String collection) {
        super(mds, collection);
    }

    public Message delete(String code, String locale) {
        return convert(getCollection(false).findAndRemove(createId(code, locale)));
    }

    @Cacheable(cacheName = "message.all")
    public Collection<Message> find() {
        return super.find();
    }

    @Cacheable(cacheName = "message")
    public Message findById(String code, String locale) {
        DBObject id = createId(code, locale);
        return convert(getCollection(true).findOne(id));
    }

    public void save(Message message) {
        DBObject query = new BasicDBObject("_id", createId(message.getCode(), message.getLocale()));
        DBObject data = new BasicDBObject("value", message.getValue());
        DBObject dataset = new BasicDBObject("$set", data);
        getCollection(false).update(query, dataset, true, false, WriteConcern.SAFE);
    }

    @Override
    protected Message convert(DBObjectWrapper data) {
        Message message = new Message();
        DBObjectWrapper document = data.getDocument("_id");
        message.setCode(document.getString("code"));
        message.setLocale(document.getString("locale"));
        message.setValue(data.getString("value"));
        return message;
    }

    private DBObject createId(String code, String locale) {
        DBObject id = new BasicDBObject("code", code);
        id.put("locale", locale);
        return id;
    }
}
