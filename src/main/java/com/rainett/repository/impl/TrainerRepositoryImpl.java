package com.rainett.repository.impl;

import com.rainett.model.Trainer;
import com.rainett.repository.TrainerRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerRepositoryImpl extends AbstractHibernateRepository<Trainer>
        implements TrainerRepository {

    public TrainerRepositoryImpl(SessionFactory sessionFactory) {
        super(Trainer.class, sessionFactory);
    }

    @Override
    public List<Trainer> findByUsernames(List<String> usernames) {
        return getCurrentSession()
                .createQuery("from Trainer where username in :usernames", Trainer.class)
                .setParameter("usernames", usernames)
                .getResultList();
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return getCurrentSession()
                .createQuery("from Trainer where username=:username", Trainer.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public List<Trainer> findWithoutTraineeByUsername(String username) {
        return getCurrentSession()
                .createQuery("select distinct t from Trainer t " +
                             "left join t.trainees tr " +
                             "with tr.username = :username " +
                             "where tr is null", Trainer.class)
                .setParameter("username", username)
                .getResultList();
    }
}
