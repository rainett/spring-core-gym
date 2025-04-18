package com.rainett.repository;

import com.rainett.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("""
            SELECT u.username
            FROM User u
            WHERE LOWER(u.username) = LOWER(:initialUsername)
                OR LOWER(u.username) LIKE LOWER(CONCAT(:initialUsername, '.%'))
            """)
    List<String> findUsernamesByInitialUsername(String initialUsername);
}
