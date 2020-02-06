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

package com.appslandia.common.utils;

import java.util.Arrays;
import java.util.Comparator;

import com.appslandia.common.base.Compat;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ObjectUtils {

	@Compat
	public static int hashCode(Object o) {
		return o != null ? o.hashCode() : 0;
	}

	@Compat
	public static boolean equals(Object a, Object b) {
		return (a == b) || (a != null && a.equals(b));
	}

	@Compat
	public static int hash(Object... values) {
		return Arrays.hashCode(values);
	}

	@Compat
	public static <T> int compare(T a, T b, Comparator<? super T> c) {
		return (a == b) ? 0 : c.compare(a, b);
	}

	@Compat
	public static String toString(Object value) {
		return String.valueOf(value);
	}

	public static String toStringOrEmpty(Object value) {
		return value != null ? value.toString() : StringUtils.EMPTY_STRING;
	}

	public static String toStringOrNull(Object value) {
		return (value != null) ? value.toString() : null;
	}

	@SuppressWarnings("unchecked")
	public static <F, T> T cast(F o) throws ClassCastException {
		return (T) o;
	}

	public static String toHashInfo(Object value) {
		if (value == null) {
			return "null";
		}
		return value.getClass().getName() + "@" + Integer.toHexString(value.hashCode());
	}
}
