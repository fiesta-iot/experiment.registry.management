package eu.fiestaiot.experiment.erm.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;


//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.UriBuilder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fiestaiot.commons.expdescriptiveids.model.ExpDescriptiveIDs;
import eu.fiestaiot.commons.expdescriptiveids.model.FemoDescriptiveID;
import eu.fiestaiot.commons.fedspec.model.FEDSpec;
import eu.fiestaiot.commons.fedspec.model.FEMO;
import eu.fiestaiot.commons.fedspec.model.FISMO;
import eu.fiestaiot.experiment.erm.utils.Deserializer;
import eu.fiestaiot.experiment.erm.utils.SecurityUtil;

import org.w3c.dom.Document;

import ch.qos.logback.core.OutputStreamAppender;




/**
 * @author Nikos Kefalakis (nkef) e-mail: nkef@ait.edu.gr
 *
 */
public class SecureErmClient {

//	private String ermServicesPath = "https://platform.fiesta-iot.eu/experiment.erm/rest/experimentservices/";
	private String ermServicesPath = "https://platform-dev.fiesta-iot.eu/experiment.erm/rest/experimentservices/";
//	private String ermServicesPath = "http://localhost:8080/experiment.erm/rest/experimentservices/"; 
	
	
	
//	private String authenticationURL = "http://platform-dev.fiesta-iot.eu:8090/openam/json/users?_action=idFromSession";
//	private String authenticationURL = "http://platform.fiesta-iot.eu:8090/openam/json/users?_action=idFromSession";
	private String authenticationURL = "http://platform-dev.fiesta-iot.eu:8090/openam/json/authenticate";
	
//	private String username = "Inria";
//	private String password = "FIESTAINRIA";
	
//	private String username = "amadmin";
//	private String password = "rblJW0DXQp6n4qh7rDy6";
	
	
//	private String username = "testuser1";
//	private String password = "lF8e7ylAl5OzMezS20nz";
	
	private String username = "testuser2";
	private String password = "WKTg6ZSO0dkFU7DCAxS1";
	
	private String getSecurityUserURL = "http://platform.fiesta-iot.eu:8090/openam/json/users?_action=idFromSession";
	
	
	//the SecurityUtil class
	private SecurityUtil securityUtil =null ;
	
	
	// logger
	final static Logger logger = LoggerFactory.getLogger(ErmClient.class);

	
	
	
	public SecureErmClient(){
		
		securityUtil = new SecurityUtil();
		
	}
	
	
	/**
	 * Prints the available services of the scheduler interface. Can be used to
	 * check that the scheduler service is alive.
	 * 
	 * @return the welcome message
	 */
	public String welcomeMessage() {
		
		String SSOtoken = securityUtil.login(authenticationURL, username, password);
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(ermServicesPath);
		
		
		Response response = target.request().header("iPlanetDirectoryPro", SSOtoken).get();
		// Read output in string format
		String value = response.readEntity(String.class);
		response.close();

		logger.debug(value);

		return value;

	}

	public FEDSpec getALLUserExperimentsEntity(String userID) {

		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(ermServicesPath).path("getALLUserExperiments");

		// Prepare the request
		target = target.queryParam("userID", userID);
		
		
		Response response = target.request(MediaType.APPLICATION_XML).get();//Response response = target.request(MediaType.APPLICATION_XML).header("iPlanetDirectoryPro", securityUtil.login(authenticationURL, username, password)).get();

		// Read the entity
		FEDSpec fedSpec = response.readEntity(FEDSpec.class);

		logger.debug("fedSpec userID:" + fedSpec.getUserID());
		for (FEMO femo : fedSpec.getFEMO()) {
			logger.debug("femo ID:" + femo.getId());
			logger.debug("femo description:" + femo.getDescription());

			for (FISMO fismo : femo.getFISMO()) {
				logger.debug("fismo ID:" + fismo.getId());
				logger.debug("fismo description:" + fismo.getDescription());
			
			
			
				//test code to be deleted//////////////////////////////////////////////////////////////////////////
				
				
				
				String SPARQL = fismo.getQueryControl().getQueryRequest().getQuery();
				
				logger.debug("Original Sparql Query:" + SPARQL);
				
				logger.debug("Sparql Query Striped from CDATA:" + stripCDATA(SPARQL));
				
				logger.debug("Sparql Query Striped from CDATA two times:" + stripCDATA(stripCDATA(SPARQL)));
				
				//End of test code to be deleted//////////////////////////////////////////////////////////////////////////
				
			
			}

		}
		response.close();

		return fedSpec;

	}
	
	
	public static String stripCDATA(String s) {
	    s = s.trim();
	    if (s.startsWith("<![CDATA[")) {
	      s = s.substring(9);
	      int i = s.indexOf("]]>");
//	      if (i == -1) {
//	        throw new IllegalStateException(
//	            "argument starts with <![CDATA[ but cannot find pairing ]]&gt;");
//	      }
	      
	      if (i == -1) {
	    	  return s;
	      }
	      
	      s = s.substring(0, i);
	    }
	    return s;
	  }
	
	

	public String getALLUserExperimentsInXmlString(String userID) {

		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(ermServicesPath ).path("getALLUserExperiments");

		// Prepare the request
		target = target.queryParam("userID", userID);

		Response response = target.request().get();

		// Read output in string format
		String value = response.readEntity(String.class);

		logger.debug("FESpec XML: \n" + value);

		response.close();

		return value;

	}
	
	
	public ExpDescriptiveIDs getAllUserExperimentsDescreptions(String userID) {

		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(ermServicesPath).path("getAllUserExperimentsDescreptions");

		// Prepare the request
		target = target.queryParam("userID", userID);

		Response response = target.request().get();

		// Read the entity
		ExpDescriptiveIDs expDescriptiveIDs = response.readEntity(ExpDescriptiveIDs.class);

		
		for (FemoDescriptiveID femoDescriptiveID: expDescriptiveIDs.getFemoDescriptiveID()){
			
			logger.debug("femo Domain of Interes:" );
			for (String domainOfInterest: femoDescriptiveID.getDomainOfInterest()){
				logger.debug(domainOfInterest);
			}
			
			logger.debug("femo Description:" + femoDescriptiveID.getDescription());
			
			
			
		}
		

		return expDescriptiveIDs;

	}
	
	
	
	
	public FEMO getExperimentModelObjectEntity(String femoID) {

		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(ermServicesPath).path("getExperimentModelObject");

		// Prepare the request
		target = target.queryParam("femoID", femoID);

		Response response = target.request().get();

		// Read the entity
		FEMO femo = response.readEntity(FEMO.class);

		logger.debug("femo ID:" + femo.getId());
		logger.debug("femo description:" + femo.getDescription());

		for (FISMO fismo : femo.getFISMO()) {
			logger.debug("fismo ID:" + fismo.getId());
			logger.debug("fismo description:" + fismo.getDescription());
		}

		response.close();

		return femo;

	}

	public FISMO getExperimentServiceModelObjectEntity(String fismoID) {

		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(ermServicesPath).path("getExperimentServiceModelObject");

		// Prepare the request
		target = target.queryParam("fismoID", fismoID);

		
//		Form form = new Form();
//		form.param("fismoID", fismoID);
	
		
		Response response = target.request().get();

		// Read the entity
		FISMO fismo = response.readEntity(FISMO.class);

		logger.debug("fismo ID:" + fismo.getId());
		logger.debug("fismo description:" + fismo.getDescription());


		response.close();

		return fismo;

	}

	

	
	public String saveUserExperiments(FEDSpec fedSpec) {
		
		String SSOtoken = securityUtil.login(authenticationURL, username, password);
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(ermServicesPath).path("saveUserExperiments");
		Response response = target.request(MediaType.TEXT_PLAIN).header("iPlanetDirectoryPro", SSOtoken)
				.post(Entity.xml(fedSpec));
		String reply = response.readEntity(String.class);
		logger.debug("Status: " + response.getStatus());
		logger.debug("Reply: " + reply);
		response.close();
		return reply;
	}


	
	public String saveFromFile2(String filepath) throws FileNotFoundException, Exception {

		
		FEDSpec fedSpec = null;
		 try {

			File file = new File(filepath);
			JAXBContext jaxbContext = JAXBContext.newInstance(FEDSpec.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			fedSpec = (FEDSpec) jaxbUnmarshaller.unmarshal(file);
			System.out.println(fedSpec);

		  } catch (JAXBException e) {
			e.printStackTrace();
		  }
		
		
		
		// Save the experiment
		String reply = saveUserExperiments(fedSpec);
		

		return reply;
	}
	


	
	
	
	

	public String populateAndSaveTestUserExperiment() {

		//Generate Sample FEDSpec
		FEDSpec fedSpec;
		FEMO femo;
		FISMO fismo;

		// FISMO============================
		fismo = new FISMO();
		fismo.setId("sampleFismoID");
		fismo.setDescription("sampleServiceDescreption");

		// FEMO=============================

		femo = new FEMO();
		femo.setId("sampleFemoID");
		femo.setDescription("sampleExperimentDescreption");
		femo.getFISMO().add(fismo);

		// FEDSpec===========================
		fedSpec = new FEDSpec();
		fedSpec.setUserID("sampleUserID");
		fedSpec.getFEMO().add(femo);

		
		
		// Save the experiment
		String reply = saveUserExperiments(fedSpec);

		return reply;

	}
	
	
	
	public void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	    transformer.transform(new DOMSource(doc), 
	         new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	    
	}

}
