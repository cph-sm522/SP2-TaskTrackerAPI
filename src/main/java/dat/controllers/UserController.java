package dat.controllers;

import dat.daos.UserDAO;
import dat.dtos.UserDTO;
import dat.entities.User;
import dat.mappers.UserMapper;
import io.javalin.http.Context;

public class UserController {

    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void getUserById(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("id"));
        User user = userDAO.getUserById(userId);

        if (user != null) {
            UserDTO userDTO = UserMapper.toDTO(user);
            ctx.status(200).json(userDTO);
        } else {
            ctx.status(404).result("User not found");
        }
    }

    public void createUser(Context ctx) {
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
        User user = UserMapper.toEntity(userDTO);
        User newUser = userDAO.createUser(user);
        ctx.status(201).json(UserMapper.toDTO(newUser));
    }

    public void updateUser(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("id"));
        User existingUser = userDAO.getUserById(userId);

        if (existingUser != null) {
            UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);

            existingUser.setUsername(userDTO.getUsername());
            existingUser.setEmail(userDTO.getEmail());

            userDAO.updateUser(existingUser);
            ctx.status(200).json(UserMapper.toDTO(existingUser));
        } else {
            ctx.status(404).result("User not found");
        }
    }

    public void deleteUser(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("id"));
        userDAO.deleteUser(userId);
        ctx.status(204);
    }
}