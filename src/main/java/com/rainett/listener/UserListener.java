package com.rainett.listener;

import com.rainett.model.User;

public interface UserListener {
    void prePersist(User user);
    void preUpdate(User user);
}
