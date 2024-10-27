package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private int taskID;

    private String description;
    private int assignedTo;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private boolean repeated;
    private boolean completed;
}
