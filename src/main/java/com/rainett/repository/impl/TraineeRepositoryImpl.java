package com.rainett.repository.impl;

import com.rainett.model.Trainee;
import com.rainett.repository.TraineeRepository;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeRepositoryImpl extends AbstractHibernateRepository<Trainee>
        implements TraineeRepository {

    public TraineeRepositoryImpl(SessionFactory sessionFactory) {
        super(Trainee.class, sessionFactory);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        return getCurrentSession()
                .createQuery("from Trainee where username=:username", Trainee.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }
}
