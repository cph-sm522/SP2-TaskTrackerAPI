package dat.entities;

import dat.security.entities.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String username;
    private String password;
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Task> tasks;

    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_name", referencedColumnName = "username")}, inverseJoinColumns = {@JoinColumn(name = "role_name", referencedColumnName = "name")})
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Role> roles = new HashSet<>();

    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, this.password);
    }

    public User(String userName, String userPass) {
        this.username = userName;
        this.password = BCrypt.hashpw(userPass, BCrypt.gensalt());
    }

    public User(String userName, Set<Role> roleList) {
        this.username = userName;
        this.roles = roleList;
    }

    public void addRole(Role role) {
        if (role == null) {
            return;
        }
        roles.add(role);
        role.getUsers().add(this);
    }
}
