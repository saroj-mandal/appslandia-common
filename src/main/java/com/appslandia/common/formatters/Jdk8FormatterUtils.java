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

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Jdk8FormatterUtils {

	public static FormatterProvider register(FormatterProvider provider) {
		provider.addFormatter(Formatter.LOCAL_TIME, new LocalTimeFormatter());
		provider.addFormatter(Formatter.LOCAL_TIME_MI, new LocalTimeMIFormatter());

		provider.addFormatter(Formatter.LOCAL_DATE, new LocalDateFormatter());
		provider.addFormatter(Formatter.LOCAL_DATE_LZ, new LocalDateFormatter(true));

		provider.addFormatter(Formatter.LOCAL_DATETIME, new LocalDateTimeFormatter());
		provider.addFormatter(Formatter.LOCAL_DATETIME_LZ, new LocalDateTimeFormatter(true));

		provider.addFormatter(Formatter.LOCAL_DATETIME_MI, new LocalDateTimeMIFormatter());
		provider.addFormatter(Formatter.LOCAL_DATETIME_MILZ, new LocalDateTimeMIFormatter(true));

		provider.addFormatter(Formatter.OFFSET_TIME, new OffsetTimeFormatter());
		provider.addFormatter(Formatter.OFFSET_TIME_MI, new OffsetTimeMIFormatter());

		provider.addFormatter(Formatter.OFFSET_DATETIME, new OffsetDateTimeFormatter());
		provider.addFormatter(Formatter.OFFSET_DATETIME_LZ, new OffsetDateTimeFormatter(true));

		provider.addFormatter(Formatter.OFFSET_DATETIME_MI, new OffsetDateTimeMIFormatter());
		provider.addFormatter(Formatter.OFFSET_DATETIME_MILZ, new OffsetDateTimeMIFormatter(true));
		return provider;
	}
}
