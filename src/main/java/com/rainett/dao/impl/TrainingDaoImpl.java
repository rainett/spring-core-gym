package com.rainett.dao.impl;

import com.rainett.dao.TrainingDao;
import com.rainett.model.Training;
import com.rainett.storage.DataStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingDaoImpl implements TrainingDao {
    private static final String NAMESPACE = "trainings";
    private final DataStorage dataStorage;

    @Override
    public void save(Training training) {
        dataStorage.getNamespace(NAMESPACE).put(training.getId(), training);
    }

    @Override
    public Training findById(Long id) {
        return (Training) dataStorage.getNamespace(NAMESPACE).get(id);
    }
}
