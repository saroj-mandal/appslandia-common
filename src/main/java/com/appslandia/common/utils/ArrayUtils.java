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

package com.appslandia.common.utils;

import java.util.Random;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ArrayUtils {

	public static byte[] append(byte[] src1, byte[] src2) {
		byte[] arr = new byte[src1.length + src2.length];
		System.arraycopy(src1, 0, arr, 0, src1.length);
		System.arraycopy(src2, 0, arr, src1.length, src2.length);
		return arr;
	}

	public static byte[] append(byte[] src1, byte[] src2, byte[] src3) {
		byte[] arr = new byte[src1.length + src2.length + src3.length];
		System.arraycopy(src1, 0, arr, 0, src1.length);
		System.arraycopy(src2, 0, arr, src1.length, src2.length);
		System.arraycopy(src3, 0, arr, src1.length + src2.length, src3.length);
		return arr;
	}

	public static void copy(byte[] src, byte[] dest1) {
		System.arraycopy(src, 0, dest1, 0, dest1.length);
	}

	public static void copy(byte[] src, byte[] dest1, byte[] dest2) {
		System.arraycopy(src, 0, dest1, 0, dest1.length);
		System.arraycopy(src, dest1.length, dest2, 0, dest2.length);
	}

	public static void copy(byte[] src, byte[] dest1, byte[] dest2, byte[] dest3) {
		System.arraycopy(src, 0, dest1, 0, dest1.length);
		System.arraycopy(src, dest1.length, dest2, 0, dest2.length);
		System.arraycopy(src, dest1.length + dest2.length, dest3, 0, dest3.length);
	}

	public static char[] append(char[] src1, char[] src2) {
		char[] arr = new char[src1.length + src2.length];
		System.arraycopy(src1, 0, arr, 0, src1.length);
		System.arraycopy(src2, 0, arr, src1.length, src2.length);
		return arr;
	}

	public static char[] append(char[] src1, char[] src2, char[] src3) {
		char[] arr = new char[src1.length + src2.length + src3.length];
		System.arraycopy(src1, 0, arr, 0, src1.length);
		System.arraycopy(src2, 0, arr, src1.length, src2.length);
		System.arraycopy(src3, 0, arr, src1.length + src2.length, src3.length);
		return arr;
	}

	public static String[] append(String[] src1, String[] src2) {
		String[] arr = new String[src1.length + src2.length];
		System.arraycopy(src1, 0, arr, 0, src1.length);
		System.arraycopy(src2, 0, arr, src1.length, src2.length);
		return arr;
	}

	public static String[] append(String[] src1, String[] src2, String[] src3) {
		String[] arr = new String[src1.length + src2.length + src3.length];
		System.arraycopy(src1, 0, arr, 0, src1.length);
		System.arraycopy(src2, 0, arr, src1.length, src2.length);
		System.arraycopy(src3, 0, arr, src1.length + src2.length, src3.length);
		return arr;
	}

	public static Object[] copy(Object[] src) {
		if ((src == null) || (src.length == 0)) {
			return src;
		}
		Object[] arr = new Object[src.length];
		System.arraycopy(src, 0, arr, 0, src.length);
		return arr;
	}

	public static String[] copy(String[] src) {
		if ((src == null) || (src.length == 0)) {
			return src;
		}
		String[] arr = new String[src.length];
		System.arraycopy(src, 0, arr, 0, src.length);
		return arr;
	}

	public static byte[] copy(byte[] src) {
		if ((src == null) || (src.length == 0)) {
			return src;
		}
		byte[] arr = new byte[src.length];
		System.arraycopy(src, 0, arr, 0, src.length);
		return arr;
	}

	public static char[] copy(char[] src) {
		if ((src == null) || (src.length == 0)) {
			return src;
		}
		char[] arr = new char[src.length];
		System.arraycopy(src, 0, arr, 0, src.length);
		return arr;
	}

	public static boolean contains(byte[] parent, byte[] child, int childIndex) {
		if (parent == child)
			return childIndex == 0;
		if (parent == null || child == null)
			return false;

		if (child.length + childIndex > parent.length)
			return false;

		for (int i = 0; i < child.length - 1; i++)
			if (child[i] != parent[i + childIndex])
				return false;

		return true;
	}

	public static <T> boolean hasElements(T[] elements) {
		return (elements != null) && (elements.length > 0);
	}

	public static <T> void shuffle(T[] src, Random random) {
		for (int i = src.length; i > 1; i--) {
			swap(src, i - 1, random.nextInt(i));
		}
	}

	public static void shuffle(int[] src, Random random) {
		for (int i = src.length; i > 1; i--) {
			swap(src, i - 1, random.nextInt(i));
		}
	}

	static void swap(int[] src, int i, int j) {
		int temp = src[i];
		src[i] = src[j];
		src[j] = temp;
	}

	static <T> void swap(T[] src, int i, int j) {
		T temp = src[i];
		src[i] = src[j];
		src[j] = temp;
	}
}
