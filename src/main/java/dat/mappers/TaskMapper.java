package dat.mappers;

import dat.dtos.TaskDTO;
import dat.dtos.UserDTO;
import dat.entities.Task;
import dat.entities.User;

public class TaskMapper {

    public static TaskDTO toDTO(Task task) {
        User user = task.getUser();
        UserDTO userDTO = new UserDTO(user.getUserId(), user.getUsername(), null, null);  // Only include the needed fields

        return new TaskDTO(
                task.getTaskID(),
                task.getDescription(),
                task.getAssignedTo(),
                task.getDueDate(),
                task.getDueTime(),
                task.isRepeated(),
                task.isCompleted()
        );
    }

    public static Task toEntity(TaskDTO taskDTO) {
        return new Task(
                taskDTO.getTaskID(),
                taskDTO.getDescription(),
                taskDTO.getAssignedTo(),
                taskDTO.getDueDate(),
                taskDTO.getDueTime(),
                taskDTO.isRepeated(),
                taskDTO.isCompleted(),
                null //sætter seperat
        );
    }
}
