package beyond.homework.repository;

import beyond.homework.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * The interface for User repository.
 */
public interface UserRepository extends JpaRepository<User, Long>
{
    /**
     * Find user by username ignore case.
     *
     * @param username the username
     * @return the optional user
     */
    Optional<User> findByUsernameIgnoreCase(String username);
}
