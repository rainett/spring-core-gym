package com.rainett.storage.impl;

import com.rainett.storage.DataStorage;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStorage implements DataStorage {
    private final Map<String, Map<Long, Object>> data = new HashMap<>();
    private final Map<String, Long> usernameIndex = new HashMap<>();

    public InMemoryStorage() {
        data.put("trainees", new HashMap<>());
        data.put("trainers", new HashMap<>());
        data.put("trainings", new HashMap<>());
    }

    @Override
    public Map<Long, Object> getNamespace(String namespace) {
        return data.get(namespace);
    }

    @Override
    public boolean usernameExists(String username) {
        return usernameIndex.containsKey(username);
    }
}
