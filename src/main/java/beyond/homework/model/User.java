package beyond.homework.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity
 */
@Entity
@Data
@Builder
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User extends Audit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    @JsonProperty("first_name")
    private String firstName;

    @Column
    @JsonProperty("last_name")
    private String lastName;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Task> taskList = new ArrayList<>();
}
