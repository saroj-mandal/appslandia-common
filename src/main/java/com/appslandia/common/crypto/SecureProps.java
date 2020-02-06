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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.appslandia.common.base.StringWriter;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.CharsetUtils;
import com.appslandia.common.utils.ExceptionUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SecureProps extends Properties {
	private static final long serialVersionUID = 1L;

	final TextEncryptor textEncryptor;

	public SecureProps(char[] password) {
		AssertUtils.assertNotNull(password);
		this.textEncryptor = new TextEncryptor(new PbeEncryptor().setTransformation("AES/CBC/PKCS5Padding").setKeySize(32).setPassword(password));
	}

	public SecureProps(String password) {
		AssertUtils.assertNotNull(password);
		char[] pwd = password.toCharArray();
		try {
			this.textEncryptor = new TextEncryptor(new PbeEncryptor().setTransformation("AES/CBC/PKCS5Padding").setKeySize(32).setPassword(pwd));
		} finally {
			CryptoUtils.clear(pwd);
		}
	}

	public SecureProps(Encryptor encryptor) {
		AssertUtils.assertNotNull(encryptor);
		this.textEncryptor = new TextEncryptor(encryptor);
	}

	public SecureProps(TextEncryptor textEncryptor) {
		AssertUtils.assertNotNull(textEncryptor);
		this.textEncryptor = textEncryptor;
	}

	@Override
	public synchronized String get(Object key) {
		String value = (String) super.get(key);
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
		put(key, (value != null) ? markEncryptedValue(this.textEncryptor.encrypt(value)) : null);
	}

	public void store(File file, String comments) throws IOException {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), CharsetUtils.UTF_8));
			store(bw, comments);
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
	}

	public void store(String file, String comments) throws IOException {
		store(new File(file), comments);
	}

	@Override
	public void store(OutputStream os, String comments) throws IOException {
		toProperties().store(os, comments);
	}

	@Override
	public void store(Writer w, String comments) throws IOException {
		toProperties().store(w, comments);
	}

	protected Properties toProperties() {
		Properties props = new LinkedProperties();
		for (Map.Entry<Object, Object> prop : this.entrySet()) {
			props.put(prop.getKey(), prop.getValue());
		}
		return props;
	}

	@Override
	public String toString() {
		try {
			StringWriter out = new StringWriter();
			store(out, getClass().getName());
			return out.toString();
		} catch (IOException ex) {
			return ExceptionUtils.toStackTrace(ex);
		}
	}

	public SecureProps copy() {
		SecureProps props = new SecureProps(this.textEncryptor.copy());
		for (Map.Entry<Object, Object> prop : this.entrySet()) {
			props.put(prop.getKey(), prop.getValue());
		}
		return props;
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

	private static class LinkedProperties extends Properties {
		private static final long serialVersionUID = 1;

		final Set<Object> keys = new LinkedHashSet<>();

		@Override
		public Set<Object> keySet() {
			return this.keys;
		}

		@Override
		public Enumeration<Object> keys() {
			return Collections.enumeration(this.keys);
		}

		@Override
		public Object put(Object key, Object value) {
			this.keys.add(key);
			return super.put(key, value);
		}
	}
}
