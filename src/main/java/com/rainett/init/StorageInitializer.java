package com.rainett.init;

import com.rainett.exceptions.DataProcessingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Slf4j
@RequiredArgsConstructor
public class StorageInitializer {
    private final String dataFile;
    private final SessionFactory sessionFactory;

    public void initializeData() {
        log.info("Loading data from file: {}", dataFile);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            String sql = getQuery(dataFile);
            session.createNativeMutationQuery(sql).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            throw new DataProcessingException("Failed to initialize data", e);
        }
    }

    private String getQuery(String dataFile) {
        try (var stream = Files.lines(Paths.get(dataFile))) {
            return stream.collect(Collectors.joining(" "));
        } catch (IOException e) {
            throw new DataProcessingException("Failed to load data from file", e);
        }
    }
}
