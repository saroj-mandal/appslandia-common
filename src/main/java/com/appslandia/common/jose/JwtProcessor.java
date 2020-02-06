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
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtProcessor extends InitializeObject {

	protected JsonProcessor jsonProcessor;
	protected Set<String> numericDateProps;
	protected JoseSigner joseSigner;

	protected String issuer;
	protected List<String> audiences;
	protected int leewayMs;

	protected final JoseVerifier<JoseHeader> headerVerifier = new JoseVerifier<>();
	protected final JoseVerifier<JwtPayload> payloadVerifier = new JoseVerifier<>();

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.jsonProcessor, "jsonProcessor is required.");

		if (this.numericDateProps == null) {
			this.numericDateProps = CollectionUtils.unmodifiableSet(JwtPayload.EXP, JwtPayload.NBF, JwtPayload.IAT);
		} else {
			CollectionUtils.unmodifiableSet(this.numericDateProps, JwtPayload.EXP, JwtPayload.NBF, JwtPayload.IAT);
		}
		this.joseSigner = ValueUtils.valueOrAlt(this.joseSigner, JoseSigner.NONE);

		this.audiences = CollectionUtils.unmodifiable(this.audiences);
		AssertUtils.assertTrue(this.leewayMs >= 0, "leewayMs is required.");

		initHeaderVerifier();
		initPayloadVerifier();
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

	protected void initPayloadVerifier() {
		// ISS
		this.payloadVerifier.addVerifier(new JoseVerifier.Delegate<JwtPayload, String>(this.issuer) {

			@Override
			public void verify(JwtPayload obj, boolean parsing) throws JoseException {
				if (!ObjectUtils.equals(obj.getIssuer(), this.arg)) {
					throw new JoseException("iss is not matched.");
				}
			}
		});
		// AUD
		this.payloadVerifier.addVerifier(new JoseVerifier.Delegate<JwtPayload, List<String>>(this.audiences) {

			@Override
			public void verify(JwtPayload obj, boolean parsing) throws JoseException {
				List<String> aud = obj.getAudiences();
				if (aud == null) {
					aud = Collections.emptyList();
				}
				if (!aud.isEmpty()) {
					if (Collections.disjoint(aud, this.arg)) {
						throw new JoseException("aud is not matched.");
					}
				}
			}
		});
		// IAT
		this.payloadVerifier.addVerifier(new JoseVerifier.Delegate<JwtPayload, Integer>(this.leewayMs) {

			@Override
			public void verify(JwtPayload obj, boolean parsing) throws JoseException {
				Date iat = obj.getIssuedAt();
				if (iat == null) {
					return;
				}
				if (!DateUtils.isPastTime(iat.getTime(), this.arg)) {
					throw new JoseException("iat is not matched.");
				}
			}
		});
		// NBF
		this.payloadVerifier.addVerifier(new JoseVerifier.Delegate<JwtPayload, Integer>(this.leewayMs) {

			@Override
			public void verify(JwtPayload obj, boolean parsing) throws JoseException {
				if (!parsing) {
					return;
				}
				Date nbf = obj.getNotBefore();
				if (nbf == null) {
					return;
				}
				if (!DateUtils.isPastTime(nbf.getTime(), this.arg)) {
					throw new JoseException("jwt can't be used before " + DateUtils.iso8601DateTime(nbf));
				}
			}
		});
		// EXP
		this.payloadVerifier.addVerifier(new JoseVerifier.Delegate<JwtPayload, Integer>(this.leewayMs) {

			@Override
			public void verify(JwtPayload obj, boolean parsing) throws JoseException {
				Date expAt = obj.getExpiresAt();
				if (expAt == null) {
					return;
				}
				if (!DateUtils.isFutureTime(expAt.getTime(), this.arg)) {
					throw new JoseException("jwt can't be used after " + DateUtils.iso8601DateTime(expAt));
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
		return new JoseHeader().setType("JWT").setAlgorithm(this.joseSigner.getAlgorithm()).setKid(this.joseSigner.getKid());
	}

	public JwtPayload newPayload() {
		this.initialize();
		return new JwtPayload().setIssuer(this.issuer).setAudiences(this.audiences.toArray(new String[this.audiences.size()]));
	}

	public String compact(JwtToken jwt) throws CryptoException, JsonException, JoseException {
		return compact(jwt, true);
	}

	public String compact(JwtToken jwt, boolean verify) throws CryptoException, JsonException, JoseException {
		this.initialize();
		AssertUtils.assertNotNull(jwt);
		AssertUtils.assertNotNull(jwt.getHeader());
		AssertUtils.assertNotNull(jwt.getPayload());

		if (verify) {
			this.headerVerifier.verify(jwt.getHeader(), false);
			this.payloadVerifier.verify(jwt.getPayload(), false);
		}

		StringWriter out = new StringWriter();
		this.jsonProcessor.write(out, jwt.getHeader());
		String base64Header = BaseEncoder.BASE64_URL_NP.encode(out.toString().getBytes(CharsetUtils.UTF_8));

		out.reset();
		this.jsonProcessor.write(out, jwt.getPayload());
		String base64Payload = BaseEncoder.BASE64_URL_NP.encode(out.toString().getBytes(CharsetUtils.UTF_8));

		// No ALG
		if (this.joseSigner == JoseSigner.NONE) {
			return base64Header + JoseUtils.JOSE_PART_SEP + base64Payload + JoseUtils.JOSE_PART_SEP;
		}

		// ALG
		String dataToSign = base64Header + JoseUtils.JOSE_PART_SEP + base64Payload;
		String base64Sig = BaseEncoder.BASE64_URL_NP.encode(this.joseSigner.sign(dataToSign.getBytes(CharsetUtils.UTF_8)));
		return dataToSign + JoseUtils.JOSE_PART_SEP + base64Sig;
	}

	public JwtToken parse(String jwt) throws CryptoException, JsonException, JoseException {
		return parse(jwt, true);
	}

	public JwtToken parse(String jwt, boolean verify) throws CryptoException, JsonException, JoseException {
		this.initialize();
		AssertUtils.assertNotNull(jwt);

		String[] parts = JoseUtils.parseParts(jwt);
		if (parts == null) {
			throw new JoseException("jwt is invalid.");
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

	protected JwtToken doParse(String headerPart, String payloadPart, boolean verify) throws JsonException, JoseException {
		JoseHeader header = parseHeader(headerPart);
		if (verify) {
			this.headerVerifier.verify(header, true);
		}
		JwtPayload payload = parsePayload(payloadPart);
		if (verify) {
			this.payloadVerifier.verify(payload, true);
		}
		return new JwtToken(header, payload);
	}

	public JoseHeader parseHeader(String headerPart) throws JsonException, JoseException {
		this.initialize();
		AssertUtils.assertNotNull(headerPart);

		String headerJson = new String(BaseEncoder.BASE64_URL_NP.decode(headerPart), CharsetUtils.UTF_8);
		JoseHeader header = new JoseHeader(this.jsonProcessor.readAsLinkedMap(new StringReader(headerJson)));
		JoseUtils.convertToLong(header, this.numericDateProps);
		return header;
	}

	public JwtPayload parsePayload(String payloadPart) {
		this.initialize();
		AssertUtils.assertNotNull(payloadPart);

		String payloadJson = new String(BaseEncoder.BASE64_URL_NP.decode(payloadPart), CharsetUtils.UTF_8);
		JwtPayload payload = new JwtPayload(this.jsonProcessor.readAsLinkedMap(new StringReader(payloadJson)));
		JoseUtils.convertToLong(payload, this.numericDateProps);
		return payload;
	}

	public JwtProcessor setJsonProcessor(JsonProcessor jsonProcessor) {
		assertNotInitialized();
		this.jsonProcessor = jsonProcessor;
		return this;
	}

	public JwtProcessor setNumericDateProps(String... numericDateProps) {
		assertNotInitialized();
		if (numericDateProps != null) {
			this.numericDateProps = CollectionUtils.toSet(numericDateProps);
		}
		return this;
	}

	public JwtProcessor setJoseSigner(JoseSigner joseSigner) {
		assertNotInitialized();
		this.joseSigner = joseSigner;
		return this;
	}

	public <A> JwtProcessor addHeaderVerifier(Delegate<JoseHeader, A> delegate) {
		assertNotInitialized();
		this.headerVerifier.addVerifier(delegate);
		return this;
	}

	public <A> JwtProcessor addPayloadVerifier(Delegate<JwtPayload, A> delegate) {
		assertNotInitialized();
		this.payloadVerifier.addVerifier(delegate);
		return this;
	}

	public JwtProcessor setIssuer(String issuer) {
		assertNotInitialized();
		this.issuer = issuer;
		return this;
	}

	public JwtProcessor setAudiences(String... audiences) {
		assertNotInitialized();
		if (audiences != null) {
			this.audiences = CollectionUtils.toList(audiences);
		}
		return this;
	}

	public JwtProcessor setLeewayMs(int leewayMs) {
		assertNotInitialized();
		this.leewayMs = leewayMs;
		return this;
	}
}
