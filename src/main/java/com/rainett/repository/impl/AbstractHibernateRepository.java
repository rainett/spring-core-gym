package com.rainett.repository.impl;

import com.rainett.repository.GenericRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractHibernateRepository<T> implements GenericRepository<T> {
    private final SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public T save(T entity) {
        log.debug("Saving entity: {}", entity);
        getCurrentSession().persist(entity);
        return entity;
    }

    @Override
    public void delete(T entity) {
        log.debug("Deleting entity: {}", entity);
        getCurrentSession().remove(entity);
    }
}
