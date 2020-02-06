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

package com.appslandia.common.jpa;

import java.util.List;
import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import com.appslandia.common.base.Params;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class EntityManagerAccessor implements EntityManager {

	private EntityManager em;

	public EntityManagerAccessor() {
	}

	public EntityManagerAccessor(EntityManager em) {
		this.em = em;
	}

	public void insert(Object entity) {
		this.em.persist(entity);
		this.em.flush();
	}

	public void insertRefresh(Object entity) {
		this.em.persist(entity);
		this.em.flush();
		this.em.refresh(entity);
	}

	public void removeByPk(Class<?> type, Object primaryKey) throws EntityNotFoundException {
		Object ref = this.em.getReference(type, primaryKey);
		this.em.remove(ref);
	}

	public boolean isInCache(Class<?> type, Object primaryKey) {
		Cache cache = this.em.getEntityManagerFactory().getCache();
		return (cache != null) && cache.contains(type, primaryKey);
	}

	public void evictCache() {
		Cache cache = this.em.getEntityManagerFactory().getCache();
		if (cache != null) {
			cache.evictAll();
		}
	}

	public void evictCache(Class<?> type) {
		Cache cache = this.em.getEntityManagerFactory().getCache();
		if (cache != null) {
			cache.evict(type);
		}
	}

	public void evictCache(Class<?> type, Object primaryKey) {
		Cache cache = this.em.getEntityManagerFactory().getCache();
		if (cache != null) {
			cache.evict(type, primaryKey);
		}
	}

	public <T> T findFetchGraph(Class<T> entityClass, Object primaryKey, String graphName) {
		return this.em.find(entityClass, primaryKey, new Params(1).put(JpaHints.HINT_JPA_FETCH_GRAPH, this.em.createEntityGraph(graphName)));
	}

	public <T> T findLoadGraph(Class<T> entityClass, Object primaryKey, String graphName) {
		return this.em.find(entityClass, primaryKey, new Params(1).put(JpaHints.HINT_JPA_LOAD_GRAPH, this.em.createEntityGraph(graphName)));
	}

	public <T> TypedQueryAccessor<T> createQueryFetchGraph(String qlString, Class<T> resultClass, String graphName) {
		return new TypedQueryAccessor<T>(this.em.createQuery(qlString, resultClass).setHint(JpaHints.HINT_JPA_FETCH_GRAPH, this.em.createEntityGraph(graphName)));
	}

	public <T> TypedQueryAccessor<T> createQueryLoadGraph(String qlString, Class<T> resultClass, String graphName) {
		return new TypedQueryAccessor<T>(this.em.createQuery(qlString, resultClass).setHint(JpaHints.HINT_JPA_LOAD_GRAPH, this.em.createEntityGraph(graphName)));
	}

	public <T> TypedQueryAccessor<T> createNamedQueryFetchGraph(String name, Class<T> resultClass, String graphName) {
		return new TypedQueryAccessor<T>(this.em.createNamedQuery(name, resultClass).setHint(JpaHints.HINT_JPA_FETCH_GRAPH, this.em.createEntityGraph(graphName)));
	}

	public <T> TypedQueryAccessor<T> createNamedQueryLoadGraph(String name, Class<T> resultClass, String graphName) {
		return new TypedQueryAccessor<T>(this.em.createNamedQuery(name, resultClass).setHint(JpaHints.HINT_JPA_LOAD_GRAPH, this.em.createEntityGraph(graphName)));
	}

	@Override
	public void remove(Object entity) {
		this.em.remove(entity);
	}

	@Override
	public void lock(Object entity, javax.persistence.LockModeType lockMode) {
		this.em.lock(entity, lockMode);
	}

	@Override
	public void lock(Object entity, javax.persistence.LockModeType lockMode, Map<String, Object> properties) {
		this.em.lock(entity, lockMode, properties);
	}

	@Override
	public void setProperty(String propertyName, Object value) {
		this.em.setProperty(propertyName, value);
	}

	@Override
	public void clear() {
		this.em.clear();
	}

	@Override
	public boolean contains(Object entity) {
		return this.em.contains(entity);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		return this.em.find(entityClass, primaryKey);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
		return this.em.find(entityClass, primaryKey, properties);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
		return this.em.find(entityClass, primaryKey, lockMode);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
		return this.em.find(entityClass, primaryKey, lockMode, properties);
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.em.getProperties();
	}

	@Override
	public void close() {
		this.em.close();
	}

	@Override
	public void flush() {
		this.em.flush();
	}

	@Override
	public <T> T merge(T entity) {
		return this.em.merge(entity);
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		return this.em.unwrap(cls);
	}

	@Override
	public boolean isOpen() {
		return this.em.isOpen();
	}

	@Override
	public void detach(Object entity) {
		this.em.detach(entity);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public QueryAccessor createNativeQuery(String sqlString, Class resultClass) {
		return new QueryAccessor(this.em.createNativeQuery(sqlString, resultClass));
	}

	@Override
	public QueryAccessor createNativeQuery(String sqlString) {
		return new QueryAccessor(this.em.createNativeQuery(sqlString));
	}

	@Override
	public QueryAccessor createNativeQuery(String sqlString, String resultSetMapping) {
		return new QueryAccessor(this.em.createNativeQuery(sqlString, resultSetMapping));
	}

	@Override
	public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
		return this.em.createNamedStoredProcedureQuery(name);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
		return this.em.createStoredProcedureQuery(procedureName, resultSetMappings);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
		return this.em.createStoredProcedureQuery(procedureName);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
		return this.em.createStoredProcedureQuery(procedureName, resultClasses);
	}

	@Override
	public boolean isJoinedToTransaction() {
		return this.em.isJoinedToTransaction();
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return this.em.getEntityManagerFactory();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return this.em.getCriteriaBuilder();
	}

	@Override
	public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
		return this.em.createEntityGraph(rootType);
	}

	@Override
	public EntityGraph<?> createEntityGraph(String graphName) {
		return this.em.createEntityGraph(graphName);
	}

	@Override
	public void persist(Object entity) {
		this.em.persist(entity);
	}

	@Override
	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		return this.em.getReference(entityClass, primaryKey);
	}

	@Override
	public void setFlushMode(javax.persistence.FlushModeType flushMode) {
		this.em.setFlushMode(flushMode);
	}

	@Override
	public FlushModeType getFlushMode() {
		return this.em.getFlushMode();
	}

	@Override
	public void refresh(Object entity, javax.persistence.LockModeType lockMode) {
		this.em.refresh(entity, lockMode);
	}

	@Override
	public void refresh(Object entity, javax.persistence.LockModeType lockMode, Map<String, Object> properties) {
		this.em.refresh(entity, lockMode, properties);
	}

	@Override
	public void refresh(Object entity) {
		this.em.refresh(entity);
	}

	@Override
	public void refresh(Object entity, Map<String, Object> properties) {
		this.em.refresh(entity, properties);
	}

	@Override
	public LockModeType getLockMode(Object entity) {
		return this.em.getLockMode(entity);
	}

	@Override
	public <T> TypedQueryAccessor<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		return new TypedQueryAccessor<T>(this.em.createQuery(criteriaQuery));
	}

	@Override
	public <T> TypedQueryAccessor<T> createQuery(String qlString, Class<T> resultClass) {
		return new TypedQueryAccessor<T>(this.em.createQuery(qlString, resultClass));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public QueryAccessor createQuery(CriteriaUpdate updateQuery) {
		return new QueryAccessor(this.em.createQuery(updateQuery));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public QueryAccessor createQuery(CriteriaDelete deleteQuery) {
		return new QueryAccessor(this.em.createQuery(deleteQuery));
	}

	@Override
	public QueryAccessor createQuery(String qlString) {
		return new QueryAccessor(this.em.createQuery(qlString));
	}

	@Override
	public <T> TypedQueryAccessor<T> createNamedQuery(String name, Class<T> resultClass) {
		return new TypedQueryAccessor<T>(this.em.createNamedQuery(name, resultClass));
	}

	@Override
	public QueryAccessor createNamedQuery(String name) {
		return new QueryAccessor(this.em.createNamedQuery(name));
	}

	@Override
	public void joinTransaction() {
		this.em.joinTransaction();
	}

	@Override
	public Object getDelegate() {
		return this.em.getDelegate();
	}

	@Override
	public EntityTransaction getTransaction() {
		return this.em.getTransaction();
	}

	@Override
	public Metamodel getMetamodel() {
		return this.em.getMetamodel();
	}

	@Override
	public EntityGraph<?> getEntityGraph(String graphName) {
		return this.em.getEntityGraph(graphName);
	}

	@Override
	public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
		return this.em.getEntityGraphs(entityClass);
	}
}
