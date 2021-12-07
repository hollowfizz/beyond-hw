package beyond.homework.model.dto;

import beyond.homework.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The User Response Data Transfer Object
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserResponseDTO
{
    private Long id;

    private String username;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    /**
     * Instantiates a new User response dto.
     *
     * @param user the user
     */
    public UserResponseDTO(User user)
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }
}
