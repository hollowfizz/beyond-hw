package beyond.homework.repository;

import beyond.homework.log.Log;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface for Log repository.
 */
public interface LogRepository extends JpaRepository<Log, Long>
{
}
