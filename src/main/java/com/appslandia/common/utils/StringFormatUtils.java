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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class StringFormatUtils {

	private static final Pattern NAMED_PARAMS_PATTERN = Pattern.compile("\\{[^}]+?}");
	private static final Pattern INDEX_PARAMS_PATTERN = Pattern.compile("\\{\\d+}");

	public static String format(String str, Map<String, Object> namedParameters) {
		Matcher matcher = NAMED_PARAMS_PATTERN.matcher(str);
		StringBuilder sb = new StringBuilder(str.length() + namedParameters.size() * 10);

		int prevEnd = 0;
		while (matcher.find()) {
			// Non parameter
			if (prevEnd == 0) {
				sb.append(str.substring(0, matcher.start()));
			} else {
				sb.append(str.substring(prevEnd, matcher.start()));
			}

			// {parameter}
			String parameterGroup = matcher.group();
			String parameterName = parameterGroup.substring(1, parameterGroup.length() - 1);
			appendParam(sb, namedParameters.get(parameterName), parameterGroup);

			prevEnd = matcher.end();
		}
		if (prevEnd < str.length()) {
			sb.append(str.substring(prevEnd));
		}
		return sb.toString();
	}

	public static String format(String str, Object... parameters) {
		Matcher matcher = INDEX_PARAMS_PATTERN.matcher(str);
		StringBuilder sb = new StringBuilder(str.length() + parameters.length * 10);
		int prevEnd = 0;
		while (matcher.find()) {
			// Non index
			if (prevEnd == 0) {
				sb.append(str.substring(0, matcher.start()));
			} else {
				sb.append(str.substring(prevEnd, matcher.start()));
			}

			// {index}
			String parameterGroup = matcher.group();
			String parameterIndex = parameterGroup.substring(1, parameterGroup.length() - 1);
			int index = -1;
			try {
				index = Integer.parseInt(parameterIndex);
			} catch (NumberFormatException ex) {
			}

			// Valid index?
			if ((0 <= index) && (index <= parameters.length - 1)) {
				appendParam(sb, parameters[index], parameterGroup);
			} else {
				sb.append(parameterGroup);
			}
			prevEnd = matcher.end();
		}
		if (prevEnd < str.length()) {
			sb.append(str.substring(prevEnd));
		}
		return sb.toString();
	}

	private static void appendParam(StringBuilder sb, Object param, String parameterGroup) {
		if (param != null) {
			if (param.getClass().isArray()) {
				sb.append(CollectionUtils.toElements(param));
			} else {
				sb.append(param.toString());
			}
		} else {
			sb.append(parameterGroup);
		}
	}
}
