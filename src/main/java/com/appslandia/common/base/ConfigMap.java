// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.base;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.SplitUtils;
import com.appslandia.common.utils.StringFormatUtils;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.common.utils.URLEncoding;
import com.appslandia.common.utils.URLUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ConfigMap extends MapWrapper<String, String> implements Config {
	private static final long serialVersionUID = 1L;

	public ConfigMap() {
		super(new HashMap<String, String>());
	}

	public ConfigMap(Map<String, String> newMap) {
		super(newMap);
	}

	@Override
	public String getString(String key) {
		return this.map.get(key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		String value = getString(key);
		return (value != null) ? value : defaultValue;
	}

	@Override
	public String getRequiredString(String key) throws IllegalStateException {
		String value = getString(key);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		return value;
	}

	@Override
	public String[] getStringArray(String key) {
		String value = getString(key);
		if (value == null) {
			return StringUtils.EMPTY_ARRAY;
		}
		List<String> list = SplitUtils.splitByComma(value);
		return !list.isEmpty() ? list.toArray(new String[list.size()]) : StringUtils.EMPTY_ARRAY;
	}

	@Override
	public String getFormatted(String key) {
		String value = getString(key);
		if (value == null) {
			return null;
		}
		Map<String, Object> m = ObjectUtils.cast(this);
		return StringFormatUtils.format(value, m);
	}

	@Override
	public String getRequiredFormatted(String key) throws IllegalStateException {
		String value = getString(key);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		Map<String, Object> m = ObjectUtils.cast(this);
		return StringFormatUtils.format(value, m);
	}

	@Override
	public String getFormatted(String key, Map<String, Object> parameters) {
		String value = getString(key);
		if (value == null) {
			return null;
		}
		return StringFormatUtils.format(value, parameters);
	}

	@Override
	public String getRequiredFormatted(String key, Map<String, Object> parameters) throws IllegalStateException {
		String value = getString(key);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		return StringFormatUtils.format(value, parameters);
	}

	@Override
	public String getFormatted(String key, Object... parameters) {
		String value = getString(key);
		if (value == null) {
			return null;
		}
		return StringFormatUtils.format(value, parameters);
	}

	@Override
	public String getRequiredFormatted(String key, Object... parameters) throws IllegalStateException {
		String value = getString(key);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		return StringFormatUtils.format(value, parameters);
	}

	@Override
	public boolean getBool(String key, boolean defaultValue) {
		String value = getString(key);
		if (value == null) {
			return defaultValue;
		}
		if (isTrueValue(value)) {
			return true;
		}
		if (isFalseValue(value)) {
			return false;
		}
		return defaultValue;
	}

	@Override
	public boolean getRequiredBool(String key) throws IllegalStateException, BoolFormatException {
		String value = getString(key);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		if (isTrueValue(value)) {
			return true;
		}
		if (isFalseValue(value)) {
			return false;
		}
		throw new BoolFormatException(value);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		String value = getString(key);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	@Override
	public int getRequiredInt(String key) throws IllegalStateException, NumberFormatException {
		String value = getString(key);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		return Integer.parseInt(value);
	}

	@Override
	public long getLong(String key, long defaultValue) {
		String value = getString(key);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	@Override
	public long getRequiredLong(String key) throws IllegalStateException, NumberFormatException {
		String value = getString(key);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		return Long.parseLong(value);
	}

	@Override
	public float getFloat(String key, float defaultValue) {
		String value = getString(key);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	@Override
	public float getRequiredFloat(String key) throws IllegalStateException, NumberFormatException {
		String value = getString(key);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		return Float.parseFloat(value);
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		String value = getString(key);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	@Override
	public double getRequiredDouble(String key) throws IllegalStateException, NumberFormatException {
		String value = getString(key);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		return Double.parseDouble(value);
	}

	@Override
	public Date getDate(String key) {
		String value = getString(key);
		if (value == null) {
			return null;
		}
		try {
			return DateUtils.iso8601Date(value);
		} catch (DateFormatException ex) {
			return null;
		}
	}

	@Override
	public java.sql.Date getRequiredDate(String key) throws IllegalStateException, DateFormatException {
		String value = getString(key);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		return DateUtils.iso8601Date(value);
	}

	@Override
	public Time getTime(String key) {
		String value = getString(key);
		if (value == null) {
			return null;
		}
		try {
			return DateUtils.iso8601Time(value);
		} catch (DateFormatException ex) {
			return null;
		}
	}

	@Override
	public Time getRequiredTime(String key) throws IllegalStateException, DateFormatException {
		String value = getString(key);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		return DateUtils.iso8601Time(value);
	}

	@Override
	public Timestamp getDateTime(String key) {
		String value = getString(key);
		if (value == null) {
			return null;
		}
		try {
			return DateUtils.iso8601DateTime(value);
		} catch (DateFormatException ex) {
			return null;
		}
	}

	@Override
	public java.sql.Timestamp getRequiredDateTime(String key) throws IllegalStateException, DateFormatException {
		String value = getString(key);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		return DateUtils.iso8601DateTime(value);
	}

	@Override
	public String get(Object key) {
		return getString((String) key);
	}

	protected String doPut(String key, String value) {
		return this.map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		for (Entry<? extends String, ? extends String> config : m.entrySet()) {
			doPut(config.getKey(), StringUtils.trimToNull(config.getValue()));
		}
	}

	@Override
	public String put(String key, String value) {
		return doPut(key, StringUtils.trimToNull(value));
	}

	public void put(String key, boolean value) {
		doPut(key, Boolean.toString(value));
	}

	public void put(String key, int value) {
		doPut(key, Integer.toString(value));
	}

	public void put(String key, long value) {
		doPut(key, Long.toString(value));
	}

	public void put(String key, float value) {
		doPut(key, Float.toString(value));
	}

	public void put(String key, double value) {
		doPut(key, Double.toString(value));
	}

	public void put(String key, Object value) {
		doPut(key, toStringValue(value));
	}

	protected String toStringValue(Object value) {
		if (value == null) {
			return null;
		}
		if (value.getClass() == String.class) {
			return StringUtils.trimToNull((String) value);
		}
		if (value instanceof java.sql.Date) {
			return DateUtils.iso8601Date((java.sql.Date) value);
		}
		if (value instanceof java.sql.Time) {
			return DateUtils.iso8601Time((java.sql.Time) value);
		}
		if (value instanceof java.sql.Timestamp) {
			return DateUtils.iso8601DateTime((java.sql.Timestamp) value);
		}
		return value.toString();
	}

	public String toQueryEncoded() {
		String q = URLUtils.toQueryParams(ObjectUtils.cast(this.map));
		return !q.isEmpty() ? q : null;
	}

	public static ConfigMap parse(String queryEncoded) throws IllegalArgumentException {
		ConfigMap map = new ConfigMap();
		if (queryEncoded == null) {
			return map;
		}
		List<String> configs = SplitUtils.split(queryEncoded, '&');
		for (String config : configs) {
			int idx = config.indexOf('=');
			if (idx <= 0)
				continue;

			String key = config.substring(0, idx).trim();
			if (!key.isEmpty()) {
				String value = config.substring(idx + 1).trim();
				map.put(URLEncoding.decodeParam(key), !value.isEmpty() ? URLEncoding.decodeParam(value) : null);
			}
		}
		return map;
	}

	public static boolean isTrueValue(String value) {
		return "true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value);
	}

	public static boolean isFalseValue(String value) {
		return "false".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value);
	}
}
