package beyond.homework.model.dto;

import beyond.homework.model.Task;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

/**
 * The Task Response Data Transfer Object
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TaskResponseDTO
{
    private Long id;

    private String name;

    private String description;

    private String status;

    @JsonProperty("date_time")
    private Date dateTime;

    /**
     * Instantiates a new Task response dto.
     *
     * @param task the task
     */
    public TaskResponseDTO(Task task)
    {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.dateTime = task.getDateTime();
        this.status = task.getStatus();
    }
}
