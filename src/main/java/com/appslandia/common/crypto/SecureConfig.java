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

package com.appslandia.common.crypto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.appslandia.common.base.PropertyConfig;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SecureConfig extends PropertyConfig {
	private static final long serialVersionUID = 1L;

	final TextEncryptor textEncryptor;

	public SecureConfig(char[] password) {
		AssertUtils.assertNotNull(password);
		this.textEncryptor = new TextEncryptor(new PbeEncryptor().setTransformation("AES/CBC/PKCS5Padding").setKeySize(32).setPassword(password));
	}

	public SecureConfig(String password) {
		AssertUtils.assertNotNull(password);
		char[] pwd = password.toCharArray();
		try {
			this.textEncryptor = new TextEncryptor(new PbeEncryptor().setTransformation("AES/CBC/PKCS5Padding").setKeySize(32).setPassword(pwd));
		} finally {
			CryptoUtils.clear(pwd);
		}
	}

	public SecureConfig(Encryptor encryptor) {
		AssertUtils.assertNotNull(encryptor);
		this.textEncryptor = new TextEncryptor(encryptor);
	}

	public SecureConfig(TextEncryptor textEncryptor) {
		AssertUtils.assertNotNull(textEncryptor);
		this.textEncryptor = textEncryptor;
	}

	@Override
	public SecureConfig load(InputStream is) throws IOException {
		super.load(is);
		return this;
	}

	@Override
	public SecureConfig load(Reader r) throws IOException {
		super.load(r);
		return this;
	}

	@Override
	public SecureConfig load(File file) throws IOException {
		super.load(file);
		return this;
	}

	@Override
	public SecureConfig load(String file) throws IOException {
		super.load(file);
		return this;
	}

	@Override
	public String getString(String key) {
		String value = super.getString(key);
		if (value == null) {
			return null;
		}
		if (!isEncryptedValue(value)) {
			return value;
		}
		return this.textEncryptor.decrypt(parseEncryptedValue(value));
	}

	public void puts(String key, String value) throws CryptoException {
		value = StringUtils.trimToNull(value);
		doPut(key, (value != null) ? markEncryptedValue(this.textEncryptor.encrypt(value)) : null);
	}

	public void puts(String key, boolean value) throws CryptoException {
		doPut(key, markEncryptedValue(this.textEncryptor.encrypt(Boolean.toString(value))));
	}

	public void puts(String key, int value) throws CryptoException {
		doPut(key, markEncryptedValue(this.textEncryptor.encrypt(Integer.toString(value))));
	}

	public void puts(String key, long value) throws CryptoException {
		doPut(key, markEncryptedValue(this.textEncryptor.encrypt(Long.toString(value))));
	}

	public void puts(String key, float value) throws CryptoException {
		doPut(key, markEncryptedValue(this.textEncryptor.encrypt(Float.toString(value))));
	}

	public void puts(String key, double value) throws CryptoException {
		doPut(key, markEncryptedValue(this.textEncryptor.encrypt(Double.toString(value))));
	}

	public void puts(String key, Object value) throws CryptoException {
		String valueAsString = toStringValue(value);
		doPut(key, (valueAsString != null) ? markEncryptedValue(this.textEncryptor.encrypt(valueAsString)) : null);
	}

	private static final String ENC_BEGIN = "ENC(";
	private static final String ENC_END = ")";

	private static String markEncryptedValue(String value) {
		return ENC_BEGIN + value + ENC_END;
	}

	private static boolean isEncryptedValue(String value) {
		return value.startsWith(ENC_BEGIN) && value.endsWith(ENC_END);
	}

	private static String parseEncryptedValue(String value) {
		return value.substring(ENC_BEGIN.length(), value.length() - ENC_END.length());
	}
}
