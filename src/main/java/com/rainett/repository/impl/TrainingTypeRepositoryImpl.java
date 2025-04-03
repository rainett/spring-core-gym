package com.rainett.repository.impl;

import com.rainett.model.TrainingType;
import com.rainett.repository.TrainingTypeRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TrainingTypeRepositoryImpl extends AbstractHibernateRepository<TrainingType>
        implements TrainingTypeRepository {

    public TrainingTypeRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<TrainingType> findByName(String name) {
        log.debug("Finding training type by name {}", name);
        return getCurrentSession()
                .createQuery("from TrainingType where name=:name", TrainingType.class)
                .setParameter("name", name)
                .uniqueResultOptional();
    }
}
