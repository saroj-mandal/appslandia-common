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

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.appslandia.common.base.MapWrapper;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtPayload extends MapWrapper<String, Object> {
	private static final long serialVersionUID = 1L;

	public static final String ISS = "iss";
	public static final String SUB = "sub";
	public static final String AUD = "aud";
	public static final String EXP = "exp";
	public static final String NBF = "nbf";
	public static final String IAT = "iat";
	public static final String JTI = "jti";

	public JwtPayload() {
		super(new LinkedHashMap<String, Object>());
	}

	protected JwtPayload(Map<String, Object> map) {
		super(map);
	}

	public String getIssuer() {
		return (String) this.map.get(ISS);
	}

	public JwtPayload setIssuer(String value) {
		this.map.put(ISS, value);
		return this;
	}

	public String getSubject() {
		return (String) this.map.get(SUB);
	}

	public JwtPayload setSubject(String value) {
		this.map.put(SUB, value);
		return this;
	}

	public List<String> getAudiences() {
		Object value = this.map.get(AUD);
		if (value == null) {
			return null;
		}
		if (value.getClass() == String.class) {
			return CollectionUtils.toList(new ArrayList<String>(1), (String) value);
		}
		return ObjectUtils.cast(value);
	}

	public JwtPayload setAudiences(String... values) {
		if (values.length == 0) {
			return this;
		}
		this.map.put(AUD, (values.length == 1) ? values[0] : CollectionUtils.toList(values));
		return this;
	}

	public Date getExpiresAt() {
		return getNumericDate(EXP);
	}

	public JwtPayload setExpiresAt(Date value) {
		return putNumericDate(EXP, value);
	}

	public Date getNotBefore() {
		return getNumericDate(NBF);
	}

	public JwtPayload setNotBefore(Date value) {
		return putNumericDate(NBF, value);
	}

	public Date getIssuedAt() {
		return getNumericDate(IAT);
	}

	public JwtPayload setIssuedAt(Date value) {
		return putNumericDate(IAT, value);
	}

	public JwtPayload setIssuedAtNow() {
		return setIssuedAt(new Date());
	}

	public String getJwtId() {
		return (String) this.map.get(JTI);
	}

	public JwtPayload setJwtId(String value) {
		this.map.put(JTI, value);
		return this;
	}

	@Override
	public JwtPayload put(String key, Object value) {
		this.map.put(key, value);
		return this;
	}

	public JwtPayload putArray(String key, Object... values) {
		this.map.put(key, values);
		return this;
	}

	public Long getLong(String key) {
		Object obj = this.map.get(key);
		return (obj != null) ? JoseUtils.toLongValue(obj) : null;
	}

	public Long getRequiredLong(String key) throws IllegalStateException {
		Object obj = this.map.get(key);
		if (obj == null) {
			throw new IllegalStateException(key + " required.");
		}
		return JoseUtils.toLongValue(obj);
	}

	public Double getDouble(String key) {
		Object obj = this.map.get(key);
		return (obj != null) ? JoseUtils.toDoubleValue(obj) : null;
	}

	public Double getRequiredDouble(String key) throws IllegalStateException {
		Object obj = this.map.get(key);
		if (obj == null) {
			throw new IllegalStateException(key + " required.");
		}
		return JoseUtils.toDoubleValue(obj);
	}

	public <T> T getObject(String key) {
		Object obj = this.map.get(key);
		return ObjectUtils.cast(obj);
	}

	public <T> T getRequiredObject(String key) throws IllegalStateException {
		Object obj = this.map.get(key);
		if (obj == null) {
			throw new IllegalStateException(key + " required.");
		}
		return ObjectUtils.cast(obj);
	}

	public Date getNumericDate(String key) {
		Long nd = (Long) this.map.get(key);
		return (nd != null) ? JoseUtils.toDate(nd) : null;
	}

	public Date getRequiredNumericDate(String key) throws IllegalStateException {
		Long nd = (Long) this.map.get(key);
		if (nd == null) {
			throw new IllegalStateException(key + " required.");
		}
		return JoseUtils.toDate(nd);
	}

	public JwtPayload putNumericDate(String key, Date value) {
		if (value == null) {
			return this;
		}
		this.map.put(key, JoseUtils.toNumericDate(value));
		return this;
	}
}
