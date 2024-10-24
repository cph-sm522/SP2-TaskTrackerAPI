package dat.daos;

import dat.entities.Task;
import dat.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class TaskDAO {
    private EntityManagerFactory emf;

    public TaskDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<Task> getAllTasks() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Task t", Task.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Task getTaskById(int taskId) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Task.class, taskId);
        } finally {
            em.close();
        }
    }


    public List<Task> getAllTasksFromUser(int userId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Task t WHERE t.user.userId = :userId", Task.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Task createTask(Task task) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(task);
            em.getTransaction().commit();
            return task;
        } finally {
            em.close();
        }
    }

    public void updateTask(Task task) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(task);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void deleteTask(int taskId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            Task task = em.find(Task.class, taskId);
            if (task != null) {
                // remove task from attached user
                User user = task.getUser();
                if (user != null) {
                    user.getTasks().remove(task);
                    em.merge(user);
                }

                em.remove(task); // Remove the task itself
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Failed to delete task", e);
        } finally {
            em.close();
        }
    }

    public void deleteAllTasksFromUser(int userID) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            em.createQuery("DELETE FROM Task t WHERE t.user.userId = :userID")
                    .setParameter("userID", userID)
                    .executeUpdate();

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Failed to delete tasks for user with ID: " + userID, e);
        } finally {
            em.close();
        }
    }


    public void deleteAllRepeatedTasks(int userId, int baseTaskId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            // Delete all tasks with the same description as the base task and same user, excluding the base task
            em.createQuery("DELETE FROM Task t WHERE t.user.userId = :userId AND t.description = (SELECT t2.description FROM Task t2 WHERE t2.taskID = :baseTaskId) AND t.taskID <> :baseTaskId")
                    .setParameter("userId", userId)
                    .setParameter("baseTaskId", baseTaskId)
                    .executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
