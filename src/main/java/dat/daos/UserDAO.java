package dat.daos;

import dat.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class UserDAO {

    private EntityManagerFactory emf;

    public UserDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public User getUserById(int userId) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, userId);
        } finally {
            em.close();
        }
    }

    public void updateUser(User user) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void deleteUser(int userId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                em.remove(user); //cascade sletter tilhørende tasks
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
