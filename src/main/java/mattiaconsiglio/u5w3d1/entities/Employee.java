package mattiaconsiglio.u5w3d1.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue
    @Setter(value = AccessLevel.NONE)
    private UUID id;
    @Column(unique = true)
    private String username;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true)
    private String email;
    private String photoUrl;
    private String password;

    public Employee(String username, String firstName, String lastName, String email, String photoUrl) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.password = username + firstName + lastName + email;
    }
}
