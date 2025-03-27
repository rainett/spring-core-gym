package com.rainett.repository.impl;

import com.rainett.model.Trainee;
import com.rainett.repository.TraineeRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TraineeRepositoryImpl extends AbstractHibernateRepository<Trainee>
        implements TraineeRepository {

    public TraineeRepositoryImpl(SessionFactory sessionFactory) {
        super(Trainee.class, sessionFactory);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        log.debug("Finding trainee by username {}", username);
        return getCurrentSession()
                .createQuery("from Trainee where username=:username", Trainee.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }
}
