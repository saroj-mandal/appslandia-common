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

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.crypto.MacDigester;
import com.appslandia.common.json.GsonProcessor;
import com.appslandia.common.utils.CharsetUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwsProcessorTest {

	@Test
	public void test() {
		try {
			JwsProcessor processor = new JwsProcessor();
			processor.setJsonProcessor(new GsonProcessor());
			processor.setJoseSigner(new JoseSigner().setAlgorithm("HS256").setSigner(new MacDigester().setAlgorithm("HmacSHA256").setSecret("secret".getBytes())));

			JoseHeader header = processor.newHeader();
			final String payload = "This is a payload";

			String jws = processor.compact(new JwsToken(header, payload.getBytes(CharsetUtils.UTF_8)));
			Assert.assertNotNull(jws);

			JwsToken parsed = processor.parse(jws, false);
			Assert.assertNotNull(parsed);
			Assert.assertArrayEquals(parsed.getPayload(), payload.getBytes(CharsetUtils.UTF_8));

		} catch (Exception ex) {
			Assert.fail();
		}
	}

	@Test
	public void test_AlgNone() {
		try {
			JwsProcessor processor = new JwsProcessor();
			processor.setJsonProcessor(new GsonProcessor());
			processor.setJoseSigner(JoseSigner.NONE);

			JoseHeader header = processor.newHeader();
			final String payload = "This is a payload";

			String jws = processor.compact(new JwsToken(header, payload.getBytes(CharsetUtils.UTF_8)));
			Assert.assertNotNull(jws);

			JwsToken parsed = processor.parse(jws, false);
			Assert.assertNotNull(parsed);
			Assert.assertArrayEquals(parsed.getPayload(), payload.getBytes(CharsetUtils.UTF_8));

		} catch (Exception ex) {
			Assert.fail();
		}
	}

	@Test
	public void test_verify() {
		try {
			JwsProcessor processor = new JwsProcessor();
			processor.setJsonProcessor(new GsonProcessor());
			processor.setJoseSigner(new JoseSigner().setAlgorithm("HS256").setSigner(new MacDigester().setAlgorithm("HmacSHA256").setSecret("secret".getBytes())));

			JoseHeader header = processor.newHeader();
			final String payload = "This is a payload";

			String jws = processor.compact(new JwsToken(header, payload.getBytes(CharsetUtils.UTF_8)));
			Assert.assertNotNull(jws);

			JwsToken parsed = processor.parse(jws);
			Assert.assertNotNull(parsed);
			Assert.assertArrayEquals(parsed.getPayload(), payload.getBytes(CharsetUtils.UTF_8));

		} catch (Exception ex) {
			Assert.fail();
		}
	}
}
