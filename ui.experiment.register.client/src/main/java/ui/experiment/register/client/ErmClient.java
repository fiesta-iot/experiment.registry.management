package ui.experiment.register.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.w3c.dom.Document;
import org.zkoss.zul.Messagebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fiestaiot.commons.fedspec.model.FEDSpec;
import eu.fiestaiot.commons.util.PropertyManagement;


/**
 * @author Nikos Kefalakis (nkef) e-mail: nkef@ait.edu.gr
 *
 */
public class ErmClient {

	/**
	 * The ERM Services' URI
	 */
	private String ermServicesPath;

	/**
	 * The property management instance
	 */
	static PropertyManagement propertyManagement = new PropertyManagement();
	
	/**
	 * The logger
	 */
	final static Logger logger = LoggerFactory.getLogger(ErmClient.class);

	/**
	 * The default constructor
	 */
	public ErmClient() {
		ermServicesPath = propertyManagement.getPortalErmServicesURI();
	}

	/**
	 * Prints the available services of the scheduler interface. Can be used to
	 * check that the scheduler service is alive.
	 * 
	 * @return the welcome message
	 */
	public String welcomeMessage() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		client.target(ermServicesPath);
		return "";
	}

	public String getALLUserExperimentsInXmlString(String userID, String SSOtoken) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(ermServicesPath).path("getALLUserExperiments");
		target = target.queryParam("userID", userID);
		Response response = target.request().header("iPlanetDirectoryPro", SSOtoken).get();
		String value = response.readEntity(String.class);
		logger.debug("FESpec XML: \n" + value);
		response.close();
		return value;
	}

	public String saveUserExperiments(FEDSpec fedSpec, String SSOtoken) {
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

	public void deleteUserExperiment(String femoID, String SSOtoken) {
		HttpResponse httpResponse = null;
		URIBuilder builder = new URIBuilder();
		builder.setHost(ermServicesPath).setPath("/deleteUserExperiment").setParameter("femoID", femoID);
		URI uri = null;
		try {
			uri = builder.build();
		} catch (URISyntaxException e1) {
			logger.error("Could not delete User Experiment. Wrong URL.");
			logger.error("" + e1);
		}

		try {
			HttpClient httpClient = HttpClients.createDefault();
			//Messagebox.show("uriI: " + uri);
			final HttpPost postRequest = new HttpPost(uri);
			postRequest.addHeader("iPlanetDirectoryPro", SSOtoken);
			final RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000)
					.setConnectionRequestTimeout(30000).build();
			postRequest.setConfig(config);
			httpResponse = httpClient.execute(postRequest);
			final int sc = httpResponse.getStatusLine().getStatusCode();

			if (sc != HttpStatus.SC_OK) {
				logger.error("Failed to delete FEMO. [status code: " + sc + " ]");
				logger.error("" + httpResponse);
			} else {
				logger.info("Successfully deleted FEMO. [status code: " + sc + " ].");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteAllUserExperiments(String userID, String SSOtoken) {
		HttpResponse httpResponse = null;
//		URIBuilder builder = new URIBuilder();
//		builder.setHost(ermServicesPath).setPath("/deleteUserExperiments").setParameter("userID", userID);
//		URI uri = null;
//		try {
//			uri = builder.build();
//		} catch (URISyntaxException e1) {
//			logger.error("Could not delete User Experiments. Wrong URL.");
//			logger.error("" + e1);
//		}

		try {
			HttpClient httpClient = HttpClients.createDefault();
			//Messagebox.show("uriI: " + ermServicesPath+"/deleteUserExperiments?userID="+userID);
			final HttpPost postRequest = new HttpPost(ermServicesPath+"/deleteUserExperiments?userID="+userID);
			postRequest.addHeader("iPlanetDirectoryPro", SSOtoken);
			final RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000)
					.setConnectionRequestTimeout(30000).build();
			postRequest.setConfig(config);
			httpResponse = httpClient.execute(postRequest);
			final int sc = httpResponse.getStatusLine().getStatusCode();

			if (sc != HttpStatus.SC_OK) {
				logger.error("Failed to delete FEMOs. [status code: " + sc + " ]");
				logger.error("" + httpResponse);
			} else {
				logger.info("Successfully deleted FEMOs. [status code: " + sc + " ].");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public String saveUserExperiments(Document fedSpecXmlDocument) {

		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(ermServicesPath).path("saveUserExperiments");
		Response response = target.request(MediaType.TEXT_PLAIN).post(Entity.xml(fedSpecXmlDocument));
		String reply = response.readEntity(String.class);
		logger.debug("Status: " + response.getStatus());
		logger.debug("Reply: " + reply);
		response.close();
		return reply;
	}

	public FEDSpec getFedspecFromFile(String fileContent) throws FileNotFoundException, Exception {//rename
		FEDSpec fedSpec = null;
		try {
			StringReader stringReader = new StringReader(fileContent);
			JAXBContext jaxbContext = JAXBContext.newInstance(FEDSpec.class);
			XMLInputFactory xif = XMLInputFactory.newInstance();
			XMLStreamReader xsr = xif.createXMLStreamReader(stringReader);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			fedSpec = (FEDSpec) jaxbUnmarshaller.unmarshal(xsr);
		} catch (JAXBException e) {
			logger.error("Could not read FEDSPEC. ");
			logger.error("" + e);
		}
		return fedSpec;
	}

}
