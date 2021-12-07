package beyond.homework.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The Task Request Data Transfer Object
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TaskRequestDTO
{
    private Long id;

    private String name;

    private String description;

    @JsonProperty("date_time")
    private String dateTime;

    /**
     * Check if TaskRequestDTO object is valid when creating a new task
     *
     * @return the boolean
     */
    public boolean isValid()
    {
        if (this.name == null || this.name.isEmpty())
        {
            return false;
        }
        if (this.description == null || this.description.isEmpty())
        {
            return false;
        }

        if (this.dateTime == null || this.dateTime.isEmpty())
        {
            return false;
        }

        return true;
    }
}
