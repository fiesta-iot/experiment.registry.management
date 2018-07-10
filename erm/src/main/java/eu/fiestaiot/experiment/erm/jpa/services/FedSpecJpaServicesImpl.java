package eu.fiestaiot.experiment.erm.jpa.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.LockTimeoutException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fiestaiot.experiment.erm.jpa.fedspec.entities.*;
import eu.fiestaiot.commons.expdescriptiveids.model.*;
import eu.fiestaiot.commons.fedspec.model.*;

import eu.fiestaiot.commons.util.PropertyManagement;
import eu.fiestaiot.experiment.erm.jpa.utils.HibernateUtil;
import eu.fiestaiot.experiment.erm.jpa.utils.MappingUtils;

/**
 * @author kefnik
 *
 */
public class FedSpecJpaServicesImpl {

	/**
	 * The logger's initialization.
	 */
	final static Logger logger = LoggerFactory.getLogger(FedSpecJpaServicesImpl.class);


	// EntityManager
	EntityManager entityManager = null;


	
	// Property Management
	PropertyManagement propertyManagement = null;

	// Mapping Utils
	MappingUtils mappingUtils = null;

	
	
	public void entityManagerOpen(){
		this.entityManager = HibernateUtil.getEntityManager();
	}
	
	public void entityManagerClose(){
		entityManager.close();
	}
	
	
	public FedSpecJpaServicesImpl() {

//		this.entityManager = entityManager;

		// Mapping Utils initialization
		mappingUtils = new MappingUtils();

		entityManagerOpen();
		
		// ============READING PROPERIES ============= //
		// Property Management Initialization
		propertyManagement = new PropertyManagement();
	}


	/**
	 * @param fedspec
	 * @throws EntityExistsException
	 * @throws RollbackException
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws TransactionRequiredException
	 */
	public String persistFedSpecOld(FEDSpec fedSpec) throws EntityExistsException, RollbackException,
			IllegalArgumentException, IllegalStateException, TransactionRequiredException {

		if ((fedSpec.getUserID() != null) && (!fedSpec.getUserID().isEmpty())) {
			

			// Check if UserID exists in security module
			Boolean existentSecurityUser = findIfSecurityUserExists(fedSpec.getUserID());

			if (existentSecurityUser) {

				// Check if UserID exist in DB
				Boolean existentErmUser = findIfErmUserExists(fedSpec.getUserID());

				// Add experiments to Existent User
				if (existentErmUser) {

					// For each FEMO check if ID exists and Store or Update the
					// experiment
					for (FEMO femo : fedSpec.getFEMO()) {

						//Store the new FEMO Bind to the UserID (fedSpec.getUserID())
						if ((femo.getId() == null) || (femo.getId().isEmpty())){
							persistFemoForUser(femo, fedSpec.getUserID());
						} 
						//Update the experiment with (femo.getId())
						else if  ((femo.getId() != null) && (!femo.getId().isEmpty())){
							
							// Check if ExperimentID exist in DB
							Boolean existentFemoID = findIfFemoIdExists(femo.getId());
							
							//Experiment ID exists So Update the experiment
							if (existentFemoID){
								
								for (FISMO fismo : femo.getFISMO()){

									persistFismoForFemo(fismo, femo.getId());

									
									//inform EEE for the FEMO updated
									eeeFismoUpdateTrigger (fismo);
									
								}
								
							}
							//Experiment ID does not exist so generate a new record
							else{
								persistFemoForUser(femo, fedSpec.getUserID());
							}
						}
					}
				}
				// Add new user along with the experiments (FEMOS) defined
				else {
/*					// begin transaction
					entityManager.getTransaction().begin();

					// convert FEDSpec to FEDSpecJpa
					FEDSpecJpa fedSpecJpa = mappingUtils.fedSpecToFedSpecJPA(fedSpec);

					// persist FEDSpecJpa
					entityManager.persist(fedSpecJpa);

					// Commit transaction
					entityManager.getTransaction().commit();*/
					
					
					FEDSpecJpa newFedSpecJpa = new FEDSpecJpa();
					newFedSpecJpa.setUserID(fedSpec.getUserID());
					
					
					
					for (FEMO newfemo : fedSpec.getFEMO()){
						// convert FEDSpec to FEDSpecJpa
						FemoJpa femoJpa = mappingUtils.femoToFemoJPA(newfemo);
						
						
						for (FISMO newfismo : newfemo.getFISMO()){
							
							// convert FEDSpec to FEDSpecJpa
							FismoJpa fismoJpa = mappingUtils.fismoToFismoJPA(newfismo);//==========================================
							
							femoJpa.addFISMO(fismoJpa);
							
							
						}
						

						
						newFedSpecJpa.addFEMO(femoJpa);
						
						
						
						
					}
					
					// begin transaction
					entityManager.getTransaction().begin();

					
			    	entityManager.persist(newFedSpecJpa);
			    	
					// Commit transaction
					entityManager.getTransaction().commit();
					
				}
			} else {

				return "NotRegisteredUser";
			}
		} else {
			return "NoUserID";
		}

		return "FedSpecSuccessfullyStored";//Response.status(200).entity("Success").build()
	}


	

	public String persistFedSpec(FEDSpec fedSpec) throws EntityExistsException, RollbackException,
			IllegalArgumentException, IllegalStateException, TransactionRequiredException {
		

		if ((fedSpec.getUserID() != null) && (!fedSpec.getUserID().isEmpty())) {
			
			// Check if UserID exist in ERM DB
			Boolean existentErmUser = findIfErmUserExists(fedSpec.getUserID());
			if (existentErmUser) {
				
				logger.debug("User id available in ERM. Initiate each FEMO storrage");
				
				for (FEMO femo : fedSpec.getFEMO()) {
					logger.debug("persist Femo with name: " + femo.getName());
					
					persistFemoForUser(femo,fedSpec.getUserID());
					
				}
				
				
				return getFedSpecJpaForUser(fedSpec.getUserID()).getId();
				
			} else {
				
			
				logger.debug("UserID does not exist in ERM so generate a new FEDSpec");
				//FEDSpec does not exist so generate a new entry

				
				// begin transaction if not active
				if (!entityManager.getTransaction().isActive())
				entityManager.getTransaction().begin();
				
				FEDSpecJpa fedSpecJpa = new FEDSpecJpa();
				fedSpecJpa.setUserID(fedSpec.getUserID());


				entityManager.persist(fedSpecJpa);
				
				// Commit transaction
				entityManager.getTransaction().commit();


				logger.debug("FEDSpec generated ID:  " + fedSpecJpa.getId());
				
				logger.debug("Initiate storrage for each FEMO ");
				
				for (FEMO femo : fedSpec.getFEMO()) {
					logger.debug("persist Femo with name: " + femo.getName());
					
					persistFemoForUser(femo,fedSpec.getUserID());
					
				}
				
				return fedSpecJpa.getId();
				
			}
			
		} else {
			
			return "UserIdIsEmpty";
			
		}
		
		
		
	}
	
	

	public String persistFemoForUser(FEMO femo, String userID) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {

		if ((userID != null) && (!userID.isEmpty())) {

			// Check if UserID exist in ERM DB
			Boolean existentErmUser = findIfErmUserExists(userID);

			// Add experiments to Existent User
			if (existentErmUser) {

				// Check if FEMO has an ID. in that case Update the existing FEMO
				if (femo.getId() != null && findIfFemoIdExists(femo.getId())) {

					FemoJpa femoJpa = entityManager.find(FemoJpa.class, femo.getId());

					// begin transaction if not active
					if (!entityManager.getTransaction().isActive())
					entityManager.getTransaction().begin();

					// Description
					if ((femo.getDescription() != null) && (!femo.getDescription().isEmpty())) {
						femoJpa.setDescription(femo.getDescription());
					}
					// EDM
					if ((femo.getEDM() != null) && (!femo.getEDM().isEmpty())) {
						femoJpa.setEDM(femo.getEDM());
					}
					// Name
					if ((femo.getName() != null) && (!femo.getName().isEmpty())) {
						femoJpa.setName(femo.getName());
					}
					
					
					// DomainOfInterest
					femoJpa.removeDomainOfInterest();
					if ((femo.getDomainOfInterest() != null) && (!femo.getDomainOfInterest().isEmpty())) {
						for (String domainOfInterest : femo.getDomainOfInterest()) {
							femoJpa.getDomainOfInterest().add(domainOfInterest);
						}
					}
					
					
					entityManager.persist(femoJpa);
					
					// Commit transaction
					entityManager.getTransaction().commit();
					
					
					
					// FISMO
					if ((femo.getFISMO() != null) && (!femo.getFISMO().isEmpty())) {
						for (FISMO fismo : femo.getFISMO()) {

							persistFismoForFemo(fismo, femoJpa.getId());

							// femoJpa.getFISMO().add(mappingUtils.fismoToFismoJPA(fismo));
						}
					}
					
					


					logger.debug("persistFemoForUser generated ID:  " + femoJpa.getId());

					return femoJpa.getId();

				} else {
					
					//FEDSpec does not exist so generate a new entry

					FEDSpecJpa fedSpecJpa = getFedSpecJpaForUser(userID);

					// begin transaction if not active
					if (!entityManager.getTransaction().isActive())
					entityManager.getTransaction().begin();

					// convert FEMO to FemoJpa
					FemoJpa femoJpa = mappingUtils.femoToFemoJPA(femo);

					fedSpecJpa.addFEMO(femoJpa);

					entityManager.persist(femoJpa);
					
					// Commit transaction
					entityManager.getTransaction().commit();

					logger.debug("persistFemoForUser generated ID:  " + femoJpa.getId());

					return femoJpa.getId();

				}

			} else {
				
				logger.debug("User id ("+userID+") does not exists.");
				
				
				// Check if FEMO has an ID. in that case Update the existing FEMO
				if (femo.getId() != null && findIfFemoIdExists(femo.getId())) {
					logger.debug("FEMO ID ("+femo.getId()+") Exists. Update this FEMO");

					
					FemoJpa femoJpa = entityManager.find(FemoJpa.class, femo.getId());

					// begin transaction if not active
					if (!entityManager.getTransaction().isActive())
					entityManager.getTransaction().begin();

					// Description
					if ((femo.getDescription() != null) && (!femo.getDescription().isEmpty())) {
						femoJpa.setDescription(femo.getDescription());
					}
					// EDM
					if ((femo.getEDM() != null) && (!femo.getEDM().isEmpty())) {
						femoJpa.setEDM(femo.getEDM());
					}
					// Name
					if ((femo.getName() != null) && (!femo.getName().isEmpty())) {
						femoJpa.setName(femo.getName());
					}
					// DomainOfInterest
					femoJpa.removeDomainOfInterest();
					if ((femo.getDomainOfInterest() != null) && (!femo.getDomainOfInterest().isEmpty())) {
						for (String domainOfInterest : femo.getDomainOfInterest()) {
							femoJpa.getDomainOfInterest().add(domainOfInterest);
						}
					}
					
					entityManager.persist(femoJpa);
					
					// Commit transaction
					entityManager.getTransaction().commit();
					
					// FISMO
					if ((femo.getFISMO() != null) && (!femo.getFISMO().isEmpty())) {
						for (FISMO fismo : femo.getFISMO()) {

							persistFismoForFemo(fismo, femoJpa.getId());

						}
					}



					logger.debug("persistFemoForUser generated ID:  " + femoJpa.getId());

					return femoJpa.getId();

				} else {
					
					//User does not exists. Generate a new FEDSpec with that user and attach FEDSpec to it.
					logger.debug("User does not exists and FEMO ID is empty. Generate a new FEDSpec with the user ("+userID+") and attach the FEDSpec to it.");
					
					// begin transaction if not active
					if (!entityManager.getTransaction().isActive())
					entityManager.getTransaction().begin();
					
					FEDSpecJpa newfedSpecJpa = new FEDSpecJpa();
					newfedSpecJpa.setUserID(userID);

					
					
					// convert FEMO to FemoJpa
					FemoJpa newfemoJpa = mappingUtils.femoToFemoJPA(femo);

					newfedSpecJpa.addFEMO(newfemoJpa);
					

					entityManager.persist(newfedSpecJpa);
					
					// Commit transaction
					entityManager.getTransaction().commit();

					logger.debug("persistFemoForUser generated ID:  " + newfemoJpa.getId());

					return newfemoJpa.getId();

				}

			}


		} else {

			return "UserIdIsEmpty";

		}
	}
	
	
	

	public String persistFismoForFemo(FISMO fismo, String femoID) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {
		
		

		
		
		// Check if FEMO Exists. if not return "FemoIdDoesNotExists"
		if (femoID != null && findIfFemoIdExists(femoID)) {



			// Check if FISMO has an ID. in that case Update the existing FISMO
			if (fismo.getId() != null && findIfFismoIdExists(fismo.getId())) {

				FismoJpa fismoJpa = entityManager.find(FismoJpa.class, fismo.getId());

				// begin transaction if not active
				if (!entityManager.getTransaction().isActive())
				entityManager.getTransaction().begin();

				SaveUpdateUtils saveUpdateUtils = new SaveUpdateUtils();

				saveUpdateUtils.saveFismoToExistentFismoJPA(fismoJpa, fismo);

				entityManager.persist(fismoJpa);
				
				// Commit transaction
				entityManager.getTransaction().commit();

				logger.debug("persistFismoForFemo generated ID:  " + fismoJpa.getId());

				return fismoJpa.getId();

			}

			else {
				
				logger.debug("FISMO ID does not exist. Instansiate a new FISMO under the FEMO with ID: " +femoID);
				
				FemoJpa femoJpa = entityManager.find(FemoJpa.class, femoID);
				
				// begin transaction if not active
				if (!entityManager.getTransaction().isActive())
				entityManager.getTransaction().begin();

				// convert Fismo to FismoJpa
				FismoJpa fismoJpa = mappingUtils.fismoToFismoJPA(fismo);

				femoJpa.addFISMO(fismoJpa);

				entityManager.persist(femoJpa);
				
				// Commit transaction
				entityManager.getTransaction().commit();

				logger.debug("persistFismoForFemo generated ID:  " + fismoJpa.getId());

				return fismoJpa.getId();

			}

		} else {
			return "FemoIdDoesNotExists";
		}
		
		
		
	}
	
	

	/**
	 * @param femo
	 * @param userID
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws QueryTimeoutException
	 * @throws TransactionRequiredException
	 * @throws PessimisticLockException
	 * @throws LockTimeoutException
	 * @throws PersistenceException
	 */
	public String persistFemoForUserOld(FEMO femo, String userID) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {
		
		FEDSpecJpa fedSpecJpa = null;
		
		if (findIfErmUserExists(userID)){
			fedSpecJpa =getFedSpecJpaForUser(userID);
		}
		
		
		
		
 

		//Check if FEMO has an ID. in that case Update the existing FEMO
		if (femo.getId()!=null && findIfFemoIdExists(femo.getId())){
			
			
			
			FemoJpa femoJpa = entityManager.find(FemoJpa.class, femo.getId());
			
			
		
			// begin transaction
			entityManager.getTransaction().begin();

			
			
			
			//Description
			if ((femo.getDescription() != null) && (!femo.getDescription().isEmpty())) {
				femoJpa.setDescription(femo.getDescription());
			}
			//EDM
			if ((femo.getEDM() != null) && (!femo.getEDM().isEmpty())) {
				femoJpa.setEDM(femo.getEDM());
			}
			//Name
			if ((femo.getName() != null) && (!femo.getName().isEmpty())) {
				femoJpa.setName(femo.getName());
			}
			//DomainOfInterest
			if ((femo.getDomainOfInterest() != null) && (!femo.getDomainOfInterest().isEmpty())) {
				for (String domainOfInterest : femo.getDomainOfInterest()){
					femoJpa.getDomainOfInterest().add(domainOfInterest);
				}
			}	
			//FISMO
			if ((femo.getFISMO() != null) && (!femo.getFISMO().isEmpty())) {
				for (FISMO fismo : femo.getFISMO()){
					
					
					persistFismoForFemo(fismo, femoJpa.getId());
					
//					femoJpa.getFISMO().add(mappingUtils.fismoToFismoJPA(fismo));
				}
			}
			
			
			

		
			// Commit transaction
			entityManager.getTransaction().commit();
			
			logger.debug("persistFemoForUser generated ID:  " + femoJpa.getId());
			
			return femoJpa.getId();
			
			
			
		} else if (fedSpecJpa!=null){
			// begin transaction
			entityManager.getTransaction().begin();

			// convert FEDSpec to FEDSpecJpa
			FemoJpa femoJpa = mappingUtils.femoToFemoJPA(femo);

			fedSpecJpa.addFEMO(femoJpa);
			

			// Commit transaction
			entityManager.getTransaction().commit();
			
			logger.debug("persistFemoForUser generated ID:  " + femoJpa.getId());
			
			return femoJpa.getId();
			
			
		} else {
			//If fedSpecJpa is null initiate a FEDSpec and then store it with the FEMO for the User
			
			FEDSpecJpa newFedSpecJpa = new FEDSpecJpa();
			newFedSpecJpa.setUserID(userID);
			
			// convert FEDSpec to FEDSpecJpa
			FemoJpa femoJpa = mappingUtils.femoToFemoJPA(femo);
			
			newFedSpecJpa.addFEMO(femoJpa);
			
			// begin transaction
			entityManager.getTransaction().begin();

			
	    	entityManager.persist(newFedSpecJpa);
			
			
			// Commit transaction
			entityManager.getTransaction().commit();
			
			logger.debug("persistFemoForUser generated ID:  " + femoJpa.getId());
			
			return femoJpa.getId();
			
		}
		
		
		
		


	}

	
	
	
	
	/**
	 * @param fismo
	 * @param femoID
	 * @return 
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws QueryTimeoutException
	 * @throws TransactionRequiredException
	 * @throws PessimisticLockException
	 * @throws LockTimeoutException
	 * @throws PersistenceException
	 */
	public String persistFismoForFemoOld(FISMO fismo, String femoID) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {

		FemoJpa femoJpa = entityManager.find(FemoJpa.class, femoID);

		// Check if FISMO has an ID. in that case Update the existing FISMO
		if (fismo.getId() != null && findIfFismoIdExists(fismo.getId())) {

			FismoJpa fismoJpa = entityManager.find(FismoJpa.class, fismo.getId());

			// begin transaction
			entityManager.getTransaction().begin();

			SaveUpdateUtils saveUpdateUtils = new SaveUpdateUtils();

			saveUpdateUtils.saveFismoToExistentFismoJPA(fismoJpa, fismo);

			// Commit transaction
			entityManager.getTransaction().commit();

			logger.debug("persistFismoForFemo generated ID:  " + fismoJpa.getId());

			return fismoJpa.getId();

		}

		else {
			// begin transaction
			entityManager.getTransaction().begin();

			// convert Fismo to FismoJpa
			FismoJpa fismoJpa = mappingUtils.fismoToFismoJPA(fismo);

			femoJpa.addFISMO(fismoJpa);

			// Commit transaction
			entityManager.getTransaction().commit();

			logger.debug("persistFismoForFemo generated ID:  " + fismoJpa.getId());

			return fismoJpa.getId();

		}

/*		FemoJpa femoJpa = entityManager.find(FemoJpa.class, femoID);
		
		// begin transaction
		entityManager.getTransaction().begin();

		// convert FISMO to FismoJpa
		FismoJpa fismoJpa = mappingUtils.fismoToFismoJPA(fismo);

		// persist FEDSpecJpa
		entityManager.persist(fismoJpa);

		//add persisted FEMO and bind it wit USER 
		femoJpa.getFISMO().add(fismoJpa);
	
		// Commit transaction
		entityManager.getTransaction().commit();
		
		logger.debug("persistFismoForFemo generated ID:  " + fismoJpa.getId());
		
		return fismoJpa.getId();*/
		
	}
	
	


	/**
	 * @param userID
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws QueryTimeoutException
	 * @throws TransactionRequiredException
	 * @throws PessimisticLockException
	 * @throws LockTimeoutException
	 * @throws PersistenceException
	 */
	public void deleteAllUserFEMOs(String userID) 
			throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
			PessimisticLockException, LockTimeoutException, PersistenceException {

		// TODO Currently the user is deleted too as it doesn't make sense to keep him. May consider to delete only FEMOS
		
		FEDSpecJpa fedSpecJpa = getFedSpecJpaForUser(userID);

		
		if (fedSpecJpa != null){
			
			// begin transaction if not active
			if (!entityManager.getTransaction().isActive())
			entityManager.getTransaction().begin();

			entityManager.remove(fedSpecJpa);

			// Commit transaction
			entityManager.getTransaction().commit();
			
			
			//======For each deleted FISMO inform EEE==========
			List<String> fismoListToTrigger = new ArrayList<String>() ;
			for (FemoJpa femoJpaToTriger: fedSpecJpa.getFEMO()){
				for (FismoJpa fismoJpaToTriger : femoJpaToTriger.getFISMO()){
					fismoListToTrigger.add(fismoJpaToTriger.getId());
				}
				
			}
			if(!fismoListToTrigger.isEmpty()){
				eeeFismoListDeleteTrigger (fismoListToTrigger);
			}
			//=================================================
		}
	
	}
	
	
	
	/**
	 * Inform EEE for a FEMO Updated from ERM
	 * 
	 * @param femoID
	 * @param userID
	 */	
	private void eeeFismoUpdateTrigger (FISMO fismo){
		// TODO Auto-generated method stub
		
		logger.debug("Starting EEE Femo Update Trigger for FismoID: "+ fismo.getId());
		
//		ResteasyClient client = new ResteasyClientBuilder().build();
//		ResteasyWebTarget target = client.target(propertyManagement.getEeeFismoUpdateTriggerURL());
		
//		Response response = target.request().post(Entity.xml(fismo));
		
//		if (response.getStatus()==200){
//			logger.debug("Femo Update Trigger for FismoID: "+ fismo.getId() + " was successful (200)");
//		} else {
//			logger.error("Femo Update Trigger for FismoID: "+ fismo.getId() + " was unsuccessful (not 200)");	
//		}
//		
//		
//		response.close();
		
		
	}
	
	
	
	
	/**
	 * Inform EEE for a FEMO deleted from ERM
	 * 
	 * @param femoID
	 * @param userID
	 */
	private void eeeFismoListDeleteTrigger (List<String> fismoIDs){
		
		
		
		logger.debug("Starting EEE Femo Delete Trigger for the following FismoIDs: " + fismoIDs.toString());
		
//		ResteasyClient client = new ResteasyClientBuilder().build();
//		ResteasyWebTarget target = client.target(propertyManagement.getEeeFismoDeleteJobListTriggerURL());
		
		
		String fismoInJsonArray = stringListToJsonArray(fismoIDs);
		
//		Response response = target.request().post(Entity.json(fismoInJsonArray));
		
		
//		if (response.getStatus()==200){
//			logger.debug("Femo Delete Trigger for FismoIDs: " + fismoInJsonArray + " was successful (200)");
//		} else {
//			logger.error("Femo Delete Trigger for FismoIDs: " + fismoInJsonArray + " was unsuccessful (not 200)");
//		}
//		
//		response.close();
		

	}
	
	
	/**
	 * Converts a String list to JSON Object Array
	 * 
	 * @param List<String>
	 * @return String JSON Object Array
	 */
	private String stringListToJsonArray(List<String> stringList){
		
		String jsonArray = "";
		
		int counter = 0;
		
		for (String lc : stringList){
			
			if (counter==0){
				jsonArray = "[ " + "\"" + lc + "\"";
			} else {
				jsonArray = jsonArray + ", \"" + lc + "\"";
			}
			counter++;
		}
		jsonArray = jsonArray + " ]";
		
		return jsonArray;
			
	}
	



	/**
	 * @param femoID
	 * @param userID 
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws TransactionRequiredException
	 */
	public void deleteFemo(String userID, String femoID)
			throws IllegalArgumentException, IllegalStateException, TransactionRequiredException {

		
		FEDSpecJpa fedSpecJpa = getFedSpecJpaForUser(userID);
		
		
		FemoJpa femoJpa = entityManager.find(FemoJpa.class, femoID);

		// begin transaction if not active
		if (!entityManager.getTransaction().isActive())
		entityManager.getTransaction().begin();

		fedSpecJpa.removeFemoJpa(femoJpa);
		
		
/*		entityManager.remove(femoJpa);*/

		// Commit transaction
		entityManager.getTransaction().commit();

		
		//======For each deleted FISMO inform EEE==========
		List<String> fismoListToTrigger = new ArrayList<String>() ;

		for (FismoJpa fismoJpaToTriger : femoJpa.getFISMO()){
			fismoListToTrigger.add(fismoJpaToTriger.getId());
		}
			
		if(!fismoListToTrigger.isEmpty()){
			eeeFismoListDeleteTrigger (fismoListToTrigger);
		}
		//=================================================
	}

	
	
	
	
	
	/**
	 * @param fismoID
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws TransactionRequiredException
	 */
	public void deleteFismo(String femoID, String fismoID) throws IllegalArgumentException, IllegalStateException, TransactionRequiredException {


		FemoJpa femoJpa = entityManager.find(FemoJpa.class, femoID);
		
		FismoJpa fismoJpa = entityManager.find(FismoJpa.class, fismoID);

		// begin transaction if not active
		if (!entityManager.getTransaction().isActive())
		entityManager.getTransaction().begin();

		femoJpa.removeFismoJpa(fismoJpa);

		// Commit transaction
		entityManager.getTransaction().commit();
		
		
		//======For deleted FISMO inform EEE==========
		List<String> fismoListToTrigger = new ArrayList<String>() ;
		
		fismoListToTrigger.add(fismoID);
			
		if(!fismoListToTrigger.isEmpty()){
			eeeFismoListDeleteTrigger (fismoListToTrigger);
		}
		//=================================================

	}

	
	
	


	
	
	/**
	 * @param userID
	 * @return
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws QueryTimeoutException
	 * @throws TransactionRequiredException
	 * @throws PessimisticLockException
	 * @throws LockTimeoutException
	 * @throws PersistenceException
	 */
	public ExpDescriptiveIDs getAllExperimentDescriptiveIDsForUser(String userID) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {

		FEDSpec fedspec = getFedSpecForUser(userID);
		
		
		if (fedspec!= null){
			
			ExpDescriptiveIDs expDescriptiveIDs= generateExpDescriptiveIDsFromFEDSpec(fedspec);

			return expDescriptiveIDs;
			
		} else {
			
			return new ExpDescriptiveIDs();
			
		}
		
	}



	/**
	 * @param userID
	 * @return
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws QueryTimeoutException
	 * @throws TransactionRequiredException
	 * @throws PessimisticLockException
	 * @throws LockTimeoutException
	 * @throws PersistenceException
	 */
	public FEDSpec getFedSpecForUser(String userID)
			throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
			PessimisticLockException, LockTimeoutException, PersistenceException {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<FEDSpecJpa> criteriaQuery = builder.createQuery(FEDSpecJpa.class);
		Root<FEDSpecJpa> rootEntity = criteriaQuery.from(FEDSpecJpa.class);

		ParameterExpression<String> parameter = builder.parameter(String.class);
		criteriaQuery.select(rootEntity).where(builder.equal(rootEntity.get("userID"), parameter));

		//set the parameter
		TypedQuery<FEDSpecJpa> query = entityManager.createQuery(criteriaQuery);
		query.setParameter(parameter, userID);
		
		// running the query
		List<FEDSpecJpa> results = query.getResultList();

		if (!results.isEmpty()){
			
			FEDSpecJpa fedSpecJpa = results.get(0);

			FEDSpec fedSpec = mappingUtils.fedSpecJpaToFedSpec(fedSpecJpa);
			
			return fedSpec;
			
		} else {
			
			return new FEDSpec();
			
		}
		


		
	}
	
	
	
	
	/**
	 * @param femoID
	 * @return
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws QueryTimeoutException
	 * @throws TransactionRequiredException
	 * @throws PessimisticLockException
	 * @throws LockTimeoutException
	 * @throws PersistenceException
	 */
/*	
	public FEDSpecJpa getFedSpecJpaForFEMO(String femoID)
			throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
			PessimisticLockException, LockTimeoutException, PersistenceException {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<FEDSpecJpa> criteriaQuery = builder.createQuery(FEDSpecJpa.class);
		Root<FEDSpecJpa> rootEntity = criteriaQuery.from(FEDSpecJpa.class);

		ParameterExpression<String> parameter = builder.parameter(String.class);
		criteriaQuery.select(rootEntity).where(builder.equal(rootEntity.get("femo"), parameter));

		//set the parameter
		TypedQuery<FEDSpecJpa> query = entityManager.createQuery(criteriaQuery);
		query.setParameter(parameter, femoID);
		
		// running the query
		List<FEDSpecJpa> results = query.getResultList();

		FEDSpecJpa fedSpecJpa = results.get(0);

//		FEDSpec fedSpec = mappingUtils.fedSpecJpaToFedSpec(fedSpecJpa);

		return fedSpecJpa;
	}
	
	*/
	
	/**
	 * @param femoID
	 * @return
	 * @throws IllegalArgumentException
	 */
	public FemoDescriptiveID getExperimentDescriptiveID(String femoID) throws IllegalArgumentException {
		
		FEMO femo = getFemo(femoID);
		
		
		
		if (femo!= null){
			
			FemoDescriptiveID femoDescriptiveID = generateExpDescriptiveIdFromFEMO(femo);
			
			return femoDescriptiveID;
		}else {
			return new FemoDescriptiveID();
		}

	}

	/**
	 * @param femoID
	 * @return
	 * @throws IllegalArgumentException
	 */
	public FEMO getFemo(String femoID) throws IllegalArgumentException {

		FemoJpa femoJpa = entityManager.find(FemoJpa.class, femoID);

		if (femoJpa!= null){
			
			FEMO femo = mappingUtils.femoJpaToFemo(femoJpa);

			return femo;
		}else {
			return new FEMO();
		}
		

	}

	/**
	 * @param fismoID
	 * @return
	 * @throws IllegalArgumentException
	 */
	public FISMO getFismo(String fismoID) throws IllegalArgumentException {

		FismoJpa fismoJpa = entityManager.find(FismoJpa.class, fismoID);

		
		if (fismoJpa!= null){
			for (FileJpa fileJpa : fismoJpa.getExperimentOutput().getFile()){
				

				logger.debug("Available fileJpa:  " + fileJpa.getType());

			}
			
			FISMO fismo = mappingUtils.fismoJpaToFismo(fismoJpa);

			return fismo;
			
		} else {
			
			return new FISMO();
			
		}
		
		
		

	}

	/**
	 * @return
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws QueryTimeoutException
	 * @throws TransactionRequiredException
	 * @throws PessimisticLockException
	 * @throws LockTimeoutException
	 * @throws PersistenceException
	 */
	public List<FISMO> getAllDiscoverableFismos()
			throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
			PessimisticLockException, LockTimeoutException, PersistenceException {

		List<FISMO> fismos = new ArrayList<FISMO>();
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<FismoJpa> criteriaQuery = builder.createQuery(FismoJpa.class);
		Root<FismoJpa> rootEntity = criteriaQuery.from(FismoJpa.class);

//		ParameterExpression<Boolean> parameter = builder.parameter(Boolean.class);
		criteriaQuery.select(rootEntity).where(builder.equal(rootEntity.get("discoverable"), true));

		// running the query
		TypedQuery<FismoJpa> query = entityManager.createQuery(criteriaQuery);
//		query.setParameter(parameter, true);

		List<FismoJpa> results = query.getResultList();

		
		for (FismoJpa fismoJpa : results){
			
			FISMO fismo = new FISMO();
			
			fismo = mappingUtils.fismoJpaToFismo(fismoJpa);
			
			fismos.add(fismo);
		}

		return fismos;

	}

	
	public String persistNewSparqlQueryWithFismo(String description, String query) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {


		
		//TODO Check if FISMO has an ID. in that case Update the existing FISMO
	
		
		FismoJpa fismoJpa = new FismoJpa();
		
		fismoJpa.setDescription(description);
		
		
		QueryControlJpa queryControlJpa = new QueryControlJpa();
		

		QueryRequestJpa queryRequestJpa = new QueryRequestJpa();
		
		
		queryRequestJpa.setQuery(query);
		
		queryControlJpa.setQueryRequest(queryRequestJpa);
		
		
		fismoJpa.setQueryControl(queryControlJpa);
		
		
		
		// begin transaction if not active
		if (!entityManager.getTransaction().isActive())
		entityManager.getTransaction().begin();


		// persist FEDSpecJpa
		entityManager.persist(fismoJpa);
	
		// Commit transaction
		entityManager.getTransaction().commit();
		
		logger.debug("persistFismoForFemo generated ID:  " + fismoJpa.getId());
		
		return fismoJpa.getId();

		
	}
	
	
	public List<FISMO> getQueryControlWithCriteria(String description, String quantityKind)
			throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
			PessimisticLockException, LockTimeoutException, PersistenceException {

		List<FISMO> fismos = new ArrayList<FISMO>();
		
		
		if (!description.equals("")) {


			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<FismoJpa> criteriaQuery = builder.createQuery(FismoJpa.class);
			Root<FismoJpa> rootEntity = criteriaQuery.from(FismoJpa.class);

			// ParameterExpression<Boolean> parameter =
			// builder.parameter(Boolean.class);
			criteriaQuery.select(rootEntity).where(builder.like(rootEntity.get("description"), description));

			// running the query
			TypedQuery<FismoJpa> query = entityManager.createQuery(criteriaQuery);
			// query.setParameter(parameter, true);

			List<FismoJpa> results = query.getResultList();

			for (FismoJpa fismoJpa : results) {

				FISMO fismo = new FISMO();

				fismo = mappingUtils.fismoJpaToFismo(fismoJpa);

				fismos.add(fismo);
			}

			return fismos;

		} 

		if (!quantityKind.equals("")) {

			

			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			
//			CriteriaQuery<FismoJpa> criteriaQuery = builder.createQuery(FismoJpa.class);
//			Root<FismoJpa> rootEntity = criteriaQuery.from(FismoJpa.class);
//			criteriaQuery.select(rootEntity).where(builder.like(rootEntity.get("description"), description));

			
			CriteriaQuery<FismoJpa> criteriaQuery = builder.createQuery(FismoJpa.class);
			Root<FismoJpa> rootEntity = criteriaQuery.from(FismoJpa.class);
			criteriaQuery.select(rootEntity).where(builder.isMember(quantityKind, rootEntity.get("queryControl").get("quantityKind")));
			
			
			// running the query
			TypedQuery<FismoJpa> query = entityManager.createQuery(criteriaQuery);
			// query.setParameter(parameter, true);

			List<FismoJpa> results = query.getResultList();

			for (FismoJpa fismoJpa : results) {

				FISMO fismo = new FISMO();

				fismo = mappingUtils.fismoJpaToFismo(fismoJpa);

				fismos.add(fismo);
			}

			return fismos;

		}

		return fismos;
		
	}
	
	
	// ========================Utilities Start===========================

	private Boolean findIfErmUserExists(String userID) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<FEDSpecJpa> rootEntity = criteriaQuery.from(FEDSpecJpa.class);
		
		criteriaQuery.select(builder.count(rootEntity));
		
		criteriaQuery.where(builder.equal(rootEntity.get("userID"), userID));
		
		Long count = entityManager.createQuery(criteriaQuery).getSingleResult();
		
		if (count>=1){
			return true;
		}else{
			return false;
		}


	}

	
	private Boolean findIfFemoIdExists(String femoId) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<FemoJpa> rootEntity = criteriaQuery.from(FemoJpa.class);
		
		criteriaQuery.select(builder.count(rootEntity));
		
		criteriaQuery.where(builder.equal(rootEntity.get("id"), femoId));
		
		Long count = entityManager.createQuery(criteriaQuery).getSingleResult();
		
		if (count>=1){
			return true;
		}else{
			return false;
		}
	}
	
	


	private boolean findIfFismoIdExists(String fismoId) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<FismoJpa> rootEntity = criteriaQuery.from(FismoJpa.class);
		
		criteriaQuery.select(builder.count(rootEntity));
		
		criteriaQuery.where(builder.equal(rootEntity.get("id"), fismoId));
		
		Long count = entityManager.createQuery(criteriaQuery).getSingleResult();
		
		if (count>=1){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	
	private Boolean findIfSecurityUserExists(String userID) {
		
		
		// TODO Auto-generated method stub

		return true;
	}
	
	private ExpDescriptiveIDs generateExpDescriptiveIDsFromFEDSpec(FEDSpec fedSpec) {

		ExpDescriptiveIDs expDescriptiveIDs = new ExpDescriptiveIDs();

		// FEMO
		if ((fedSpec.getFEMO() != null) && (!fedSpec.getFEMO().isEmpty())) {
			for (FEMO femo : fedSpec.getFEMO()) {

				FemoDescriptiveID femoDescriptiveID = new FemoDescriptiveID();

				femoDescriptiveID = generateExpDescriptiveIdFromFEMO(femo);

				expDescriptiveIDs.getFemoDescriptiveID().add(femoDescriptiveID);

			}
		}

		return expDescriptiveIDs;
	}

	private FemoDescriptiveID generateExpDescriptiveIdFromFEMO(FEMO femo) {
		
		FemoDescriptiveID femoDescriptiveID = new FemoDescriptiveID();

		// ID
		if ((femo.getId() != null) && (!femo.getId().isEmpty()))
			femoDescriptiveID.setId(femo.getId());
		// Name
		if ((femo.getName() != null) && (!femo.getName().isEmpty()))
			femoDescriptiveID.setName(femo.getName());
		// Description
		if ((femo.getDescription() != null) && (!femo.getDescription().isEmpty()))
			femoDescriptiveID.setDescription(femo.getDescription());
		// DomainOfInterest
		if ((femo.getDomainOfInterest() != null) && (!femo.getDomainOfInterest().isEmpty()))
			femoDescriptiveID.getDomainOfInterest().addAll(femo.getDomainOfInterest());

		// FismoDescriptiveID
		if ((femo.getFISMO() != null) && (!femo.getFISMO().isEmpty())) {
			for (FISMO fismo : femo.getFISMO()) {

				FismoDescriptiveID fismoDescriptiveID = new FismoDescriptiveID();

				// ID
				if ((fismo.getId() != null) && (!fismo.getId().isEmpty())) {
					fismoDescriptiveID.setId(fismo.getId());
				}
				// Description
				if ((fismo.getDescription() != null) && (!fismo.getDescription().isEmpty())) {
					fismoDescriptiveID.setDescription(fismo.getDescription());
				}
				// Name
				if ((fismo.getName() != null) && (!fismo.getName().isEmpty())) {
					fismoDescriptiveID.setName(fismo.getName());
				}
				femoDescriptiveID.getFismoDescriptiveID().add(fismoDescriptiveID);
			}
		}
		
		return femoDescriptiveID;
	}
	
	/**
	 * @param userID
	 * @return
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws QueryTimeoutException
	 * @throws TransactionRequiredException
	 * @throws PessimisticLockException
	 * @throws LockTimeoutException
	 * @throws PersistenceException
	 */
	public FEDSpecJpa getFedSpecJpaForUser(String userID)
			throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
			PessimisticLockException, LockTimeoutException, PersistenceException {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<FEDSpecJpa> criteriaQuery = builder.createQuery(FEDSpecJpa.class);
		Root<FEDSpecJpa> rootEntity = criteriaQuery.from(FEDSpecJpa.class);

		ParameterExpression<String> parameter = builder.parameter(String.class);
		criteriaQuery.select(rootEntity).where(builder.equal(rootEntity.get("userID"), parameter));

		//set the parameter
		TypedQuery<FEDSpecJpa> query = entityManager.createQuery(criteriaQuery);
		query.setParameter(parameter, userID);
		
		// running the query
		List<FEDSpecJpa> results = query.getResultList();

		
		if (results.size()==0){
			return new FEDSpecJpa();
		}
		
		
		FEDSpecJpa fedSpecJpa = results.get(0);

		return fedSpecJpa;
	}

	/**
	 * @param userID
	 * @return
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws QueryTimeoutException
	 * @throws TransactionRequiredException
	 * @throws PessimisticLockException
	 * @throws LockTimeoutException
	 * @throws PersistenceException
	 */
	private String getFedSpecJpaIdForUserId(String userID) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {
		
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
		Root<FEDSpecJpa> rootEntity = criteriaQuery.from(FEDSpecJpa.class);

		ParameterExpression<String> parameter = builder.parameter(String.class);
		criteriaQuery.multiselect(rootEntity.get("id")).where(builder.equal(rootEntity.get("userID"), parameter));

		//set the parameter
		TypedQuery<String> query = entityManager.createQuery(criteriaQuery);
		query.setParameter(parameter, userID);
		
		// running the query
		List<String> results = query.getResultList();
		
		String fedSpecJpaID = null;
		
		if (results.size()!=0){
			fedSpecJpaID = results.get(0);
		} 
		
		
		return fedSpecJpaID;
	}


	
	// ========================Utilities Stop===========================

}
