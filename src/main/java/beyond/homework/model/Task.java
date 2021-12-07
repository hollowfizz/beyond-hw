package beyond.homework.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Task entity
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Data
@Builder
@Table(name = "TASKS")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Task extends Audit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Date dateTime;

    @Column
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
