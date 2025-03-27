package com.rainett.repository;

import java.util.Optional;

public interface GenericRepository<T> {
    T save(T entity);

    T update(T entity);

    Optional<T> findById(Long id);

    void delete(T entity);
}
