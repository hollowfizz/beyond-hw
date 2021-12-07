package beyond.homework.service;

import beyond.homework.log.Logger;
import beyond.homework.model.Task;
import beyond.homework.model.User;
import beyond.homework.model.dto.TaskRequestDTO;
import beyond.homework.repository.TaskRepository;
import beyond.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Business logic for handling tasks
 */
@Transactional
@Service
public class TaskService
{
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final Logger log;

    /**
     * Instantiates a new Task service.
     *
     * @param taskRepository the task repository
     * @param userRepository the user repository
     * @param log            the log
     */
    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, Logger log)
    {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.log = log;
    }

    /**
     * Create task.
     *
     * @param userId         the user id
     * @param taskRequestDTO the task request dto
     * @return the task
     */
    public Task createTask(Long userId, TaskRequestDTO taskRequestDTO)
    {
        log.trace("Enter createTask");
        if (taskRequestDTO == null || !taskRequestDTO.isValid())
        {
            log.error("Task object is invalid!", null);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task object is invalid!");
        }

        User user = this.getUserIfExists(userId);

        Date dateTime;
        try

        {
            dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(taskRequestDTO.getDateTime());
        }
        catch (ParseException ex)
        {
            log.error("Failed to parse date from string: " + taskRequestDTO.getDateTime(), ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse date from string: " + taskRequestDTO.getDateTime());
        }

        Task newTask = Task.builder()
                .name(taskRequestDTO.getName())
                .description(taskRequestDTO.getDescription())
                .dateTime(dateTime)
                .user(user)
                .status("pending")
                .build();

        log.trace("Exit createTask");
        return this.taskRepository.save(newTask);
    }

    /**
     * Update task.
     *
     * @param userId         the user id
     * @param taskId         the task id
     * @param taskRequestDTO the task request dto
     * @return the task
     */
    public Task updateTask(Long userId, Long taskId, TaskRequestDTO taskRequestDTO)
    {
        log.trace("Enter updateTask");
        if (taskRequestDTO == null)
        {
            log.error("Task object is null!", null);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task object is null!");
        }

        this.getUserIfExists(userId);

        Optional<Task> task = this.taskRepository.findById(taskId);
        if (!task.isPresent())
        {
            log.error("Task does not exists with id: " + taskId + "!", null);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task does not exists with id: " + taskId + "!");
        }

        Task taskToUpdate = task.get();
        String name = taskRequestDTO.getName();
        String description = taskRequestDTO.getDescription();
        String date = taskRequestDTO.getDateTime();

        if (taskRequestDTO.getName() != null && !taskRequestDTO.getName()
                .isEmpty())
        {
            log.info("Name updated from: " + taskToUpdate.getName() + " to: " + name);
            taskToUpdate.setName(name);
        }

        if (description != null && !description.isEmpty())
        {
            log.info("Description updated from: " + taskToUpdate.getDescription() + " to: " + description);
            taskToUpdate.setDescription(description);
        }

        if (date != null && !date.isEmpty())
        {
            Date dateTime;
            try
            {
                dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(taskRequestDTO.getDateTime());
            }
            catch (ParseException ex)
            {
                log.error("Failed to parse date from string: " + taskRequestDTO.getDateTime(), ex);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse date from string: " + taskRequestDTO.getDateTime());
            }
            taskToUpdate.setDateTime(dateTime);
        }

        log.trace("Exit updateTask");
        return this.taskRepository.save(taskToUpdate);
    }

    /**
     * Gets all tasks.
     *
     * @param userId the user id
     * @return the all tasks
     */
    public List<Task> getAllTasks(Long userId)
    {
        log.trace("Enter getAllTasks");

        User user = this.getUserIfExists(userId);

        List<Task> taskList = this.taskRepository.findAllByUserId(userId);
        if (taskList.isEmpty())
        {
            log.error("No tasks found in the system for user: " + user.getUsername() + "!", null);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tasks found in the system for user: " + user.getUsername() + "!");
        }

        log.trace("Exit getAllTasks");
        return taskList;
    }

    /**
     * Gets task.
     *
     * @param userId the user id
     * @param taskId the task id
     * @return the task
     */
    public Task getTask(Long userId, Long taskId)
    {
        log.trace("Enter getTask");
        User user = this.getUserIfExists(userId);

        Optional<Task> task = this.taskRepository.findByUserIdAndTaskId(userId, taskId);
        if (!task.isPresent())
        {
            log.error("Task does not exists with id: " + taskId + " for user: " + user.getUsername() + "!", null);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task does not exists with id: " + taskId + " for user: " + user.getUsername() + "!");
        }

        log.trace("Exit getTask");
        return task.get();
    }

    /**
     * Delete task.
     *
     * @param userId the user id
     * @param taskId the task id
     */
    public void deleteTask(Long userId, Long taskId)
    {
        log.trace("Enter deleteTask");
        User user = this.getUserIfExists(userId);

        Optional<Task> task = this.taskRepository.findById(taskId);
        if (!task.isPresent())
        {
            log.error("Task does not exists with id: " + taskId + " for user: " + user.getUsername() + "!", null);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task does not exists with id: " + taskId + " for user: " + user.getUsername() + "!");
        }

        this.taskRepository.delete(task.get());
        log.trace("Exit deleteTask");
    }

    private User getUserIfExists(Long userId)
    {
        log.trace("Enter getUserIfExists");

        Optional<User> user = this.userRepository.findById(userId);
        if (!user.isPresent())
        {
            log.error("User does not exists!", null);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists!");
        }

        log.trace("Exit getUserIfExists");
        return user.get();
    }

    /**
     * Check pending tasks every 5 minutes.
     */
    @Scheduled(fixedDelay = 300000)
    public void findPendingTasks()
    {
        log.trace("Enter findPendingTasks");
        System.out.println("Scheduler running");

        List<Task> doneTasks = this.taskRepository.findAllPendingTasks()
                .stream()
                .filter(t -> t.getDateTime().getTime() < new Date().getTime())
                .collect(Collectors.toList());

        for (Task task : doneTasks)
        {
            task.setStatus("done");
            System.out.println("Task: " + task.getName() + " is done!");
            log.info("Task: " + task.getName() + " is done!");
            this.taskRepository.save(task);
        }
        log.trace("Exit findPendingTasks");
    }
}
