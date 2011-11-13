package org.inigma.shared.config;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.inigma.shared.webapp.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConfigController extends BaseController {
    @Autowired
    private Configuration configuration;

    @RequestMapping(value = "/config/boolean/{key}", method = RequestMethod.GET)
    public void getConfigBoolean(@PathVariable String key, HttpServletResponse response) {
        response(response, configuration.getBoolean(key, null));
    }

    @RequestMapping(value = "/config/byte/{key}", method = RequestMethod.GET)
    public void getConfigByte(@PathVariable String key, HttpServletResponse response) {
        response(response, configuration.getByte(key, null));
    }

    @RequestMapping(value = "/config/date/{key}", method = RequestMethod.GET)
    public void getConfigDate(@PathVariable String key, HttpServletResponse response) {
        response(response, configuration.getDate(key, null));
    }

    @RequestMapping(value = "/config/double/{key}", method = RequestMethod.GET)
    public void getConfigDouble(@PathVariable String key, HttpServletResponse response) {
        response(response, configuration.getDouble(key, null));
    }

    @RequestMapping(value = "/config/float/{key}", method = RequestMethod.GET)
    public void getConfigFloat(@PathVariable String key, HttpServletResponse response) {
        response(response, configuration.getFloat(key, null));
    }

    @RequestMapping(value = "/config/int/{key}", method = RequestMethod.GET)
    public void getConfigInt(@PathVariable String key, HttpServletResponse response) {
        response(response, configuration.getInteger(key, null));
    }

    @RequestMapping(value = "/config/list/{key}", method = RequestMethod.GET)
    public void getConfigList(@PathVariable String key, HttpServletResponse response) {
        response(response, configuration.getList(key, null));
    }

    @RequestMapping(value = "/config/long/{key}", method = RequestMethod.GET)
    public void getConfigLong(@PathVariable String key, HttpServletResponse response) {
        response(response, configuration.getLong(key, null));
    }

    @RequestMapping(value = "/config/map/{key}", method = RequestMethod.GET)
    public void getConfigMap(@PathVariable String key, HttpServletResponse response) {
        response(response, configuration.getMap(key, null));
    }

    @RequestMapping(value = "/config/string/{key}", method = RequestMethod.GET)
    public void getConfigString(@PathVariable String key, HttpServletResponse response) {
        response(response, configuration.getString(key, null));
    }

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public void getConfigurations(HttpServletResponse response) {
        response(response, configuration.configs);
    }

    @RequestMapping(value = "/config/boolean/{key}", method = RequestMethod.POST)
    public void setConfigBoolean(@PathVariable String key, @RequestParam boolean value, HttpServletResponse response) {
        configuration.set(key, value);
        response(response, null);
    }

    @RequestMapping(value = "/config/byte/{key}", method = RequestMethod.POST)
    public void setConfigByte(@PathVariable String key, @RequestParam byte value, HttpServletResponse response) {
        configuration.set(key, value);
        response(response, null);
    }

    @RequestMapping(value = "/config/date/{key}", method = RequestMethod.POST)
    public void setConfigDate(@PathVariable String key, @RequestParam long value, HttpServletResponse response) {
        Date date = new Date(value);
        configuration.set(key, date);
        response(response, null);
    }

    @RequestMapping(value = "/config/double/{key}", method = RequestMethod.POST)
    public void setConfigDouble(@PathVariable String key, @RequestParam double value, HttpServletResponse response) {
        configuration.set(key, value);
        response(response, null);
    }

    @RequestMapping(value = "/config/float/{key}", method = RequestMethod.POST)
    public void setConfigFloat(@PathVariable String key, @RequestParam float value, HttpServletResponse response) {
        configuration.set(key, value);
        response(response, null);
    }

    @RequestMapping(value = "/config/int/{key}", method = RequestMethod.POST)
    public void setConfigInt(@PathVariable String key, @RequestParam int value, HttpServletResponse response) {
        configuration.set(key, value);
        response(response, null);
    }

    @RequestMapping(value = "/config/list/{key}", method = RequestMethod.POST)
    public void setConfigList(@PathVariable String key, @RequestParam List<Object> value, HttpServletResponse response) {
        configuration.set(key, value);
        response(response, null);
    }

    @RequestMapping(value = "/config/long/{key}", method = RequestMethod.POST)
    public void setConfigLong(@PathVariable String key, @RequestParam long value, HttpServletResponse response) {
        configuration.set(key, value);
        response(response, null);
    }

    @RequestMapping(value = "/config/map/{key}", method = RequestMethod.POST)
    public void setConfigMap(@PathVariable String key, HttpServletRequest request, HttpServletResponse response) {
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

        configuration.set(key, map);
        response(response, null);
    }

    @RequestMapping(value = "/config/string/{key}", method = RequestMethod.POST)
    public void setConfigString(@PathVariable String key, @RequestParam String value, HttpServletResponse response) {
        configuration.set(key, value);
        response(response, null);
    }
}
