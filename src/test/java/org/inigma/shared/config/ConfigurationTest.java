package org.inigma.shared.config;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.inigma.shared.mongo.MongoDataStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/configuration.xml")
public class ConfigurationTest {
    private static class TestListener implements ConfigurationObserver {
        public Map<String, Object> originals = new HashMap<String, Object>();
        @Override
        public void onConfigurationUpdate(String key, Object original, Object current) {
            originals.put(key, original);
        }
    }

    @Autowired
    private Configuration config;
    @Autowired
    private MongoDataStore mds;
    
    @Test(expected=IllegalStateException.class)
    public void keyNotFound() {
        config.getString("noSuchKey");
    }

    @Test
    public void reload() {
        config.remove("reload");
        config.set("reload", "my original value");
        TestListener tl = new TestListener();
        config.addObserver(tl);
        DBCollection collection = mds.getCollection("config");
        BasicDBObject object = (BasicDBObject) collection.findOne(new BasicDBObject("_id", "reload"));
        assertEquals("my original value", object.getString("value"));
        object.put("value", "My NEW value");
        collection.save(object);
        assertFalse(tl.originals.containsKey("reload"));
        config.reload();
        assertTrue(tl.originals.containsKey("reload"));
        assertEquals("My NEW value", config.getString("reload"));
        config.removeObserver(tl);
    }

    @Test
    public void observer() {
        config.remove("silly");
        TestListener tl = new TestListener();
        assertTrue(config.addObserver(tl));
        assertFalse(tl.originals.containsKey("silly"));
        config.set("silly", "putty");
        assertTrue(tl.originals.containsKey("silly"));
        assertNull(tl.originals.get("silly"));
        config.set("silly", "rabbit");
        assertEquals("putty", tl.originals.get("silly"));
        assertTrue(config.removeObserver(tl));
    }

    @Test
    public void nativeDate() {
        config.remove("date");
        Date d = new Date();
        assertTrue(config.set("date", d));
        assertFalse(config.set("date", d));
        Date d2 = new Date(d.getTime() + 1L);
        assertTrue(config.set("date", d2));
        assertEquals(d2, config.getDate("date"));
    }

    @Test
    public void nativeList() {
        config.remove("list");
        List<String> names = new ArrayList<String>();
        names.add("Big");
        names.add("Fat");
        names.add("Burger");
        assertTrue(config.set("list", names));
        assertEquals(names, config.getList("list"));
    }

    @Test
    public void primitiveBoolean() {
        config.remove("boolean");
        assertTrue(config.set("boolean", true));
        assertFalse(config.set("boolean", true));
        assertTrue(config.set("boolean", false));
        assertFalse(config.set("boolean", false));
        assertEquals(false, config.remove("boolean"));
        assertNull(config.remove("boolean"));
    }

    @Test
    public void primitiveByte() {
        byte b = 42;
        config.remove("byte"); // just in case it exists already
        assertTrue(config.set("byte", b));
        assertFalse(config.set("byte", b));
        assertEquals((Object) b, config.getByte("byte"));
        try {
            config.getString("byte");
            fail("Should not be able to get a byte as a string");
        } catch(ClassCastException e) {
            // success
        }
        assertEquals(b, config.remove("byte"));
    }

    @Test
    public void removeNonExistantKey() {
        config.remove("noSuchKey");
    }
}
