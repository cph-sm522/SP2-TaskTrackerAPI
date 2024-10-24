package dat.dtos;

import dat.entities.Task;
import dat.security.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private int userId;

    private String username;
    private String password;
    private String email;

    private List<TaskDTO> tasks;
    private Set<Role> roles;

    public UserDTO(int userId, String username, String email, List<TaskDTO> tasks) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.tasks = tasks;
    }

    public UserDTO(int i, String username, Object o, Object o1, Set<String> roles) {
    }
}
