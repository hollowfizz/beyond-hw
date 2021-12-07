package beyond.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Parent class which responsibility to populate and update CREATE_DATE and UPDATE_DATE columns in the database for the entities
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createDate", "updateDate"}, allowGetters = true)
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@SuperBuilder
public abstract class Audit implements Serializable
{
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE", nullable = false, updatable = false)
    @CreatedDate
    @JsonIgnoreProperties
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_DATE", nullable = false)
    @LastModifiedDate
    @JsonIgnoreProperties
    private Date updateDate;
}
