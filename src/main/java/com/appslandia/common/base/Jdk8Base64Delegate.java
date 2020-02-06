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

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Jdk8Base64Delegate extends Base64Delegate {

	@Override
	public byte[] encode(byte[] src) {
		return java.util.Base64.getEncoder().encode(src);
	}

	@Override
	public byte[] encodeNP(byte[] src) {
		return java.util.Base64.getEncoder().withoutPadding().encode(src);
	}

	@Override
	public byte[] decode(byte[] src) throws IllegalArgumentException {
		return java.util.Base64.getDecoder().decode(src);
	}

	@Override
	public byte[] urlEncode(byte[] src) {
		return java.util.Base64.getUrlEncoder().encode(src);
	}

	@Override
	public byte[] urlEncodeNP(byte[] src) {
		return java.util.Base64.getUrlEncoder().withoutPadding().encode(src);
	}

	@Override
	public byte[] urlDecode(byte[] src) throws IllegalArgumentException {
		return java.util.Base64.getUrlDecoder().decode(src);
	}

	@Override
	public byte[] mimeEncode(byte[] src) {
		return java.util.Base64.getMimeEncoder().encode(src);
	}

	@Override
	public byte[] mimeEncodeNP(byte[] src) {
		return java.util.Base64.getMimeEncoder().withoutPadding().encode(src);
	}

	@Override
	public byte[] mimeDecode(byte[] src) throws IllegalArgumentException {
		return java.util.Base64.getMimeDecoder().decode(src);
	}
}
