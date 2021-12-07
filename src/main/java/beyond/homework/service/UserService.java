package beyond.homework.service;

import beyond.homework.log.Logger;
import beyond.homework.model.User;
import beyond.homework.model.dto.UserRequestDTO;
import beyond.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for handling users
 */
@Transactional
@Service
public class UserService
{
    private final UserRepository userRepository;

    private final Logger log;

    /**
     * Instantiates a new User service.
     *
     * @param userRepository the user repository
     * @param log            the log
     */
    @Autowired
    public UserService(UserRepository userRepository, Logger log)
    {
        this.userRepository = userRepository;
        this.log = log;
    }

    /**
     * Create user.
     *
     * @param userRequestDTO the user request dto
     * @return the user
     */
    public User createUser(UserRequestDTO userRequestDTO)
    {
        log.trace("Enter createUser");

        if (userRequestDTO == null || !userRequestDTO.isValid())
        {
            log.error("User object is null or invalid!", null);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User object is null or invalid!");
        }

        Optional<User> existingUser = this.userRepository.findByUsernameIgnoreCase(userRequestDTO.getUsername());
        if (existingUser.isPresent())
        {
            log.error("User already exists in the system!", null);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists in the system!");
        }

        User newUser = User.builder()
                .username(userRequestDTO.getUsername())
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .password(new BCryptPasswordEncoder().encode("test"))
                .build();

        log.trace("Exit createUser");
        return this.userRepository.save(newUser);
    }

    /**
     * Update user.
     *
     * @param id             the id
     * @param userRequestDTO the user request dto
     * @return the user
     */
    public User updateUser(Long id, UserRequestDTO userRequestDTO)
    {
        log.trace("Enter updateUser");
        if (userRequestDTO == null)
        {
            log.error("User object is null!", null);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User object is null!");
        }

        Optional<User> existingUser = this.userRepository.findById(id);
        if (!existingUser.isPresent())
        {
            log.error("User does not exists!", null);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists!");
        }

        User userToUpdate = existingUser.get();
        String firstName = userRequestDTO.getFirstName();
        String lastName = userRequestDTO.getLastName();

        if (firstName != null && !firstName.isEmpty())
        {
            log.info("First name updated from: " + userToUpdate.getFirstName() + " to: " + firstName);
            userToUpdate.setFirstName(firstName);
        }

        if (lastName != null && !lastName.isEmpty())
        {
            log.info("Last name updated from: " + userToUpdate.getLastName() + " to: " + lastName);
            userToUpdate.setLastName(lastName);
        }

        log.trace("Exit updateUser");
        return this.userRepository.save(userToUpdate);
    }

    /**
     * Gets all users.
     *
     * @return all users
     */
    public List<User> getAllUsers()
    {
        log.trace("Enter getAllUsers");
        List<User> userList = this.userRepository.findAll();
        if (userList.isEmpty())
        {
            log.error("No user found in the system!", null);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found in the system!");
        }
        log.trace("Exit getAllUsers");
        return userList;
    }

    /**
     * Gets user.
     *
     * @param id the id
     * @return the user
     */
    public User getUser(Long id)
    {
        log.trace("Enter getUser");
        Optional<User> user = this.userRepository.findById(id);
        if (!user.isPresent())
        {
            log.error("No user found with id: " + id + "!", null);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with id: " + id + "!");
        }
        log.trace("Exit getUser");
        return user.get();
    }
}
