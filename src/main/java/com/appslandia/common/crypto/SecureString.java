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

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.RoundRobinPool;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SecureString implements Destroyable {

	public interface Accessor<T> {
		T access(char[] clearChars) throws CryptoException;
	}

	final Encryptor encryptor = EncryptorFactory.getDefault().newEncryptor();
	final byte[] secured;
	private boolean destroyed = false;

	public SecureString(char[] clearChars) throws CryptoException {
		this.secured = encrypt(clearChars);
	}

	private byte[] encrypt(char[] clearChars) throws CryptoException {
		byte[] clearBytes = CryptoUtils.toByteArray(clearChars);
		try {
			return this.encryptor.encrypt(clearBytes);
		} finally {
			CryptoUtils.clear(clearBytes);
		}
	}

	private char[] decrypt() throws CryptoException {
		byte[] clearBytes = null;
		try {
			clearBytes = this.encryptor.decrypt(this.secured);
			return CryptoUtils.toCharArray(clearBytes);
		} finally {
			CryptoUtils.clear(clearBytes);
		}
	}

	public <T> T access(Accessor<T> accessor) throws CryptoException {
		assertNotDestroyed();

		char[] clearChars = null;
		try {
			clearChars = decrypt();
			return accessor.access(clearChars);
		} finally {
			CryptoUtils.clear(clearChars);
		}
	}

	@Override
	public void destroy() throws DestroyFailedException {
		if (!this.destroyed) {
			CryptoUtils.clear(this.secured);

			if (this.encryptor != null) {
				this.encryptor.destroy();
			}
			this.destroyed = true;
		}
	}

	@Override
	public boolean isDestroyed() {
		return this.destroyed;
	}

	protected void assertNotDestroyed() {
		if (this.destroyed) {
			throw new IllegalStateException("destroyed.");
		}
	}

	public SecureString copy() throws CryptoException {
		assertNotDestroyed();

		char[] clearChars = null;
		try {
			clearChars = decrypt();
			return new SecureString(clearChars);
		} finally {
			CryptoUtils.clear(clearChars);
		}
	}

	public static abstract class EncryptorFactory {

		private static volatile EncryptorFactory __default;
		private static final Object MUTEX = new Object();

		public static EncryptorFactory getDefault() {
			EncryptorFactory obj = __default;
			if (obj == null) {
				synchronized (MUTEX) {
					if ((obj = __default) == null) {
						__default = obj = initEncryptorFactory();
					}
				}
			}
			return obj;
		}

		private static EncryptorFactory initEncryptorFactory() {
			return new Impl();
		}

		public abstract Encryptor newEncryptor();

		static final class Impl extends EncryptorFactory {

			@Override
			public Encryptor newEncryptor() {
				return new EncryptorImpl();
			}

			static final class EncryptorImpl extends InitializeObject implements Encryptor {
				private SecretKey key;
				private int poolSize;

				private Decryptor[] decryptors;
				private RoundRobinPool<Decryptor> decryptorPool;

				private static final String ALGORITHM = "AES";
				private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

				// @formatter:off
				private static final IvParameterSpec IV = new IvParameterSpec(new byte[] {
			         (byte) 0x51,(byte) 0x65,(byte) 0x22,(byte) 0x23,
			         (byte) 0x64,(byte) 0x05,(byte) 0x6A,(byte) 0xBE,
			         (byte) 0x51,(byte) 0x65,(byte) 0x22,(byte) 0x23,
			         (byte) 0x64,(byte) 0x05,(byte) 0x6A,(byte) 0xBE,
			     });

				// @formatter:on

				@Override
				protected void init() throws Exception {
					// AES Key
					this.key = KeyGenerator.getInstance(ALGORITHM).generateKey();
					this.poolSize = ValueUtils.valueOrMin(this.poolSize, Runtime.getRuntime().availableProcessors());

					// DECRYPTORS
					this.decryptors = new Decryptor[this.poolSize];

					for (int i = 0; i < this.poolSize; i++) {
						this.decryptors[i] = new Decryptor();
					}
					this.decryptorPool = new RoundRobinPool<>(this.decryptors);
				}

				@Override
				public void destroy() throws DestroyException {
					CryptoUtils.destroyQuietly(this.key);
				}

				@Override
				public byte[] encrypt(byte[] message) throws CryptoException {
					this.initialize();
					AssertUtils.assertNotNull(message, "message is required.");

					try {
						Cipher cipher = Cipher.getInstance(TRANSFORMATION);
						cipher.init(Cipher.ENCRYPT_MODE, key, IV);
						return cipher.doFinal(message);

					} catch (GeneralSecurityException ex) {
						throw new CryptoException(ex);
					}
				}

				@Override
				public byte[] decrypt(byte[] message) throws CryptoException {
					this.initialize();
					AssertUtils.assertNotNull(message, "message is required.");
					return this.decryptorPool.next().decrypt(message);
				}

				public EncryptorImpl setPoolSize(int poolSize) {
					assertNotInitialized();
					this.poolSize = poolSize;
					return this;
				}

				@Override
				public EncryptorImpl copy() {
					throw new UnsupportedOperationException();
				}

				final class Decryptor {

					private Cipher cipher;
					final Object mutex = new Object();

					public Decryptor() throws CryptoException {
						try {
							this.cipher = Cipher.getInstance(TRANSFORMATION);
							this.cipher.init(Cipher.DECRYPT_MODE, key, IV);
						} catch (GeneralSecurityException ex) {
							throw new CryptoException(ex);
						}
					}

					public byte[] decrypt(byte[] message) throws CryptoException {
						synchronized (this.mutex) {
							try {
								return this.cipher.doFinal(message);
							} catch (GeneralSecurityException ex) {
								throw new CryptoException(ex);
							}
						}
					}
				}
			}
		}
	}
}
