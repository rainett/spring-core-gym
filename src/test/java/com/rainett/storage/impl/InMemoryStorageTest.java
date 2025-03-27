package com.rainett.storage.impl;

import com.rainett.annotations.Id;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class InMemoryStorageTest {

    private InMemoryStorage<TestEntity> storage;

    @BeforeEach
    void setUp() {
        storage = new InMemoryStorage<>();
    }

    @Data
    public static class TestEntity {
        @Id
        private Long id;
        private String name;

        public TestEntity(String name) {
            this.name = name;
        }
    }

    @Test
    void testSaveNewEntity() {
        TestEntity entity = new TestEntity("Entity1");
        TestEntity saved = storage.save(entity);

        assertNotNull(saved.getId(), "ID should be assigned after saving");
        assertEquals("Entity1", saved.getName(), "Name should be preserved after saving");
    }

    @Test
    void testFindById() {
        TestEntity entity = new TestEntity("Entity2");
        TestEntity saved = storage.save(entity);
        Long id = saved.getId();

        TestEntity found = storage.findById(TestEntity.class, id);
        assertNotNull(found, "Entity should be found by its ID");
        assertEquals(id, found.getId(), "Found entity's ID should match");
        assertEquals("Entity2", found.getName(), "Found entity's name should match");
    }

    @Test
    void testDeleteEntity() {
        TestEntity entity = new TestEntity("Entity3");
        TestEntity saved = storage.save(entity);
        Long id = saved.getId();

        TestEntity deleted = storage.delete(saved);
        assertEquals(saved, deleted, "Deleted entity should be the same as the saved entity");

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> storage.findById(TestEntity.class, id));
        assertTrue(exception.getMessage().contains("No entity found for id"),
                "Expected exception message to mention missing entity id");
    }

    @Test
    void testFindAll() {
        TestEntity entity1 = new TestEntity("Entity4");
        TestEntity entity2 = new TestEntity("Entity5");
        storage.save(entity1);
        storage.save(entity2);

        List<TestEntity> allEntities = storage.findAll(TestEntity.class);
        assertEquals(2, allEntities.size(), "findAll should return exactly 2 entities");
    }

    @Test
    void testFindByIdNonExistent() {
        assertThrows(EntityNotFoundException.class, () -> {
            storage.findById(TestEntity.class, 999L);
        }, "findById should throw an exception for non-existent ID");
    }

    @Test
    void testDeleteNonExistentEntity() {
        TestEntity entity = new TestEntity("NonExistent");
        assertThrows(EntityNotFoundException.class,
                () -> storage.delete(entity), "delete should throw an exception for an unsaved entity");
    }

    @Test
    void testSaveExistingEntity() {
        TestEntity entity = new TestEntity("Entity6");
        entity.setId(100L);
        TestEntity saved = storage.save(entity);

        assertEquals(100L, saved.getId(), "The existing ID should be preserved");

        TestEntity found = storage.findById(TestEntity.class, 100L);
        assertEquals("Entity6", found.getName(), "Found entity should match the saved data");
    }
}
