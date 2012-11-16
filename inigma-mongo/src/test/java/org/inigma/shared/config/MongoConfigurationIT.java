package org.inigma.shared.config;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.inigma.shared.test.CategoryClassRunner;
import org.inigma.shared.test.TestCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

@RunWith(CategoryClassRunner.class)
@ContextConfiguration(locations = { "/datastore.xml" })
@TestCategory("testMongo")
public class MongoConfigurationIT {
    private static class TestObject {
        public String name;
        public int age;
        public boolean alive;
    }

    @Autowired
    private MongoOperations mongo;
    private MongoConfiguration config;

    @Test
    public void readDate() {
        assertNotNull(config.getDate("test_date"));
    }

    @Test
    public void readList() {
        List<String> list = config.getList("test_list");
        assertNotNull(list);
        assertEquals(3, list.size());
        assertTrue(list.contains("Apple"));
        assertTrue(list.contains("Sauce"));
        assertTrue(list.contains("World Championship"));
    }

    @Test
    public void readMap() {
        Map<String, Object> map = config.getMap("test_map");
        assertNotNull(map);
        assertEquals(3, map.size());
        assertEquals("Sejal", map.get("name"));
        assertEquals(42, map.get("age"));
        assertEquals(true, map.get("alive"));
    }

    @Test
    public void readNumbers() {
        assertEquals(42, (int) config.getInteger("test_int"));
        assertEquals(42L, (long) config.getLong("test_long"));
        assertEquals(42.0f, config.getFloat("test_float"), 0);
        assertEquals(42.0, config.getDouble("test_double"), 0);
    }

    @Test
    public void readObject() {
        TestObject object = config.get("test_object", TestObject.class);
        assertNotNull(object);
        assertEquals("Sejal", object.name);
        assertEquals(42, object.age);
        assertEquals(true, object.alive);
    }

    @Test
    public void readString() {
        assertEquals("my string text", config.getString("test_string"));
    }
    
    @Test
    public void readObjectFromInitializedReadings() {
        config = new MongoConfiguration(mongo, "testConfig");
        readObject(); // rerun some tests.
        readMap();
        readList();
    }
    
    @Test
    public void writeThenReadDate() {
        Date now = new Date();
        config.set("test_current_date", now);
        config = new MongoConfiguration(mongo, "testConfig");
        Date date = config.getDate("test_current_date");
        assertNotNull(date);
        assertEquals(now.getTime(), date.getTime());
    }

    @Before
    public void setup() {
        DBCollection collection = mongo.getCollection("testConfig");
        collection.remove(new BasicDBObject()); // remove everything from config

        config = new MongoConfiguration(mongo, "testConfig");
        collection.insert(new BasicDBObject("_id", "test_int").append("value", 42));
        collection.insert(new BasicDBObject("_id", "test_string").append("value", "my string text"));
        collection.insert(new BasicDBObject("_id", "test_long").append("value", 42L));
        collection.insert(new BasicDBObject("_id", "test_float").append("value", 42.0f));
        collection.insert(new BasicDBObject("_id", "test_double").append("value", 42.0));
        BasicDBObject obj = new BasicDBObject("name", "Sejal").append("age", 42).append("alive", true);
        collection.insert(new BasicDBObject("_id", "test_map").append("value", obj));
        obj.append("_class", TestObject.class.getName());
        collection.insert(new BasicDBObject("_id", "test_object").append("value", obj));
        BasicDBList list = new BasicDBList();
        list.add("Apple");
        list.add("Sauce");
        list.add("World Championship");
        collection.insert(new BasicDBObject("_id", "test_list").append("value", list));
        collection.insert(new BasicDBObject("_id", "test_date").append("value", new Date()));
    }
}
