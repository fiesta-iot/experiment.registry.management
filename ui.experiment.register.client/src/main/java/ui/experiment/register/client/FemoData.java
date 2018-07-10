package ui.experiment.register.client;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.zkoss.zk.ui.Executions;

public class FemoData {

	/**
	 * A list of femos
	 */
	private static List<Femo> femos = new ArrayList<Femo>();
	/**
	 * The name of the cookie
	 */
	private final static String IPLANETDIRECTORYPRO = "iPlanetDirectoryPro";
	
	/**
	 * The logger
	 */
	final static Logger logger = LoggerFactory.getLogger(FemoData.class);
	

	/**
	 * Gets all Femos
	 * 
	 * @param userID the id of the user
	 */
	public static List<Femo> getAllFemos(String userID) {
		femos = new ArrayList<Femo>();
		ErmClient ermClient = new ErmClient();
		
		HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		String token = request.getParameter(IPLANETDIRECTORYPRO);

		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equalsIgnoreCase(IPLANETDIRECTORYPRO)) {
				token = cookie.getValue();
			}
		}
		
		String allExperiments = ermClient.getALLUserExperimentsInXmlString(userID, token);		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(allExperiments));
			Document doc = builder.parse(is);
			NodeList nList = doc.getElementsByTagName("ns2:FEMO");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String femoName = eElement.getAttribute("name");
					String femoID = eElement.getAttribute("id");
					femos.add(new Femo(femoName, femoID));
				}
			}
		} catch (ParserConfigurationException e) {
			logger.error("Could not parse available femos. ");
			logger.error("" + e);
		} catch (SAXException e) {
			logger.error("Could not parse available femos. ");
			logger.error("" + e);
		} catch (IOException e) {
			logger.error("Could not get femos. ");
			logger.error("" + e);
		}
		
		logger.info("Successfully retrieved femos.");
		return femos;
	}

}
