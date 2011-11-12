package org.inigma.shared.mongo;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteConcern;

public class TestTemplate extends MongoDaoTemplate<SimpleBean> {
    @Autowired
    public TestTemplate(MongoDataStore pool) {
        super(pool, "simple");
    }

    protected SimpleBean convert(DBObjectWrapper data) {
        return convert(data, SimpleBean.class);
    }

    public void save(SimpleBean bean) {
        BasicDBObject data = new BasicDBObject("_id", bean.getId());
        data.put("age", bean.getBirthdate());
        data.put("name", bean.getName());
        data.put("rating", bean.getRating());
        data.put("weight", bean.getWeight());
        getCollection(false).save(data, WriteConcern.SAFE);
    }
}
