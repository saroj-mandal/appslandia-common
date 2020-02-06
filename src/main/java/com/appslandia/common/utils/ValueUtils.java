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

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ValueUtils {

	public static int valueOrMin(int checkValue, int min) {
		if (checkValue < min) {
			return min;
		}
		return checkValue;
	}

	public static long valueOrMin(long checkValue, long min) {
		if (checkValue < min) {
			return min;
		}
		return checkValue;
	}

	public static int valueOrMax(int checkValue, int max) {
		if (checkValue > max) {
			return max;
		}
		return checkValue;
	}

	public static long valueOrMax(long checkValue, long max) {
		if (checkValue > max) {
			return max;
		}
		return checkValue;
	}

	public static long inRange(long checkValue, long min, long max) {
		if (checkValue < min) {
			return min;
		}
		if (checkValue > max) {
			return max;
		}
		return checkValue;
	}

	public static double inRange(double checkValue, double min, double max) {
		if (checkValue < min) {
			return min;
		}
		if (checkValue > max) {
			return max;
		}
		return checkValue;
	}

	public static <T> T valueOrAlt(T checkValue, T altValue) {
		return (checkValue != null) ? checkValue : altValue;
	}

	public static int valueOrAlt(int checkValue, int altValue) {
		return (checkValue != 0) ? checkValue : altValue;
	}

	public static long valueOrAlt(long checkValue, long altValue) {
		return (checkValue != 0L) ? checkValue : altValue;
	}
}
