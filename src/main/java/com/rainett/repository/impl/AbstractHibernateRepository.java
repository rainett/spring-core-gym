package com.rainett.repository.impl;

import com.rainett.repository.GenericRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractHibernateRepository<T> implements GenericRepository<T> {
    private final Class<T> entityClass;
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
    public T update(T entity) {
        log.debug("Updating entity: {}", entity);
        return getCurrentSession().merge(entity);
    }

    @Override
    public Optional<T> findById(Long id) {
        log.debug("Finding entity by id: {}", id);
        return Optional.ofNullable(getCurrentSession().get(entityClass, id));
    }

    @Override
    public void delete(T entity) {
        log.debug("Deleting entity: {}", entity);
        getCurrentSession().remove(entity);
    }
}
