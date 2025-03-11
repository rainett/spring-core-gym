package com.rainett.init;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.rainett.exceptions.DataLoadException;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.model.Training;
import com.rainett.storage.impl.InMemoryStorage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

@ExtendWith(MockitoExtension.class)
class StorageInitializerTest {

    @InjectMocks
    private StorageInitializer storageInitializer;

    @Mock
    private InMemoryStorage inMemoryStorage;

    private File tempDataFile;

    @BeforeEach
    void setUp() throws IOException {
        tempDataFile = File.createTempFile("data", ".json");
        try (FileWriter writer = new FileWriter(tempDataFile)) {
            writer.write("""
                {
                  "trainees": [
                    { "userId": 1, "firstName": "John", "lastName": "Doe", "username": "john.doe" }
                  ],
                  "trainers": [
                    { "userId": 2, "firstName": "Jane", "lastName": "Smith", "username": "jane.smith" }
                  ],
                  "trainings": [
                    { "id": 3, "name": "Morning Strength Session" }
                  ]
                }
            """);
        }
    }

    @Test
    @SneakyThrows
    void testPostProcessAfterInitialization_WithInMemoryStorage_LoadsDataSuccessfully() {
        Field dataFile = storageInitializer.getClass().getDeclaredField("dataFile");
        dataFile.setAccessible(true);
        ReflectionUtils.setField(dataFile, storageInitializer, tempDataFile.getAbsolutePath());

        when(inMemoryStorage.save(any(Trainee.class))).thenReturn(new Trainee());
        when(inMemoryStorage.save(any(Trainer.class))).thenReturn(new Trainer());
        when(inMemoryStorage.save(any(Training.class))).thenReturn(new Training());

        BeanPostProcessor postProcessor = storageInitializer;
        Object result = postProcessor.postProcessAfterInitialization(inMemoryStorage, "inMemoryStorage");

        verify(inMemoryStorage, times(1)).save(any(Trainee.class));
        verify(inMemoryStorage, times(1)).save(any(Trainer.class));
        verify(inMemoryStorage, times(1)).save(any(Training.class));
        Assertions.assertEquals(inMemoryStorage, result, "Bean should be returned as is");
    }

    @Test
    void testPostProcessAfterInitialization_NonStorageBean_NoInteraction() {
        Object nonStorageBean = new Object();

        Object result = storageInitializer.postProcessAfterInitialization(nonStorageBean, "someBean");

        verifyNoInteractions(inMemoryStorage);
        Assertions.assertEquals(nonStorageBean, result, "Non-storage beans should be returned as is");
    }

    @Test
    @SneakyThrows
    void testLoadDataFromFile_WhenFileIsInvalid_ThrowsException() {
        String invalidFilePath = "/invalid/path/data.json";
        Field dataFile = storageInitializer.getClass().getDeclaredField("dataFile");
        dataFile.setAccessible(true);
        ReflectionUtils.setField(dataFile, storageInitializer, invalidFilePath);

        DataLoadException exception = Assertions.assertThrows(
                DataLoadException.class,
                () -> storageInitializer.postProcessAfterInitialization(inMemoryStorage, "inMemoryStorage")
        );

        Assertions.assertTrue(exception.getMessage().contains("Failed to load data from file"));
    }

    @AfterEach
    void tearDown() {
        if (tempDataFile != null && tempDataFile.exists()) {
            tempDataFile.delete();
        }
    }
}
