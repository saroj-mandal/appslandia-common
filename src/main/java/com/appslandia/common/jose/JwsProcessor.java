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

import java.io.StringReader;
import java.util.Set;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.StringWriter;
import com.appslandia.common.crypto.CryptoException;
import com.appslandia.common.jose.JoseVerifier.Delegate;
import com.appslandia.common.json.JsonException;
import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.CharsetUtils;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwsProcessor extends InitializeObject {

	protected JsonProcessor jsonProcessor;
	protected Set<String> numericDateProps;
	protected JoseSigner joseSigner;

	protected long leewayMs;
	protected final JoseVerifier<JoseHeader> headerVerifier = new JoseVerifier<>();

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.jsonProcessor, "jsonProcessor is required.");

		this.numericDateProps = CollectionUtils.unmodifiable(this.numericDateProps);
		this.joseSigner = ValueUtils.valueOrAlt(this.joseSigner, JoseSigner.NONE);

		AssertUtils.assertTrue(this.leewayMs >= 0, "leewayMs is invalid.");
		initHeaderVerifier();
	}

	protected void initHeaderVerifier() {
		this.headerVerifier.addVerifier(new JoseVerifier.Delegate<JoseHeader, JoseSigner>(this.joseSigner) {

			@Override
			public void verify(JoseHeader obj, boolean parsing) throws JoseException {
				// ALG
				if (obj.getAlgorithm() == null) {
					throw new JoseException("alg is required.");
				}
				if (!obj.getAlgorithm().equals(this.arg.getAlgorithm())) {
					throw new JoseException("alg is not matched.");
				}
				// KID
				if (!ObjectUtils.equals(obj.getKid(), this.arg.getKid())) {
					throw new JoseException("kid is not matched.");
				}
			}
		});
	}

	@Override
	public void destroy() throws DestroyException {
		if (this.jsonProcessor != null) {
			this.jsonProcessor.destroy();
		}
		if (this.joseSigner != null) {
			this.joseSigner.destroy();
		}
	}

	public JoseHeader newHeader() {
		this.initialize();
		return new JoseHeader().setAlgorithm(this.joseSigner.getAlgorithm()).setKid(this.joseSigner.getKid());
	}

	public String compact(JwsToken jws) throws CryptoException, JsonException, JoseException {
		return compact(jws, true);
	}

	public String compact(JwsToken jws, boolean verify) throws CryptoException, JsonException, JoseException {
		this.initialize();
		AssertUtils.assertNotNull(jws);
		AssertUtils.assertNotNull(jws.getHeader());
		AssertUtils.assertNotNull(jws.getPayload());

		if (verify) {
			this.headerVerifier.verify(jws.getHeader(), false);
		}

		StringWriter out = new StringWriter();
		this.jsonProcessor.write(out, jws.getHeader());
		String base64Header = BaseEncoder.BASE64_URL_NP.encode(out.toString().getBytes(CharsetUtils.UTF_8));
		String base64Payload = BaseEncoder.BASE64_URL_NP.encode(jws.getPayload());

		// No ALG
		if (this.joseSigner == JoseSigner.NONE) {
			return base64Header + JoseUtils.JOSE_PART_SEP + base64Payload + JoseUtils.JOSE_PART_SEP;
		}

		// ALG
		String dataToSign = base64Header + JoseUtils.JOSE_PART_SEP + base64Payload;
		String base64Sig = BaseEncoder.BASE64_URL_NP.encode(this.joseSigner.sign(dataToSign.getBytes(CharsetUtils.UTF_8)));
		return dataToSign + JoseUtils.JOSE_PART_SEP + base64Sig;
	}

	public JwsToken parse(String jws) throws CryptoException, JsonException, JoseException {
		return parse(jws, true);
	}

	public JwsToken parse(String jws, boolean verify) throws CryptoException, JsonException, JoseException {
		this.initialize();
		AssertUtils.assertNotNull(jws);

		String[] parts = JoseUtils.parseParts(jws);
		if (parts == null) {
			throw new JoseException("jws is invalid.");
		}

		// No ALG
		if (parts[2] == null) {
			if (this.joseSigner != JoseSigner.NONE) {
				throw new JoseException("Signature is required.");
			}
			return doParse(parts[0], parts[1], verify);
		}

		// ALG
		String dataToSign = parts[0] + JoseUtils.JOSE_PART_SEP + parts[1];
		byte[] signature = BaseEncoder.BASE64_URL_NP.decode(parts[2]);

		if (!this.joseSigner.verify(dataToSign.getBytes(CharsetUtils.UTF_8), signature)) {
			throw new JoseException("Failed to verify signature.");
		}
		return doParse(parts[0], parts[1], verify);
	}

	protected JwsToken doParse(String headerPart, String payloadPart, boolean verify) throws JsonException, JoseException {
		JoseHeader header = parseHeader(headerPart);
		if (verify) {
			this.headerVerifier.verify(header, true);
		}
		byte[] payload = parsePayload(payloadPart);
		return new JwsToken(header, payload);
	}

	public JoseHeader parseHeader(String headerPart) throws JsonException, JoseException {
		this.initialize();
		AssertUtils.assertNotNull(headerPart);

		String headerJson = new String(BaseEncoder.BASE64_URL_NP.decode(headerPart), CharsetUtils.UTF_8);
		JoseHeader header = new JoseHeader(this.jsonProcessor.readAsLinkedMap(new StringReader(headerJson)));
		JoseUtils.convertToLong(header, this.numericDateProps);
		return header;
	}

	public byte[] parsePayload(String payloadPart) {
		this.initialize();
		AssertUtils.assertNotNull(payloadPart);
		return BaseEncoder.BASE64_URL_NP.decode(payloadPart);
	}

	public JwsProcessor setJsonProcessor(JsonProcessor jsonProcessor) {
		assertNotInitialized();
		this.jsonProcessor = jsonProcessor;
		return this;
	}

	public JwsProcessor setNumericDateProps(String... numericDateProps) {
		assertNotInitialized();
		if (numericDateProps != null) {
			this.numericDateProps = CollectionUtils.toSet(numericDateProps);
		}
		return this;
	}

	public JwsProcessor setJoseSigner(JoseSigner joseSigner) {
		assertNotInitialized();
		this.joseSigner = joseSigner;
		return this;
	}

	public <A> JwsProcessor addHeaderVerifier(Delegate<JoseHeader, A> delegate) {
		assertNotInitialized();
		this.headerVerifier.addVerifier(delegate);
		return this;
	}

	public JwsProcessor setLeewayMs(long leewayMs) {
		assertNotInitialized();
		this.leewayMs = leewayMs;
		return this;
	}
}
