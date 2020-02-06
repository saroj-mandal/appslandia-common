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

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.CharsetUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwsToken implements Serializable {
	private static final long serialVersionUID = 1L;

	private JoseHeader header;
	private byte[] payload;

	public JwsToken(JoseHeader header, String payload) {
		this(header, AssertUtils.assertNotNull(payload).getBytes(CharsetUtils.UTF_8));
	}

	public JwsToken(JoseHeader header, byte[] payload) {
		this.header = AssertUtils.assertNotNull(header);
		this.payload = AssertUtils.assertNotNull(payload);
	}

	public JoseHeader getHeader() {
		return this.header;
	}

	public byte[] getPayload() {
		return this.payload;
	}

	public String getPayloadAsString() {
		return new String(this.payload, CharsetUtils.UTF_8);
	}
}
