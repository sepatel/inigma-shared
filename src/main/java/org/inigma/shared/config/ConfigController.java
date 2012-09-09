package org.inigma.shared.config;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.map.ObjectMapper;
import org.inigma.shared.webapp.AjaxController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConfigController extends AjaxController {
    @Autowired
    private Configuration configuration;

    @RequestMapping(value = "/config/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Object getConfig(@PathVariable String key) {
        return configuration.get(key);
    }

    @RequestMapping(value = "/config/{key}/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Object getConfig(@PathVariable String key, @PathVariable String type) {
        return configuration.get(key, convertToClass(type));
    }

    @RequestMapping(value = "/config", method = RequestMethod.POST)
    @ResponseBody
    public Object setConfig(@RequestBody Map<String, Object> value) {
        for (Entry<String, Object> entry : value.entrySet()) {
            configuration.set(entry.getKey(), entry.getValue());
        }
        return value;
    }

    @RequestMapping(value = "/config/{key}/{type}", method = RequestMethod.POST)
    @ResponseBody
    public Object setConfig(@PathVariable String key, @PathVariable String type, @RequestBody String jsonValue)
            throws Exception {
        // TODO: Convert the request body into the desired object value type
        Object value = jsonValue;
        Class<?> clazz = convertToClass(type);
        if (ClassUtils.isPrimitiveOrWrapper(clazz)) {
            if (clazz == Boolean.class || clazz == boolean.class) {
                value = Boolean.parseBoolean(jsonValue);
            } else if (clazz == Byte.class || clazz == byte.class) {
                value = Byte.parseByte(jsonValue);
            } else if (clazz == Double.class || clazz == double.class) {
                value = Double.parseDouble(jsonValue);
            } else if (clazz == Float.class || clazz == float.class) {
                value = Float.parseFloat(jsonValue);
            } else if (clazz == Integer.class || clazz == int.class) {
                value = Integer.parseInt(jsonValue);
            } else if (clazz == Long.class || clazz == long.class) {
                value = Long.parseLong(jsonValue);
            } else if (clazz == Short.class || clazz == short.class) {
                value = Short.parseShort(jsonValue);
            } else {
                logger.warn("Missing definition for class {} with value {}", clazz, jsonValue);
            }
        } else {
            ObjectMapper om = new ObjectMapper();
            value = om.readValue(jsonValue, clazz);
        }
        return configuration.set(key, value);
    }

    private Class<?> convertToClass(String type) {
        if ("date".equalsIgnoreCase(type)) {
            return Date.class;
        } else if ("list".equalsIgnoreCase(type)) {
            return List.class;
        } else if ("map".equalsIgnoreCase(type)) {
            return Map.class;
        }
        return ClassUtils.resolveClassName(type, ClassUtils.getDefaultClassLoader());
    }
}
