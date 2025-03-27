package com.rainett.repository.impl;

import com.rainett.repository.GenericRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@RequiredArgsConstructor
public abstract class AbstractHibernateRepository<T> implements GenericRepository<T> {
    private final Class<T> entityClass;
    private final SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public T save(T entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        return getCurrentSession().merge(entity);
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(getCurrentSession().get(entityClass, id));
    }

    @Override
    public void delete(T entity) {
        getCurrentSession().remove(entity);
    }
}
