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

package com.appslandia.common.jose;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.crypto.CryptoException;
import com.appslandia.common.crypto.Digester;
import com.appslandia.common.crypto.DsaDigester;
import com.appslandia.common.crypto.MacDigester;
import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JoseSigner extends InitializeObject {

	public static final JoseSigner NONE = new JoseSigner();

	private String algorithm;
	private Digester signer;
	private String kid;

	@Override
	protected void init() throws Exception {
		if (this.signer == null) {
			if (this.algorithm == null) {
				this.algorithm = JoseUtils.ALG_NONE;
			}
		}
		AssertUtils.assertNotNull(this.algorithm, "algorithm is required.");

		if (this.signer == null) {
			AssertUtils.assertTrue(this.algorithm.equals(JoseUtils.ALG_NONE));
		} else {
			AssertUtils.assertTrue(!this.algorithm.equals(JoseUtils.ALG_NONE));
		}

		if (this.kid != null) {
			AssertUtils.assertNotNull(this.signer, "signer is required.");
		}
	}

	public byte[] sign(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertTrue(this != NONE, "signer must be not NONE.");
		AssertUtils.assertNotNull(this.signer);

		return this.signer.digest(message);
	}

	public boolean verify(byte[] message, byte[] signature) throws CryptoException {
		this.initialize();
		AssertUtils.assertTrue(this != NONE, "signer must be not NONE.");
		AssertUtils.assertNotNull(this.signer);

		return this.signer.verify(message, signature);
	}

	public String getAlgorithm() {
		this.initialize();
		return this.algorithm;
	}

	public JoseSigner setAlgorithm(String algorithm) {
		assertNotInitialized();
		this.algorithm = algorithm;
		return this;
	}

	public Digester getSigner() {
		this.initialize();
		return this.signer;
	}

	public JoseSigner setSigner(MacDigester signer) {
		assertNotInitialized();
		this.signer = signer;
		return this;
	}

	public JoseSigner setSigner(DsaDigester signer) {
		assertNotInitialized();
		this.signer = signer;
		return this;
	}

	public String getKid() {
		this.initialize();
		return this.kid;
	}

	public JoseSigner setKid(String kid) {
		assertNotInitialized();
		this.kid = kid;
		return this;
	}

	public JoseSigner copy() {
		if (this == NONE) {
			return NONE;
		}
		JoseSigner impl = new JoseSigner();
		impl.algorithm = this.algorithm;
		if (this.signer != null) {
			impl.signer = this.signer.copy();
		}
		impl.kid = this.kid;
		return impl;
	}
}
