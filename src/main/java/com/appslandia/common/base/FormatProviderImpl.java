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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FormatProviderImpl implements FormatProvider {

	protected final Language language;
	protected Map<String, SimpleDateFormat> dateFormatCache;

	protected NumberFormat integerFormat;
	protected NumberFormat numberFormat;
	protected NumberFormat bigDecimalFormat;

	public FormatProviderImpl() {
		this(Language.getDefault());
	}

	public FormatProviderImpl(Language language) {
		this.language = language;
	}

	@Override
	public Language getLanguage() {
		return this.language;
	}

	@Override
	public NumberFormat getIntegerFormat() {
		if (this.integerFormat == null) {
			this.integerFormat = NumberFormat.getIntegerInstance(this.language.getLocale());
		}
		return this.integerFormat;
	}

	@Override
	public NumberFormat getNumberFormat() {
		if (this.numberFormat == null) {
			this.numberFormat = NumberFormat.getNumberInstance(this.language.getLocale());
		}
		return this.numberFormat;
	}

	@Override
	public NumberFormat getBigDecimalFormat() {
		if (this.bigDecimalFormat == null) {
			NumberFormat impl = NumberFormat.getNumberInstance(this.language.getLocale());
			if (impl instanceof DecimalFormat) {
				((DecimalFormat) impl).setParseBigDecimal(true);
			}
			this.bigDecimalFormat = impl;
		}
		return this.bigDecimalFormat;
	}

	@Override
	public DateFormat getDateFormat(String pattern) {
		return this.getDateFormatCache().get(pattern);
	}

	protected Map<String, SimpleDateFormat> getDateFormatCache() {
		if (this.dateFormatCache == null) {
			this.dateFormatCache = new HashMap<String, SimpleDateFormat>() {
				private static final long serialVersionUID = 1L;

				@Override
				public SimpleDateFormat get(Object pattern) {
					SimpleDateFormat impl = super.get(pattern);
					if (impl != null) {
						return impl;
					}
					impl = new SimpleDateFormat((String) pattern);
					impl.setLenient(false);
					this.put((String) pattern, impl);
					return impl;
				}
			};
		}
		return this.dateFormatCache;
	}
}
