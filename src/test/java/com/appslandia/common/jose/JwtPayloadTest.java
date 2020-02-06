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

import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtPayloadTest {

	@Test
	public void test() {
		try {
			JwtPayload payload = new JwtPayload();

			payload.setIssuer("Issuer1");
			payload.setAudiences("Aud1", "Aud2");
			payload.setSubject("Sub1");
			payload.setJwtId("Id1");

			payload.setExpiresAt(DateUtils.iso8601DateTime("2118-11-02T10:00:00.999"));
			payload.setIssuedAt(DateUtils.iso8601DateTime("2017-11-02T10:00:00.999"));
			payload.setNotBefore(DateUtils.iso8601DateTime("2017-11-02T10:10:10.999"));

			Assert.assertEquals(payload.getIssuer(), "Issuer1");
			Assert.assertTrue((payload.getAudiences() != null) && payload.getAudiences().contains("Aud1"));
			Assert.assertEquals(payload.getSubject(), "Sub1");
			Assert.assertEquals(payload.getJwtId(), "Id1");

			Assert.assertEquals(payload.getExpiresAt().getTime(), DateUtils.iso8601DateTime("2118-11-02T10:00:00.000").getTime());
			Assert.assertEquals(payload.getIssuedAt().getTime(), DateUtils.iso8601DateTime("2017-11-02T10:00:00.000").getTime());
			Assert.assertEquals(payload.getNotBefore().getTime(), DateUtils.iso8601DateTime("2017-11-02T10:10:10.000").getTime());

		} catch (Exception ex) {
			Assert.fail();
		}
	}
}
