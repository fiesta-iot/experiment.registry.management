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
public class ErmClient {

//	private String ermServicesPath = "http://platform-dev.fiesta-iot.eu/experiment.erm/rest/experimentservices/";
	private String ermServicesPath = "http://localhost:8080/experiment.erm/rest/experimentservices/"; 
	

	
	
	// logger
	final static Logger logger = LoggerFactory.getLogger(ErmClient.class);

	

	
	/**
	 * Prints the available services of the scheduler interface. Can be used to
	 * check that the scheduler service is alive.
	 * 
	 * @return the welcome message
	 */
	public String welcomeMessage() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(ermServicesPath);
		
//		String token = securityUtil.login(authenticationURL, username, password);
		
		Response response = target.request().get();//Response response = target.request().header("iPlanetDirectoryPro", token).get();
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
		
		
		Response response = target.request(MediaType.APPLICATION_XML).get();

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
	

	public String saveUserExperiments(FEDSpec fedSpec) {

		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(ermServicesPath).path("saveUserExperiments");
		Response response = target.request(MediaType.TEXT_PLAIN).post(Entity.xml(fedSpec));
		
		//also works with:
		//Response response = target.request(MediaType.TEXT_PLAIN).post(Entity.entity(fedSpec, MediaType.APPLICATION_XML));
		
		
		// Read output in string format
		String reply = response.readEntity(String.class);
		
		logger.debug("Status: " + response.getStatus());
		logger.debug("Reply: " + reply);
		
		
		response.close();

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

	
	//utility
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
	
	
	
	//Deprecated
	public String saveUserExperiments(Document fedSpecXmlDocument) {

		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(ermServicesPath).path("saveUserExperiments");
		Response response = target.request(MediaType.TEXT_PLAIN).post(Entity.xml(fedSpecXmlDocument));
		
		//also works with:
		//Response response = target.request(MediaType.TEXT_PLAIN).post(Entity.entity(fedSpec, MediaType.APPLICATION_XML));
		
		
		// Read output in string format
		String reply = response.readEntity(String.class);
		
		logger.debug("Status: " + response.getStatus());
		logger.debug("Reply: " + reply);
		
		
		response.close();

		return reply;

	}
	

	//Deprecated
	public String saveFromFile1(String filepath) throws FileNotFoundException, Exception {

		
		File file = new File(filepath);
		
		//Build the xml document
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document xmlDocument = documentBuilder.parse(file);
		//xmlDocument.getDocumentElement().normalize();
		
		//pretty print the document
		printDocument(xmlDocument, System.out);
		

		//send the generated document to the ERM 
		String reply = saveUserExperiments(xmlDocument);
		

		return reply;
	}

	
}
