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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtToken implements Serializable {
	private static final long serialVersionUID = 1L;

	final JoseHeader header;
	final JwtPayload payload;

	public JwtToken(JoseHeader header, JwtPayload payload) {
		this.header = AssertUtils.assertNotNull(header);
		this.payload = AssertUtils.assertNotNull(payload);
	}

	public JoseHeader getHeader() {
		return this.header;
	}

	public JwtPayload getPayload() {
		return this.payload;
	}

	public String getIssuer() {
		return this.payload.getIssuer();
	}

	public String getSubject() {
		return this.payload.getSubject();
	}

	public List<String> getAudiences() {
		return this.payload.getAudiences();
	}

	public Date getExpiresAt() {
		return this.payload.getExpiresAt();
	}

	public Date getNotBefore() {
		return this.payload.getNotBefore();
	}

	public Date getIssuedAt() {
		return this.payload.getIssuedAt();
	}

	public String getJwtId() {
		return this.payload.getJwtId();
	}

	public Long getLong(String key) {
		return this.payload.getLong(key);
	}

	public Long getRequiredLong(String key) throws IllegalStateException {
		return this.payload.getRequiredLong(key);
	}

	public Double getDouble(String key) {
		return this.payload.getDouble(key);
	}

	public Double getRequiredDouble(String key) throws IllegalStateException {
		return this.payload.getRequiredDouble(key);
	}

	public <T> T getObject(String key) {
		return this.payload.getObject(key);
	}

	public <T> T getRequiredObject(String key) throws IllegalStateException {
		return this.payload.getRequiredObject(key);
	}

	public Date getNumericDate(String key) {
		return this.payload.getNumericDate(key);
	}

	public Date getRequiredNumericDate(String key) throws IllegalStateException {
		return this.payload.getRequiredNumericDate(key);
	}
}
