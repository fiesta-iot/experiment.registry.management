package ui.experiment.register.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.MaximizeEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import eu.fiestaiot.commons.fedspec.model.FEDSpec;
import eu.fiestaiot.commons.fedspec.model.FEMO;
import eu.fiestaiot.commons.util.PropertyManagement;

public class ExperimentRegisterController extends SelectorComposer<Component> {

	/**
	 * Random Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Wired variables with frontend
	 */
	@Wire
	private Window winExperimentRegisterClient;

	@Wire
	private Listbox femosListbox;

	@Wire
	private Textbox fileNameListbox;

	@Wire
	private Textbox femoXMLTextbox;

	private String femoXML;

	/**
	 * The filename of the FEDSPEC file: input from selection
	 */
	private String fileName;

	/**
	 * The femo service
	 */
	private FemoService femoService;

	/**
	 * The femos model
	 */
	private ListModel<Femo> femos;

	/**
	 * The femos list
	 */
	List<Femo> allFemos;

	/**
	 * The fedspec of the user
	 */
	FEDSpec userFEDSpec;

	/**
	 * Media for upload functionality
	 */
	Media media;

	/**
	 * The user ID
	 */
	private String userID;

	String token;

	/**
	 * The URL of the experiment service
	 */
	private final String experimentServiceURL;

	/**
	 * The property management instance
	 */
	static PropertyManagement propertyManagement = new PropertyManagement();

	/**
	 * The name of the cookie
	 */
	private final static String IPLANETDIRECTORYPRO = "iPlanetDirectoryPro";

	/**
	 * The user authentication endpoint
	 */
	private static String userInfoEndpoint;

	/**
	 * The ERM Client instance
	 */
	ErmClient ermClient = new ErmClient();
	
	/**
	 * The logger
	 */
	final static Logger logger = LoggerFactory.getLogger(ExperimentRegisterController.class);

	/**
	 * Default constructor
	 * 
	 * Initializes all variables need for the tpi configurator
	 */
	public ExperimentRegisterController() {
		experimentServiceURL = propertyManagement.getErmServicesURI();
		userInfoEndpoint = propertyManagement.getAuthenticationURI();

		userID = getFiestaUserIDFromCookie();
		femoService = new FemoServiceImpl(userID);

		allFemos = femoService.findAll();
		ArrayList<String> femoNames = new ArrayList<String>();
		ArrayList<String> femoIDs = new ArrayList<String>();
		for (Femo femo : allFemos) {
			femoNames.add(femo.getFemoName());
			femoIDs.add(femo.getFemoID());
		}
		femos = new ListModelList<Femo>(allFemos);
	}

	/**
	 * Listener of button with id deleteFEDSPEC. 
	 * Deletes the FEDSPEC.
	 */
	@Listen("onClick = #deleteFEDSPEC")
	public void deleteFEDSPEC() {
		try {
			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			String token = request.getParameter(IPLANETDIRECTORYPRO);

			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equalsIgnoreCase(IPLANETDIRECTORYPRO)) {
					token = cookie.getValue();
				}
			}

			Set<Femo> femoSelected = ((ListModelList<Femo>) femos).getSelection();
			String selectedFemo = null;
			for (Femo f : femoSelected) {
				selectedFemo = (f.getFemoID());
			}

			if (selectedFemo != null) {
				ermClient.deleteUserExperiment(selectedFemo, token);
				//update list
				setFemoXML("");
				femoXMLTextbox.setValue("");
			}
		} catch (Exception e) {
			logger.error("Could not delete selected femo. ");
			logger.error("" + e);
			Messagebox.show("Could not delete selected femo. Please try again.");
		}
		logger.info("Successfully deleted FEDSPEC.");
	}
	
	
	/**
	 * Listener of button with id deleteAllFEDSPEC. 
	 * Deletes the FEDSPEC.
	 */
	@Listen("onClick = #deleteAllFEDSPEC")
	public void deleteAllFEDSPEC() {
		try {

			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			String token = request.getParameter(IPLANETDIRECTORYPRO);

			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equalsIgnoreCase(IPLANETDIRECTORYPRO)) {
					token = cookie.getValue();
				}
			}
			ermClient.deleteAllUserExperiments(userID, token);
			
			allFemos = femoService.clearFemos();
			femos = new ListModelList<Femo>(allFemos);			
			femosListbox.setModel(femos);
			userFEDSpec = null;
			fileName = "";
			fileNameListbox.setText(fileName);
			setFemoXML("");
			femoXMLTextbox.setValue("");
			
		} catch (Exception e) {
			logger.error("Could not delete all Femos. ");
			logger.error("" + e);
			Messagebox.show("Could not delete all Femos. ");
		}
		logger.info("Successfully deleted all Femos.");
	}

	/**
	 * Listener of button with id exportFEDSPEC. Saves the FEDSPEC xml file in
	 * local directory.
	 */
	@Listen("onClick = #exportFEDSPEC")
	public void exportFedspec() {
		String fedspec = ermClient.getALLUserExperimentsInXmlString(userID, token);
				
		if(fedspec!=null){		
			if (fedspec != null || !fedspec.isEmpty()) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = null;
				try {
					builder = factory.newDocumentBuilder();
				} catch (ParserConfigurationException e1) {
					logger.error("Could not parse femo.");
					logger.error("" + e1);
					e1.printStackTrace();
				}
				Filedownload.save(fedspec, "xml", "FEDSPEC-"+userID);
				logger.info("Successfully exported FEDSPEC.");
			}
		}
		else{
			logger.error("Failed to export FEDSPEC.");
		}
	}
	
	
	/**
	 * Listener of button with id exportFEMO. Saves the Femo xml file in
	 * local directory.
	 */
	@Listen("onClick = #exportFEMO")
	public void exportFemo() {
		Set<Femo> femoSelected = ((ListModelList<Femo>) femos).getSelection();
		String selectedFemo = null;
		for (Femo f : femoSelected) {
			selectedFemo = (f.getFemoName());
		}
		if (selectedFemo != null) {
			String xmlFromString = getFemoXML();
			if (xmlFromString != null || !xmlFromString.isEmpty()) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = null;
				try {
					builder = factory.newDocumentBuilder();
				} catch (ParserConfigurationException e1) {
					logger.error("Could not parse fedspec.");
					logger.error("" + e1);
					e1.printStackTrace();
				}
				Filedownload.save(xmlFromString, "xml", selectedFemo);
				logger.info("Successfully exported FEMO.");
			}
		}
		else{
			Messagebox.show("Please select a Femo");
			logger.error("Failed to export FEMO.");
		}
	}
	
	

	/**
	 * Gets the user ID from cookie
	 */
	public String getFiestaUserIDFromCookie() {

		HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		token = request.getParameter(IPLANETDIRECTORYPRO);

		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equalsIgnoreCase(IPLANETDIRECTORYPRO)) {
				token = cookie.getValue();
			}
		}

		Client client = Client.create();
		WebResource webResourceOpenAM = client.resource(userInfoEndpoint);

		ClientResponse responseAuth = webResourceOpenAM.type("application/json").header(IPLANETDIRECTORYPRO, token)
				.post(ClientResponse.class, "{}");
		if (responseAuth.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + responseAuth.getStatus());
		}
		String userObject = responseAuth.getEntity(String.class);
		final ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map;
		try {
			map = mapper.readValue(userObject, new TypeReference<HashMap<String, Object>>() {
			});
			userID = (String) map.get("id");
		} catch (JsonParseException e) {
			logger.error("Could not parse response from OpenAM containing user ID.");
			logger.error("" + e);
		} catch (JsonMappingException e) {
			logger.error("Could not parse response from OpenAM containing user ID.");
			logger.error("" + e);
		} catch (IOException e) {
			logger.error("Could not parse response from OpenAM containing user ID.");
			logger.error("" + e);
		}
		
		logger.info("Successfully retrieved user ID.");
		return userID;
	}

	/**
	 * Listener for any change at the div with id femosListbox. Updates the
	 * femos Listbox
	 */
	@Listen("onSelect = #femosListbox")
	public void changeResource() {
		
		for(int i=0; i<femos.getSize(); i++){
			logger.error("femo id: " +femos.getElementAt(i).getFemoID());
			logger.error("femo name: " +femos.getElementAt(i).getFemoName());
		}
		

		Set<Femo> femoSelected = ((ListModelList<Femo>) femos).getSelection();
		
		String selectedFemo = null;
		for (Femo f : femoSelected) {
			selectedFemo = (f.getFemoID());
			logger.error("selected Femo: "+selectedFemo);

		}
		
		if (selectedFemo != null) {
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget target = client.target(experimentServiceURL).path("getExperimentModelObject");
			target = target.queryParam("femoID", selectedFemo);
			Response response = target.request().header("iPlanetDirectoryPro", token).get();
			String femo = response.readEntity(String.class);
			setFemoXML(femo);
			femoXMLTextbox.setValue(femo);
		}
		else{
			logger.error("Selected FEMO is null.");
		}
	}

	/**
	 * Listener that handles the widow's minimization functionality
	 */
	@Listen("onMinimize = #winExperimentRegisterClient")
	public void OnMinimizeWindowExperimentRegisterClient() {
		winExperimentRegisterClient.setWidth("20%");
		winExperimentRegisterClient.setHeight("45px");
		winExperimentRegisterClient.setMinimizable(false);
		winExperimentRegisterClient.setMaximizable(true);
		winExperimentRegisterClient.setVisible(true);
		winExperimentRegisterClient.invalidate();
	}

	/**
	 * Listener that handles the widow's "full screen" functionality
	 */
	@Listen("onMaximize = #winExperimentRegisterClient")
	public void OnMaximizeWindowExperimentRegisterClient(MaximizeEvent event) {
		event.stopPropagation();
		winExperimentRegisterClient.setMaximized(false);
		winExperimentRegisterClient.setWidth("99.4%");
		winExperimentRegisterClient.setHeight("100%");
		winExperimentRegisterClient.setMinimizable(true);
		winExperimentRegisterClient.setMaximizable(false);
		winExperimentRegisterClient.setVisible(true);
		winExperimentRegisterClient.invalidate();
	}

	/**
	 * Displays notifications
	 */
	private void showNotify(String msg, Component ref) {
		Clients.showNotification(msg, "info", ref, "end_center", 2000);
	}

	/**
	 * Listener of button with id fileName Changes the value of the name of the
	 * file
	 */
	@Listen("onChange = #fileName")
	public void changeFileName() {
		fileName = fileNameListbox.getValue();
	}

	/**
	 * Listener of button with id openFEDSPEC Opens a filechooser to select a
	 * FEDSPEC file to upload
	 */
	@Listen("onUpload = #openFEDSPEC")
	public void openFedspec(UploadEvent event) {
		try {
			String fileContent = event.getMedia().getStringData().toString();
			File myFile = new File(event.getMedia().getName());
			fileName = myFile.getAbsolutePath();
			fileNameListbox.setText(event.getMedia().getName());
			
//			logger.info("FEDSpec." + fileContent);
			
			userFEDSpec = ermClient.getFedspecFromFile(fileContent);
		} catch (Exception e) {
			logger.error("Could not open FEDSPEC file.");
			logger.error("" + e);
			Messagebox.show("An error occured.");
		}
		logger.info("Successfully opened FEDSPEC.");
	}

	/**
	 * Listener of button with id saveFEDSPEC. Saves the FEDSPEC file on the
	 * specified directory
	 */
	@Listen("onClick = #saveFEDSPEC")
	public void saveFEDSPEC() {
		try {
			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			String token1 = request.getParameter(IPLANETDIRECTORYPRO);

			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equalsIgnoreCase(IPLANETDIRECTORYPRO)) {
					token1 = cookie.getValue();
				}
			}

			if (userFEDSpec != null){
				ermClient.saveUserExperiments(userFEDSpec, token1);
				femoService = new FemoServiceImpl(userID);

				allFemos = femoService.findAll();
				ArrayList<String> femoNames = new ArrayList<String>();
				ArrayList<String> femoIDs = new ArrayList<String>();
				for (Femo femo : allFemos) {
					femoNames.add(femo.getFemoName());
					femoIDs.add(femo.getFemoID());
				}
				
				femos = new ListModelList<Femo>(allFemos);
				femosListbox.setModel(femos);
				userFEDSpec = null;
				fileName = "";
				fileNameListbox.setText(fileName);
				logger.info("Successfully saved FEDSPEC.");
				
			}else {
				logger.error("FEDSPEC file is null.");
				alert("No FEDSpec found. Please try again.");
				return;
			}
		} catch (Exception e) {
			logger.error("Could not save FEDSPEC file.");
			logger.error("" + e);
		}
	
	}

	/**
	 * Getter method for fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Setter method for fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Getter method for femos
	 */
	public ListModel<Femo> getFemos() {
		return femos;
	}

	/**
	 * Setter method for Femos
	 */
	public void setFemos(ListModel<Femo> femos) {
		this.femos = femos;
	}

	/**
	 * Getter method for femoXML
	 */
	public String getFemoXML() {
		return femoXML;
	}

	/**
	 * Setter method for femoXML
	 */
	public void setFemoXML(String femoXML) {
		this.femoXML = femoXML;
	}

	/**
	 * Getter method of userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * Getter method of userID
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
}
