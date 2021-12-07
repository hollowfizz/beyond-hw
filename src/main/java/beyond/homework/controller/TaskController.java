package beyond.homework.controller;

import beyond.homework.log.Logger;
import beyond.homework.model.dto.TaskRequestDTO;
import beyond.homework.model.dto.TaskResponseDTO;
import beyond.homework.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller to manage tasks
 */
@RestController
@RequestMapping("/api")
public class TaskController
{
    private final TaskService taskService;

    private final Logger log;

    /**
     * Instantiates a new Task controller.
     *
     * @param taskService the task service
     * @param log         the log
     */
    @Autowired
    public TaskController(TaskService taskService, Logger log)
    {
        this.taskService = taskService;
        this.log = log;
    }

    /**
     * Create task entity.
     *
     * @param userId         the user id
     * @param taskRequestDTO the task request dto
     * @return the newly created task
     */
    @PostMapping(path = "/user/{user_id}/task", produces = "application/json", consumes = "application/json")
    public ResponseEntity<TaskResponseDTO> createTask(@PathVariable("user_id") Long userId, @RequestBody TaskRequestDTO taskRequestDTO)
    {
        log.trace("Enter createTask (controller)");
        return ResponseEntity.ok(new TaskResponseDTO(this.taskService.createTask(userId, taskRequestDTO)));
    }

    /**
     * Update task entity.
     *
     * @param userId         the user id
     * @param taskId         the task id
     * @param taskRequestDTO the task request dto
     * @return the updated task
     */
    @PutMapping(path = "/user/{user_id}/task/{task_id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId, @RequestBody TaskRequestDTO taskRequestDTO)
    {
        log.trace("Enter updateTask (controller)");
        return ResponseEntity.ok(new TaskResponseDTO(this.taskService.updateTask(userId, taskId, taskRequestDTO)));
    }

    /**
     * Delete task entity.
     *
     * @param userId the user id
     * @param taskId the task id
     * @return empty body and HTTP status OK
     */
    @DeleteMapping(path = "/user/{user_id}/task/{task_id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> deleteTask(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId)
    {
        log.trace("Enter deleteTask (controller)");
        this.taskService.deleteTask(userId, taskId);
        return ResponseEntity.ok()
                .build();
    }

    /**
     * Gets task.
     *
     * @param userId the user id
     * @param taskId the task id
     * @return the task
     */
    @GetMapping(path = "/user/{user_id}/task/{task_id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId)
    {
        log.trace("Enter getTask (controller)");
        return ResponseEntity.ok(new TaskResponseDTO(this.taskService.getTask(userId, taskId)));
    }

    /**
     * Gets all tasks.
     *
     * @param userId the user id
     * @return all tasks
     */
    @GetMapping(path = "/user/{user_id}/task", produces = "application/json", consumes = "application/json")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@PathVariable("user_id") Long userId)
    {
        log.trace("Enter getAllTasks (controller)");
        return ResponseEntity.ok(this.taskService.getAllTasks(userId)
                .stream()
                .map(TaskResponseDTO::new)
                .collect(Collectors.toList()));
    }
}
