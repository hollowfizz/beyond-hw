package beyond.homework.controller;

import beyond.homework.log.Logger;
import beyond.homework.model.dto.UserRequestDTO;
import beyond.homework.model.dto.UserResponseDTO;
import beyond.homework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api")
public class UserController
{

    private final UserService userService;

    private final Logger log;

    /**
     * Instantiates a new User controller.
     *
     * @param userService the user service
     * @param log         the log
     */
    @Autowired
    public UserController(UserService userService, Logger log)
    {
        this.userService = userService;
        this.log = log;
    }

    /**
     * Create user response entity.
     *
     * @param userRequestDTO the user request dto
     * @return the newly created user
     */
    @PostMapping(path = "/user", produces = "application/json", consumes = "application/json")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO)
    {
        log.trace("Enter createUser (controller)");
        return ResponseEntity.ok(new UserResponseDTO(this.userService.createUser(userRequestDTO)));
    }

    /**
     * Update user response entity.
     *
     * @param id             the id
     * @param userRequestDTO the user request dto
     * @return the updated user
     */
    @PutMapping(path = "/user/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") Long id, @RequestBody UserRequestDTO userRequestDTO)
    {
        log.trace("Enter updateUser (controller)");
        return ResponseEntity.ok(new UserResponseDTO(this.userService.updateUser(id, userRequestDTO)));
    }

    /**
     * Gets all users.
     *
     * @return all users
     */
    @GetMapping(path = "/user", produces = "application/json", consumes = "application/json")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers()
    {
        log.trace("Enter getAllUsers (controller)");
        return ResponseEntity.ok(this.userService.getAllUsers()
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList()));
    }

    /**
     * Gets user.
     *
     * @param id the id
     * @return the user
     */
    @GetMapping(path = "/user/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("id") Long id)
    {
        log.trace("Enter getUser (controller)");
        return ResponseEntity.ok(new UserResponseDTO(this.userService.getUser(id)));
    }
}
