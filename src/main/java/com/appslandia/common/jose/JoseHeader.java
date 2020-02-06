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

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.base.MapWrapper;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JoseHeader extends MapWrapper<String, Object> {
	private static final long serialVersionUID = 1L;

	public static final String TYP = "typ";
	public static final String ALG = "alg";
	public static final String KID = "kid";

	public JoseHeader() {
		super(new LinkedHashMap<String, Object>());
	}

	protected JoseHeader(Map<String, Object> map) {
		super(map);
	}

	public String getType() {
		return (String) this.map.get(TYP);
	}

	public JoseHeader setType(String value) {
		this.map.put(TYP, value);
		return this;
	}

	public String getAlgorithm() {
		return (String) this.map.get(ALG);
	}

	public JoseHeader setAlgorithm(String value) {
		this.map.put(ALG, value);
		return this;
	}

	public String getKid() {
		return (String) this.map.get(KID);
	}

	public JoseHeader setKid(String value) {
		this.map.put(KID, value);
		return this;
	}

	@Override
	public JoseHeader put(String key, Object value) {
		this.map.put(key, value);
		return this;
	}

	public Date getNumericDate(String key) {
		Long nd = (Long) this.map.get(key);
		return JoseUtils.toDate(nd);
	}

	public JoseHeader putNumericDate(String key, Date value) {
		if (value == null) {
			return this;
		}
		this.map.put(key, JoseUtils.toNumericDate(value));
		return this;
	}
}
