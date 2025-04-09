package com.rainett.repository;

import com.rainett.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT count(*) FROM User " +
           "WHERE username=:initialUsername " +
           "OR username LIKE concat(:initialUsername, '.%') ")
    long findSuffixUsernameCount(String initialUsername);

    Optional<User> findByUsername(String username);
}
