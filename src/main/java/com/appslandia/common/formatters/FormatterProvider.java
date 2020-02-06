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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Provider;
import com.appslandia.common.utils.TypeUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FormatterProvider extends InitializeObject {

	private Map<String, Formatter> formatterMap = new HashMap<>();

	@Override
	protected void init() throws Exception {
		addDefault(Formatter.BYTE, new ByteFormatter());

		addDefault(Formatter.SHORT, new ShortFormatter());
		addDefault(Formatter.SHORT_LZ, new ShortFormatter(true));

		addDefault(Formatter.INTEGER, new IntegerFormatter());
		addDefault(Formatter.INTEGER_LZ, new IntegerFormatter(true));

		addDefault(Formatter.LONG, new LongFormatter());
		addDefault(Formatter.LONG_LZ, new LongFormatter(true));

		addDefault(Formatter.FLOAT, new FloatFormatter());
		addDefault(Formatter.FLOAT_LZ, new FloatFormatter(true));

		addDefault(Formatter.DOUBLE, new DoubleFormatter());
		addDefault(Formatter.DOUBLE_LZ, new DoubleFormatter(true));

		addDefault(Formatter.BIGDECIMAL, new BigDecimalFormatter());
		addDefault(Formatter.BIGDECIMAL_LZ, new BigDecimalFormatter(true));

		addDefault(Formatter.TIME, new SqlTimeFormatter());
		addDefault(Formatter.TIME_MI, new SqlTimeMIFormatter());

		addDefault(Formatter.DATE, new SqlDateFormatter());
		addDefault(Formatter.DATE_LZ, new SqlDateFormatter(true));

		addDefault(Formatter.TIMESTAMP, new SqlTimestampFormatter());
		addDefault(Formatter.TIMESTAMP_LZ, new SqlTimestampFormatter(true));

		addDefault(Formatter.TIMESTAMP_MI, new SqlTimestampMIFormatter());
		addDefault(Formatter.TIMESTAMP_MILZ, new SqlTimestampMIFormatter(true));

		addDefault(Formatter.BOOLEAN, new BooleanFormatter());
		addDefault(Formatter.STRING, new StringFormatter());
		addDefault(Formatter.SINGLE_LINE, new SingleLineStringFormatter());
		addDefault(Formatter.TRIM_TO_EMPTY, new TrimToEmptyFormatter());

		addDefault(Formatter.UPPER, new LowerUpperFormatter(false, null));
		addDefault(Formatter.LOWER, new LowerUpperFormatter(true, null));

		addDefault(Formatter.EN_UPPER, new LowerUpperFormatter(false, Locale.ENGLISH));
		addDefault(Formatter.EN_LOWER, new LowerUpperFormatter(true, Locale.ENGLISH));

		addDefault(Formatter.INVARIANT_UPPER, new LowerUpperFormatter(false, Locale.ROOT));
		addDefault(Formatter.INVARIANT_LOWER, new LowerUpperFormatter(true, Locale.ROOT));

		this.formatterMap = Collections.unmodifiableMap(this.formatterMap);
	}

	protected void addDefault(String formatterId, Formatter formatter) {
		if (!this.formatterMap.containsKey(formatterId)) {
			this.formatterMap.put(formatterId, formatter);
		}
	}

	public void addFormatter(String formatterId, Formatter formatter) {
		this.assertNotInitialized();
		this.formatterMap.put(formatterId, formatter);
	}

	public Formatter findFormatter(Fmt fmt, Class<?> targetType) {
		this.initialize();
		if (fmt != null) {
			return getFormatter(fmt.value());
		} else {
			return this.formatterMap.get(TypeUtils.wrap(targetType).getSimpleName());
		}
	}

	public Formatter findFormatter(String formatterId, Class<?> targetType) {
		this.initialize();
		if (formatterId != null) {
			return getFormatter(formatterId);
		} else {
			return this.formatterMap.get(TypeUtils.wrap(targetType).getSimpleName());
		}
	}

	public Formatter getFormatter(Class<?> targetType) throws IllegalArgumentException {
		this.initialize();
		Formatter formatter = this.formatterMap.get(TypeUtils.wrap(targetType).getSimpleName());
		if (formatter == null) {
			throw new IllegalArgumentException("Formatter is not found (targetType=" + targetType + ")");
		}
		return formatter;
	}

	public Formatter getFormatter(String formatterId) throws IllegalArgumentException {
		this.initialize();
		Formatter formatter = this.formatterMap.get(formatterId);
		if (formatter == null) {
			throw new IllegalArgumentException("Formatter is not found (formatterId=" + formatterId + ")");
		}
		return formatter;
	}

	private static volatile FormatterProvider __default;
	private static final Object MUTEX = new Object();

	public static FormatterProvider getDefault() {
		FormatterProvider obj = __default;
		if (obj == null) {
			synchronized (MUTEX) {
				if ((obj = __default) == null) {
					__default = obj = initFormatterProvider();
				}
			}
		}
		return obj;
	}

	public static void setDefault(FormatterProvider impl) {
		if (__default == null) {
			synchronized (MUTEX) {
				if (__default == null) {
					__default = impl;
					return;
				}
			}
		}
		throw new IllegalStateException("FormatterProvider.__default must be null.");
	}

	private static Provider<FormatterProvider> __provider;

	public static void setProvider(Provider<FormatterProvider> impl) {
		if (__default == null) {
			synchronized (MUTEX) {
				if (__default == null) {
					__provider = impl;
					return;
				}
			}
		}
		throw new IllegalStateException("FormatterProvider.__default must be null.");
	}

	private static FormatterProvider initFormatterProvider() {
		if (__provider != null) {
			return __provider.get();
		}
		return new FormatterProvider();
	}
}
