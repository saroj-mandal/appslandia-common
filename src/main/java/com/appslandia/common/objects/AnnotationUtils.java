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

package com.appslandia.common.objects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Qualifier;

import com.appslandia.common.utils.ReflectionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class AnnotationUtils {

	public static boolean equals(Annotation[] src, Annotation[] annotations) {
		if (annotations.length != src.length) {
			return false;
		}
		if (annotations.length == 0) {
			return true;
		}
		for (Annotation ann2 : annotations) {
			boolean matched = false;
			for (Annotation ann1 : src) {
				if (matched = ann1.equals(ann2)) {
					break;
				}
			}
			if (!matched) {
				return false;
			}
		}
		return true;
	}

	public static boolean hasAnnotations(Annotation[] src, Annotation[] annotations) {
		if (annotations.length == 0) {
			return src.length == 0;
		}
		for (Annotation ann2 : annotations) {
			boolean matched = false;
			for (Annotation ann1 : src) {
				if (matched = ann1.equals(ann2)) {
					break;
				}
			}
			if (!matched) {
				return false;
			}
		}
		return true;
	}

	public static Annotation[] parseQualifiers(Annotation[] annotations) {
		List<Annotation> qualifiers = new ArrayList<>(3);
		for (Annotation ann : annotations) {
			if (ann.annotationType().getDeclaredAnnotation(Qualifier.class) != null) {
				qualifiers.add(ann);
			}
		}
		return !qualifiers.isEmpty() ? qualifiers.toArray(new Annotation[qualifiers.size()]) : ReflectionUtils.EMPTY_ANNOTATIONS;
	}

	public static Annotation[] parseQualifiers(Field field) {
		return parseQualifiers(field.getDeclaredAnnotations());
	}

	public static Annotation[] parseQualifiers(Method method) {
		return parseQualifiers(method.getDeclaredAnnotations());
	}

	public static Annotation[] parseQualifiers(Parameter parameter) {
		return parseQualifiers(parameter.getDeclaredAnnotations());
	}

	public static Annotation[] parseQualifiers(Class<?> clazz) {
		return parseQualifiers(clazz.getDeclaredAnnotations());
	}
}
