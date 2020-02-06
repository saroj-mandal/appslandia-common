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

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import com.appslandia.common.base.Compat;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CharsetUtils {

	@Compat
	public static final Charset US_ASCII = Charset.forName("US-ASCII");

	@Compat
	public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

	@Compat
	public static final Charset UTF_8 = Charset.forName("UTF-8");

	@Compat
	public static final Charset UTF_16BE = Charset.forName("UTF-16BE");

	@Compat
	public static final Charset UTF_16LE = Charset.forName("UTF-16LE");

	@Compat
	public static final Charset UTF_16 = Charset.forName("UTF-16");

	public static String parse(String contentType) {
		if (contentType == null) {
			return UTF_8.name();
		}
		return parse(contentType, UTF_8.name());
	}

	public static String parse(String contentType, String defaultValue) {
		if (contentType == null) {
			return defaultValue;
		}
		List<String> items = SplitUtils.split(contentType, ';');
		for (int i = items.size() - 1; i >= 0; i--) {
			String item = items.get(i);

			if (item.toLowerCase(Locale.ENGLISH).startsWith("charset=")) {
				return item.substring(8);
			}
		}
		return defaultValue;
	}
}
