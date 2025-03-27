package com.rainett.repository.impl;

import com.rainett.model.TrainingType;
import com.rainett.repository.TrainingTypeRepository;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeRepositoryImpl extends AbstractHibernateRepository<TrainingType>
        implements TrainingTypeRepository {

    public TrainingTypeRepositoryImpl(SessionFactory sessionFactory) {
        super(TrainingType.class, sessionFactory);
    }

    @Override
    public Optional<TrainingType> findByName(String name) {
        return getCurrentSession()
                .createQuery("from TrainingType where name=:name", TrainingType.class)
                .setParameter("name", name)
                .uniqueResultOptional();
    }
}
