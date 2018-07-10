package eu.fiestaiot.experiment.erm.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import eu.fiestaiot.commons.fedspec.model.FEDSpec;
import eu.fiestaiot.commons.fedspec.model.FEMO;
import eu.fiestaiot.commons.fedspec.model.FISMO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * @author Nikos Kefalakis (nkef) e-mail: nkef@ait.edu.gr
 *
 */
public class Deserializer {

	
	
	// logger
	final static Logger logger = LoggerFactory.getLogger(Deserializer.class);

	/**
	 * This method deserializes an FEDSpec specification from an input
	 * stream.
	 * 
	 * @param inputStream
	 *            to deserialize
	 * @return FEDSpec
	 * @throws Exception
	 *             if deserialization fails
	 */
	public static FEDSpec deserializeOSDSpec(InputStream inputStream) throws Exception {
		FEDSpec osdspec = null;
		try {

//			String JAXB_CONTEXT = "eu.fiestaiot.commons.fedspec.model";

			// initialize jaxb context and unmarshaller
			JAXBContext context = JAXBContext.newInstance(FEDSpec.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			osdspec = (FEDSpec) unmarshaller.unmarshal(inputStream);

		} catch (JAXBException e) {
			logger.error("Error deserializing OSDSpec", e);
		}
		return osdspec;
	}

	/**
	 * This method deserializes an FEDSpec Spec from a file.
	 * 
	 * @param pathName
	 *            of the file containing the APDL Spec
	 * @return FEDSpec object
	 * @throws FileNotFoundException
	 *             if the file could not be found
	 * @throws Exception
	 *             if deserialization fails
	 */
	public static FEDSpec deserializeFEDSpecFile(String pathName) throws FileNotFoundException, Exception {
		FileInputStream inputStream = new FileInputStream(pathName);

		return deserializeOSDSpec(inputStream);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
