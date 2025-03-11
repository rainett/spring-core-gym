package com.rainett.storage.impl;

import com.rainett.annotations.Id;
import com.rainett.exceptions.EntityIdException;
import com.rainett.exceptions.EntityNotFoundException;
import com.rainett.exceptions.IdNotFoundException;
import com.rainett.storage.DataStorage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InMemoryStorage<T> implements DataStorage<T> {
    private final Map<Class<?>, Table<T>> tables = new HashMap<>();

    @Override
    public T save(T entity) {
        log.info("Saving entity: {}", entity);
        if (!tables.containsKey(entity.getClass())) {
            tables.put(entity.getClass(), new Table<>());
        }
        Table<T> table = tables.get(entity.getClass());
        return table.save(entity);
    }

    @Override
    public T findById(Class<T> entityClass, Long id) {
        log.info("Finding entity by id: {}", id);
        if (!tables.containsKey(entityClass)) {
            throw new EntityNotFoundException(
                    "No entity found for class: " + entityClass.getSimpleName());
        }
        Table<T> table = tables.get(entityClass);
        T entity = table.get(id);
        if (entity != null) {
            return entity;
        }
        throw new EntityNotFoundException("No entity found for id: " + id);
    }

    @Override
    public T delete(T entity) {
        log.info("Deleting entity: {}", entity);
        if (!tables.containsKey(entity.getClass())) {
            throw new EntityNotFoundException(
                    "No entity found for class: " + entity.getClass().getSimpleName());
        }
        Table<T> table = tables.get(entity.getClass());
        return table.delete(entity);
    }

    @Override
    public List<T> findAll(Class<? extends T> entityClass) {
        log.info("Finding all entities for class: {}", entityClass);
        return tables.get(entityClass).getAll();
    }

    private static class Table<T> {
        private final Map<Long, T> data = new HashMap<>();
        private final AtomicLong idGenerator = new AtomicLong();

        public T save(T entity) {
            Field field = getIdField(entity);
            Long fieldValue = getFieldValue(entity, field);
            if (fieldValue != null) {
                data.put(fieldValue, entity);
            } else {
                Long id = idGenerator.getAndIncrement();
                data.put(id, entity);
                setFieldValue(entity, field, id);
            }
            return entity;
        }

        public T get(Long id) {
            return data.get(id);
        }

        public List<T> getAll() {
            return new ArrayList<>(data.values());
        }

        public T delete(T entity) {
            Long entityId = getFieldValue(entity, getIdField(entity));
            if (data.containsKey(entityId)) {
                return data.remove(entityId);
            }
            throw new EntityNotFoundException("No entity found for id: " + entityId);
        }

        private static <T> Field getIdField(T entity) {
            return Arrays.stream(entity.getClass().getDeclaredFields())
                    .filter(f -> f.getType() == Long.class)
                    .filter(f -> f.isAnnotationPresent(Id.class))
                    .findFirst()
                    .orElseThrow(() -> new IdNotFoundException(entity.getClass().getSimpleName()));
        }

        private static <T> Long getFieldValue(T entity, Field field) {
            try {
                field.setAccessible(true);
                return (Long) field.get(entity);
            } catch (IllegalAccessException e) {
                throw new EntityIdException("Failed to get field value", e);
            }
        }

        private static <T> void setFieldValue(T entity, Field field, Long id) {
            field.setAccessible(true);
            try {
                field.set(entity, id);
            } catch (IllegalAccessException e) {
                throw new EntityIdException("Failed to set field value", e);
            }
        }
    }
}
