package dat.mappers;

import dat.dtos.UserDTO;
import dat.entities.User;
import dat.security.entities.Role;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getTasks() != null ?
                        user.getTasks().stream()
                                .map(TaskMapper::toDTO)
                                .collect(Collectors.toList()) : null,

                user.getRoles() != null ?
                        user.getRoles().stream()
                                .map(Role::getRoleName) //konverter role til rolenavn
                                .collect(Collectors.toSet()) : new HashSet<>() // opretter og bruger et tomt Set hvis roles er null
        );
    }

    public static dk.bugelhartmann.UserDTO toBugelUserDTO(UserDTO localUserDTO) {
        return new dk.bugelhartmann.UserDTO(
                localUserDTO.getUsername(),
                localUserDTO.getPassword(),
                localUserDTO.getRoles() != null ? new HashSet<>(localUserDTO.getRoles()) : Set.of()
        );
    }

    public static UserDTO fromBugelUserDTO(dk.bugelhartmann.UserDTO bugelUserDTO) {

        UserDTO localUserDTO = new UserDTO();
        localUserDTO.setUsername(bugelUserDTO.getUsername());

        // mapper roler til String i stedet for sets
        localUserDTO.setRoles(bugelUserDTO.getRoles().stream()
                .map(String::valueOf)
                .collect(Collectors.toSet()));

        return localUserDTO;
    }
}
