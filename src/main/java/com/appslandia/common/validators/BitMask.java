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

package com.appslandia.common.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.MathUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { BitMask.ConstraintValidatorImpl.class })
@Documented
public @interface BitMask {

	String message() default "{com.appslandia.common.validators.BitMask.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int value();

	@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		BitMask[] value();
	}

	public static class ConstraintValidatorImpl implements ConstraintValidator<BitMask, Number> {
		private long max;

		@Override
		public void initialize(BitMask annotation) {
			int size = annotation.value();
			AssertUtils.assertTrue((size >= 1) && (size <= 63), "size is out of range [1-63]");
			if (size == 63) {
				this.max = Long.MAX_VALUE;
			} else {
				this.max = MathUtils.pow2(size) - 1;
			}
		}

		@Override
		public boolean isValid(Number value, ConstraintValidatorContext context) {
			if (value == null) {
				return true;
			}
			long longValue = value.longValue();
			if (longValue < 0) {
				return false;
			}
			if (this.max > 0) {
				if (longValue > max) {
					return false;
				}
			}
			return true;
		}
	}
}
