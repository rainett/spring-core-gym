package com.rainett.storage;

import java.util.Map;

public interface DataStorage {
    Map<String, Object> getNamespace(String namespace);
}
