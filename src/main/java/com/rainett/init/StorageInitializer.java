package com.rainett.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.model.Training;
import com.rainett.storage.DataStorage;
import com.rainett.storage.impl.InMemoryStorage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class StorageInitializer implements BeanPostProcessor {
    @Value("${data.file}")
    private String dataFile;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof InMemoryStorage) {
            loadDataFromFile(dataFile, (DataStorage) bean);
        }
        return bean;
    }

    private void loadDataFromFile(String dataFile, DataStorage dataStorage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper()
                    .registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());
            DataWrapper dataWrapper = objectMapper.readValue(new File(dataFile), DataWrapper.class);
            processTrainees(dataWrapper, dataStorage);
            processTrainers(dataWrapper, dataStorage);
            processTrainings(dataWrapper, dataStorage);
        } catch (IOException e) {
            throw new DataLoadException("Failed to load data from file: " + dataFile, e);
        }
    }

    private void processTrainings(DataWrapper dataWrapper, DataStorage dataStorage) {
        Map<Long, Object> trainingStorage = dataStorage.getNamespace("trainings");
        dataWrapper.getTrainings().forEach(training ->
                trainingStorage.put(training.getId(), training)
        );
    }

    private void processTrainers(DataWrapper dataWrapper, DataStorage dataStorage) {
        Map<Long, Object> trainerStorage = dataStorage.getNamespace("trainers");
        for (Trainer trainer : dataWrapper.getTrainers()) {
            trainerStorage.put(trainer.getUserId(), trainer);
            dataStorage.addUsername(trainer.getUsername(), trainer.getUserId());
        }
    }

    private void processTrainees(DataWrapper dataWrapper, DataStorage dataStorage) {
        Map<Long, Object> traineeStorage = dataStorage.getNamespace("trainees");
        for (Trainee trainee : dataWrapper.getTrainees()) {
            traineeStorage.put(trainee.getUserId(), trainee);
            dataStorage.addUsername(trainee.getUsername(), trainee.getUserId());
        }
    }


    @Data
    private static class DataWrapper {
        private List<Trainee> trainees;
        private List<Trainer> trainers;
        private List<Training> trainings;
    }

}
