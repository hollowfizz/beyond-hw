package beyond.homework.service;

import beyond.homework.log.Logger;
import beyond.homework.model.User;
import beyond.homework.model.dto.UserRequestDTO;
import beyond.homework.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
public class UserServiceTest
{

    @Mock
    private UserRepository userRepository;

    @Mock
    private Logger log;

    @InjectMocks
    private UserService userService;

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
    public void createUser_Present()
    {
        Mockito.when(this.userRepository.findByUsernameIgnoreCase(any(String.class)))
                .thenReturn(Optional.of(this.createTestUser()));
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.userService.createUser(this.createTestUserRequestDTO_Create()));

        assertTrue(exception.getMessage()
                .contains("400"));
        assertTrue(exception.getMessage()
                .contains("User already exists in the system!"));
    }

    @Test
    public void createUser_Null()
    {
        Mockito.when(this.userRepository.findByUsernameIgnoreCase(any(String.class)))
                .thenReturn(null);
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.userService.createUser(null));

        assertTrue(exception.getMessage()
                .contains("400"));
        assertTrue(exception.getMessage()
                .contains("User object is null or invalid!"));
    }

    @Test
    public void createUser_Invalid()
    {
        Mockito.when(this.userRepository.findByUsernameIgnoreCase(any(String.class)))
                .thenReturn(null);
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.userService.createUser(this.createTestUserRequestDTO_Invalid()));

        assertTrue(exception.getMessage()
                .contains("400"));
        assertTrue(exception.getMessage()
                .contains("User object is null or invalid!"));
    }

    @Test
    public void createUser()
    {
        Mockito.when(this.userRepository.findByUsernameIgnoreCase(any(String.class)))
                .thenReturn(Optional.empty());
        Mockito.when(this.userRepository.save(any()))
                .thenReturn(this.createTestUserFromDTO(this.createTestUserRequestDTO_Create()));
        User createdUser = this.userService.createUser(this.createTestUserRequestDTO_Create());

        assertEquals("user4", createdUser.getUsername());
        assertEquals("Joe", createdUser.getFirstName());
        assertEquals("Little", createdUser.getLastName());
    }

    @Test
    public void updateUser_Null()
    {
        Mockito.when(this.userRepository.findById(any(Long.class)))
                .thenReturn(null);
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.userService.updateUser(1L, null));

        assertTrue(exception.getMessage()
                .contains("400"));
        assertTrue(exception.getMessage()
                .contains("User object is null!"));
    }

    @Test
    public void updateUser_NotPresent()
    {
        Mockito.when(this.userRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.userService.updateUser(1L, this.createTestUserRequestDTO_Update()));

        assertTrue(exception.getMessage()
                .contains("404"));
        assertTrue(exception.getMessage()
                .contains("User does not exists!"));
    }

    @Test
    public void updateUser()
    {
        Mockito.when(this.userRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(this.createTestUser()));
        Mockito.when(this.userRepository.save(any()))
                .thenReturn(this.createTestUpdatedUser());
        User updatedUser = this.userService.updateUser(1L, this.createTestUserRequestDTO_Update());

        assertEquals("admin", updatedUser.getUsername());
        assertEquals("Jan", updatedUser.getFirstName());
        assertEquals("Doe", updatedUser.getLastName());
    }

    @Test
    public void getAllUsers()
    {
        Mockito.when(this.userRepository.findAll())
                .thenReturn(this.createTestUserList());
        List<User> userList = this.userService.getAllUsers();

        assertEquals("user2", userList.get(1)
                .getUsername());
        assertEquals("John", userList.get(1)
                .getFirstName());
        assertEquals("Smith", userList.get(1)
                .getLastName());

        assertEquals("user3", userList.get(2)
                .getUsername());
        assertEquals("James", userList.get(2)
                .getFirstName());
        assertEquals("Bond", userList.get(2)
                .getLastName());
    }

    @Test
    public void getAllUsers_Empty()
    {
        Mockito.when(this.userRepository.findAll())
                .thenReturn(Collections.EMPTY_LIST);
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.userService.getAllUsers());

        assertTrue(exception.getMessage()
                .contains("404"));
        assertTrue(exception.getMessage()
                .contains("No user found in the system!"));
    }

    @Test
    public void getUser()
    {
        Mockito.when(this.userRepository.findById(1L))
                .thenReturn(Optional.of(this.createTestUser()));
        User user = this.userService.getUser(1L);

        assertEquals("admin", user.getUsername());
        assertEquals("Daniel", user.getFirstName());
        assertEquals("Gerendas", user.getLastName());
    }

    @Test
    public void getAllUsers_NotPresent()
    {
        Mockito.when(this.userRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> this.userService.getUser(1L));

        assertTrue(exception.getMessage()
                .contains("404"));
        assertTrue(exception.getMessage()
                .contains("No user found with id: 1!"));
    }

    private User createTestUser()
    {
        return User.builder()
                .username("admin")
                .firstName("Daniel")
                .lastName("Gerendas")
                .build();
    }

    private User createTestUpdatedUser()
    {
        return User.builder()
                .username("admin")
                .firstName("Jan")
                .lastName("Doe")
                .build();
    }

    private List<User> createTestUserList()
    {
        User user1 = User.builder()
                .username("admin")
                .firstName("Daniel")
                .lastName("Gerendas")
                .build();

        User user2 = User.builder()
                .username("user2")
                .firstName("John")
                .lastName("Smith")
                .build();

        User user3 = User.builder()
                .username("user3")
                .firstName("James")
                .lastName("Bond")
                .build();

        return new ArrayList<>(Arrays.asList(user1, user2, user3));
    }

    private UserRequestDTO createTestUserRequestDTO_Create()
    {
        return UserRequestDTO.builder()
                .username("user4")
                .firstName("Joe")
                .lastName("Little")
                .build();
    }

    private UserRequestDTO createTestUserRequestDTO_Update()
    {
        return UserRequestDTO.builder()
                .firstName("Jan")
                .lastName("Doe")
                .build();
    }

    private UserRequestDTO createTestUserRequestDTO_Invalid()
    {
        return UserRequestDTO.builder()
                .firstName("John")
                .lastName("Little")
                .build();
    }

    private User createTestUserFromDTO(UserRequestDTO userRequestDTO)
    {
        return User.builder()
                .username(userRequestDTO.getUsername())
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .password(new BCryptPasswordEncoder().encode("test"))
                .build();
    }
}