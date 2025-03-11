package com.rainett.dao;

import com.rainett.model.Trainer;
import java.util.List;

public interface TrainerDao {
    Trainer save(Trainer trainer);

    Trainer findByUserId(Long userId);

    List<Trainer> findAll();
}
