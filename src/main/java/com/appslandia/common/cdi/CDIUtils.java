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

package com.appslandia.common.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.el.ELProcessor;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ReflectionException;
import com.appslandia.common.utils.ReflectionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CDIUtils {

	public static <T> T getReference(BeanManager beanManager, Class<? extends T> type) {
		return getReference(beanManager, type, ReflectionUtils.EMPTY_ANNOTATIONS);
	}

	public static <T> T getReference(BeanManager beanManager, Class<? extends T> type, Annotation... qualifiers) {
		Set<Bean<?>> matchedBeans = beanManager.getBeans(type, qualifiers);
		if (matchedBeans.isEmpty()) {
			return null;
		}
		Bean<?> bean = beanManager.resolve(matchedBeans);
		if (bean == null) {
			return null;
		}
		return ObjectUtils.cast(beanManager.getReference(bean, type, beanManager.createCreationalContext(bean)));
	}

	public static <T> T getReference(BeanManager beanManager, String name) {
		Set<Bean<?>> matchedBeans = beanManager.getBeans(name);
		if (matchedBeans.isEmpty()) {
			return null;
		}
		Bean<?> bean = beanManager.resolve(matchedBeans);
		if (bean == null) {
			return null;
		}
		return ObjectUtils.cast(beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean)));
	}

	public static <T> T getInstance(BeanManager beanManager, Class<? extends T> type) {
		return getInstance(beanManager, type, ReflectionUtils.EMPTY_ANNOTATIONS);
	}

	public static <T> T getInstance(BeanManager beanManager, Class<? extends T> type, Annotation... qualifiers) {
		Set<Bean<?>> matchedBeans = beanManager.getBeans(type, qualifiers);
		if (matchedBeans.isEmpty()) {
			return null;
		}
		Bean<T> bean = ObjectUtils.cast(beanManager.resolve(matchedBeans));
		if (bean == null) {
			return null;
		}
		return beanManager.getContext(bean.getScope()).get(bean, beanManager.createCreationalContext(bean));
	}

	public static <T> T getInstance(BeanManager beanManager, String name) {
		Set<Bean<?>> matchedBeans = beanManager.getBeans(name);
		if (matchedBeans.isEmpty()) {
			return null;
		}
		Bean<T> bean = ObjectUtils.cast(beanManager.resolve(matchedBeans));
		if (bean == null) {
			return null;
		}
		return beanManager.getContext(bean.getScope()).get(bean, beanManager.createCreationalContext(bean));
	}

	public static <T, A extends Annotation> void scanReferences(BeanManager beanManager, Class<T> type, Annotation[] qualifiers, Class<A> annotationClass,
			BiConsumer<A, T> consumer) {
		Set<Bean<T>> beans = ObjectUtils.cast(beanManager.getBeans(type, qualifiers));
		for (Bean<T> bean : beans) {
			if (annotationClass == null) {
				T impl = ObjectUtils.cast(beanManager.getReference(bean, type, beanManager.createCreationalContext(bean)));
				consumer.accept(null, impl);
			} else {
				A annotation = bean.getBeanClass().getDeclaredAnnotation(annotationClass);
				if (annotation == null) {
					if (CDIFactory.class.isAssignableFrom(bean.getBeanClass())) {

						Method factoryMethod = getFactoryMethod(ObjectUtils.cast(bean.getBeanClass()));
						annotation = factoryMethod.getDeclaredAnnotation(annotationClass);
					}
				}
				if (annotation != null) {
					T impl = ObjectUtils.cast(beanManager.getReference(bean, type, beanManager.createCreationalContext(bean)));
					consumer.accept(annotation, impl);
				}
			}
		}
	}

	public static void scanSuppliers(BeanManager beanManager, Annotation[] qualifiers, Class<?> constraintType, Consumer<CDISupplier> consumer) {
		Set<Bean<CDISupplier>> beans = ObjectUtils.cast(beanManager.getBeans(CDISupplier.class, qualifiers));
		for (Bean<CDISupplier> bean : beans) {

			Supplier supplier = bean.getBeanClass().getDeclaredAnnotation(Supplier.class);
			if (supplier == null) {
				if (CDIFactory.class.isAssignableFrom(bean.getBeanClass())) {

					Method factoryMethod = getFactoryMethod(ObjectUtils.cast(bean.getBeanClass()));
					supplier = factoryMethod.getDeclaredAnnotation(Supplier.class);
				}
			}
			if ((supplier != null) && (supplier.value() == constraintType)) {
				CDISupplier impl = ObjectUtils.cast(beanManager.getReference(bean, CDISupplier.class, beanManager.createCreationalContext(bean)));
				consumer.accept(impl);
			}
		}
	}

	public static <T, A extends Annotation> void scanBeanClasses(BeanManager beanManager, Class<T> type, Annotation[] qualifiers, Class<A> annotationClass,
			BiConsumer<A, Class<?>> consumer) {
		Set<Bean<T>> beans = ObjectUtils.cast(beanManager.getBeans(type, qualifiers));
		for (Bean<T> bean : beans) {

			A annotation = bean.getBeanClass().getDeclaredAnnotation(annotationClass);
			if (annotation == null) {
				if (CDIFactory.class.isAssignableFrom(bean.getBeanClass())) {

					Method factoryMethod = getFactoryMethod(ObjectUtils.cast(bean.getBeanClass()));
					annotation = factoryMethod.getDeclaredAnnotation(annotationClass);
				}
			}
			if (annotation != null) {
				consumer.accept(annotation, bean.getBeanClass());
			}
		}
	}

	public static Method getFactoryMethod(Class<? extends CDIFactory<?>> factoryClass) {
		try {
			return factoryClass.getMethod("produce");
		} catch (ReflectiveOperationException ex) {
			throw new ReflectionException(ex);
		}
	}

	public static Annotation getImpl(Class<? extends Annotation> qualifier) {
		try {
			Field impl = qualifier.getDeclaredField("IMPL");
			AssertUtils.assertTrue(ReflectionUtils.isPublicConst(impl.getModifiers()));

			return (Annotation) impl.get(null);
		} catch (ReflectiveOperationException ex) {
			throw new ReflectionException(ex);
		}
	}

	public static <A extends Annotation> A getAnnotation(BeanManager beanManager, Annotated annotated, Class<A> annotationType) {
		if (annotated.getAnnotations().isEmpty()) {
			return null;
		}
		if (annotated.isAnnotationPresent(annotationType)) {
			return annotated.getAnnotation(annotationType);
		}

		Queue<Annotation> annotations = new LinkedList<>(annotated.getAnnotations());
		return getAnnotation(beanManager, annotationType, annotations);
	}

	public static <A extends Annotation> A getAnnotation(BeanManager beanManager, Class<?> annotatedClass, Class<A> annotationType) {
		if (annotatedClass.isAnnotationPresent(annotationType)) {
			return annotatedClass.getAnnotation(annotationType);
		}

		LinkedList<Annotation> annotations = new LinkedList<>();
		CollectionUtils.toList(annotations, annotatedClass.getAnnotations());

		return getAnnotation(beanManager, annotationType, annotations);
	}

	static <A extends Annotation> A getAnnotation(BeanManager beanManager, Class<A> annotationType, Queue<Annotation> annotations) {
		while (!annotations.isEmpty()) {
			Annotation annotation = annotations.remove();

			if (annotation.annotationType().equals(annotationType)) {
				return annotationType.cast(annotation);
			}

			if (beanManager.isStereotype(annotation.annotationType())) {
				annotations.addAll(beanManager.getStereotypeDefinition(annotation.annotationType()));
			}
		}
		return null;
	}

	public static ELProcessor getELProcessor(BeanManager beanManager) {
		ELProcessor elProcessor = new ELProcessor();
		elProcessor.getELManager().addELResolver(beanManager.getELResolver());

		return elProcessor;
	}
}
