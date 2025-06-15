package com.example.healthcare.sessionbeans;

import com.example.healthcare.entities.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException; // Import this
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery; // Import this for better type safety

@Stateless
public class UsersFacade extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "HealthcareSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersFacade() {
        super(Users.class);
    }

    public int countAllPatients() {
        return ((Long) em.createQuery(
            "SELECT COUNT(u) FROM Users u WHERE u.role = :role")
            .setParameter("role", "patient")
            .getSingleResult()).intValue();
    }

    public Users findByUsername(String username) {
        System.out.println("--- UsersFacade.findByUsername() called ---");
        System.out.println("Received username parameter: '" + username + "'"); // Print the received parameter

        try {
            // Use TypedQuery for better type safety
            TypedQuery<Users> query = em.createNamedQuery("Users.findByUsername", Users.class);
            query.setParameter("username", username);

            // Execute the query and check results
            java.util.List<Users> results = query.getResultList(); // Use getResultList() for debugging

            System.out.println("Query 'Users.findByUsername' executed. Number of results: " + results.size());

            if (results.isEmpty()) {
                System.out.println("No user found with username: '" + username + "' in the database.");
                return null;
            } else if (results.size() > 1) {
                System.out.println("WARNING: Multiple users found for username: '" + username + "'. Returning the first one.");
                // You might want to handle this as an error in a real system
                return results.get(0);
            } else {
                Users foundUser = results.get(0);
                System.out.println("User found: ID=" + foundUser.getUserId() + ", Username='" + foundUser.getUsername() + "', Role='" + foundUser.getRole() + "'");
                return foundUser;
            }

        } catch (Exception e) { // Catch generic Exception to log everything
            System.err.println("Error in UsersFacade.findByUsername: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for any unexpected errors
            return null;
        }
        
        
    }
    
    public List<Users> findUsersByRole(String roleName) {
        return em.createQuery("SELECT u FROM Users u WHERE u.role = :roleName") // <-- CHANGED 'u.roleName' to 'u.role'
                .setParameter("roleName", roleName)
                .getResultList();
    }
}