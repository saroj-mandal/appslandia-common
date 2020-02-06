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

import com.appslandia.common.utils.ReflectionUtils;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class Base64Delegate {

	public abstract byte[] encode(byte[] src);

	public abstract byte[] encodeNP(byte[] src);

	public abstract byte[] decode(byte[] src) throws IllegalArgumentException;

	public abstract byte[] urlEncode(byte[] src);

	public abstract byte[] urlEncodeNP(byte[] src);

	public abstract byte[] urlDecode(byte[] src) throws IllegalArgumentException;

	public abstract byte[] mimeEncode(byte[] src);

	public abstract byte[] mimeEncodeNP(byte[] src);

	public abstract byte[] mimeDecode(byte[] src) throws IllegalArgumentException;

	private static volatile Base64Delegate __default;
	private static final Object MUTEX = new Object();

	public static Base64Delegate getDefault() {
		Base64Delegate obj = __default;
		if (obj == null) {
			synchronized (MUTEX) {
				if ((obj = __default) == null) {
					__default = obj = initBase64Delegate();
				}
			}
		}
		return obj;
	}

	public static void setDefault(Base64Delegate impl) {
		if (__default == null) {
			synchronized (MUTEX) {
				if (__default == null) {
					__default = impl;
					return;
				}
			}
		}
		throw new IllegalStateException("Base64Delegate.__default must be null.");
	}

	private static Base64Delegate initBase64Delegate() {
		try {
			Class<? extends Base64Delegate> implClass = ReflectionUtils.loadClass("com.appslandia.common.base.Jdk8Base64Delegate", null);
			return ReflectionUtils.newInstance(implClass);
		} catch (Exception ex) {
			throw new InitializeException(ex);
		}
	}
}
