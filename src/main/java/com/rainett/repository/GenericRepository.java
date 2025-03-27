package com.rainett.repository;

public interface GenericRepository<T> {
    T save(T entity);

    void delete(T entity);
}
