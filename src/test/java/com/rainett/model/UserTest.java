package com.rainett.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    @DisplayName("Test equals and hashcode")
    void testEqualsAndHashCode() {
        User u1 = new User();
        u1.setId(1L);
        User u2 = new User();
        u2.setId(2L);
        assertNotEquals(u1, u2);
        u2.setId(1L);
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        u2 = new Trainee();
        assertNotEquals(u1, u2);
    }
}