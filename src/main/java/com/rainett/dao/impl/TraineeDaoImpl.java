package com.rainett.dao.impl;

import com.rainett.dao.TraineeDao;
import com.rainett.model.Trainee;
import com.rainett.storage.DataStorage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TraineeDaoImpl implements TraineeDao {
    @Autowired
    private DataStorage<Trainee> dataStorage;

    @Override
    public Trainee save(Trainee trainee) {
        return dataStorage.save(trainee);
    }

    @Override
    public Trainee findByUserId(Long userId) {
        return dataStorage.findById(Trainee.class, userId);
    }

    @Override
    public Trainee deleteByUserId(Long userId) {
        return dataStorage.delete(findByUserId(userId));
    }

    @Override
    public List<Trainee> findAll() {
        return dataStorage.findAll(Trainee.class);
    }
}
