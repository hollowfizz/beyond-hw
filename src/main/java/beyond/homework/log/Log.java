package beyond.homework.log;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Data
@Builder
@Table(name = "LOGS")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Log
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String message;

    @Column
    private String log_level;

    @SuppressWarnings("JpaDataSourceORMInspection")
    @Column(name = "exception")
    @Lob
    private String exception;

    @Column
    private Timestamp log_time;
}
