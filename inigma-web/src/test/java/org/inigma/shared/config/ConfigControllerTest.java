package org.inigma.shared.config;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.inigma.shared.jdbc.DataSourceConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/datastore.xml" })
public class ConfigControllerTest {
    @Autowired
    private ConfigController controller;

    @Test
    public void setPrimitiveBoolean() throws Exception {
        assertTrue((Boolean) controller.setConfig("test_boolean", "boolean", "true"));
        assertEquals(true, controller.getConfig("test_boolean", "boolean"));
        assertEquals(true, controller.getConfig("test_boolean"));
    }

    @Test
    public void setPrimitiveNumbers() throws Exception {
        assertTrue((Boolean) controller.setConfig("test_number", "short", "42"));
        assertEquals((short) 42, controller.getConfig("test_number", "short"));
        assertEquals((short) 42, controller.getConfig("test_number"));

        assertTrue((Boolean) controller.setConfig("test_number", "int", "" + (Short.MAX_VALUE + 1)));
        assertEquals(Short.MAX_VALUE + 1, controller.getConfig("test_number", "int"));
        assertEquals(Short.MAX_VALUE + 1, controller.getConfig("test_number"));

        assertTrue((Boolean) controller.setConfig("test_number", "long", "" + ((long) Integer.MAX_VALUE + 1)));
        assertEquals((long) Integer.MAX_VALUE + 1, controller.getConfig("test_number", "long"));
        assertEquals((long) Integer.MAX_VALUE + 1, controller.getConfig("test_number"));

        assertTrue((Boolean) controller.setConfig("test_number", "float", "3.14"));
        assertEquals(3.14f, controller.getConfig("test_number", "float"));
        assertEquals(3.14f, controller.getConfig("test_number"));

        assertTrue((Boolean) controller.setConfig("test_number", "double", "" + Math.PI));
        assertEquals(Math.PI, controller.getConfig("test_number", "double"));
        assertEquals(Math.PI, controller.getConfig("test_number"));
    }

    @Test
    public void setDbConfiguration() throws Exception {
        assertTrue((Boolean) controller.setConfig("test_db", DataSourceConfig.class.getName(),
                "{\"driver\": \"My Driver\"}"));
        DataSourceConfig dsc = (DataSourceConfig) controller.getConfig("test_db", DataSourceConfig.class.getName());
        assertNotNull(dsc);
        assertEquals("My Driver", dsc.getDriver());
        assertEquals(0, dsc.getMinSize());
        assertNull(dsc.getPassword());
    }

    @Test
    public void setListOfPrimitives() throws Exception {
        assertTrue((Boolean) controller.setConfig("test_list", "list", "[\"driver\", \"My Driver\"]"));
        List<?> list = (List<?>) controller.getConfig("test_list", "list");
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("driver", list.get(0));
        assertEquals("My Driver", list.get(1));
    }

    @Test
    public void setMapOfPrimitives() throws Exception {
        assertTrue((Boolean) controller.setConfig("test_map", "map", "{\"driver\": \"My Driver\"}"));
        Map<?, ?> map = (Map<?, ?>) controller.getConfig("test_map", "map");
        assertNotNull(map);
        assertEquals(1, map.size());
        assertEquals("My Driver", map.get("driver"));
    }

    @Test @Ignore
    public void getWithoutGetType() throws Exception {
        assertTrue((Boolean) controller.setConfig("test_string", "string", "Hello World"));
        assertEquals("Hello World", controller.getConfig("test_string"));
        long now = System.currentTimeMillis();
        assertTrue((Boolean) controller.setConfig("test_long", "long", "" + now));
        assertEquals(now, controller.getConfig("test_long"));
        assertTrue((Boolean) controller.setConfig("test_db", DataSourceConfig.class.getName(),
                "{\"driver\": \"My Driver\"}"));
        setListOfPrimitives();
        setMapOfPrimitives();
    }
}
