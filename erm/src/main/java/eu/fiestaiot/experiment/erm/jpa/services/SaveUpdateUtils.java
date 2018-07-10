package eu.fiestaiot.experiment.erm.jpa.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fiestaiot.commons.fedspec.model.*;
import eu.fiestaiot.commons.sparql.protocoltypes.model.QueryRequest;
import eu.fiestaiot.experiment.erm.jpa.fedspec.entities.*;

/**
 * @author kefnik
 *
 */
public class SaveUpdateUtils {

	
	//Logger's initialization
	final static Logger logger = LoggerFactory.getLogger(SaveUpdateUtils.class);
	
	
	//==================Start of FedSpec To FedSpecJPA Mapping=======================
	
	
/*	public FEDSpecJpa fedSpecToFedSpecJPA(FEDSpec fedSpec) {

		FEDSpecJpa fedSpecJpa = new FEDSpecJpa();

		//UserID
		if ((fedSpec.getUserID() != null) && (!fedSpec.getUserID().isEmpty())) {
			fedSpecJpa.setUserID(fedSpec.getUserID());
		}
		//FEMO
		if ((fedSpec.getFEMO() != null) && (!fedSpec.getFEMO().isEmpty()))
			for (FEMO femo : fedSpec.getFEMO()) {
				fedSpecJpa.getFEMO().add(femoToFemoJPA(femo));
			}

		return fedSpecJpa;
	}*/

	
	
	/**
	 * @param femo
	 * @return
	 */
	
/*	public FemoJpa saveFemoToExistentFemoJPA(FemoJpa femoJpa, FEMO femo) {


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
				femoJpa.getFISMO().add(fismoToFismoJPA(fismo));
			}
		}
		return femoJpa;
	}*/
	

	/**
	 * @param fismo
	 * @return
	 */
	public FismoJpa saveFismoToExistentFismoJPA(FismoJpa fismoJpa, FISMO fismo) {

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

		// TODO Auto-generated method stub
		return ruleJpa;
	}	
	
	//==================End of FedSpec To FedSpecJPA Mapping=======================
	
	

	
	
	
	
	
}
