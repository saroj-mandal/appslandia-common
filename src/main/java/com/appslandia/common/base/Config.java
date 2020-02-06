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

import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public interface Config {

	String getString(String key);

	String getString(String key, String defaultValue);

	String getRequiredString(String key) throws IllegalStateException;

	String[] getStringArray(String key);

	String getFormatted(String key);

	String getRequiredFormatted(String key) throws IllegalStateException;

	String getFormatted(String key, Map<String, Object> parameters);

	String getRequiredFormatted(String key, Map<String, Object> parameters) throws IllegalStateException;

	String getFormatted(String key, Object... parameters);

	String getRequiredFormatted(String key, Object... parameters) throws IllegalStateException;

	boolean getBool(String key, boolean defaultValue);

	boolean getRequiredBool(String key) throws IllegalStateException, BoolFormatException;

	int getInt(String key, int defaultValue);

	int getRequiredInt(String key) throws IllegalStateException, NumberFormatException;

	long getLong(String key, long defaultValue);

	long getRequiredLong(String key) throws IllegalStateException, NumberFormatException;

	float getFloat(String key, float defaultValue);

	float getRequiredFloat(String key) throws IllegalStateException, NumberFormatException;

	double getDouble(String key, double defaultValue);

	double getRequiredDouble(String key) throws IllegalStateException, NumberFormatException;

	java.sql.Date getDate(String key);

	java.sql.Date getRequiredDate(String key) throws IllegalStateException, DateFormatException;

	java.sql.Time getTime(String key);

	java.sql.Time getRequiredTime(String key) throws IllegalStateException, DateFormatException;

	java.sql.Timestamp getDateTime(String key);

	java.sql.Timestamp getRequiredDateTime(String key) throws IllegalStateException, DateFormatException;
}
