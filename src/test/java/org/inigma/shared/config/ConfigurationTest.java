package org.inigma.shared.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ConfigurationTest {
    private static class TestListener implements ConfigurationObserver {
        public Map<String, Object> originals = new HashMap<String, Object>();

        @Override
        public void onConfigurationUpdate(String key, Object original, Object current) {
            originals.put(key, original);
        }
    }

    private AbstractConfiguration config;

    @Test(expected = IllegalStateException.class)
    public void keyNotFound() {
        config.getString("noSuchKey");
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
        } catch (ClassCastException e) {
            // success
        }
        assertEquals(b, config.remove("byte"));
    }

    @Test
    public void reload() {
        config.remove("reload");
        config.set("reload", "my original value");
        TestListener tl = new TestListener();
        config.addObserver(tl);

        Map<String, Object> newconfigs = new HashMap<String, Object>();
        newconfigs.put("reload", "My NEW value");
        config.reload(newconfigs);
        assertTrue(tl.originals.containsKey("reload"));
        assertEquals("My NEW value", config.getString("reload"));
        config.removeObserver(tl);
    }

    @Test
    public void removeNonExistantKey() {
        config.remove("noSuchKey");
    }

    @Before
    public void setup() {
        config = new AbstractConfiguration() {
            @Override
            protected Object getValue(String key) {
                return null;
            }

            @Override
            protected void setValue(String key, Object value) {
            }
        };
    }
}
