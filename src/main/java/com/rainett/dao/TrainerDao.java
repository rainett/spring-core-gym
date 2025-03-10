package com.rainett.dao;

import com.rainett.model.Trainer;

public interface TrainerDao {
    Long save(Trainer trainer);

    Trainer findByUserId(Long userId);
}
