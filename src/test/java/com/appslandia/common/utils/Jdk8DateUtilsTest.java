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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Jdk8DateUtilsTest {

	@Test
	public void test_parse_localDate() {
		try {
			LocalDate t = Jdk8DateUtils.iso8601Date("2017-11-02");
			String s = Jdk8DateUtils.iso8601Date(t);

			Assert.assertEquals(s, "2017-11-02");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_localTime() {
		try {
			LocalTime t = Jdk8DateUtils.iso8601Time("09:45:00.999");
			String s = Jdk8DateUtils.iso8601Time(t);

			Assert.assertEquals(s, "09:45:00.999");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_localDateTime() {
		try {
			LocalDateTime t = Jdk8DateUtils.iso8601DateTime("2017-11-02T09:45:00.999");
			String s = Jdk8DateUtils.iso8601DateTime(t);

			Assert.assertEquals(s, "2017-11-02T09:45:00.999");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_offsetTime() {
		try {
			OffsetTime t = Jdk8DateUtils.iso8601TimeZ("09:45:00.999-05:00");
			String s = Jdk8DateUtils.iso8601TimeZ(t);

			Assert.assertEquals(s, "09:45:00.999-05:00");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_offsetDateTime() {
		try {
			OffsetDateTime t = Jdk8DateUtils.iso8601DateTimeZ("2017-11-02T09:45:00.999-05:00");
			String s = Jdk8DateUtils.iso8601DateTimeZ(t);

			Assert.assertEquals(s, "2017-11-02T09:45:00.999-05:00");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

}
