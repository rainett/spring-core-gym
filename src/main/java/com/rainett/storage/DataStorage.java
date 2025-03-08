package com.rainett.storage;

import java.util.Map;

public interface DataStorage {
    Map<Long, Object> getNamespace(String namespace);
    boolean usernameExists(String username);
    void addUsername(String username, Long userId);
    void removeUsername(String username);
}
