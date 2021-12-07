package beyond.homework.repository;

import beyond.homework.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * The interface for Task repository.
 */
public interface TaskRepository extends JpaRepository<Task, Long>
{
    /**
     * Find all user by user id.
     *
     * @param userId the user id
     * @return the list
     */
    List<Task> findAllByUserId(Long userId);

    /**
     * Find user by user id and task id.
     *
     * @param userId the user id
     * @param taskId the task id
     * @return the optional task
     */
    @Query(value = "SELECT * FROM tasks WHERE user_id = ?1 AND id = ?2", nativeQuery = true)
    Optional<Task> findByUserIdAndTaskId(Long userId, Long taskId);

    /**
     * Find all pending tasks.
     *
     * @return the list
     */
    @Query(value = "SELECT * FROM tasks WHERE UPPER(status) = UPPER('pending')", nativeQuery = true)
    List<Task> findAllPendingTasks();
}
