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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CharUtils {

	private static final Pattern CHAR_RANGE_GROUP_PATTERN = Pattern.compile(".\\-.");

	public static char[] toCharRanges(String charRanges) {
		Matcher matcher = CHAR_RANGE_GROUP_PATTERN.matcher(charRanges);
		char[] chars = null;
		int lastEnd = 0;

		while (matcher.find()) {
			String range = matcher.group();
			if (matcher.start() != lastEnd) {
				char[] notRange = charRanges.substring(lastEnd, matcher.start()).toCharArray();
				chars = (chars == null) ? notRange : ArrayUtils.append(chars, notRange);
			}

			lastEnd = matcher.end();
			char from = range.charAt(0);
			char to = range.charAt(2);
			if (from > to) {
				throw new IllegalArgumentException("invalid charRanges (value=" + charRanges + ")");
			}
			char[] newChars = new char[to - from + 1];
			for (int i = 0; i < newChars.length; i++) {
				newChars[i] = (char) (from + i);
			}
			chars = (chars == null) ? newChars : ArrayUtils.append(chars, newChars);
		}
		if (lastEnd < charRanges.length()) {
			char[] notRange = charRanges.substring(lastEnd).toCharArray();
			chars = (chars == null) ? notRange : ArrayUtils.append(chars, notRange);
		}
		return chars;
	}
}
