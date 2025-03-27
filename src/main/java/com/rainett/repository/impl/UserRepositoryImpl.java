package com.rainett.repository.impl;

import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserRepositoryImpl extends AbstractHibernateRepository<User>
        implements UserRepository {
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    @Override
    public long findSuffixUsernameCount(String initialUsername) {
        log.debug("Finding suffix username count for {}", initialUsername);
        return getCurrentSession()
                .createQuery(
                        "select count(*) from User " +
                        "where username=:initialUsername " +
                        "or username like concat(:initialUsername, '.%') ",
                        Long.class)
                .setParameter("initialUsername", initialUsername)
                .getSingleResult();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        log.debug("Finding user by username {}", username);
        return getCurrentSession()
                .createQuery("from User where username=:username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }
}
