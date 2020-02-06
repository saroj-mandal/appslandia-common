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

import java.util.Locale;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ReflectionUtils;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.common.utils.SystemUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Language extends InitializeObject {

	public static final Language EN = new Language().setLocale(Locale.US).setPatternDate("MM/dd/yyyy");
	public static final Language VI = new Language().setLocale(new Locale("vi", "VN")).setPatternDate("dd/MM/yyyy");

	private Locale locale;
	private String patternDate;

	private String patternDateTime;
	private String patternDateTimeZ;

	private String patternDateTimeMI;
	private String patternDateTimeMIZ;

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.locale, "locale is required.");
		AssertUtils.assertNotNull(this.patternDate, "datePattern is required.");

		this.patternDateTime = String.format("%s %s", this.patternDate, DateUtils.PATTERN_TIME);
		this.patternDateTimeZ = String.format("%s %s", this.patternDate, DateUtils.PATTERN_TIME_Z);

		this.patternDateTimeMI = String.format("%s %s", this.patternDate, DateUtils.PATTERN_TIME_MI);
		this.patternDateTimeMIZ = String.format("%s %s", this.patternDate, DateUtils.PATTERN_TIME_MIZ);
	}

	public String getId() {
		this.initialize();
		return this.locale.getLanguage();
	}

	public String getDisplayName() {
		this.initialize();
		return this.locale.getDisplayLanguage(this.locale);
	}

	public Locale getLocale() {
		this.initialize();
		return this.locale;
	}

	public Language setLocale(Locale locale) {
		this.assertNotInitialized();
		this.locale = locale;
		return this;
	}

	public String getPatternDate() {
		this.initialize();
		return this.patternDate;
	}

	public Language setPatternDate(String patternDate) {
		this.assertNotInitialized();
		this.patternDate = StringUtils.trimToNull(patternDate);
		return this;
	}

	public String getPatternDateTime() {
		this.initialize();
		return this.patternDateTime;
	}

	public String getPatternDateTimeZ() {
		this.initialize();
		return this.patternDateTimeZ;
	}

	public String getPatternDateTimeMI() {
		this.initialize();
		return this.patternDateTimeMI;
	}

	public String getPatternDateTimeMIZ() {
		this.initialize();
		return this.patternDateTimeMIZ;
	}

	public String getPatternTimeMI() {
		this.initialize();
		return DateUtils.PATTERN_TIME_MI;
	}

	public String getPatternTimeMIZ() {
		this.initialize();
		return DateUtils.PATTERN_TIME_MIZ;
	}

	public String getPatternTime() {
		this.initialize();
		return DateUtils.PATTERN_TIME;
	}

	public String getPatternTimeZ() {
		this.initialize();
		return DateUtils.PATTERN_TIME_Z;
	}

	@Override
	public int hashCode() {
		return ObjectUtils.hashCode(this.getId().toLowerCase(Locale.ENGLISH));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Language)) {
			return false;
		}
		Language another = (Language) obj;
		return this.getId().equalsIgnoreCase(another.getId());
	}

	private static volatile Language __default;
	private static final Object MUTEX = new Object();

	public static Language getDefault() {
		Language obj = __default;
		if (obj == null) {
			synchronized (MUTEX) {
				if ((obj = __default) == null) {
					__default = obj = initLanguage();
				}
			}
		}
		return obj;
	}

	public static void setDefault(Language impl) {
		if (__default == null) {
			synchronized (MUTEX) {
				if (__default == null) {
					__default = impl;
					return;
				}
			}
		}
		throw new IllegalStateException("Language.__default must be null.");
	}

	private static Provider<Language> __provider;

	public static void setProvider(Provider<Language> provider) {
		AssertUtils.assertNull(__default);
		__provider = provider;
	}

	public static final String PROP_LANGUAGE_IMPL = "languageImpl";

	private static Language initLanguage() {
		if (__provider != null) {
			return __provider.get();
		}
		try {
			String implName = SystemUtils.getProp(PROP_LANGUAGE_IMPL);
			if (implName == null) {
				return EN;
			}
			Class<? extends Language> implClass = ReflectionUtils.loadClass(implName, null);
			return ReflectionUtils.newInstance(implClass);
		} catch (Exception ex) {
			throw new InitializeException(ex);
		}
	}
}
