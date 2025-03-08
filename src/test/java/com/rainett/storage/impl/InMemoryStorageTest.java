package com.rainett.storage.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryStorageTest {
    private InMemoryStorage inMemoryStorage;

    @BeforeEach
    void setUp() {
        inMemoryStorage = new InMemoryStorage();
    }

    @Test
    void testNamespaceInitialization() {
        Assertions.assertNotNull(inMemoryStorage.getNamespace("trainees"));
        Assertions.assertNotNull(inMemoryStorage.getNamespace("trainers"));
        Assertions.assertNotNull(inMemoryStorage.getNamespace("trainings"));
    }

    @Test
    void testUsernameIndex() {
        inMemoryStorage.addUsername("username", 1L);
        Assertions.assertTrue(inMemoryStorage.usernameExists("username"));
        Assertions.assertFalse(inMemoryStorage.usernameExists("nonexistent"));
        inMemoryStorage.removeUsername("username");
        Assertions.assertFalse(inMemoryStorage.usernameExists("username"));
    }
}