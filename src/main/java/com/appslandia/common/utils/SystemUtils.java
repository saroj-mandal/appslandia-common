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

package com.appslandia.common.utils;

import com.appslandia.common.base.Compat;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SystemUtils {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	@Compat
	public static String lineSeparator() {
		return LINE_SEPARATOR;
	}

	public static String getRequiredProp(String key) throws IllegalStateException {
		String value = getProp(key, null);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		return value;
	}

	public static String getProp(String key) {
		return getProp(key, null);
	}

	public static String getProp(String key, String defaultValue) {
		String value = System.getProperty(key);

		if (StringUtils.isNullOrEmpty(value)) {
			value = StringUtils.trimToNull(System.getenv(key));
		}
		return (value == null) ? defaultValue : value;
	}

	public static String getRequiredArg(String key) throws IllegalStateException {
		String value = getArg(key, null);
		if (value == null) {
			throw new IllegalStateException(key + " required.");
		}
		return value;
	}

	public static String getArg(String key) {
		return getArg(key, null);
	}

	public static String getArg(String key, String defaultValue) {
		String value = System.getProperty(key);
		return StringUtils.isNullOrEmpty(value) ? defaultValue : value;
	}

	public static int getIntArg(String key, int defaultValue) {
		String value = System.getProperty(key);
		try {
			return StringUtils.isNullOrEmpty(value) ? defaultValue : Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	public static long getLongArg(String key, long defaultValue) {
		String value = System.getProperty(key);
		try {
			return StringUtils.isNullOrEmpty(value) ? defaultValue : Long.parseLong(value);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	public static boolean getBoolArg(String key, boolean defaultValue) {
		String value = System.getProperty(key);
		try {
			return StringUtils.isNullOrEmpty(value) ? defaultValue : Boolean.parseBoolean(value);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}
}
