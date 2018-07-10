package eu.fiestaiot.experiment.erm.jpa.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fiestaiot.commons.fedspec.model.*;
import eu.fiestaiot.commons.sparql.protocoltypes.model.QueryRequest;
import eu.fiestaiot.experiment.erm.jpa.fedspec.entities.*;

/**
 * @author kefnik
 *
 */
public class MappingUtils {

	
	//Logger's initialization
	final static Logger logger = LoggerFactory.getLogger(MappingUtils.class);
	
	
	//==================Start of FedSpec To FedSpecJPA Mapping=======================
	
	
	public FEDSpecJpa fedSpecToFedSpecJPA(FEDSpec fedSpec) {

		FEDSpecJpa fedSpecJpa = new FEDSpecJpa();

		//UserID
		if ((fedSpec.getUserID() != null) && (!fedSpec.getUserID().isEmpty())) {
			fedSpecJpa.setUserID(fedSpec.getUserID());
		}
		//FEMO
		if ((fedSpec.getFEMO() != null) && (!fedSpec.getFEMO().isEmpty()))
			for (FEMO femo : fedSpec.getFEMO()) {
				fedSpecJpa.addFEMO(femoToFemoJPA(femo));//fedSpecJpa.getFEMO().add(femoToFemoJPA(femo))
			}

		return fedSpecJpa;
	}

	
	
	/**
	 * @param femo
	 * @return
	 */
	public FemoJpa femoToFemoJPA(FEMO femo) {
		FemoJpa femoJpa = new FemoJpa();

		//Description
		if ((femo.getDescription() != null) && (!femo.getDescription().isEmpty())) {
			femoJpa.setDescription(femo.getDescription());
		}
		//EDM
		if ((femo.getEDM() != null) && (!femo.getEDM().isEmpty())) {
			femoJpa.setEDM(femo.getEDM());
		}
		//Name
		if ((femo.getName() != null) && (!femo.getName().isEmpty())) {
			femoJpa.setName(femo.getName());
		}
		//DomainOfInterest
		if ((femo.getDomainOfInterest() != null) && (!femo.getDomainOfInterest().isEmpty())) {
			for (String domainOfInterest : femo.getDomainOfInterest()){
				femoJpa.getDomainOfInterest().add(domainOfInterest);
			}
		}	
		//FISMO
		if ((femo.getFISMO() != null) && (!femo.getFISMO().isEmpty())) {
			for (FISMO fismo : femo.getFISMO()){
				femoJpa.addFISMO(fismoToFismoJPA(fismo)); //femoJpa.getFISMO().add(fismoToFismoJPA(fismo));
			}
		}
		return femoJpa;
	}
	

	/**
	 * @param fismo
	 * @return
	 */
	public FismoJpa fismoToFismoJPA(FISMO fismo) {
		FismoJpa fismoJpa = new FismoJpa();
		//Description
		if ((fismo.getDescription() != null) && (!fismo.getDescription().isEmpty())) {
			fismoJpa.setDescription(fismo.getDescription());
		}
		//Name
		if ((fismo.getName() != null) && (!fismo.getName().isEmpty())) {
			fismoJpa.setName(fismo.getName());
		}
		
		//Discoverable
		if (fismo.isDiscoverable() != null) {
			fismoJpa.setDiscoverable(fismo.isDiscoverable());
		}	
		//Service
		if ((fismo.getService() != null) && (!fismo.getService().isEmpty())) {
			fismoJpa.setService(fismo.getService());
		}	
		//ExperimentControl
		if (fismo.getExperimentControl() != null) {
			fismoJpa.setExperimentControl(experimentControlToExperimentControlJPA(fismo.getExperimentControl()));
		}
		//ExperimentOutput
		if (fismo.getExperimentOutput() != null) {
			fismoJpa.setExperimentOutput(experimentOutputToExperimentOutputJPA(fismo.getExperimentOutput()));
		}
		//QueryControl
		if (fismo.getQueryControl() != null) {
			fismoJpa.setQueryControl(queryControlToQueryControlJPA(fismo.getQueryControl()));
		}
		if (fismo.getRule() != null) {
		//Rule
			fismoJpa.setRule(ruleToRuleJPA(fismo.getRule()));
		}
		

		return fismoJpa;
	}	
	
	
	/**
	 * @param experimentControl
	 * @return
	 */
	public ExperimentControlJpa experimentControlToExperimentControlJPA(ExperimentControl experimentControl) {
		ExperimentControlJpa experimentControlJpa = new ExperimentControlJpa();
		

		//ReportIfEmpty
		if (experimentControl.isReportIfEmpty() != null)
			experimentControlJpa.setReportIfEmpty(experimentControl.isReportIfEmpty());
		//Trigger
		if ((experimentControl.getTrigger() != null) && (!experimentControl.getTrigger().isEmpty())) 
			experimentControlJpa.setTrigger(experimentControl.getTrigger());
		//Scheduling
		if (experimentControl.getScheduling() != null){
			SchedulingJpa schedulingJpa = new SchedulingJpa();
			//Periodicity
			if (experimentControl.getScheduling().getPeriodicity() != null) 
				schedulingJpa.setPeriodicity(experimentControl.getScheduling().getPeriodicity());
			//StartTime
			if (experimentControl.getScheduling().getStartTime() != null) 
				schedulingJpa.setStartTime(experimentControl.getScheduling().getStartTime());
			//StopTime
			if (experimentControl.getScheduling().getStopTime() != null) 
				schedulingJpa.setStopTime(experimentControl.getScheduling().getStopTime());
			experimentControlJpa.setScheduling(schedulingJpa);
		}

		

		return experimentControlJpa;
	}	
	
	
	
	/**
	 * @param experimentOutput
	 * @return
	 */
	public ExperimentOutputJpa experimentOutputToExperimentOutputJPA(ExperimentOutput experimentOutput) {
		ExperimentOutputJpa experimentOutputJpa = new ExperimentOutputJpa();
		
		

		//Location
		if ((experimentOutput.getLocation() != null) && (!experimentOutput.getLocation().isEmpty())) 
			experimentOutputJpa.setLocation(experimentOutput.getLocation());
		
		//File
		if ((experimentOutput.getFile() != null) && (!experimentOutput.getFile().isEmpty())) {
			for (File file : experimentOutput.getFile()){
				
				FileJpa fileJpa = new FileJpa();
				fileJpa.setType(file.getType());
								
				experimentOutputJpa.getFile().add(fileJpa);
			}
		}	
		
		
		//Widget
		if ((experimentOutput.getWidget() != null) && (!experimentOutput.getWidget().isEmpty())) {
			for (Widget widget : experimentOutput.getWidget()){
				WidgetJpa widgetJpa = new WidgetJpa();
				
				//WidgetID
				if ((widget.getWidgetID() != null) && (!widget.getWidgetID().isEmpty())) 
					widgetJpa.setWidgetID(widget.getWidgetID());
				
				//PresentationAttr
				if ((widget.getPresentationAttr() != null) && (!widget.getPresentationAttr().isEmpty())) {
					for (PresentationAttr presentationAttr : widget.getPresentationAttr()){
						PresentationAttrJpa presentationAttrJpa = new PresentationAttrJpa();
						//Name
						if (presentationAttr.getName() != null)
							presentationAttrJpa.setName(presentationAttr.getName());
						//Value
						if (presentationAttr.getValue() != null)
							presentationAttrJpa.setValue(presentationAttr.getValue());
						
						widgetJpa.getPresentationAttr().add(presentationAttrJpa);
					}
				}
				
				experimentOutputJpa.getWidget().add(widgetJpa);
			}
		}	
		return experimentOutputJpa;
	}	
	
	
	
	
	/**
	 * @param queryControl
	 * @return
	 */
	public QueryControlJpa queryControlToQueryControlJPA(QueryControl queryControl) {
		QueryControlJpa queryControlJpa = new QueryControlJpa();

		//DynamicAttrs
		if (queryControl.getDynamicAttrs() != null){
			DynamicAttrsJpa dynamicAttrsJpa = new DynamicAttrsJpa ();
			
			logger.debug("");
			
			//PredefinedDynamicAttr
			if (queryControl.getDynamicAttrs().getPredefinedDynamicAttr() != null){
				PredefinedDynamicAttrJpa predefinedDynamicAttrJpa = new PredefinedDynamicAttrJpa();
				//PredefinedDynamicAttr > DynamicGeoLocation: Latitude, Longitude
				if (queryControl.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicGeoLocation() != null){
					DynamicGeoLocationJpa dynamicGeoLocationJpa = new DynamicGeoLocationJpa();
					//Latitude
					if (queryControl.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicGeoLocation().getLatitude() != null)
						dynamicGeoLocationJpa.setLatitude(queryControl.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicGeoLocation().getLatitude());
					//Longitude
					if (queryControl.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicGeoLocation().getLongitude() != null)
						dynamicGeoLocationJpa.setLongitude(queryControl.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicGeoLocation().getLongitude());
						
					predefinedDynamicAttrJpa.setDynamicGeoLocation(dynamicGeoLocationJpa);
				}
					
				//PredefinedDynamicAttr > DynamicQueryInterval: FromDateTime, ToDateTime, IntervalNowToPast
				if (queryControl.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval() != null){
					DynamicQueryIntervalJpa dynamicQueryIntervalJpa = new DynamicQueryIntervalJpa();
					//FromDateTime
					if (queryControl.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval().getFromDateTime() != null)
						dynamicQueryIntervalJpa.setFromDateTime(queryControl.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval().getFromDateTime());
					//ToDateTime
					if (queryControl.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval().getToDateTime() != null)
						dynamicQueryIntervalJpa.setToDateTime(queryControl.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval().getToDateTime());
					//IntervalNowToPast
					if (queryControl.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval().getIntervalNowToPast() != null)
						dynamicQueryIntervalJpa.setIntervalNowToPast(queryControl.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval().getIntervalNowToPast());
				
					predefinedDynamicAttrJpa.setDynamicQueryInterval(dynamicQueryIntervalJpa);
				}
				dynamicAttrsJpa.setPredefinedDynamicAttr(predefinedDynamicAttrJpa);
			}
			
			
			//DynamicAttr: Name, Value
			if ((queryControl.getDynamicAttrs().getDynamicAttr() != null) && (!queryControl.getDynamicAttrs().getDynamicAttr().isEmpty())) {
				for (DynamicAttr dynamicAttr : queryControl.getDynamicAttrs().getDynamicAttr()){
					DynamicAttrJpa dynamicAttrJpa = new DynamicAttrJpa();
					//Name
					if (dynamicAttr.getName() != null)
						dynamicAttrJpa.setName(dynamicAttr.getName());
					//Value
					if (dynamicAttr.getValue() != null)
						dynamicAttrJpa.setValue(dynamicAttr.getValue());
					
					dynamicAttrsJpa.getDynamicAttr().add(dynamicAttrJpa);
				}
				
			}
			queryControlJpa.setDynamicAttrs(dynamicAttrsJpa);
		}
		
		//QueryInterval
		if (queryControl.getQueryInterval() != null){
			QueryIntervalJpa queryIntervalJpa = new QueryIntervalJpa();
			//FromDateTime
			if (queryControl.getQueryInterval().getFromDateTime() != null)
				queryIntervalJpa.setFromDateTime(queryControl.getQueryInterval().getFromDateTime());
			//ToDateTime
			if (queryControl.getQueryInterval().getToDateTime() != null)
				queryIntervalJpa.setToDateTime(queryControl.getQueryInterval().getToDateTime());
			//IntervalNowToPast
			if (queryControl.getQueryInterval().getIntervalNowToPast() != null)
				queryIntervalJpa.setIntervalNowToPast(queryControl.getQueryInterval().getIntervalNowToPast());
		
			queryControlJpa.setQueryInterval(queryIntervalJpa);
		}
		
		//QueryRequest
		if (queryControl.getQueryRequest() != null){
			QueryRequestJpa queryRequestJpa = new QueryRequestJpa();
			//DefaultGraphUri
			if ((queryControl.getQueryRequest().getDefaultGraphUri() != null) && (!queryControl.getQueryRequest().getDefaultGraphUri().isEmpty())) {
				for (String defaultGraphUri : queryControl.getQueryRequest().getDefaultGraphUri()){
					queryRequestJpa.getDefaultGraphUri().add(defaultGraphUri);
				}
			}
			//NamedGraphUri
			if ((queryControl.getQueryRequest().getNamedGraphUri() != null) && (!queryControl.getQueryRequest().getNamedGraphUri().isEmpty())) {
				for (String namedGraphUri : queryControl.getQueryRequest().getNamedGraphUri()){
					queryRequestJpa.getNamedGraphUri().add(namedGraphUri);
				}
			}
			//Query
			if ((queryControl.getQueryRequest().getQuery() != null) && (!queryControl.getQueryRequest().getQuery().isEmpty()))
				queryRequestJpa.setQuery(queryControl.getQueryRequest().getQuery());
		
			queryControlJpa.setQueryRequest(queryRequestJpa);
		}

		//StaticLocation
		if (queryControl.getStaticLocation() != null){
			StaticLocationJpa staticLocationJpa = new StaticLocationJpa();
			//Latitude
			if (queryControl.getStaticLocation().getLatitude() != null)
				staticLocationJpa.setLatitude(queryControl.getStaticLocation().getLatitude());
			//Longitude
			if (queryControl.getStaticLocation().getLongitude() != null)
				staticLocationJpa.setLongitude(queryControl.getStaticLocation().getLongitude());
			
			queryControlJpa.setStaticLocation(staticLocationJpa);
		}
		
		//QuantityKind
		if ((queryControl.getQuantityKind() != null) && (!queryControl.getQuantityKind().isEmpty())) {
			for (String quantityKind : queryControl.getQuantityKind()){
				queryControlJpa.getQuantityKind().add(quantityKind);
			}
		}
		return queryControlJpa;
	}	
	
	
	
	
	/**
	 * @param rule
	 * @return
	 */
	public RuleJpa ruleToRuleJPA(Rule rule) {

		RuleJpa ruleJpa = new RuleJpa();
		//DomainKnowledge
		if ((rule.getDomainKnowledge() != null) && (!rule.getDomainKnowledge().isEmpty()))
			ruleJpa.setDomainKnowledge(rule.getDomainKnowledge());
		//Name
		if ((rule.getName() != null) && (!rule.getName().isEmpty()))
			ruleJpa.setName(rule.getName());
		//RuleDefinition
		if ((rule.getRuleDefinition() != null) && (!rule.getRuleDefinition().isEmpty()))
			ruleJpa.setRuleDefinition(rule.getRuleDefinition());
		
		//QuantityKind
		if ((rule.getQuantityKind() != null) && (!rule.getQuantityKind().isEmpty())) {
			for (String quantityKind : rule.getQuantityKind()){
				ruleJpa.getQuantityKind().add(quantityKind);
			}
		}

		return ruleJpa;
	}	
	
	//==================End of FedSpec To FedSpecJPA Mapping=======================
	
	
	
	
	//==================Start of FedSpecJPA To FedSpec Mapping=======================

	
	public FEDSpec fedSpecJpaToFedSpec(FEDSpecJpa fedSpecJpa) {

		FEDSpec fedSpec = new FEDSpec();

		//UserID
		if ((fedSpecJpa.getUserID() != null) && (!fedSpecJpa.getUserID().isEmpty())) {
			fedSpec.setUserID(fedSpecJpa.getUserID());
		}
		//FEMO
		if ((fedSpecJpa.getFEMO() != null) && (!fedSpecJpa.getFEMO().isEmpty()))
			for (FemoJpa femoJpa : fedSpecJpa.getFEMO()) {
				fedSpec.getFEMO().add(femoJpaToFemo(femoJpa));
			}

		return fedSpec;
	}


	
	/**
	 * @param femo
	 * @return
	 */
	public FEMO femoJpaToFemo(FemoJpa femoJpa) {
		FEMO femo = new FEMO();

		
		
		//ID
		if ((femoJpa.getId() != null) && (!femoJpa.getId().isEmpty())) {
			femo.setId(femoJpa.getId());
		}
		//Description
		if ((femoJpa.getDescription() != null) && (!femoJpa.getDescription().isEmpty())) {
			femo.setDescription(femoJpa.getDescription());
		}
		//EDM
		if ((femoJpa.getEDM() != null) && (!femoJpa.getEDM().isEmpty())) {
			femo.setEDM(femoJpa.getEDM());
		}
		//Name
		if ((femoJpa.getName() != null) && (!femoJpa.getName().isEmpty())) {
			femo.setName(femoJpa.getName());
		}
		//DomainOfInterest
		if ((femoJpa.getDomainOfInterest() != null) && (!femoJpa.getDomainOfInterest().isEmpty())) {
			for (String domainOfInterest : femoJpa.getDomainOfInterest()){
				femo.getDomainOfInterest().add(domainOfInterest);
			}
		}	
		//FISMO
		if ((femoJpa.getFISMO() != null) && (!femoJpa.getFISMO().isEmpty())) {
			for (FismoJpa fismoJpa : femoJpa.getFISMO()){
				femo.getFISMO().add(fismoJpaToFismo(fismoJpa));
			}
		}
		return femo;
	}
	

	/**
	 * @param fismo
	 * @return
	 */
	public FISMO fismoJpaToFismo(FismoJpa fismoJpa) {
		FISMO fismo = new FISMO();
		
		
		//ID
		if ((fismoJpa.getId() != null) && (!fismoJpa.getId().isEmpty())) {
			fismo.setId(fismoJpa.getId());
		}
		//Description
		if ((fismoJpa.getDescription() != null) && (!fismoJpa.getDescription().isEmpty())) {
			fismo.setDescription(fismoJpa.getDescription());
		}
		//Name
		if ((fismoJpa.getName() != null) && (!fismoJpa.getName().isEmpty())) {
			fismo.setName(fismoJpa.getName());
		}
		//Discoverable
		if (fismoJpa.isDiscoverable() != null) {
			fismo.setDiscoverable(fismoJpa.isDiscoverable());
		}	
		//Service
		if ((fismoJpa.getService() != null) && (!fismoJpa.getService().isEmpty())) {
			fismo.setService(fismoJpa.getService());
		}	
		//ExperimentControl
		if (fismoJpa.getExperimentControl() != null) {
			fismo.setExperimentControl(experimentControlJpaToExperimentControl(fismoJpa.getExperimentControl()));
		}
		//ExperimentOutput
		if (fismoJpa.getExperimentOutput() != null) {
			fismo.setExperimentOutput(experimentOutputJpaToExperimentOutput(fismoJpa.getExperimentOutput()));
		}
		//QueryControl
		if (fismoJpa.getQueryControl() != null) {
			fismo.setQueryControl(queryControlJpaToQueryControl(fismoJpa.getQueryControl()));
		}
		if (fismoJpa.getRule() != null) {
		//Rule
			fismo.setRule(ruleJpaToRule(fismoJpa.getRule()));
		}
		

		return fismo;
	}	

	
	/**
	 * @param experimentControl
	 * @return
	 */
	public ExperimentControl experimentControlJpaToExperimentControl(ExperimentControlJpa experimentControlJpa) {
		ExperimentControl experimentControl = new ExperimentControl();
		

		//ReportIfEmpty
		if (experimentControlJpa.isReportIfEmpty() != null)
			experimentControl.setReportIfEmpty(experimentControlJpa.isReportIfEmpty());
		//Trigger
		if ((experimentControlJpa.getTrigger() != null) && (!experimentControlJpa.getTrigger().isEmpty())) 
			experimentControl.setTrigger(experimentControlJpa.getTrigger());
		//Scheduling
		if (experimentControlJpa.getScheduling() != null){
			Scheduling scheduling = new Scheduling();
			//Periodicity
			if (experimentControlJpa.getScheduling().getPeriodicity() != null) 
				scheduling.setPeriodicity(experimentControlJpa.getScheduling().getPeriodicity());
			//StartTime
			if (experimentControlJpa.getScheduling().getStartTime() != null) 
				scheduling.setStartTime(experimentControlJpa.getScheduling().getStartTime());
			//StopTime
			if (experimentControlJpa.getScheduling().getStopTime() != null) 
				scheduling.setStopTime(experimentControlJpa.getScheduling().getStopTime());
			experimentControl.setScheduling(scheduling);
		}

		

		return experimentControl;
	}	
	
	
	/**
	 * @param experimentOutput
	 * @return
	 */
	public ExperimentOutput experimentOutputJpaToExperimentOutput(ExperimentOutputJpa experimentOutputJpa) {
		ExperimentOutput experimentOutput = new ExperimentOutput();
		
		

		//Location
		if ((experimentOutputJpa.getLocation() != null) && (!experimentOutputJpa.getLocation().isEmpty())) 
			experimentOutput.setLocation(experimentOutputJpa.getLocation());
		
		//File
		if ((experimentOutputJpa.getFile() != null) && (!experimentOutputJpa.getFile().isEmpty())) {
			for (FileJpa fileJpa : experimentOutputJpa.getFile()){
				
				File file = new File();
				file.setType(fileJpa.getType());
								
				experimentOutput.getFile().add(file);
			}
		}	
		
		
		//Widget
		if ((experimentOutputJpa.getWidget() != null) && (!experimentOutputJpa.getWidget().isEmpty())) {
			for (WidgetJpa widgetJpa : experimentOutputJpa.getWidget()){
				Widget widget = new Widget();
				
				//WidgetID
				if ((widgetJpa.getWidgetID() != null) && (!widgetJpa.getWidgetID().isEmpty())) 
					widget.setWidgetID(widgetJpa.getWidgetID());
				
				//PresentationAttr
				if ((widgetJpa.getPresentationAttr() != null) && (!widgetJpa.getPresentationAttr().isEmpty())) {
					for (PresentationAttrJpa presentationAttrJpa : widgetJpa.getPresentationAttr()){
						PresentationAttr presentationAttr = new PresentationAttr();
						//Name
						if (presentationAttrJpa.getName() != null)
							presentationAttr.setName(presentationAttrJpa.getName());
						//Value
						if (presentationAttrJpa.getValue() != null)
							presentationAttr.setValue(presentationAttrJpa.getValue());
						
						widget.getPresentationAttr().add(presentationAttr);
					}
				}
				
				experimentOutput.getWidget().add(widget);
			}
		}	
		return experimentOutput;
	}	
	
	
	
	
	/**
	 * @param queryControl
	 * @return
	 */
	public QueryControl queryControlJpaToQueryControl(QueryControlJpa queryControlJpa) {
		QueryControl queryControl = new QueryControl();

		//DynamicAttrs
		if (queryControlJpa.getDynamicAttrs() != null){
			DynamicAttrs dynamicAttrs = new DynamicAttrs();
			
			//PredefinedDynamicAttr
			if (queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr() != null){
				PredefinedDynamicAttr predefinedDynamicAttr = new PredefinedDynamicAttr();
				//PredefinedDynamicAttr > DynamicGeoLocation: Latitude, Longitude
				if (queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicGeoLocation() != null){
					DynamicGeoLocation dynamicGeoLocation = new DynamicGeoLocation();
					//Latitude
					if (queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicGeoLocation().getLatitude() != null)
						dynamicGeoLocation.setLatitude(queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicGeoLocation().getLatitude());
					//Longitude
					if (queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicGeoLocation().getLongitude() != null)
						dynamicGeoLocation.setLongitude(queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicGeoLocation().getLongitude());
						
					predefinedDynamicAttr.setDynamicGeoLocation(dynamicGeoLocation);
				}
					
				//PredefinedDynamicAttr > DynamicQueryInterval: FromDateTime, ToDateTime, IntervalNowToPast
				if (queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval() != null){
					DynamicQueryInterval dynamicQueryInterval = new DynamicQueryInterval();
					//FromDateTime
					if (queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval().getFromDateTime() != null)
						dynamicQueryInterval.setFromDateTime(queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval().getFromDateTime());
					//ToDateTime
					if (queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval().getToDateTime() != null)
						dynamicQueryInterval.setToDateTime(queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval().getToDateTime());
					//IntervalNowToPast
					if (queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval().getIntervalNowToPast() != null)
						dynamicQueryInterval.setIntervalNowToPast(queryControlJpa.getDynamicAttrs().getPredefinedDynamicAttr().getDynamicQueryInterval().getIntervalNowToPast());
				
					predefinedDynamicAttr.setDynamicQueryInterval(dynamicQueryInterval);
				}
				dynamicAttrs.setPredefinedDynamicAttr(predefinedDynamicAttr);
			}
			
			
			//DynamicAttr: Name, Value
			if ((queryControlJpa.getDynamicAttrs().getDynamicAttr() != null) && (!queryControlJpa.getDynamicAttrs().getDynamicAttr().isEmpty())) {
				for (DynamicAttrJpa dynamicAttrJpa : queryControlJpa.getDynamicAttrs().getDynamicAttr()){
					DynamicAttr dynamicAttr = new DynamicAttr();
					//Name
					if (dynamicAttrJpa.getName() != null)
						dynamicAttr.setName(dynamicAttrJpa.getName());
					//Value
					if (dynamicAttrJpa.getValue() != null)
						dynamicAttr.setValue(dynamicAttrJpa.getValue());
					
					dynamicAttrs.getDynamicAttr().add(dynamicAttr);
				}
			}
			queryControl.setDynamicAttrs(dynamicAttrs);
		}
		
		//QueryInterval
		if (queryControlJpa.getQueryInterval() != null){
			QueryInterval queryInterval = new QueryInterval();
			//FromDateTime
			if (queryControlJpa.getQueryInterval().getFromDateTime() != null)
				queryInterval.setFromDateTime(queryControlJpa.getQueryInterval().getFromDateTime());
			//ToDateTime
			if (queryControlJpa.getQueryInterval().getToDateTime() != null)
				queryInterval.setToDateTime(queryControlJpa.getQueryInterval().getToDateTime());
			//IntervalNowToPast
			if (queryControlJpa.getQueryInterval().getIntervalNowToPast() != null)
				queryInterval.setIntervalNowToPast(queryControlJpa.getQueryInterval().getIntervalNowToPast());
		
			queryControl.setQueryInterval(queryInterval);
		}
		
		//QueryRequest
		if (queryControlJpa.getQueryRequest() != null){
			QueryRequest queryRequest = new QueryRequest();
			//DefaultGraphUri
			if ((queryControlJpa.getQueryRequest().getDefaultGraphUri() != null) && (!queryControlJpa.getQueryRequest().getDefaultGraphUri().isEmpty())) {
				for (String defaultGraphUri : queryControlJpa.getQueryRequest().getDefaultGraphUri()){
					queryRequest.getDefaultGraphUri().add(defaultGraphUri);
				}
			}
			//NamedGraphUri
			if ((queryControlJpa.getQueryRequest().getNamedGraphUri() != null) && (!queryControlJpa.getQueryRequest().getNamedGraphUri().isEmpty())) {
				for (String namedGraphUri : queryControlJpa.getQueryRequest().getNamedGraphUri()){
					queryRequest.getNamedGraphUri().add(namedGraphUri);
				}
			}
			//Query
			if ((queryControlJpa.getQueryRequest().getQuery() != null) && (!queryControlJpa.getQueryRequest().getQuery().isEmpty()))
				queryRequest.setQuery(queryControlJpa.getQueryRequest().getQuery());
		
			queryControl.setQueryRequest(queryRequest);
		}

		//StaticLocation
		if (queryControlJpa.getStaticLocation() != null){
			StaticLocation staticLocation = new StaticLocation();
			//Latitude
			if (queryControlJpa.getStaticLocation().getLatitude() != null)
				staticLocation.setLatitude(queryControlJpa.getStaticLocation().getLatitude());
			//Longitude
			if (queryControlJpa.getStaticLocation().getLongitude() != null)
				staticLocation.setLongitude(queryControlJpa.getStaticLocation().getLongitude());
			
			queryControl.setStaticLocation(staticLocation);
		}
		
		//QuantityKind
		if ((queryControlJpa.getQuantityKind() != null) && (!queryControlJpa.getQuantityKind().isEmpty())) {
			for (String quantityKind : queryControlJpa.getQuantityKind()){
				queryControl.getQuantityKind().add(quantityKind);
			}
		}
		return queryControl;
	}	
	
	
	
	
	/**
	 * @param rule
	 * @return
	 */
	public Rule ruleJpaToRule(RuleJpa ruleJpa) {

		Rule rule = new Rule();
		//DomainKnowledge
		if ((ruleJpa.getDomainKnowledge() != null) && (!ruleJpa.getDomainKnowledge().isEmpty()))
			rule.setDomainKnowledge(ruleJpa.getDomainKnowledge());
		//Name
		if ((ruleJpa.getName() != null) && (!ruleJpa.getName().isEmpty()))
			rule.setName(ruleJpa.getName());
		//RuleDefinition
		if ((ruleJpa.getRuleDefinition() != null) && (!ruleJpa.getRuleDefinition().isEmpty()))
			rule.setRuleDefinition(ruleJpa.getRuleDefinition());
		
		//QuantityKind
		if ((ruleJpa.getQuantityKind() != null) && (!ruleJpa.getQuantityKind().isEmpty())) {
			for (String quantityKind : ruleJpa.getQuantityKind()){
				rule.getQuantityKind().add(quantityKind);
			}
		}

		// TODO Auto-generated method stub
		return rule;
	}
	
	//==================End of FedSpecJPA To FedSpec Mapping=======================
	
	
	
	
	
	
}
