package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Set<String> roles;

    public UserDTO(int userId, String username, String email, List<TaskDTO> tasks, Set<String> roles) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.tasks = tasks;
        this.roles = roles;
    }
}
