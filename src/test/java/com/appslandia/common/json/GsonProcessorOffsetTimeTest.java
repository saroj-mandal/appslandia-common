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

package com.appslandia.common.json;

import java.io.StringReader;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.Jdk8DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GsonProcessorOffsetTimeTest {

	@Test
	public void test() {
		GsonProcessor jsonProcessor = new GsonProcessor().setBuilder(Jdk8GsonUtils.newBuilder());

		TestModel fromM = new TestModel();
		fromM.timez = OffsetTime.now();
		fromM.datetimez = OffsetDateTime.now();

		TestModel toM = null;
		try {

			String jsonString = jsonProcessor.toString(fromM);
			toM = jsonProcessor.read(new StringReader(jsonString), TestModel.class);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		Assert.assertEquals(Jdk8DateUtils.iso8601TimeZ(toM.timez), Jdk8DateUtils.iso8601TimeZ(fromM.timez));
		Assert.assertEquals(Jdk8DateUtils.iso8601DateTimeZ(toM.datetimez), Jdk8DateUtils.iso8601DateTimeZ(fromM.datetimez));
	}

	static class TestModel {
		OffsetTime timez;
		OffsetDateTime datetimez;
	}
}
