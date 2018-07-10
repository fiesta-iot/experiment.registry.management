package eu.fiestaiot.experiment.erm.utils;

import java.io.IOException;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import eu.fiestaiot.commons.fedspec.model.FEDSpec;
import eu.fiestaiot.commons.fedspec.model.FEMO;
import eu.fiestaiot.commons.fedspec.model.FISMO;

import eu.fiestaiot.commons.fedspec.model.FEDSpec;
import eu.fiestaiot.commons.fedspec.model.ObjectFactory;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Nikos Kefalakis (nkef) e-mail: nkef@ait.edu.gr
 *
 */

public class Serializer {

	
	// logger
	final static Logger logger = LoggerFactory.getLogger(Serializer.class);

	/**
	 * This method serializes an FEDSpec Object to an xml and writes it
	 * into a file.
	 * 
	 * @param spec
	 *            the FEDSpec to be written into a file
	 * @param pathName
	 *            the file where to store
	 * @throws IOException
	 *             whenever an io problem occurs
	 */
	public static void serializeFEDSpec(FEDSpec fedSpec, Writer writer) throws IOException {
		ObjectFactory objectFactory = new ObjectFactory();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(FEDSpec.class);
//			JAXBElement<FEDSpec> item = objectFactory.createFEDSpec()
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//			marshaller.marshal(item, writer);
		} catch (JAXBException e) {
			logger.error("error serializing FEDSpec", e);
		}
	}
	
	
	
	
	
	
}
