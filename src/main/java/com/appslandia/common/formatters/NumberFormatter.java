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

package com.appslandia.common.formatters;

import java.text.NumberFormat;
import java.text.ParsePosition;

import com.appslandia.common.base.FormatProvider;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class NumberFormatter implements Formatter {

	final boolean localized;

	public NumberFormatter(boolean localized) {
		this.localized = localized;
	}

	protected abstract NumberFormat getLocalizedFormat(FormatProvider formatProvider);

	@Override
	public String format(Object obj, FormatProvider formatProvider) {
		if (obj == null) {
			return null;
		}
		if (this.localized) {
			return getLocalizedFormat(formatProvider).format(getArgType().cast(obj));
		}
		return getArgType().cast(obj).toString();
	}

	protected Number parseNumber(String str, FormatProvider formatProvider) throws FormatterException {
		ParsePosition pos = new ParsePosition(0);
		Number parsedValue = getLocalizedFormat(formatProvider).parse(str, pos);

		if ((pos.getErrorIndex() < 0) && (pos.getIndex() == str.length()) && (parsedValue != null)) {
			return parsedValue;
		}
		throw new FormatterException("An error occurred while parsing number (str=" + str + ", type=" + getArgType() + ")", getErrorMsgKey());
	}

	protected FormatterException parsedOvfException(String str) {
		return new FormatterException("An overflow occurred while parsing number (str=" + str + ", type=" + getArgType() + ")", getErrorMsgKey());
	}
}
