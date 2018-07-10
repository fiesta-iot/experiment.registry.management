package eu.fiestaiot.experiment.erm.jpa.utils;
  
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;



public class HibernateUtil {
  
	
	@PersistenceUnit
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("FedspecJpaPersistenceUnit");
	


    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
  
    public static void shutdown() {
        // Close caches and connection pools
    	emf.close();
    }
  
}
