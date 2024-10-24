package dat.mappers;

import dat.dtos.UserDTO;
import dat.entities.User;
import dat.security.entities.Role;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                null,
                user.getEmail(),
                user.getTasks() != null ?
                        user.getTasks().stream()
                                .map(TaskMapper::toDTO)
                                .collect(Collectors.toList()) : null,
                user.getRoles() != null ?
                        user.getRoles().stream()
                                .collect(Collectors.toSet()) : null
        );
    }

    public static User toEntity(UserDTO userDTO) {
        return new User(
                userDTO.getUserId(),
                userDTO.getUsername(),
                null,
                userDTO.getEmail(),
                null,
                userDTO.getRoles() != null ?
                        userDTO.getRoles().stream()
                                .collect(Collectors.toSet()) : null
        );
    }

    // from local dto to bugelhartmann dto
    public static dk.bugelhartmann.UserDTO toBugelUserDTO(UserDTO userDTO) {
        return new dk.bugelhartmann.UserDTO(
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getRoles() != null ?
                        userDTO.getRoles().stream()
                                .map(Role::getRoleName)
                                .collect(Collectors.toSet()) : null
        );
    }

    // from bugelhartmann dto to local dto
    public static UserDTO fromBugelUserDTO(dk.bugelhartmann.UserDTO bugelUserDTO) {
        return new UserDTO(
                0,
                bugelUserDTO.getUsername(),
                null,
                null,
                bugelUserDTO.getRoles() // Mapping roles
        );
    }
}
