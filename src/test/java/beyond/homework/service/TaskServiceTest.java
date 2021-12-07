package beyond.homework.service;

import beyond.homework.log.Logger;
import beyond.homework.model.Task;
import beyond.homework.model.User;
import beyond.homework.model.dto.TaskRequestDTO;
import beyond.homework.repository.TaskRepository;
import beyond.homework.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
public class TaskServiceTest
{

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Logger log;

    @InjectMocks
    private TaskService taskService;

    @Before
    public void init()
    {
        MockitoAnnotations.openMocks(this);
        Mockito.doNothing()
                .when(this.log)
                .trace(any(String.class));
        Mockito.doNothing()
                .when(this.log)
                .error(any(String.class), any());
        Mockito.doNothing()
                .when(this.log)
                .info(any(String.class));
    }

    @Test
    public void createTask_Invalid()
    {
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.createTask(1L, this.createTestTaskRequestDTO_Invalid()));

        assertTrue(exception.getMessage()
                .contains("400"));
        assertTrue(exception.getMessage()
                .contains("Task object is invalid!"));
    }

    @Test
    public void createTask_UserNotPresent()
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.createTask(1L, this.createTestTaskRequestDTO_Create()));

        assertTrue(exception.getMessage()
                .contains("404"));
        assertTrue(exception.getMessage()
                .contains("User does not exists!"));
    }

    @Test
    public void createTask_ParseException()
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUser()));
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.createTask(1L, this.createTestTaskRequestDTO_Create_InvalidDate()));

        assertTrue(exception.getMessage()
                .contains("400"));
        assertTrue(exception.getMessage()
                .contains("Failed to parse date from string: 2021-12-06 1:"));
    }

    @Test
    public void createTask() throws ParseException
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUser()));
        Mockito.when(this.taskRepository.save(any()))
                .thenReturn(this.createTestTask());
        Task task = this.taskService.createTask(1L, this.createTestTaskRequestDTO_Create());

        Date dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-01-01 10:00:00");

        assertEquals("Task", task.getName());
        assertEquals("Description", task.getDescription());
        assertEquals(dateTime, task.getDateTime());
        assertEquals("pending", task.getStatus());
    }

    @Test
    public void updateTask_Null()
    {
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.updateTask(1L, 1L, null));

        assertTrue(exception.getMessage()
                .contains("400"));
        assertTrue(exception.getMessage()
                .contains("Task object is null!"));
    }

    @Test
    public void updateTask_UserNotPresent()
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.updateTask(1L, 1L, this.createTestTaskRequestDTO_Update()));

        assertTrue(exception.getMessage()
                .contains("404"));
        assertTrue(exception.getMessage()
                .contains("User does not exists!"));
    }

    @Test
    public void updateTask_NotPresent()
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUser()));
        Mockito.when(this.taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.updateTask(1L, 1L, this.createTestTaskRequestDTO_Update()));

        assertTrue(exception.getMessage()
                .contains("404"));
        assertTrue(exception.getMessage()
                .contains("Task does not exists with id: 1!"));
    }

    @Test
    public void updateTask_ParseException() throws ParseException
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUser()));
        Mockito.when(this.taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(this.createTestTask()));
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.updateTask(1L, 1L, this.createTestTaskRequestDTO_Create_InvalidDate()));

        assertTrue(exception.getMessage()
                .contains("400"));
        assertTrue(exception.getMessage()
                .contains("Failed to parse date from string: 2021-12-06 1:"));
    }

    @Test
    public void updateTask() throws ParseException
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUser()));
        Mockito.when(this.taskRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUpdatedTask()));
        Mockito.when(this.taskRepository.save(any()))
                .thenReturn(this.createTestUpdatedTask());
        Task task = this.taskService.updateTask(1L, 1L, this.createTestTaskRequestDTO_Update());

        Date dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-01-01 10:00:00");

        assertEquals("Task", task.getName());
        assertEquals("Updated description", task.getDescription());
        assertEquals(dateTime, task.getDateTime());
        assertEquals("pending", task.getStatus());
    }

    @Test
    public void getAllTasks_UserNotPresent()
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.getAllTasks(1L));

        assertTrue(exception.getMessage()
                .contains("404"));
        assertTrue(exception.getMessage()
                .contains("User does not exists!"));
    }

    @Test
    public void getAllTasks_Empty()
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUser()));
        Mockito.when(this.taskRepository.findAllByUserId(any(Long.class)))
                .thenReturn(Collections.EMPTY_LIST);
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.getAllTasks(1L));

        assertTrue(exception.getMessage()
                .contains("404"));
        assertTrue(exception.getMessage()
                .contains("No tasks found in the system for user: admin!"));
    }

    @Test
    public void getAllTasks() throws ParseException
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUser()));
        Mockito.when(this.taskRepository.findAllByUserId(any(Long.class)))
                .thenReturn(this.createTestTaskList());
        List<Task> taskList = this.taskService.getAllTasks(1L);

        assertEquals("Task1", taskList.get(0)
                .getName());
        assertEquals("Description1", taskList.get(0)
                .getDescription());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-02-02 22:12:10"), taskList.get(0)
                .getDateTime());
        assertEquals("done", taskList.get(0)
                .getStatus());
        assertEquals("admin", taskList.get(0)
                .getUser()
                .getUsername());

        assertEquals("Task2", taskList.get(1)
                .getName());
        assertEquals("Description2", taskList.get(1)
                .getDescription());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-12-14 13:52:00"), taskList.get(1)
                .getDateTime());
        assertEquals("pending", taskList.get(1)
                .getStatus());
        assertEquals("admin", taskList.get(1)
                .getUser()
                .getUsername());

        assertEquals("Task3", taskList.get(2)
                .getName());
        assertEquals("Description3", taskList.get(2)
                .getDescription());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-01-27 05:17:00"), taskList.get(2)
                .getDateTime());
        assertEquals("done", taskList.get(2)
                .getStatus());
        assertEquals("admin", taskList.get(2)
                .getUser()
                .getUsername());
    }

    @Test
    public void getTask_UserNotPresent()
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.getTask(1L, 1L));

        assertTrue(exception.getMessage()
                .contains("404"));
        assertTrue(exception.getMessage()
                .contains("User does not exists!"));
    }

    @Test
    public void getTask_NotPresent()
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUser()));
        Mockito.when(this.taskRepository.findByUserIdAndTaskId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.getTask(1L, 1L));

        assertTrue(exception.getMessage()
                .contains("404"));
        assertTrue(exception.getMessage()
                .contains("Task does not exists with id: 1 for user: admin!"));
    }

    @Test
    public void getTask() throws ParseException
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUser()));
        Mockito.when(this.taskRepository.findByUserIdAndTaskId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.of(this.createTestTask()));
        Task task = this.taskService.getTask(1L, 1L);

        assertEquals("Task", task.getName());
        assertEquals("Description", task.getDescription());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-01-01 10:00:00"), task.getDateTime());
        assertEquals("pending", task.getStatus());
        assertEquals("admin", task.getUser()
                .getUsername());
    }

    @Test
    public void deleteTask_UserNotPresent()
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.deleteTask(1L, 1L));

        assertTrue(exception.getMessage()
                .contains("404"));
        assertTrue(exception.getMessage()
                .contains("User does not exists!"));
    }

    @Test
    public void deleteTask_NotPresent()
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUser()));
        Mockito.when(this.taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.taskService.deleteTask(1L, 1L));

        assertTrue(exception.getMessage()
                .contains("404"));
        assertTrue(exception.getMessage()
                .contains("Task does not exists with id: 1 for user: admin!"));
    }

    @Test
    public void deleteTask() throws ParseException
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUser()));
        Mockito.when(this.taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(this.createTestTask()));
        this.taskService.deleteTask(1L, 1L);

        Mockito.verify(this.taskRepository, Mockito.times(1))
                .delete(any());
    }

    private TaskRequestDTO createTestTaskRequestDTO_Create()
    {
        return TaskRequestDTO.builder()
                .name("Task1")
                .description("Description1")
                .dateTime("2021-12-06 14:47:00")
                .build();
    }

    private TaskRequestDTO createTestTaskRequestDTO_Update()
    {
        return TaskRequestDTO.builder()
                .description("Description1")
                .build();
    }

    private TaskRequestDTO createTestTaskRequestDTO_Create_InvalidDate()
    {
        return TaskRequestDTO.builder()
                .name("Task1")
                .description("Description1")
                .dateTime("2021-12-06 1:")
                .build();
    }

    private TaskRequestDTO createTestTaskRequestDTO_Invalid()
    {
        return TaskRequestDTO.builder()
                .description("Description1")
                .dateTime("2021-12-06 14:47:00")
                .build();
    }

    private Task createTestTask() throws ParseException
    {
        return Task.builder()
                .name("Task")
                .description("Description")
                .dateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-01-01 10:00:00"))
                .status("pending")
                .user(this.createTestUser())
                .build();
    }

    private Task createTestUpdatedTask() throws ParseException
    {
        return Task.builder()
                .name("Task")
                .description("Updated description")
                .dateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-01-01 10:00:00"))
                .status("pending")
                .user(this.createTestUser())
                .build();
    }

    private List<Task> createTestTaskList() throws ParseException
    {
        Task task1 = Task.builder()
                .name("Task1")
                .description("Description1")
                .dateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-02-02 22:12:10"))
                .status("done")
                .user(this.createTestUser())
                .build();

        Task task2 = Task.builder()
                .name("Task2")
                .description("Description2")
                .dateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-12-14 13:52:00"))
                .status("pending")
                .user(this.createTestUser())
                .build();

        Task task3 = Task.builder()
                .name("Task3")
                .description("Description3")
                .dateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-01-27 05:17:00"))
                .status("done")
                .user(this.createTestUser())
                .build();

        return new ArrayList<>(Arrays.asList(task1, task2, task3));
    }

    private User createTestUser()
    {
        return User.builder()
                .username("admin")
                .firstName("Daniel")
                .lastName("Gerendas")
                .build();
    }
}