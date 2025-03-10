package com.rainett.dao.impl;

import com.rainett.dao.TrainingDao;
import com.rainett.model.Training;
import com.rainett.storage.DataStorage;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainingDaoImpl implements TrainingDao {
    private static final String NAMESPACE = "trainings";
    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    @Autowired
    private DataStorage dataStorage;

    @Override
    public Long save(Training training) {
        Long id = Optional.ofNullable(training.getId())
                .orElseGet(ID_GENERATOR::getAndIncrement);
        dataStorage.getNamespace(NAMESPACE).put(id, training);
        return id;
    }

    @Override
    public Training findById(Long id) {
        return (Training) dataStorage.getNamespace(NAMESPACE).get(id);
    }
}
