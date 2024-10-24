package dat.controllers;

import dat.daos.TaskDAO;
import dat.daos.UserDAO;
import dat.dtos.TaskDTO;
import dat.entities.Task;
import dat.entities.User;
import dat.mappers.TaskMapper;
import dat.mappers.UserMapper;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class TaskController {

    private TaskDAO taskDAO;
    private UserDAO userDAO;

    public TaskController(TaskDAO taskDAO, UserDAO userDAO) {
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
    }

    public void getAllTasks(Context ctx) {
        List<Task> tasks = taskDAO.getAllTasks();
        List<TaskDTO> taskDTOs = tasks.stream().map(TaskMapper::toDTO).collect(Collectors.toList());
        ctx.json(taskDTOs);
    }

    public void getAllTasksFromUser(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("id"));
        List<Task> tasks = taskDAO.getAllTasksFromUser(userId);
        List<TaskDTO> taskDTOs = tasks.stream().map(TaskMapper::toDTO).collect(Collectors.toList());
        ctx.json(taskDTOs);
    }

    public void createTask(Context ctx) {
        TaskDTO taskDTO = ctx.bodyAsClass(TaskDTO.class);
        User assignedUser = userDAO.getUserById(taskDTO.getAssignedTo());
        if (assignedUser == null) {
            ctx.status(404).result("User not found");
            return;
        }
        if (taskDTO.isRepeated()) {
            // opretter 5 tasks, én hver uge
            for (int i = 0; i < 5; i++) {
                Task newTask = TaskMapper.toEntity(taskDTO);
                newTask.setUser(assignedUser);

                // gentager sig hver 7 dag
                newTask.setDueDate(taskDTO.getDueDate().plusDays(7 * i));
                taskDAO.createTask(newTask);
            }
            ctx.status(201).result("Repeated tasks created");

        } else {
            Task newTask = TaskMapper.toEntity(taskDTO);
            newTask.setUser(assignedUser);
            taskDAO.createTask(newTask);

            assignedUser.getTasks().add(newTask);
            userDAO.updateUser(assignedUser);

            User updatedUser = userDAO.getUserById(taskDTO.getAssignedTo());
            ctx.status(201).json(UserMapper.toDTO(updatedUser));
        }
    }

    public void updateTask(Context ctx) {
        int taskId = Integer.parseInt(ctx.pathParam("id"));
        Task existingTask = taskDAO.getTaskById(taskId);

        if (existingTask != null) {
            TaskDTO taskDTO = ctx.bodyAsClass(TaskDTO.class);
            boolean wasRepeated = existingTask.isRepeated();
            boolean nowRepeated = taskDTO.isRepeated();

            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setAssignedTo(taskDTO.getAssignedTo());
            existingTask.setDueDate(taskDTO.getDueDate());
            existingTask.setDueTime(taskDTO.getDueTime());
            existingTask.setRepeated(nowRepeated);
            existingTask.setCompleted(taskDTO.isCompleted());

            taskDAO.updateTask(existingTask);

            if (wasRepeated && !nowRepeated) {
                taskDAO.deleteAllRepeatedTasks(existingTask.getUser().getUserId(), taskId);
            }

            ctx.status(200).json(TaskMapper.toDTO(existingTask));
        } else {
            ctx.status(404).result("Task not found");
        }
    }


    public void deleteTask(Context ctx) {
        int taskId = Integer.parseInt(ctx.pathParam("id"));
        taskDAO.deleteTask(taskId);
        ctx.status(204);
    }

    public void deleteAllTasksFromUser(Context ctx) {
        try {
            int userId = Integer.parseInt(ctx.pathParam("id"));
            taskDAO.deleteAllTasksFromUser(userId);

            ctx.status(204);
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid user ID format");
        } catch (RuntimeException e) {
            ctx.status(500).result("Error deleting tasks: " + e.getMessage());
        }
    }


}
