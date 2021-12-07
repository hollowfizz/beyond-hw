package beyond.homework.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The Task Response Data Transfer Object
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserRequestDTO
{
    private Long id;

    private String username;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    /**
     * Check if UserRequestDTO object is valid when creating a new user
     *
     * @return the boolean
     */
    public boolean isValid()
    {
        if (this.username == null || this.username.isEmpty())
        {
            return false;
        }
        if (this.firstName == null || this.firstName.isEmpty())
        {
            return false;
        }
        if (this.lastName == null || this.lastName.isEmpty())
        {
            return false;
        }

        return true;
    }
}
