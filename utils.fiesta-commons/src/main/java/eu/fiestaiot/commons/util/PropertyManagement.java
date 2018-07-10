package eu.fiestaiot.commons.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * @author Nikos Kefalakis (nkef) e-mail: nkef@ait.edu.gr
 */
public class PropertyManagement {

	final static Logger logger = LoggerFactory.getLogger(PropertyManagement.class);
	
	/**
	 * The properties file.
	 */
	private static final String PROPERTIES_FILE = "fiesta-iot.properties"; //"fiestaiot.properties";
	
	/**
	 * The tpi api metadata graph
	 */
	private static final String TPI_API_META_GRAPH = "tpi.api.storrage.fiestaMetaGraph";
	
	/**
	 * The tpi api data graph
	 */
	private static final String TPI_API_DATA_GRAPH = "tpi.api.storrage.fiestaDataGraph";
	
	/**
	 * The messagebus queue
	 */
	private static final String MESSAGEBUS_QUEUE = "tpi.api.dms.messagebus.messagebusQueue";
	
	/**
	 * The tpi api messagebus uri
	 */
	private static final String MESSAGEBUS_ENDPOINT_URI = "tpi.api.dms.messagebus.endpointURI";
	
	/**
	 * The tpi api messagebus uri
	 */
	private static final String USERNAME_DB = "tpi.api.dms.usernameDB";
	
	/**
	 * The tpi api messagebus uri
	 */
	private static final String PASSWORD_DB = "tpi.api.dms.passwordDB";
	
	/**
	 * The tpi message dispatcher topic
	 */
	private static final String DISPATCHER_TOPIC = "messagebus.dispatcher.topic";
	
	
	/**
	 * The tpi message dispatcher topic
	 */
	private static final String DISPATCHER_CLIENTID = "messagebus.dispatcher.clientID";
	
	
	/**
	 * The tpi message dispatcher broker
	 */
	private static final String BROKER_URI = "messagebus.dispatcher.brokerURI";
	
	/**
	 * The JDBC driver package 
	 */
	private static final String JDBC_DRIVER = "tpi.api.dms.jdbcDriver";
	
	/**
	 * The database URL
	 */
	private static final String DB_URL = "tpi.api.dms.dbURL";

	/**
	 * The database table name
	 */
	private static final String TABLE_NAME = "tpi.api.dms.tableName";
	
	/**
	 * The connectionTimeout for get last observations service
	 */
	private static final String GET_LAST_OBSERVATION_CONNECTION_TIMEOUT = "tpi.api.dms.getLastObservationConnectionTimeout";
	
	/**
	 * The connectionTimeout for get observations service
	 */
	private static final String GET_OBSERVATIONS_CONNECTION_TIMEOUT = "tpi.api.dms.getObservationsConnectionTimeout";
	
	
	// ==============PORTALUI====================
	/**
	 * The URI of the subscribeToObservations service
	 */
	private static final String SUBSCRIBE_TO_OBSERVATIONS_URI = "portal.dms.subscribeToObservationsURI";
	
	/**
	 * The minimum frequency fro GET jobs in minuted
	 */
	private static final String MINIMUM_FREQUENCY_IN_MINUTES = "portal.dms.minimumFrequencyInMinutes";

	
	/**
	 * The URI of the subscribeToObservations service
	 */
	private static final String PUSH_OBSERVATIONS_STREAM_PROXY_URI = "portal.dms.pushObservationsStreamProxyURI";
	
	/**
	 * The URI of the unsubscribeFromObservations service
	 */
	private static final String UNSUBSCRIBE_FROM_OBSERVATIONS_URI = "portal.dms.unsubscribeFromObservationsURI";
	
	/**
	 * The URI of the subscribeToObservationsStream service
	 */
	private static final String SUBSCRIBE_TO_OBSERVATION_STREAM_URI = "portal.dms.subscribeToObservationStreamURI";
	
	/**
	 * The URI of the stopPushOfObservations service
	 */
	private static final String STOP_PUSH_OF_OBSERVATIONS_URI = "portal.dms.stopPushOfObservationsURI";
	
	/**
	 * The URI of the getAllScheduledJobsFromDB service
	 */
	private static final String GET_ALL_SCHEDULED_JOBS_URI = "portal.dms.getAllScheduledJobsFromDB";
	
	/**
	 * The URI of the getAuthenticationURI service
	 */
	private static final String AUTHENTICATION_URI = "portal.dms.getAuthenticationURL";
	
	/**
	 * The URI of the testbed info service
	 */
	private static final String TESTBED_INFO_RETRIEVE = "portal.dms.testbedInfoRetrieve";
	
	/**
	 * The URI to get all available testbeds
	 */
	private static final String AVAILABLE_TESTBEDS_ENDPOINT = "portal.dms.availableTestbedsEndpoint";
	
	/**
	 * The URI to get testbeds
	 */
	private static final String TESTBEDS_RETRIEVE = "portal.dms.TestbedsRetrieve";
	
	/**
	 * The URI to get resources
	 */
	private static final String RESOURCES_RETRIEVE = "portal.dms.ResourcesRetrieve";
	
	/**
	 * The URI to get the IoT Registry path
	 */
	private static final String IoTREGISTRY = "messagebus.dispatcher.iotRegistry";
	
	/**
	 * The URI to get the erm services
	 */
	private static final String ERMSERVICES = "eee.scheduler.ERMSERVICES";
	
	/**
	 * The URI to get the erm services
	 */
	private static final String PORTAL_ERMSERVICES = "portal.erc.ERMSERVICES";
	
	/**
	 * The URI to the OpenAM login UI 
	 */
	private static final String LOGIN_REDIRECT_URI = "portal.login.redirectURL";
	
	/**
	 * The URI to the OpenAM logout 
	 */
	private static final String LOGOUT_URI = "portal.openAM.logoutURI";
		
	// ==============ERM====================

	/**
	 * Activate the usage of a token (user validation) with possible values (true/false)
	 */
	private static final String ERM_Validate_User_With_iPlanetDirectoryPro_Token = "experiment.erm.validateUserWithiPlanetDirectoryProToken";
	
	/**
	 * The URI to trigger EEE when a FEMO has been Updated from ERM 
	 */
	private static final String EEE_Fismo_Update_Trigger_URL = "experiment.erm.eeeFismoUpdateTriggerURL";
	
	/**
	 * The URI to trigger EEE when a FEMO has been deleted from ERM 
	 */
	private static final String EEE_Fismo_Delete_Job_List_Trigger_URL = "experiment.erm.eeeDeleteFismoJobListTriggerURL";
	
	
	/**
	 * The URI to Get the Security User from OpenAM 
	 */
	private static final String ERM_Get_Security_User_URL = "experiment.erm.getSecurityUserURL";
	
	
	// ==============EEE====================

	// ==============SDR====================

	// ==============Security====================
	
	// ==============UI TPI Configurator====================

	

	/**
	 * The properties
	 */
	private Properties props = null;

	/**
	 * The constructor
	 */
	public PropertyManagement() {
		initializeProperties();
	}

	/**
	 * Initialize the Properties
	 */
	private void initializeProperties() {
		String jbosServerConfigDir = System.getProperty("jboss.server.config.dir");
		String openIotConfigFile = jbosServerConfigDir + File.separator + PROPERTIES_FILE;
		props = new Properties();

		logger.info("[JBoss Server Configuration Directory]: " + openIotConfigFile);

		InputStream fis = null;

		try {
			fis = new FileInputStream(openIotConfigFile);
		} catch (FileNotFoundException e) {
			// TODO Handle exception
			logger.warn("Unable to find file: " + openIotConfigFile);
		}

		// trying to find the file in the classpath
		if (fis == null) {
			fis = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
			if (fis == null)
				logger.error("Unable to find file in the classpath: " + PROPERTIES_FILE);
		}

		// loading properites from properties file
		try {
			props.load(fis);
		} catch (IOException e) {
			// TODO Handle exception
			logger.error("Unable to load properties from file " + openIotConfigFile);
		}
	}
	
	
	/**
	 * Gets the requested property
	 * 
	 * @param key
	 * 		The key of the property
	 * @param defaultValue
	 * 		The default value for the requested key
	 * 
	 * @return
	 * 		the requested property
	 */
	public String getProperty(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

	
	/**
	 * Gets the Metadata graph
	 * 
	 * @return
	 * 		the Metadata graph
	 */
	public String getTpiApiStorrageMetaGraph() {
		return props.getProperty(TPI_API_META_GRAPH);
	}

	/**
	 * Gets the Data graph
	 * 
	 * @return
	 * 		the Data graph
	 */
	public String getTpiApiStorrageDataGraph() {
		return props.getProperty(TPI_API_DATA_GRAPH);
	}

	
	/**
	 * Gets the Messagebus queue
	 * 
	 * @return
	 * 		the Messagebus queue
	 */
	public String getTpiApiMessagebusQueue() {
		return props.getProperty(MESSAGEBUS_QUEUE);
	}
	
	
	/**
	 * Gets the Messagebus URI
	 * 
	 * @return
	 * 		the Messagebus URI
	 */
	public String getTpiApiMessagebusEndpointURI() {
		return props.getProperty(MESSAGEBUS_ENDPOINT_URI);
	}
	
	
	/**
	 * Gets the general topic for the message dispatcher
	 * 
	 * @return
	 * 		the general topic for the message dispatcher
	 */
	public String getMessagebusDispatcherTopic() {
		return props.getProperty(DISPATCHER_TOPIC);
	}
	
	
	/**
	 * Gets the broker url of the message dispatcher
	 * 
	 * @return
	 * 		the broker url of the message dispatcher
	 */
	public String getMessagebusDispatcherBrokerURI() {
		return props.getProperty(BROKER_URI);
	}
	
	
	/**
	 * Gets the username of the Database
	 * 
	 * @return
	 * 		the username of the Database
	 */
	public String getUsernameDB() {
		return props.getProperty(USERNAME_DB);
	}

	
	/**
	 * Gets the password of the Database
	 * 
	 * @return
	 * 		the password of the Database
	 */
	public String getPasswordDB() {
		return props.getProperty(PASSWORD_DB);
	}
	
	
	/**
	 * Gets the JDBC driver package
	 * 
	 * @return
	 * 		the JDBC driver package
	 */
	public String getJDBCDriver() {
		return props.getProperty(JDBC_DRIVER);
	}
	
	
	/**
	 * Gets the URL of the database
	 * 
	 * @return
	 * 		the URL of the database
	 */
	public String getDBURL() {
		return props.getProperty(DB_URL);
	}

	
	/**
	 * Gets the name of the table in the database
	 * 
	 * @return
	 * 		the name of the table in the database
	 */
	public String getTableName() {
		return props.getProperty(TABLE_NAME);	
	}

	
	/**
	 * Gets the clientID used by the Dispatcher
	 * 
	 * @return
	 * 		the clientID used by the Dispatcher
	 */
	public String getDispatcherClientid() {
		return props.getProperty(DISPATCHER_CLIENTID);
	}

	/**
	 * Gets the URI of the subscribeToObservations service
	 * 
	 * @return
	 * 		URI of the subscribeToObservations service
	 */
	public String getSubscribeToObservationsURI() {
		return props.getProperty(SUBSCRIBE_TO_OBSERVATIONS_URI);
	}
	
	/**
	 * Gets the URI of the pushObservationsStreamProxy service
	 * 
	 * @return
	 * 		URI of the pushObservationsStreamProxy service
	 */
	public String getPushObservationsStreamProxyURI() {
		return props.getProperty(PUSH_OBSERVATIONS_STREAM_PROXY_URI);
	}
	
	/**
	 * Gets the minimum freequency for GET jobs
	 * 
	 * @return
	 * 		the minimum freequency for GET jobs
	 */
	public String getMinimumFrequency() {
		return props.getProperty(MINIMUM_FREQUENCY_IN_MINUTES);
	}

	/**
	 * Gets the URI of the unsubscribeFromObservations service
	 * 
	 * @return
	 * 		the URI of the unsubscribeFromObservations service
	 */
	public String getUnsubscribeFromObservationsURI() {
		return props.getProperty(UNSUBSCRIBE_FROM_OBSERVATIONS_URI);
	}
	
	/**
	 * Gets the URI of the subscribeToObservationStream service
	 * 
	 * @return
	 * 		the URI of the subscribeToObservationStream service
	 */
	public String getSubscribeToObservationStreamURI() {
		return props.getProperty(SUBSCRIBE_TO_OBSERVATION_STREAM_URI);
	}
	
	/**
	 * Gets the URI of the stopPushOfObservations service
	 * 
	 * @return
	 * 		the URI of the stopPushOfObservations service
	 */
	public String getStopPushOfObservationsURI() {
		return props.getProperty(STOP_PUSH_OF_OBSERVATIONS_URI);
	}
			
	/**
	 * Gets the URI of the authentication service
	 * 
	 * @return
	 * 		the URI of the authentication service
	 */
	public String getAuthenticationURI() {
		return props.getProperty(AUTHENTICATION_URI);
	}
	
	/**
	 * Gets the URI of the testbed info endpoint
	 * 
	 * @return
	 * 		the URI of the testbed info endpoint
	 */
	public String getTestbedInfoRetrieveURI() {
		return props.getProperty(TESTBED_INFO_RETRIEVE);
	}
	
	/**
	 * Gets the URI of the available testbeds endpoint
	 * 
	 * @return
	 * 		the URI of the testbed info endpoint
	 */
	public String getAvailableTestbedsEndpointURI() {
		return props.getProperty(AVAILABLE_TESTBEDS_ENDPOINT);
	}
	
	/**
	 * Gets the URI of the testbeds endpoint
	 * 
	 * @return
	 * 		the URI of the testbeds endpoint
	 */
	public String getTestbedsRetrieveURI() {
		return props.getProperty(TESTBEDS_RETRIEVE);
	}
	
	
	/**
	 * Gets the URI of the resources endpoint
	 * 
	 * @return
	 * 		the URI of the resources endpoint
	 */
	public String getResourcesRetrieveURI() {
		return props.getProperty(RESOURCES_RETRIEVE);
	}
	
	
	/**
	 * Gets the URI of the getAllScheduledJobsFromDB service
	 * 
	 * @return
	 * 		the URI of the getAllScheduledJobsFromDB service
	 */
	public String getGetAllScheduledJobsFromDB() {
		return props.getProperty(GET_ALL_SCHEDULED_JOBS_URI);
	}
	
	/**
	 * Gets the URI of the iot-registry endpoint
	 * 
	 * @return
	 * 		the URI of the iot-registry endpoint
	 */
	public String getIoTRegistryURI() {
		return props.getProperty(IoTREGISTRY);
	}
	
	/**
	 * Gets the URI of the ERM Services endpoint
	 * 
	 * @return
	 * 		the URI of the ERM Services endpoint
	 */
	public String getErmServicesURI() {
		return props.getProperty(ERMSERVICES);
	}
	
	/**
	 * Gets the URI of the ERM Services endpoint
	 * 
	 * @return
	 * 		the URI of the ERM Services endpoint
	 */
	public String getPortalErmServicesURI() {
		return props.getProperty(PORTAL_ERMSERVICES);
	}
	
	/**
	 * Gets the URI of the login
	 * 
	 * @return
	 * 		the URI of the login
	 */
	public String getLoginRedirectURI() {
		return props.getProperty(LOGIN_REDIRECT_URI);
	}
	
	/**
	 * Gets the URI of the logout
	 * 
	 * @return
	 * 		the URI of the logout
	 */
	public String getLogoutURI() {
		return props.getProperty(LOGOUT_URI);
	}
	
	
	/**
	 * Gets the EEE Femo Update Trigger URL
	 * 
	 * @return
	 * 		the Metadata graph
	 */
	public String getEeeFismoUpdateTriggerURL() {
		return props.getProperty(EEE_Fismo_Update_Trigger_URL);
	}

	/**
	 * Gets the EEE Femo Delete Jobs Trigger URL
	 * 
	 * @return
	 * 		the Data graph
	 */
	public String getEeeFismoDeleteJobListTriggerURL() {
		return props.getProperty(EEE_Fismo_Delete_Job_List_Trigger_URL);
	}
	

	
	/**
	 * Returns if the usage of a token (user validation) is active
	 * 
	 * @return
	 * 		the Data graph
	 */

	public boolean getErmValidateUserWithiPlanetDirectoryProToken() {
		if (props.getProperty(ERM_Validate_User_With_iPlanetDirectoryPro_Token).equals("true")){
			return true;
		} else {
			
			return false;
		}
	}
	
	/**
	 * Gets the The URI for the Security User from OpenAM
	 * 
	 * @return
	 * 		the Data graph
	 */
	public String ermGetSecurityUserURL() {
		return props.getProperty(ERM_Get_Security_User_URL);
	}
	
	
	/**
	 * Gets the connection timeout for get last observations service
	 * 
	 * @return
	 * 		the connection timeout
	 */
	public String getLastObservationConnectionTimeout() {
		return props.getProperty(GET_LAST_OBSERVATION_CONNECTION_TIMEOUT);
	}
	
	
	/**
	 * Gets the connection timeout for get observations service
	 * 
	 * @return
	 * 		the connection timeout
	 */
	public String getObservationsConnectionTimeout() {
		return props.getProperty(GET_OBSERVATIONS_CONNECTION_TIMEOUT);
	}
	
}
