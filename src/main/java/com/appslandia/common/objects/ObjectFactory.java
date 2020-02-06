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
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ReflectionException;
import com.appslandia.common.utils.ReflectionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ObjectFactory extends InitializeObject {

	final List<ObjectInstance> instances = new ArrayList<>();

	@Override
	protected void init() throws Exception {
		validateFactory();
	}

	private void validateFactory() throws ObjectException {
		for (ObjectInstance inst : this.instances) {
			if (inst.definition.getProducer() != null) {
				continue;
			}
			new InjectTraverser() {

				@Override
				public boolean isValidationContext() {
					return true;
				}

				@Override
				public void onParameter(Parameter parameter) throws ObjectException {
					validateInject(parameter.getType(), AnnotationUtils.parseQualifiers(parameter), parameter);
				}

				@Override
				public void onField(Field field) throws ObjectException {
					validateInject(field.getType(), AnnotationUtils.parseQualifiers(field), field);
				}

				@Override
				public void onMethod(Method method) throws ObjectException {
					throw new UnsupportedOperationException();
				}

				private String toMemberInfo(AnnotatedElement member) {
					if (member instanceof Parameter) {
						return ((Parameter) member).getDeclaringExecutable().toString();
					}
					return member.toString();
				}

				private void validateInject(Class<?> type, Annotation[] qualifiers, AnnotatedElement member) throws ObjectException {
					if (ObjectFactory.class.isAssignableFrom(type)) {
						return;
					}
					if (type == Instance.class) {
						return;
					}
					int count = countMatchesForInject(type, qualifiers);
					if (count == 0) {
						throw new ObjectException(
								"Unsatisfied dependency (type=" + type + ", qualifiers=" + Arrays.toString(qualifiers) + ", member=" + toMemberInfo(member) + ")");
					}
					if (count > 1) {
						throw new ObjectException("Ambiguous dependency (type=" + type + ", qualifiers=" + Arrays.toString(qualifiers) + ", member=" + toMemberInfo(member) + ")");
					}
				}

			}.traverse(inst.definition.getImplClass());
		}
	}

	private int countMatchesForInject(Class<?> type, Annotation[] qualifiers) {
		int count = 0;
		for (ObjectInstance inst : this.instances) {
			if ((inst.definition.hasType(type) || (type == Object.class)) && AnnotationUtils.equals(inst.definition.getQualifiers(), qualifiers)) {
				count++;
			}
		}
		return count;
	}

	public <T> ObjectFactory register(Class<T> type, Annotation[] qualifiers, ObjectProducer<T> producer) {
		return register(type, qualifiers, ObjectScope.SINGLETON, producer);
	}

	public <T> ObjectFactory register(Class<T> type, Annotation[] qualifiers, ObjectScope scope, ObjectProducer<T> producer) {
		assertNotInitialized();
		AssertUtils.assertNotNull(type);
		AssertUtils.assertNotNull(scope);
		AssertUtils.assertNotNull(producer);

		ObjectInstance inst = new ObjectInstance(new ObjectDefinition().setTypes(new Class<?>[] { type }).setQualifiers(qualifiers).setScope(scope).setProducer(producer));
		this.instances.add(inst);
		return this;
	}

	public <T> ObjectFactory register(Class<T> type, Class<? extends T> implClass) {
		AssertUtils.assertNotNull(implClass);
		return register(type, AnnotationUtils.parseQualifiers(implClass), ObjectScope.SINGLETON, implClass);
	}

	public <T> ObjectFactory register(Class<T> type, ObjectScope scope, Class<? extends T> implClass) {
		AssertUtils.assertNotNull(implClass);
		return register(type, AnnotationUtils.parseQualifiers(implClass), scope, implClass);
	}

	public <T> ObjectFactory register(Class<T> type, Annotation[] qualifiers, ObjectScope scope, Class<? extends T> implClass) {
		assertNotInitialized();
		AssertUtils.assertNotNull(type);
		AssertUtils.assertNotNull(scope);
		AssertUtils.assertNotNull(implClass);

		Class<?>[] types = (type != implClass) ? new Class<?>[] { type, implClass } : new Class<?>[] { type };
		ObjectInstance inst = new ObjectInstance(new ObjectDefinition().setTypes(types).setQualifiers(qualifiers).setScope(scope).setImplClass(implClass));
		this.instances.add(inst);
		return this;
	}

	public ObjectFactory unregister(Class<?> type) {
		return unregister(type, ReflectionUtils.EMPTY_ANNOTATIONS);
	}

	public ObjectFactory unregister(Class<?> type, Annotation... qualifiers) {
		assertNotInitialized();
		AssertUtils.assertNotNull(type);
		AssertUtils.assertNotNull(qualifiers);

		Iterator<ObjectInstance> iter = this.instances.iterator();
		while (iter.hasNext()) {
			ObjectInstance inst = iter.next();
			if ((inst.definition.hasType(type) || (type == Object.class)) && AnnotationUtils.equals(inst.definition.getQualifiers(), qualifiers)) {
				iter.remove();
			}
		}
		return this;
	}

	public <T> ObjectFactory unregister(Class<T> type, Class<? extends T> implClass) {
		AssertUtils.assertNotNull(implClass);
		return unregister(type, AnnotationUtils.parseQualifiers(implClass));
	}

	public ObjectFactory inject(final Object obj) throws ObjectException {
		initialize();
		AssertUtils.assertNotNull(obj);

		new InjectTraverser() {

			@Override
			public boolean isValidationContext() {
				return false;
			}

			@Override
			public void onParameter(Parameter parameter) throws ObjectException {
				throw new UnsupportedOperationException();
			}

			@Override
			public void onField(Field field) throws ObjectException {
				try {
					field.setAccessible(true);
					Annotation[] qualifiers = AnnotationUtils.parseQualifiers(field);
					// @Inject T t;
					if (field.getType() != Instance.class) {
						Object value = getObject(field.getType(), qualifiers);
						field.set(obj, value);
						return;
					}
					// @Inject @Instance<T>
					AssertUtils.assertTrue(field.getGenericType() instanceof ParameterizedType);
					Type argType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
					AssertUtils.assertTrue(argType instanceof Class);
					Class<?> type = (Class<?>) argType;

					List<ObjectInstance> insts = new ArrayList<>();
					for (ObjectInstance inst : instances) {
						if ((inst.definition.hasType(type) || (type == Object.class)) && AnnotationUtils.hasAnnotations(inst.definition.getQualifiers(), qualifiers)) {
							insts.add(new ObjectInstance(inst.definition).setInstance(produceObject(inst)));
						}
					}
					field.set(obj, new InstanceImpl<>(type, qualifiers, insts));

				} catch (ObjectException ex) {
					throw ex;
				} catch (Exception ex) {
					throw new ObjectException(ex);
				}
			}

			@Override
			public void onMethod(Method method) throws ObjectException {
				try {
					method.setAccessible(true);
					method.invoke(obj, createArguments(method.getParameters()));

				} catch (ObjectException ex) {
					throw ex;
				} catch (Exception ex) {
					throw new ObjectException(ex);
				}
			}
		}.traverse(obj.getClass());
		return this;
	}

	private Object[] createArguments(Parameter[] parameters) throws ObjectException {
		Object[] args = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			args[i] = getObject(parameter.getType(), AnnotationUtils.parseQualifiers(parameter));
		}
		return args;
	}

	private Object produceObject(ObjectInstance inst) throws ObjectException {
		try {
			// Producer
			if (inst.definition.getProducer() != null) {
				return inst.definition.getProducer().produce(this);
			}
			// Constructor
			Constructor<?> emptyCtor = null, injectCtor = null;
			for (Constructor<?> ctor : inst.definition.getImplClass().getDeclaredConstructors()) {
				if (ctor.getDeclaredAnnotation(Inject.class) != null) {
					injectCtor = ctor;
					break;
				}
				if (ctor.getParameterCount() == 0) {
					emptyCtor = ctor;
				}
			}
			if ((injectCtor == null) && (emptyCtor == null)) {
				throw new ObjectException("Could not instantiate (implClass=" + inst.definition.getImplClass() + ")");
			}
			Object instance = null;
			if (injectCtor != null) {
				injectCtor.setAccessible(true);
				instance = injectCtor.newInstance(createArguments(injectCtor.getParameters()));
			} else {
				emptyCtor.setAccessible(true);
				instance = emptyCtor.newInstance(ReflectionUtils.EMPTY_OBJECTS);
			}
			return this.inject(instance).postConstruct(instance);

		} catch (ObjectException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ObjectException(ex);
		}
	}

	private ObjectInstance getObjectInst(Class<?> type, Annotation[] qualifiers) throws ObjectException {
		ObjectInstance obj = null;
		for (ObjectInstance inst : this.instances) {
			if ((inst.definition.hasType(type) || (type == Object.class)) && AnnotationUtils.equals(inst.definition.getQualifiers(), qualifiers)) {
				if (obj != null) {
					throw new ObjectException("Ambiguous dependency (type=" + type + ", qualifiers=" + Arrays.toString(qualifiers));
				}
				obj = inst;
			}
		}
		return obj;
	}

	public <T, I extends T> I getObject(Class<T> type) throws ObjectException {
		return getObject(type, ReflectionUtils.EMPTY_ANNOTATIONS);
	}

	public <T, I extends T> I getObject(Class<T> type, Annotation... qualifiers) throws ObjectException {
		initialize();
		AssertUtils.assertNotNull(type);
		AssertUtils.assertNotNull(qualifiers);

		if (ObjectFactory.class.isAssignableFrom(type)) {
			return ObjectUtils.cast(this);
		}

		ObjectInstance inst = getObjectInst(type, qualifiers);
		if (inst == null) {
			throw new ObjectException("Unsatisfied dependency (type=" + type + ", qualifiers=" + Arrays.toString(qualifiers));
		}
		// PROTOTYPE
		if (inst.definition.getScope() == ObjectScope.PROTOTYPE) {
			return ObjectUtils.cast(produceObject(inst));
		}
		// SINGLETON
		Object obj = inst.getInstance();
		if (obj == null) {
			synchronized (this.mutex) {
				if ((obj = inst.getInstance()) == null) {
					inst.setInstance(obj = produceObject(inst));
				}
			}
		}
		return ObjectUtils.cast(obj);
	}

	public <T> T postConstruct(T obj) throws ObjectException {
		initialize();
		AssertUtils.assertNotNull(obj);
		ReflectionUtils.traverse(obj.getClass(), new ReflectionUtils.MethodHandler() {

			@Override
			public boolean matches(Method m) {
				return m.getDeclaredAnnotation(PostConstruct.class) != null;
			}

			@Override
			public boolean handle(Method m) throws ReflectionException {
				try {
					m.setAccessible(true);
					m.invoke(obj);
				} catch (ObjectException ex) {
					throw ex;
				} catch (Exception ex) {
					throw new ObjectException(ex);
				}
				return false;
			}
		});
		return obj;
	}

	public <T> T preDestroy(T obj) throws ObjectException {
		initialize();
		AssertUtils.assertNotNull(obj);
		ObjectFactoryUtils.destroy(obj);
		return obj;
	}

	@Override
	public void destroy() throws ObjectException {
		for (ObjectInstance inst : this.instances) {
			Object obj = inst.getInstance();
			if (obj == null) {
				continue;
			}
			if (inst.definition.getProducer() == null) {
				preDestroy(obj);
			} else {
				inst.definition.getProducer().destroy(obj);
			}
			inst.setInstance(null);
		}
	}

	public Iterator<ObjectDefinition> getDefinitionIterator() {
		return new Iterator<ObjectDefinition>() {
			int index = -1;

			@Override
			public ObjectDefinition next() {
				ObjectInstance inst = instances.get(++this.index);
				return inst.definition;
			}

			@Override
			public boolean hasNext() {
				return this.index < instances.size() - 1;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	static abstract class InjectTraverser {

		public abstract boolean isValidationContext();

		public abstract void onParameter(Parameter parameter) throws ObjectException;

		public abstract void onField(Field field) throws ObjectException;

		public abstract void onMethod(Method method) throws ObjectException;

		public void traverse(Class<?> implClass) throws ObjectException {
			Class<?> clazz = null;
			if (isValidationContext()) {
				// Constructor
				for (Constructor<?> ctor : implClass.getDeclaredConstructors()) {
					if (ctor.getDeclaredAnnotation(Inject.class) != null) {
						for (Parameter parameter : ctor.getParameters()) {
							onParameter(parameter);
						}
						break;
					}
				}

				// Method
				clazz = implClass;
				while (clazz != Object.class) {
					Method[] methods = clazz.getDeclaredMethods();
					for (Method method : methods) {
						if (method.getDeclaredAnnotation(Inject.class) != null) {
							for (Parameter parameter : method.getParameters()) {
								onParameter(parameter);
							}
						}
					}
					clazz = clazz.getSuperclass();
				}
			}

			// Fields
			clazz = implClass;
			while (clazz != Object.class) {
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					if (field.getDeclaredAnnotation(Inject.class) != null) {
						onField(field);
					}
				}
				clazz = clazz.getSuperclass();
			}

			// Injection Context
			if (!isValidationContext()) {
				clazz = implClass;
				while (clazz != Object.class) {
					Method[] methods = clazz.getDeclaredMethods();
					for (Method method : methods) {
						if (method.getDeclaredAnnotation(Inject.class) != null) {
							onMethod(method);
						}
					}
					clazz = clazz.getSuperclass();
				}
			}
		}
	}
}
