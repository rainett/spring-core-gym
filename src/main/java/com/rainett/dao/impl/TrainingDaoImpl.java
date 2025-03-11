package com.rainett.dao.impl;

import com.rainett.dao.TrainingDao;
import com.rainett.model.Training;
import com.rainett.storage.DataStorage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainingDaoImpl implements TrainingDao {
    @Autowired
    private DataStorage<Training> dataStorage;

    @Override
    public Training save(Training training) {
        return dataStorage.save(training);
    }

    @Override
    public Training findById(Long id) {
        return dataStorage.findById(Training.class, id);
    }

    @Override
    public List<Training> findAll() {
        return dataStorage.findAll(Training.class);
    }
}
