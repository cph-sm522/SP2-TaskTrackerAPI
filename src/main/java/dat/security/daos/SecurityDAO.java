package dat.security.daos;

import dat.entities.User;
import dat.security.entities.Role;
import dat.security.exceptions.ApiException;
import dat.security.exceptions.ValidationException;
import dat.dtos.UserDTO;
import jakarta.persistence.*;

import java.util.stream.Collectors;


public class SecurityDAO implements ISecurityDAO {

    private static EntityManagerFactory emf;

    public SecurityDAO(EntityManagerFactory _emf) {
        emf = _emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public UserDTO getVerifiedUser(String username, String password) throws ValidationException {
        EntityManager em = getEntityManager();
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            if (user == null || !user.verifyPassword(password)) {
                throw new ValidationException("Invalid username or password.");
            }


            UserDTO localUserDTO = new dat.dtos.UserDTO();
            localUserDTO.setUsername(user.getUsername());

            // konverterer fra et sæt roller til en Strings af roller
            localUserDTO.setRoles(
                    user.getRoles().stream()
                            .map(Role::getRoleName)
                            .collect(Collectors.toSet())
            );

            return (localUserDTO);

        } catch (NoResultException e) {
            throw new ValidationException("User not found");
        }
    }

    @Override
    public User createUser(String username, String password, String email) {
        EntityManager em = getEntityManager();
        try {
            User existingUser = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existingUser != null) {
                throw new EntityExistsException("User with username: " + username + " already exists");
            }

            User user = new User(username, password, email);
            em.getTransaction().begin();

            // tjek "user" rollen og tilegn den hvis tom
            Role userRole = em.find(Role.class, "user");
            if (userRole == null) {
                userRole = new Role("user");
                em.persist(userRole);
            }

            // kun tilføj hvis ikke allerede oprettet
            if (!user.getRoles().contains(userRole)) {
                user.addRole(userRole);
            }

            em.persist(user);
            em.getTransaction().commit();
            return user;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new ApiException(400, e.getMessage());
        }
    }


    @Override
    public User addRole(dat.dtos.UserDTO userDTO, String newRole) {
        try (EntityManager em = getEntityManager()) {

            System.out.println("Username to query: " + userDTO.getUsername());

            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", userDTO.getUsername())
                    .getSingleResult();

            if (user == null)
                throw new EntityNotFoundException("No user found with username: " + userDTO.getUsername());
            em.getTransaction().begin();

            Role role = em.find(Role.class, newRole);
            if (role == null) {
                role = new Role(newRole);
                em.persist(role);
            }
            user.addRole(role);
            em.merge(user);
            em.getTransaction().commit();
            return user;
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No user found with username: " + userDTO.getUsername());
        }
    }
}