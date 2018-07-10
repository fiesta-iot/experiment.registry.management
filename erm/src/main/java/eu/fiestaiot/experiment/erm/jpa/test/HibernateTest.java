package eu.fiestaiot.experiment.erm.jpa.test;
 
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

//import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
//import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


import javax.persistence.criteria.ParameterExpression;

import eu.fiestaiot.experiment.erm.jpa.fedspec.entities.ExperimentOutputJpa;
import eu.fiestaiot.experiment.erm.jpa.fedspec.entities.FEDSpecJpa;
import eu.fiestaiot.experiment.erm.jpa.fedspec.entities.FemoJpa;
import eu.fiestaiot.experiment.erm.jpa.fedspec.entities.FileJpa;
import eu.fiestaiot.experiment.erm.jpa.utils.*;
import eu.fiestaiot.commons.fedspec.model.FEDSpec;
import eu.fiestaiot.commons.fedspec.model.FEMO;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;



public class HibernateTest {
 
	
public void runTest() {
          
	
	//initialize entity manager
	EntityManager entityManager = null;
//	entityManager = HibernateUtil.getEntityManager();
		
	//====(sample) ExperimentOutput Persistence
	//experimentOutputPersistence(entityManager);

    //=====delete Entity from with primary key
	//deleteExperimentOutput(entityManager);
	
    //====query based on criteria
	//criteriaBasedQuery1(entityManager);
	
	

	//======== store FedSpec FromFile
	
//	String path = Thread.currentThread().getContextClassLoader().getResource("FEDSpec-Sample2.xml").getPath();
//	File f = new File(path);
//	System.out.println("FEDSpec AbsolutePath: " + f.getAbsolutePath());
	
	storeFedSpecFromFile("C:\\Users\\kefnik\\Documents\\FEDSpec-Sample2.xml", entityManager);
	
	
	
	
	//========get all FEDSpec IDs
	List<FEDSpecJpa> fedSpecJPAs = getAllFedSpecJPA(entityManager);
	
	
	//=======convert FEDSpecJPA to FEDSpec and FEDSpec to XML and print it
	
	MappingUtils mappingTools = new MappingUtils();
	
	for (FEDSpecJpa fedSpecJpa: fedSpecJPAs){
		
		FEDSpec fedSpec = new FEDSpec();
		
		fedSpec = mappingTools.fedSpecJpaToFedSpec(fedSpecJpa);
		
		fedSpecToXML(fedSpec);
		
		
	}
	
	
	
	
    //Stop EntityManager
    entityManager.close();

 
    }


private List<FEDSpecJpa> getAllFedSpecJPA(EntityManager entityManager) {
    //----Query (1) SELECT c FROM ExperimentOutputJpa c 
    
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<FEDSpecJpa> criteriaQuery = builder.createQuery(FEDSpecJpa.class);
    Root<FEDSpecJpa> rootEntity = criteriaQuery.from(FEDSpecJpa.class);
    
    criteriaQuery.select(rootEntity);
    

    
    //running the query
    TypedQuery<FEDSpecJpa> query = entityManager.createQuery(criteriaQuery);
    List<FEDSpecJpa> results = query.getResultList();
    
    for (FEDSpecJpa fedSpecJpa : results){
    	
    	
    	System.out.println("FEDSpecJpa ID: "+fedSpecJpa.getId());
    	
    	System.out.println("fedSpecJpa UseID: "+fedSpecJpa.getUserID());

    	
    }
	return results;
    
	}
	
private void fedSpecToXML(FEDSpec fedSpec) {

    try {
        JAXBContext context = JAXBContext.newInstance(FEDSpec.class);
        Marshaller m = context.createMarshaller();
        //for pretty-print XML in JAXB
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

//		m.setProperty("com.sun.xml.internal.bind.characterEscapeHandler",
//			new CharacterEscapeHandler() {
//				public void escape(char[] ch, int start, int length,
//						boolean isAttVal, Writer writer)
//						throws IOException {
//					writer.write(ch, start, length);
//				}
//			});
        
		StringWriter sw = new StringWriter();
        
        // Write to System.out for debugging
        m.marshal(fedSpec, sw);
        System.out.println(sw);

        // Write to File
        //m.marshal(emp, new File(FILE_NAME));
    } catch (JAXBException e) {
        e.printStackTrace();
    }
}


private void storeFedSpecFromFile(String filepath, EntityManager entityManager) {
	
	entityManager.getTransaction().begin();
	
    //FEDSpec persistence
    
	//open FEDSpec from file
	FEDSpec fedspec = getFedSpecFromFile(filepath);

	//convert FEDSpec to FEDSpecJpa
	MappingUtils mappingTools = new MappingUtils();
	FEDSpecJpa fedSpecJpa = mappingTools.fedSpecToFedSpecJPA(fedspec);
    
	//persist FEDSpecJpa
    entityManager.persist(fedSpecJpa);
    
    //Commit transaction
    entityManager.getTransaction().commit();
	
	
}





private FEDSpec getFedSpecFromFile(String filepath) {

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

	
	
	
	return fedSpec;
}




private void departmentEmployeePersistence(EntityManager entityManager){
	entityManager.getTransaction().begin();
	
	//Department & Employee persistence
	/*
    Department department = new Department("java");
    entityManager.persist(department);
    
    entityManager.persist(new Employee("Jakab Gipsz",department));
    entityManager.persist(new Employee("Captain Nemo",department));
    */
    //Commit transaction
    entityManager.getTransaction().commit();
}


private void employeeSampleQuery(EntityManager entityManager){

	
    //Execute sample query
/*
    Query q = entityManager.createQuery("From Employee ");
             
    List<Employee> resultList = q.getResultList();
    System.out.println("num of employess:" + resultList.size());
    for (Employee next : resultList) {
        System.out.println("next employee: " + next);
    }
    
*/
	
	
}



private void sampleFismoPersistence(EntityManager entityManager){
	entityManager.getTransaction().begin();
	

/*	
    FISMOHiber fismoHiber = new FISMOHiber();
    
    fismoHiber.setDescription("a descreption");
    fismoHiber.setName("a Fismo name");
    fismoHiber.setService("http://aservice");
    
    entityManager.persist(fismoHiber);
    */
    //Commit transaction
    entityManager.getTransaction().commit();
}

private void experimentOutputPersistence(EntityManager entityManager){
	entityManager.getTransaction().begin();
	
    //ExperimentOutput persistence
    
    ExperimentOutputJpa experimentOutputJpa = new ExperimentOutputJpa();
    
    FileJpa fileJpa = new FileJpa();
    
    fileJpa.setType("afileType");
    
//    entityManager.persist(fileJpa);
    
    experimentOutputJpa.getFile().add(fileJpa);
    
    experimentOutputJpa.setLocation("http://adomainname.location");
    
    entityManager.persist(experimentOutputJpa);
    
    //Commit transaction
    entityManager.getTransaction().commit();
}






private void deleteExperimentOutput(EntityManager entityManager){
	//==========delete Item
  

	ExperimentOutputJpa experimentOutputJpaToDelete = entityManager.find(ExperimentOutputJpa.class, "ad81cdba-9c64-11e6-a1cb-00ff664810d1");


	entityManager.getTransaction().begin();

	entityManager.remove(experimentOutputJpaToDelete);
	    

	//Commit transaction
	entityManager.getTransaction().commit();


	}

private void criteriaBasedQuery1(EntityManager entityManager){
    
    //----Query (1) SELECT c FROM ExperimentOutputJpa c 
    
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ExperimentOutputJpa> criteriaQuery = builder.createQuery(ExperimentOutputJpa.class);
    Root<ExperimentOutputJpa> rootEntity = criteriaQuery.from(ExperimentOutputJpa.class);
    
    criteriaQuery.select(rootEntity);
    

    
    //running the query
    TypedQuery<ExperimentOutputJpa> query = entityManager.createQuery(criteriaQuery);
    List<ExperimentOutputJpa> results = query.getResultList();
    
    for (ExperimentOutputJpa experimentOutput : results){
    	System.out.println("experimentOutput ID: "+experimentOutput.getId());
    	
    	System.out.println("experimentOutput Location: "+experimentOutput.getLocation());

    	
    }
    
	}

private void criteriaBasedQuery2(EntityManager entityManager){
//----Query (2) SELECT c FROM ExperimentOutputJpa c WHERE c.location > :p    
/*    
CriteriaBuilder builder2 = entityManager.getCriteriaBuilder();
CriteriaQuery<ExperimentOutputJpa> criteriaQuery2 = builder.createQuery(ExperimentOutputJpa.class);
Root<ExperimentOutputJpa> rootEntity2 = criteriaQuery.from(ExperimentOutputJpa.class);



ParameterExpression<String> parameter = builder2.parameter(String.class);
criteriaQuery2.select(rootEntity2).where(builder2.like(rootEntity2.get("location"), parameter));
//running the query
TypedQuery<ExperimentOutputJpa> query2 = entityManager.createQuery(criteriaQuery2);
query2.setParameter(parameter, 10000000);
List<ExperimentOutputJpa> results2 = query2.getResultList();

*/
}

}
