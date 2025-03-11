package com.rainett.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppConfigTest {
    private final AppConfig appConfig = new AppConfig();

    @Test
    void testPropertyConfig() {
        assertNotNull(appConfig.propertyConfig());
    }
}