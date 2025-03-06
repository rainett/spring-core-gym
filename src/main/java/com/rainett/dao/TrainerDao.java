package com.rainett.dao;

import com.rainett.model.Trainer;

public interface TrainerDao {
    void save(Trainer trainer);

    Trainer findByUserId(Long userId);
}
