package timekeeper.users.models;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @ApiModelProperty(notes = "The database generated employee ID")
  private Long userId;

  @Column(name = "first_name", nullable = false)
  @ApiModelProperty(notes = "The users first name")
  private String firstName;

  @Column(name = "last_name", nullable = false)
  @ApiModelProperty(notes = "The users last name")
  private String lastName;

  @Column(name = "email_address", nullable = false)
  @ApiModelProperty(notes = "The users email address")
  private String emailAddress;

  @Column(name = "approver_id")
  @ApiModelProperty(notes = "The userId of the user that can approve this users absences")
  private Long approverId;
}
