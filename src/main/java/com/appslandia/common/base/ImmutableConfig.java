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
import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ImmutableConfig implements Config {

	final Config config;

	public ImmutableConfig(Config config) {
		this.config = config;
	}

	@Override
	public String getString(String key) {
		return this.config.getString(key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		return this.config.getString(key, defaultValue);
	}

	@Override
	public String getRequiredString(String key) throws IllegalStateException {
		return this.config.getRequiredString(key);
	}

	@Override
	public String[] getStringArray(String key) {
		return this.config.getStringArray(key);
	}

	@Override
	public String getFormatted(String key) {
		return this.config.getFormatted(key);
	}

	@Override
	public String getRequiredFormatted(String key) throws IllegalStateException {
		return this.config.getRequiredFormatted(key);
	}

	@Override
	public String getFormatted(String key, Map<String, Object> parameters) {
		return this.config.getFormatted(key, parameters);
	}

	@Override
	public String getRequiredFormatted(String key, Map<String, Object> parameters) throws IllegalStateException {
		return this.config.getRequiredFormatted(key, parameters);
	}

	@Override
	public String getFormatted(String key, Object... parameters) {
		return this.config.getFormatted(key, parameters);
	}

	@Override
	public String getRequiredFormatted(String key, Object... parameters) throws IllegalStateException {
		return this.config.getRequiredFormatted(key, parameters);
	}

	@Override
	public boolean getBool(String key, boolean defaultValue) {
		return this.config.getBool(key, defaultValue);
	}

	@Override
	public boolean getRequiredBool(String key) throws IllegalStateException, BoolFormatException {
		return this.config.getRequiredBool(key);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		return this.config.getInt(key, defaultValue);
	}

	@Override
	public int getRequiredInt(String key) throws IllegalStateException, NumberFormatException {
		return this.config.getRequiredInt(key);
	}

	@Override
	public long getLong(String key, long defaultValue) {
		return this.config.getLong(key, defaultValue);
	}

	@Override
	public long getRequiredLong(String key) throws IllegalStateException, NumberFormatException {
		return this.config.getRequiredLong(key);
	}

	@Override
	public float getFloat(String key, float defaultValue) {
		return this.config.getFloat(key, defaultValue);
	}

	@Override
	public float getRequiredFloat(String key) throws IllegalStateException, NumberFormatException {
		return this.config.getRequiredFloat(key);
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		return this.config.getDouble(key, defaultValue);
	}

	@Override
	public double getRequiredDouble(String key) throws IllegalStateException, NumberFormatException {
		return this.config.getRequiredDouble(key);
	}

	@Override
	public Date getDate(String key) {
		return this.config.getDate(key);
	}

	@Override
	public Date getRequiredDate(String key) throws IllegalStateException, DateFormatException {
		return this.config.getRequiredDate(key);
	}

	@Override
	public Time getTime(String key) {
		return this.config.getTime(key);
	}

	@Override
	public Time getRequiredTime(String key) throws IllegalStateException, DateFormatException {
		return this.config.getTime(key);
	}

	@Override
	public Timestamp getDateTime(String key) {
		return this.config.getDateTime(key);
	}

	@Override
	public Timestamp getRequiredDateTime(String key) throws IllegalStateException, DateFormatException {
		return this.config.getRequiredDateTime(key);
	}
}
