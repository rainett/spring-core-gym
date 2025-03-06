package com.rainett.storage;

import java.util.Map;

public interface DataStorage {
    Map<Long, Object> getNamespace(String namespace);
    boolean usernameExists(String username);
}
