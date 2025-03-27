package com.rainett.init;

import com.rainett.exceptions.DataProcessingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class StorageInitializer {
    private final String dataFile;
    private final SessionFactory sessionFactory;

    public StorageInitializer(String dataFile, SessionFactory sessionFactory) {
        this.dataFile = dataFile;
        this.sessionFactory = sessionFactory;
    }

    public void initializeData() {
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
