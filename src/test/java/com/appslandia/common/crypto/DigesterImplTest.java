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

package com.appslandia.common.crypto;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.base.ThreadSafeTester;
import com.appslandia.common.utils.CharsetUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DigesterImplTest {

	@Test
	public void test() {
		DigesterImpl impl = new DigesterImpl("MD5");
		try {
			byte[] data = "data".getBytes(CharsetUtils.UTF_8);
			byte[] hash = impl.digest(data);

			Assert.assertTrue(impl.verify(data, hash));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_copy() {
		DigesterImpl impl = new DigesterImpl("MD5");
		impl = impl.copy();
		try {
			byte[] data = "data".getBytes(CharsetUtils.UTF_8);
			byte[] hash = impl.digest(data);

			Assert.assertTrue(impl.verify(data, hash));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invalid() {
		DigesterImpl impl = new DigesterImpl("MD5");
		try {
			byte[] data = "data".getBytes(CharsetUtils.UTF_8);
			byte[] hash = impl.digest(data);

			byte[] modifiedData = "invalid".getBytes(CharsetUtils.UTF_8);
			Assert.assertFalse(impl.verify(modifiedData, hash));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_threadSafe() {
		final DigesterImpl impl = new DigesterImpl("MD5");

		new ThreadSafeTester() {

			@Override
			protected Runnable newTask() {
				return new Runnable() {

					@Override
					public void run() {
						try {
							byte[] data = "data".getBytes(CharsetUtils.UTF_8);
							byte[] hash = impl.digest(data);

							Assert.assertTrue(impl.verify(data, hash));

						} catch (Exception ex) {
							Assert.fail(ex.getMessage());

						} finally {
							countDown();
						}
					}
				};
			}
		}.executeThenAwait();
	}
}
