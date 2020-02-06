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

package com.appslandia.common.caching;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;

import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JCacheManager implements AppCacheManager {

	final CacheManager cacheManager;

	public JCacheManager() {
		this.cacheManager = Caching.getCachingProvider().getCacheManager();
	}

	public JCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public <K, V> AppCache<K, V> createCache(String cacheName, Object configuration) {
		return new JCache<>(this.cacheManager.createCache(cacheName, ObjectUtils.cast(configuration)));
	}

	@Override
	public <K, V> AppCache<K, V> getCache(String cacheName) {
		Cache<K, V> cache = this.cacheManager.getCache(cacheName);
		return (cache != null) ? new JCache<>(cache) : null;
	}

	@Override
	public <K, V> AppCache<K, V> getRequiredCache(String cacheName) throws IllegalArgumentException {
		Cache<K, V> cache = this.cacheManager.getCache(cacheName);
		if (cache == null) {
			throw new IllegalArgumentException("cache is required (name=" + cacheName + ")");
		}
		return new JCache<>(cache);
	}

	@Override
	public void destroyCache(String cacheName) {
		this.cacheManager.destroyCache(cacheName);
	}

	@Override
	public Iterable<String> getCacheNames() {
		return this.cacheManager.getCacheNames();
	}

	@Override
	public void close() {
		this.cacheManager.close();
	}
}
