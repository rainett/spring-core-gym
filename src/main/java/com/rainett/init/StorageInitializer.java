package com.rainett.init;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Slf4j
@RequiredArgsConstructor
public class StorageInitializer {
    private final String dataFile;
    private final DataSource dataSource;

    public void initializeData() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource(dataFile));
        populator.execute(dataSource);
    }
}
