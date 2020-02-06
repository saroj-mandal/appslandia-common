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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.CharsetUtils;
import com.appslandia.common.utils.IOUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class BOMInputStreamTest {

	@Test
	public void test() {
		byte[] d = "data".getBytes(CharsetUtils.ISO_8859_1);

		try (BOMInputStream bis = new BOMInputStream(new ByteArrayInputStream(d))) {
			byte[] dr = IOUtils.toByteArray(bis);

			Assert.assertArrayEquals(dr, d);

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_utf8() {
		byte[] d = "data".getBytes(CharsetUtils.UTF_8);
		byte[] bd = ArrayUtils.append(BOM.UTF_8.getBytes(), d);

		try (BOMInputStream bis = new BOMInputStream(new ByteArrayInputStream(bd))) {
			byte[] dr = IOUtils.toByteArray(bis);

			Assert.assertArrayEquals(dr, d);

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
