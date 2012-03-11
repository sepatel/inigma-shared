package org.inigma.shared.config;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Configuration {
	boolean addObserver(ConfigurationObserver listener);

	Boolean getBoolean(String key);

	Boolean getBoolean(String key, Boolean defaultValue);

	Byte getByte(String key);

	Byte getByte(String key, Number defaultValue);

	Date getDate(String key);

	Date getDate(String key, Date defaultValue);

	Double getDouble(String key);

	Double getDouble(String key, Number defaultValue);

	Float getFloat(String key);

	Float getFloat(String key, Number defaultValue);

	Integer getInteger(String key);

	Integer getInteger(String key, Number defaultValue);

	Set<String> getKeys();

	<T> List<T> getList(String key);

	<T> List<T> getList(String key, List<T> defaultValue);

	Long getLong(String key);

	Long getLong(String key, Number defaultValue);

	<T> Map<String, T> getMap(String key);

	<T> Map<String, T> getMap(String key, Map<String, T> defaultValue);

	Short getShort(String key);

	Short getShort(String key, Number defaultValue);

	String getString(String key);

	String getString(String key, String defaultValue);

	Object remove(String key);

	boolean removeObserver(ConfigurationObserver listener);

	boolean set(String key, Object value);
}