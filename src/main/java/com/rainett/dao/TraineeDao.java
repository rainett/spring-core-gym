package com.rainett.dao;

import com.rainett.model.Trainee;
import java.util.List;

public interface TraineeDao {
    Trainee save(Trainee trainee);

    Trainee findByUserId(Long userId);

    Trainee deleteByUserId(Long userId);

    List<Trainee> findAll();
}
