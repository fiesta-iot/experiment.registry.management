package eu.fiestaiot.experiment.erm.rest;



import java.net.HttpURLConnection;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.LockTimeoutException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import eu.fiestaiot.commons.util.PropertyManagement;
import eu.fiestaiot.experiment.erm.jpa.services.FedSpecJpaServicesImpl;
import eu.fiestaiot.experiment.erm.jpa.utils.SecurityUtil;
import eu.fiestaiot.commons.fedspec.model.*;
import eu.fiestaiot.commons.expdescriptiveids.model.*;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Nikos Kefalakis (nkef) e-mail: nkef@ait.edu.gr
 * 
 */
@Path("/experimentservices")
@Consumes({ "application/xml", "application/json" })
@Produces({ "application/xml", "application/json" })
public class ExperimentRegistryManagementRsControler {
	
	
	//FedspecJpaPersistenceUnit Entity Manager
//	EntityManager entityManager = null;
	
//	//FedSpec Jpa Services
//	FedSpecJpaServicesImpl fedSpecJpaServicesImpl = null;

	//Logger's initialization
	final static Logger logger = LoggerFactory.getLogger(ExperimentRegistryManagementRsControler.class);
	
	
	
	
	// Property Management
	PropertyManagement propertyManagement = null;
	

	public ExperimentRegistryManagementRsControler(){
		
		
		logger.debug("Initializing EntityManager in the ExperimentRegistryManagementRsControler constructor...");
		
		//Initializing entity manager
//		entityManager = HibernateUtil.getEntityManager();

//		//Initializing FedSpec Jpa Services
//		FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
		
		
		
		

		
		//===========start of Test code==============
//		HibernateTest hibernateTest = new HibernateTest();
//		
//		hibernateTest.runTest();
		//===========stop of Test code==============

		
		// ============READING PROPERIES ============= //
		// Property Management Initialization
		propertyManagement = new PropertyManagement();
		
		
	}
	
	
	/**
	 * @return
	 */
	@GET()
	@Produces("text/plain")
	public String welcomeMessage() {

		String welcomeText;

		welcomeText = "Welcome to Experiment Registry Management Services\n"
			    + "==================================================\n\n"
				
				+ "The exposed Services are:\n"
				+ "POST:saveUserExperiments(fedSpec:FEDSpec):String \n"
				+ "POST:deleteUserExperiments (userID:String):String \n"
				+ "POST:saveUserExperiment(femo:FEMO, userID:String):String \n"
				+ "POST:deleteUserExperiment (femoID:String):String \n"
				+ "POST:saveExperimentServiceModelObject (fismo:FISMO, femoID:String):String \n"
				+ "POST:deleteExperimentServiceModelObject (fismoID:String):String \n"
				+ "GET: getALLUserExperiments (userID:String):FEDSpec \n"
				+ "GET:getAllUserExperimentsDescriptions (userID:String):ExpDescriptiveIDs \n"
				+ "GET: getExperimentDescription (femoID:String):FemoDescriptiveID \n"
				+ "GET:getExperimentModelObject (femoID:String):FEMO \n"
				+ "GET:getExperimentServiceModelObject (fismoID:String):FISMO \n"
				
				+ "=============================================================\n\n";

		logger.debug(welcomeText);
		
		return welcomeText;
	}
		



	@POST
	@Path("/saveUserExperiments")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public Response saveUserExperiments(FEDSpec fedSpec, @Context HttpHeaders httpHeaders) throws EntityExistsException,
			RollbackException, IllegalArgumentException, IllegalStateException, TransactionRequiredException {

		logger.debug("Entering saveUserExperiments...");
		
		logger.debug(
				"Is User Validation Enabled? " + propertyManagement.getErmValidateUserWithiPlanetDirectoryProToken());

		if (propertyManagement.getErmValidateUserWithiPlanetDirectoryProToken()) {

			String token = "";
			if (!httpHeaders.getRequestHeader("iPlanetDirectoryPro").isEmpty()) {

				token = httpHeaders.getRequestHeader("iPlanetDirectoryPro").get(0);

			}

			logger.debug("recieved iPlanetDirectoryPro: " + token);

			if (token.equals("")) {

				logger.debug("Didnt Recieved a Token. Exiting Service!");

				return Response.status(HttpURLConnection.HTTP_BAD_METHOD).entity("iPlanetDirectoryProTokenNotProvided")
						.build();

			} else {

				SecurityUtil securityUtil = new SecurityUtil();

				String loggedInuserID = securityUtil.getUserID(token, propertyManagement.ermGetSecurityUserURL());

				if (loggedInuserID.equals(fedSpec.getUserID()) && !loggedInuserID.equals("")) {
					
					logger.debug("token Recieved match with User ID. Procieding to method execution");

					// Initializing FedSpec Jpa Services
					FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();

					String status = fedSpecJpaServicesImpl.persistFedSpec(fedSpec);

					fedSpecJpaServicesImpl.entityManagerClose();

					return Response.status(HttpURLConnection.HTTP_OK).entity(status).build();

				} else {

					logger.error("token Recieved does not match with User ID Recieved Exiting service");
					
					return Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
							.entity("iPlanetDirectoryProTokenDoesntMachWithUserID").build();
				}

			}

		} else {
			// Initializing FedSpec Jpa Services
			FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();

			String status = fedSpecJpaServicesImpl.persistFedSpec(fedSpec);

			fedSpecJpaServicesImpl.entityManagerClose();

			return Response.status(HttpURLConnection.HTTP_OK).entity(status).build();
		}
	}

	@POST
	@Path("/deleteUserExperiments")
	public Response deleteUserExperiments (@QueryParam("userID") String userID, @Context HttpHeaders httpHeaders) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {
		logger.debug("Entering deleteUserExperiments...");
		logger.debug("recieved UserID: "+ userID);
		
		logger.debug(
				"Is User Validation Enabled? " + propertyManagement.getErmValidateUserWithiPlanetDirectoryProToken());

		if (propertyManagement.getErmValidateUserWithiPlanetDirectoryProToken()) {

			String token = "";
			if (!httpHeaders.getRequestHeader("iPlanetDirectoryPro").isEmpty()) {

				token = httpHeaders.getRequestHeader("iPlanetDirectoryPro").get(0);

			}

			logger.debug("recieved iPlanetDirectoryPro: " + token);

			if (token.equals("")) {

				logger.debug("Didnt Recieved a Token. Exiting Service!");

				return Response.status(HttpURLConnection.HTTP_BAD_METHOD).entity("iPlanetDirectoryProTokenNotProvided")
						.build();

			} else {

				SecurityUtil securityUtil = new SecurityUtil();

				String loggedInUserID = securityUtil.getUserID(token, propertyManagement.ermGetSecurityUserURL());

				if (loggedInUserID.equals(userID) && !loggedInUserID.equals("")) {
					
					logger.debug("token Recieved match with User ID. Procieding to method execution");

					//Initializing FedSpec Jpa Services
					FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
					
					fedSpecJpaServicesImpl.deleteAllUserFEMOs(userID);
					
					fedSpecJpaServicesImpl.entityManagerClose();

					return Response.status(HttpURLConnection.HTTP_OK).entity("UserDeleteSuccessful").build();

				} else {

					logger.error("token Recieved does not match with User ID Recieved Exiting service");
					
					return Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
							.entity("iPlanetDirectoryProTokenDoesntMachWithUserID").build();
				}

			}

		} else {
			//Initializing FedSpec Jpa Services
			FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();

			fedSpecJpaServicesImpl.deleteAllUserFEMOs(userID);
			
			fedSpecJpaServicesImpl.entityManagerClose();

			return Response.status(HttpURLConnection.HTTP_OK).entity("UserDeleteSuccessful").build();
		}
		


	}

	
	@POST
	@Path("/saveUserExperiment")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public Response saveUserExperiment(FEMO femo, @QueryParam("userID") String userID, @Context HttpHeaders httpHeaders) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {
		logger.debug("Entering saveUserExperiment...");
		
		logger.debug(
				"Is User Validation Enabled? " + propertyManagement.getErmValidateUserWithiPlanetDirectoryProToken());

		if (propertyManagement.getErmValidateUserWithiPlanetDirectoryProToken()) {

			String token = "";
			if (!httpHeaders.getRequestHeader("iPlanetDirectoryPro").isEmpty()) {

				token = httpHeaders.getRequestHeader("iPlanetDirectoryPro").get(0);

			}

			logger.debug("recieved iPlanetDirectoryPro: " + token);

			if (token.equals("")) {

				logger.debug("Didnt Recieved a Token. Exiting Service!");

				return Response.status(HttpURLConnection.HTTP_BAD_METHOD).entity("iPlanetDirectoryProTokenNotProvided")
						.build();

			} else {

				SecurityUtil securityUtil = new SecurityUtil();

				String loggedInuserID = securityUtil.getUserID(token, propertyManagement.ermGetSecurityUserURL());

				if (loggedInuserID.equals(userID) && !loggedInuserID.equals("")) {
					
					logger.debug("token Recieved match with User ID. Procieding to method execution");

					logger.debug("recieved UserID: "+ userID);

					//Initializing FedSpec Jpa Services
					FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
					
//					fedSpecJpaServicesImpl.entityManagerOpen();
					
					
					String femoID = fedSpecJpaServicesImpl.persistFemoForUser(femo, userID);
					
					
					fedSpecJpaServicesImpl.entityManagerClose();

					return Response.status(HttpURLConnection.HTTP_OK).entity(femoID).build();

				} else {

					logger.error("token Recieved does not match with User ID Recieved Exiting service");
					
					return Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
							.entity("iPlanetDirectoryProTokenDoesntMachWithUserID").build();
				}

			}

		} else {
			
			logger.debug("recieved UserID: "+ userID);

			//Initializing FedSpec Jpa Services
			FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
			
//			fedSpecJpaServicesImpl.entityManagerOpen();
			
			
			String femoID = fedSpecJpaServicesImpl.persistFemoForUser(femo, userID);
			
			
			fedSpecJpaServicesImpl.entityManagerClose();
			
			return Response.status(HttpURLConnection.HTTP_OK).entity(femoID).build();
		}
		


	}
	
	

	

	@POST
	@Path("/deleteUserExperiment")
	public String deleteUserExperiment (@QueryParam("userID") String userID, @QueryParam("femoID") String femoID, @Context HttpHeaders httpHeaders)   throws IllegalArgumentException, IllegalStateException, TransactionRequiredException{
		logger.debug("Entering deleteUserExperiment...");
		
		logger.debug("recieved femoID: "+ femoID);
		
		//Initializing FedSpec Jpa Services
		FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
		
//		fedSpecJpaServicesImpl.entityManagerOpen();
		

		
		fedSpecJpaServicesImpl.deleteFemo(userID, femoID);

		

		fedSpecJpaServicesImpl.entityManagerClose();
		
		return "successful";

	}
	
	
	
	@POST
	@Path("/saveExperimentServiceModelObject")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String saveUserExperimentServiceModelObject(FISMO fismo, @QueryParam("femoID") String femoID) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {
		logger.debug("Entering saveExperimentServiceModelObject...");
		logger.debug("recieved femoID: "+ femoID);

		//Initializing FedSpec Jpa Services
		FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
		
//		fedSpecJpaServicesImpl.entityManagerOpen();
		
	
		String fismoID = fedSpecJpaServicesImpl.persistFismoForFemo(fismo, femoID);


		fedSpecJpaServicesImpl.entityManagerClose();
	
		return fismoID;

	}
	

	@POST
	@Path("/deleteExperimentServiceModelObject")
	public String deleteExperimentServiceModelObject (@QueryParam("femoID") String femoID, @QueryParam("fismoID") String fismoID, @Context HttpHeaders httpHeaders) throws IllegalArgumentException, IllegalStateException, TransactionRequiredException  {
		logger.debug("Entering deleteExperimentServiceModelObject...");
		logger.debug("recieved fismoID: "+ fismoID);

		
		//Initializing FedSpec Jpa Services
		FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
		
//		fedSpecJpaServicesImpl.entityManagerOpen();
		

		fedSpecJpaServicesImpl.deleteFismo(femoID, fismoID);
		

		fedSpecJpaServicesImpl.entityManagerClose();
		
		return "successful";

	}	
	
	
	
	

	@GET
	@Path("/getALLUserExperiments")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_XML)
	public FEDSpec getALLUserExperiments (@QueryParam("userID") String userID, @Context HttpHeaders httpHeaders) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException, PessimisticLockException, LockTimeoutException, PersistenceException{
		logger.debug("Entering getALLUserExperiments...");
		logger.debug("recieved UserID: "+ userID);
		
		//Initializing FedSpec Jpa Services
		FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
		
//		fedSpecJpaServicesImpl.entityManagerOpen();
		

		
		FEDSpec fedSpec = fedSpecJpaServicesImpl.getFedSpecForUser(userID);
		
		
		// ===========Debug Code========================
		if (logger.isDebugEnabled() && fedSpec!= null) {
			for (FEMO femo : fedSpec.getFEMO()) {
				logger.debug("femo ID:" + femo.getId());
				logger.debug("femo description:" + femo.getDescription());

				for (FISMO fismo : femo.getFISMO()) {
					logger.debug("fismo ID:" + fismo.getId());
					logger.debug("fismo description:" + fismo.getDescription());
					logger.debug("Original Sparql Query:" + fismo.getQueryControl().getQueryRequest().getQuery());
				}
			}
		}
		// =============End of Debug Code ============
		
		

		fedSpecJpaServicesImpl.entityManagerClose();
		return fedSpec;

	}
	
	@GET 
	@Path("/getAllUserExperimentsDescriptions")
	public ExpDescriptiveIDs getAllUserExperimentsDescriptions (@QueryParam("userID") String userID, @Context HttpHeaders httpHeaders) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {
		logger.debug("Entering getAllUserExperimentsDescriptions...");
		logger.debug("recieved UserID: "+ userID);	
		
		//Initializing FedSpec Jpa Services
		FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
		
//		fedSpecJpaServicesImpl.entityManagerOpen();
		

		
		ExpDescriptiveIDs expDescriptiveIDs = fedSpecJpaServicesImpl.getAllExperimentDescriptiveIDsForUser(userID);
		

		fedSpecJpaServicesImpl.entityManagerClose();
		return expDescriptiveIDs;

	}

	@GET
	@Path("/getExperimentDescription")
	public FemoDescriptiveID getExperimentDescription (@QueryParam("femoID") String femoID) throws IllegalArgumentException {
		logger.debug("Entering getExperimentDescription...");
		logger.debug("recieved femoID: "+ femoID);		

		//Initializing FedSpec Jpa Services
		FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
		
//		fedSpecJpaServicesImpl.entityManagerOpen();
		

		
		FemoDescriptiveID femoDescriptiveID = fedSpecJpaServicesImpl.getExperimentDescriptiveID(femoID);
		

		fedSpecJpaServicesImpl.entityManagerClose();
		return femoDescriptiveID;

	}
	
	
	
	@GET
	@Path("/getExperimentModelObject")
	public FEMO getExperimentModelObject (@QueryParam("femoID") String femoID) throws IllegalArgumentException {
		logger.debug("Entering getExperimentModelObject...");
		logger.debug("recieved femoID: "+ femoID);
		
		//Initializing FedSpec Jpa Services
		FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
		
//		fedSpecJpaServicesImpl.entityManagerOpen();
		

		
		FEMO femo = fedSpecJpaServicesImpl.getFemo(femoID);
		

		fedSpecJpaServicesImpl.entityManagerClose();
		
		return femo;

	}
	
	
	@GET
	@Path("/getExperimentServiceModelObject")
	public FISMO getExperimentServiceModelObject (@QueryParam("fismoID") String fismoID) throws IllegalArgumentException {
		logger.debug("Entering getExperimentServiceModelObject...");
		logger.debug("recieved fismoID: "+ fismoID);
		
		
		//Initializing FedSpec Jpa Services
		FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
		
//		fedSpecJpaServicesImpl.entityManagerOpen();
		

		
		FISMO fismo = fedSpecJpaServicesImpl.getFismo(fismoID);
		

		fedSpecJpaServicesImpl.entityManagerClose();
		
		
		return fismo;

	}

	
	
	@GET
	@Path("/getDiscoverableExperimentServiceModelObject")
	public List<FISMO> getDiscoverableExperimentServiceModelObjects () throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {
		logger.debug("Entering getDiscoverableExperimentServiceModelObject...");
		//Initializing FedSpec Jpa Services
		FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();		
		
//		fedSpecJpaServicesImpl.entityManagerOpen();

		List<FISMO> discoverableFismos = fedSpecJpaServicesImpl.getAllDiscoverableFismos();
				

		fedSpecJpaServicesImpl.entityManagerClose();
		
		return discoverableFismos;

	}
	
	
	
	@POST
	@Path("/saveNewSparqlQueryWithFismo")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String saveNewSparqlQueryWithFismo(String query, @QueryParam("description") String description) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {
		logger.debug("Entering saveNewSparqlQueryWithFismo...");
		logger.debug("recieved femoID: "+ query);

		//Initializing FedSpec Jpa Services
		FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();
		
		
	
		String fismoID = fedSpecJpaServicesImpl.persistNewSparqlQueryWithFismo(description, query);


		fedSpecJpaServicesImpl.entityManagerClose();
	
		return fismoID;

	}

	@GET
	@Path("/getAvailableQueriesWithCriteria")
	public List<FISMO> getAvailableQueriesWithCriteria (@Context HttpHeaders httpHeaders) throws IllegalStateException, IllegalArgumentException, QueryTimeoutException, TransactionRequiredException,
	PessimisticLockException, LockTimeoutException, PersistenceException {
		logger.debug("Entering getAvailableQueriesWithCriteria...");
//		@QueryParam("description") String description,@QueryParam("quantityKind") String quantityKind
		
		String description = "";
		String quantityKind = "";
		
		if (!httpHeaders.getRequestHeader("description").isEmpty()) {

			description = httpHeaders.getRequestHeader("description").get(0);

		}

		if (!httpHeaders.getRequestHeader("quantityKind").isEmpty()) {

			quantityKind = httpHeaders.getRequestHeader("quantityKind").get(0);

		}
		
		

		
		
		//Initializing FedSpec Jpa Services
		FedSpecJpaServicesImpl fedSpecJpaServicesImpl = new FedSpecJpaServicesImpl();		
		

		List<FISMO> queryControlWithCriteriaList = fedSpecJpaServicesImpl.getQueryControlWithCriteria(description, quantityKind);
				

		fedSpecJpaServicesImpl.entityManagerClose();
		
		return queryControlWithCriteriaList;

	}
	
}
