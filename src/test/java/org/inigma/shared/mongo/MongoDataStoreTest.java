package org.inigma.shared.mongo;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.Bytes;
import com.mongodb.DB;
import com.mongodb.DBCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/data-store.xml")
public class MongoDataStoreTest {
    private static final Random RAND = new SecureRandom();

    @Autowired
    private TestTemplate template;

    @Test
    public void getCollection() {
        DBCollection collection = template.getCollection(false);
        assertEquals("simple", collection.getName());

        int slaveCheck = collection.getOptions() & Bytes.QUERYOPTION_SLAVEOK;
        assertFalse(slaveCheck == Bytes.QUERYOPTION_SLAVEOK);

        collection = template.getCollection(true);
        slaveCheck = collection.getOptions() & Bytes.QUERYOPTION_SLAVEOK;
        assertTrue(slaveCheck == Bytes.QUERYOPTION_SLAVEOK);

        collection = template.getCollection();
        slaveCheck = collection.getOptions() & Bytes.QUERYOPTION_SLAVEOK;
        assertTrue(slaveCheck == Bytes.QUERYOPTION_SLAVEOK);
    }

    @Test
    public void getDb() {
        DB db = template.getCollection().getDB();
        assertEquals("junit", db.getName());
    }

    @Test
    public void readWriteData() {
        Date date = new Date();
        SimpleBean bean = new SimpleBean();
        bean.setAlive(true);
        bean.setBirthdate(date);
        bean.setName("Special Name");
        bean.setRating(RAND.nextFloat());
        bean.setWeight(RAND.nextInt(500));
        bean.setId(template.generateId());

        assertNull(template.findById(bean.getId()));
        template.save(bean);
        assertNotNull(template.findById(bean.getId()));
    }
}
