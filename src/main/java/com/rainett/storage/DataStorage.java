package com.rainett.storage;

import java.util.List;

public interface DataStorage<T> {
    T save(T entity);
    T findById(Class<T> entityClass, Long id);
    T delete(T entity);
    List<T> findAll(Class<? extends T> entityClass);
}
