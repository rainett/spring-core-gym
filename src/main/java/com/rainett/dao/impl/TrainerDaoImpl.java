package com.rainett.dao.impl;

import com.rainett.dao.TrainerDao;
import com.rainett.model.Trainer;
import com.rainett.storage.DataStorage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainerDaoImpl implements TrainerDao {
    @Autowired
    private DataStorage<Trainer> dataStorage;

    @Override
    public Trainer save(Trainer trainer) {
        return dataStorage.save(trainer);
    }

    @Override
    public Trainer findByUserId(Long userId) {
        return dataStorage.findById(Trainer.class, userId);
    }

    @Override
    public List<Trainer> findAll() {
        return dataStorage.findAll(Trainer.class);
    }
}
