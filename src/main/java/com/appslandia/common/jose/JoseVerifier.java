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

import java.util.ArrayList;
import java.util.List;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JoseVerifier<T> extends InitializeObject {

	final List<Delegate<T, ?>> delegates = new ArrayList<>();

	public static abstract class Delegate<T, A> {
		final A arg;

		public Delegate(A arg) {
			this.arg = arg;
		}

		public abstract void verify(T obj, boolean parsing) throws JoseException;
	}

	@Override
	protected void init() throws Exception {
	}

	public void verify(T obj, boolean parsing) throws JoseException {
		this.initialize();
		AssertUtils.assertNotNull(obj);

		for (Delegate<T, ?> impl : this.delegates) {
			impl.verify(obj, parsing);
		}
	}

	public <A> JoseVerifier<T> addVerifier(Delegate<T, A> delegate) {
		assertNotInitialized();
		this.delegates.add(delegate);
		return this;
	}
}
