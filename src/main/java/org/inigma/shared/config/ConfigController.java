package org.inigma.shared.config;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.inigma.shared.webapp.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConfigController extends BaseController {
    @Autowired
    private Configuration configuration;

    @RequestMapping(value = "/config/boolean/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Boolean getConfigBoolean(@PathVariable String key) {
        return configuration.getBoolean(key, null);
    }

    @RequestMapping(value = "/config/byte/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Byte getConfigByte(@PathVariable String key) {
        return configuration.getByte(key, null);
    }

    @RequestMapping(value = "/config/date/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Date getConfigDate(@PathVariable String key) {
        return configuration.getDate(key, null);
    }

    @RequestMapping(value = "/config/double/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Double getConfigDouble(@PathVariable String key) {
        return configuration.getDouble(key, null);
    }

    @RequestMapping(value = "/config/float/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Float getConfigFloat(@PathVariable String key) {
        return configuration.getFloat(key, null);
    }

    @RequestMapping(value = "/config/int/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Integer getConfigInt(@PathVariable String key) {
        return configuration.getInteger(key, null);
    }

    @RequestMapping(value = "/config/list/{key}", method = RequestMethod.GET)
    @ResponseBody
    public List<?> getConfigList(@PathVariable String key) {
        return configuration.getList(key, null);
    }

    @RequestMapping(value = "/config/long/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Long getConfigLong(@PathVariable String key) {
        return configuration.getLong(key, null);
    }

    @RequestMapping(value = "/config/map/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Map<?, ?> getConfigMap(@PathVariable String key) {
        return configuration.getMap(key, null);
    }

    @RequestMapping(value = "/config/string/{key}", method = RequestMethod.GET)
    @ResponseBody
    public String getConfigString(@PathVariable String key) {
        return configuration.getString(key, null);
    }

    @RequestMapping(value = "/config/boolean/{key}", method = RequestMethod.POST)
    @ResponseBody
    public boolean setConfigBoolean(@PathVariable String key, @RequestParam boolean value) {
        return configuration.set(key, value);
    }

    @RequestMapping(value = "/config/byte/{key}", method = RequestMethod.POST)
    @ResponseBody
    public boolean setConfigByte(@PathVariable String key, @RequestParam byte value) {
        return configuration.set(key, value);
    }

    @RequestMapping(value = "/config/date/{key}", method = RequestMethod.POST)
    @ResponseBody
    public boolean setConfigDate(@PathVariable String key, @RequestParam long value) {
        Date date = new Date(value);
        return configuration.set(key, date);
    }

    @RequestMapping(value = "/config/double/{key}", method = RequestMethod.POST)
    @ResponseBody
    public boolean setConfigDouble(@PathVariable String key, @RequestParam double value) {
        return configuration.set(key, value);
    }

    @RequestMapping(value = "/config/float/{key}", method = RequestMethod.POST)
    @ResponseBody
    public boolean setConfigFloat(@PathVariable String key, @RequestParam float value) {
        return configuration.set(key, value);
    }

    @RequestMapping(value = "/config/int/{key}", method = RequestMethod.POST)
    @ResponseBody
    public boolean setConfigInt(@PathVariable String key, @RequestParam int value) {
        return configuration.set(key, value);
    }

    @RequestMapping(value = "/config/list/{key}", method = RequestMethod.POST)
    @ResponseBody
    public boolean setConfigList(@PathVariable String key, @RequestParam List<Object> value) {
        return configuration.set(key, value);
    }

    @RequestMapping(value = "/config/long/{key}", method = RequestMethod.POST)
    @ResponseBody
    public boolean setConfigLong(@PathVariable String key, @RequestParam long value) {
        return configuration.set(key, value);
    }

    @RequestMapping(value = "/config/map/{key}", method = RequestMethod.POST)
    @ResponseBody
    public boolean setConfigMap(@PathVariable String key, HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Set<Entry<String, Object[]>> entries = request.getParameterMap().entrySet();
        Map<String, Object> map = new HashMap<String, Object>();
        for (Entry<String, Object[]> entry : entries) {
            if (entry.getValue() != null) {
                if (entry.getValue().length == 1) {
                    map.put(entry.getKey(), entry.getValue()[0]);
                } else {
                    map.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return configuration.set(key, map);
    }

    @RequestMapping(value = "/config/string/{key}", method = RequestMethod.POST)
    @ResponseBody
    public boolean setConfigString(@PathVariable String key, @RequestParam String value) {
        return configuration.set(key, value);
    }
}
