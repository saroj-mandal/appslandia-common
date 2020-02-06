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

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PropertyConfigTest {

	@Test
	public void test() {
		PropertyConfig config = new PropertyConfig();

		config.put("stringConfig", "data");
		config.put("boolConfig", true);
		config.put("intConfig", Integer.MAX_VALUE);
		config.put("longConfig", Long.MAX_VALUE);

		config.put("floatConfig", 12.345f);
		config.put("doubleConfig", 12.345);

		config.put("dateConfig", DateUtils.iso8601Date("2000-01-30"));
		config.put("timeConfig", DateUtils.iso8601Time("12:00:00.000"));
		config.put("dateTimeConfig", DateUtils.iso8601DateTime("2000-01-30T12:00:00.000"));

		Assert.assertNotNull(config.getString("stringConfig"));
		Assert.assertNotNull(config.getString("boolConfig"));
		Assert.assertNotNull(config.getString("intConfig"));
		Assert.assertNotNull(config.getString("longConfig"));

		Assert.assertNotNull(config.getString("floatConfig"));
		Assert.assertNotNull(config.getString("doubleConfig"));

		Assert.assertNotNull(config.getString("dateConfig"));
		Assert.assertNotNull(config.getString("timeConfig"));
		Assert.assertNotNull(config.getString("dateTimeConfig"));
	}

	@Test
	public void test_assertEquals() {
		PropertyConfig config = new PropertyConfig();

		config.put("stringConfig", "data");
		config.put("boolConfig", true);
		config.put("intConfig", Integer.MAX_VALUE);
		config.put("longConfig", Long.MAX_VALUE);

		config.put("floatConfig", 12.345f);
		config.put("doubleConfig", 12.345);

		config.put("dateConfig", DateUtils.iso8601Date("2000-01-30"));
		config.put("timeConfig", DateUtils.iso8601Time("12:00:00.000"));
		config.put("dateTimeConfig", DateUtils.iso8601DateTime("2000-01-30T12:00:00.000"));

		try {
			Assert.assertEquals(config.getRequiredString("stringConfig"), "data");
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Assert.assertEquals(config.getRequiredBool("boolConfig"), true);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Assert.assertEquals(config.getRequiredInt("intConfig"), Integer.MAX_VALUE);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Assert.assertEquals(config.getRequiredLong("longConfig"), Long.MAX_VALUE);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Assert.assertTrue(config.getRequiredFloat("floatConfig") == 12.345f);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Assert.assertTrue(config.getRequiredDouble("doubleConfig") == 12.345);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Assert.assertEquals(config.getRequiredDate("dateConfig"), DateUtils.iso8601Date("2000-01-30"));
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Assert.assertEquals(config.getRequiredTime("timeConfig"), DateUtils.iso8601Time("12:00:00.000"));
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Assert.assertEquals(config.getRequiredDateTime("dateTimeConfig"), DateUtils.iso8601DateTime("2000-01-30T12:00:00.000"));
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_assertNumbers() {
		PropertyConfig config = new PropertyConfig();

		config.put("intConfig", Integer.MAX_VALUE);
		config.put("longConfig", Long.MAX_VALUE);

		config.put("floatConfig", 12.345f);
		config.put("doubleConfig", 12.345);

		try {
			Assert.assertEquals(config.getRequiredInt("intConfig"), Integer.MAX_VALUE);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Assert.assertEquals(config.getRequiredLong("longConfig"), Long.MAX_VALUE);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Assert.assertTrue(config.getRequiredFloat("floatConfig") == 12.345f);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Assert.assertTrue(config.getRequiredDouble("doubleConfig") == 12.345);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_getFormatted() {
		PropertyConfig config = new PropertyConfig();
		config.put("contextPath", "/app");
		config.put("authUrl", "{contextPath}/auth.html");

		String authUrl = config.getFormatted("authUrl");
		Assert.assertEquals(authUrl, "/app/auth.html");
	}

	@Test
	public void test_getFormatted_namedParameters() {
		PropertyConfig config = new PropertyConfig();
		config.put("authUrl", "{contextPath}/auth.html");

		String authUrl = config.getFormatted("authUrl", new Params().put("contextPath", "/app"));
		Assert.assertEquals(authUrl, "/app/auth.html");
	}

	@Test
	public void test_getFormatted_indexParameters() {
		PropertyConfig config = new PropertyConfig();
		config.put("authUrl", "{0}/auth.html");

		String authUrl = config.getFormatted("authUrl", "/app");
		Assert.assertEquals(authUrl, "/app/auth.html");
	}

	@Test
	public void test_getRequiredFormatted() {
		PropertyConfig config = new PropertyConfig();
		config.put("contextPath", "/app");
		config.put("authUrl", "{contextPath}/auth.html");

		String authUrl = config.getRequiredFormatted("authUrl");
		Assert.assertEquals(authUrl, "/app/auth.html");
	}

	@Test
	public void test_getRequiredFormatted_namedParameters() {
		PropertyConfig config = new PropertyConfig();
		config.put("authUrl", "{contextPath}/auth.html");

		String authUrl = config.getRequiredFormatted("authUrl", new Params().put("contextPath", "/app"));
		Assert.assertEquals(authUrl, "/app/auth.html");
	}

	@Test
	public void test_getRequiredFormatted_indexParameters() {
		PropertyConfig config = new PropertyConfig();
		config.put("authUrl", "{0}/auth.html");

		String authUrl = config.getRequiredFormatted("authUrl", "/app");
		Assert.assertEquals(authUrl, "/app/auth.html");
	}
}
