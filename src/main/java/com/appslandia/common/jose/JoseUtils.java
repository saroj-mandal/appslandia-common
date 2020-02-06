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

package com.appslandia.common.jose;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JoseUtils {

	public static final String JOSE_PART_SEP = ".";
	private static final Pattern JOSE_PART_SEP_PATTERN = Pattern.compile("\\.");

	public static final String ALG_NONE = "none";

	public static String[] parseParts(String token) {
		AssertUtils.assertNotNull(token);
		String[] parts = JOSE_PART_SEP_PATTERN.split(token);

		if (parts.length == 2) {
			return token.endsWith(".") ? new String[] { parts[0], parts[1], null } : null;
		}

		if (parts.length != 3) {
			return null;
		}
		return parts;
	}

	// number of seconds from 1970-01-01T00:00:00Z UTC until the specified UTC date/time
	public static Long toNumericDate(Date value) {
		if (value == null) {
			return null;
		}
		return DateUtils.clearMs(value).getTime() / 1000;
	}

	public static Date toDate(Long numericDate) {
		if (numericDate == null) {
			return null;
		}
		return new Date(numericDate * 1000L);
	}

	public static Long toLongValue(Object value) {
		if ((value == null) || (value.getClass() == Long.class)) {
			return (Long) value;
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		throw new IllegalArgumentException("value must be instance of Number.");
	}

	public static Double toDoubleValue(Object value) {
		if ((value == null) || (value.getClass() == Double.class)) {
			return (Double) value;
		}
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		throw new IllegalArgumentException("value must be instance of Number.");
	}

	public static void convertToLong(Map<String, Object> map, Set<String> props) {
		for (String prop : props) {
			if (map.containsKey(prop)) {
				map.put(prop, JoseUtils.toLongValue(map.get(prop)));
			}
		}
	}
}
