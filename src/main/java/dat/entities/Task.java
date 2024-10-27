package dat.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskID;

    private String description;
    private int assignedTo;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private boolean repeated;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
