/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.vcttrac.web.controller;

import java.text.DateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Obs;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ServiceContext;
import org.openmrs.module.vcttrac.VCTClient;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.module.vcttrac.util.VCTTracConstant;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * @author Yves GAKUBA
 */
public class VCTReceptionOfResultController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());
		VCTModuleService service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
		
		if (request.getParameter("save") != null)
			saveResultReceptionByTheClient(request);
		
		mav.addObject("clientCodes", service.getAllClientCodeForReceptionOfResult());
		
		return mav;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param index
	 * @return
	 */
	private PatientIdentifier createPatientIdentifier(HttpServletRequest request, int index) {
		PatientIdentifier pi = new PatientIdentifier();
		try {
			pi.setDateCreated(new Date());
			pi.setIdentifierType(Context.getPatientService().getPatientIdentifierType(
			    Integer.valueOf(request.getParameter("identifierType_" + index))));
			pi.setIdentifier(request.getParameter("identifier_" + index));
			pi.setLocation(Context.getLocationService().getLocation(
			    Integer.valueOf(request.getParameter("identifierLocation_" + index))));
		}
		catch (Exception ex) {
			String msg = "An error occured [" + ex.getMessage() + "], please check your log file.";
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
			log.error(">>>>>>>>>>>>VCT>>Result>>Reception>>Form>>>> An error occured : " + ex.getMessage());
			ex.printStackTrace();
		}
		return pi;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @return
	 */
	/*private Person saveDateOfReceptionOfResults(HttpServletRequest request) {
		VCTModuleService service;
		VCTClient client = null;
		Date resultReceivedOn;
		Obs hivTestConstruct;
		Obs dateResultOfHivTestReceived = new Obs();
		try {
			service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
			client = service.getClientByCodeTest(request.getParameter("clientCode"));
			resultReceivedOn = df.parse(request.getParameter("dateHivTestResultReceived"));
			hivTestConstruct = client.getResultObs();
			
			log
			        .info(">>>>>VCT>>Result>>Reception>>From>>>> Trying to save the dateResultOfHivTestReceived for Person#" + client.getClient().getPersonId()
			                + "...");
			dateResultOfHivTestReceived.setCreator(Context.getAuthenticatedUser());
			dateResultOfHivTestReceived.setDateCreated(new Date());
			dateResultOfHivTestReceived.setLocation(hivTestConstruct.getLocation());
			dateResultOfHivTestReceived.setPerson(client.getClient());
			dateResultOfHivTestReceived.setConcept(Context.getConceptService().getConcept(
			    VCTTracUtil.getDateResultOfHivTestReceivedConceptId()));
			dateResultOfHivTestReceived.setValueDatetime(resultReceivedOn);
			dateResultOfHivTestReceived.setObsDatetime(resultReceivedOn);
			
			hivTestConstruct.addGroupMember(dateResultOfHivTestReceived);
			
			Context.getObsService().saveObs(hivTestConstruct, "Reception of result");
			
			log.info(">>>>>VCT>>Result>>Reception>>From>>>> Obs dateResultOfHivTestReceived saved successfully.");
			
			client.setArchived(true);
			service.saveVCTClient(client);
			log.info(">>>>>VCT>>Result>>Reception>>From>>>> Client Archived successfully.");
		}
		catch (Exception e) {
			log.error(">>>>>>>>>>>>VCT>>Result>>Reception>>Form>>>> An error occured : "+e.getMessage());
			e.printStackTrace();
		}
		return client.getClient();
	}*/
	
	private void addNumberOfCondomsTaken(Obs o, HttpServletRequest request) throws Exception{
		if(request.getParameter("numberOfCondom")!=null){
			DateFormat df = Context.getDateFormat();
			Date obsDatetime=df.parse(request.getParameter("dateHivTestResultReceived"));
			
			Obs obs5 = new Obs();
			obs5.setPerson(o.getPerson());
			obs5.setCreator(Context.getAuthenticatedUser());
			obs5.setDateCreated(new Date());
			obs5.setLocation(o.getLocation());
			obs5.setObsDatetime(obsDatetime);
			obs5.setConcept(Context.getConceptService().getConcept(VCTConfigurationUtil.getNumberOfCondomsTakenConceptId()));
			obs5.setValueNumeric(Double.valueOf(request.getParameter("numberOfCondom")));
			
			obs5.setObsGroup(o);
			Context.getObsService().saveObs(obs5, null);
		}
	}

	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 */
	private void saveResultReceptionByTheClient(HttpServletRequest request) {
		
		VCTModuleService vms;
		DateFormat df = Context.getDateFormat();
		try {
			
			VCTClient client = null;
			Date resultReceivedOn;
			Obs hivTestConstruct;
			Obs dateResultOfHivTestReceived = new Obs();
			
			vms = Context.getService(VCTModuleService.class);
			client = vms.getClientByCodeTest(request.getParameter("clientCode"));
			resultReceivedOn = df.parse(request.getParameter("dateHivTestResultReceived"));
			hivTestConstruct = client.getResultObs();
			
			//save the number of condoms taken by the client
			addNumberOfCondomsTaken(client.getCounselingObs(), request);
			
			log.info(">>>>>VCT>>Result>>Reception>>From>>>> Trying to save the dateResultOfHivTestReceived for Person#"
			        + client.getClient().getPersonId() + "...");
			dateResultOfHivTestReceived.setCreator(Context.getAuthenticatedUser());
			dateResultOfHivTestReceived.setDateCreated(new Date());
			dateResultOfHivTestReceived.setLocation(hivTestConstruct.getLocation());
			dateResultOfHivTestReceived.setPerson(client.getClient());
			dateResultOfHivTestReceived.setConcept(Context.getConceptService().getConcept(
			    VCTConfigurationUtil.getDateResultOfHivTestReceivedConceptId()));
			dateResultOfHivTestReceived.setValueDatetime(resultReceivedOn);
			dateResultOfHivTestReceived.setObsDatetime(resultReceivedOn);
			
			hivTestConstruct.addGroupMember(dateResultOfHivTestReceived);
			
			Context.getObsService().saveObs(dateResultOfHivTestReceived, "Reception of result");
			
			log.info(">>>>>VCT>>Result>>Reception>>Form>>>> Obs dateResultOfHivTestReceived saved successfully.");
			
			//			trying to transfer that to the last psy-social UI
			//			client.setArchived(true); 
			if (request.getParameter("clientDecision") != null) {
				client.setClientDecision(Integer.valueOf(request.getParameter("clientDecision")));
			}
			vms.saveVCTClient(client);
			
			log.info(">>>>>VCT>>Result>>Reception>>Form>>>> Client Archived successfully.");
			
			/*
					 * trying to transfer that to the last psy-social UI
					 * 
					 * if(request.getParameter("clientDecision").compareTo("1")==0){				
				Person person = client.getClient();
				
				if (!client.getClient().isPatient() && request.getParameter("hivStatus").compareToIgnoreCase("1") == 0) {
					Patient p = new Patient();
					PersonName pn = new PersonName(person.getPersonName().getGivenName(), person.getPersonName()
					        .getMiddleName(), person.getPersonName().getFamilyName());
					p.getNames().add(pn);
					p.setBirthdate(person.getBirthdate());
					p.setGender(person.getGender());//string
					p.setCreator(Context.getAuthenticatedUser());
					
					Program program = Context.getProgramWorkflowService().getProgram(VCTConfigurationUtil.getHivProgramId());
					PatientProgram patientProgram = new PatientProgram();
					PatientService patientService = Context.getPatientService();
					ProgramWorkflowService programWorkFlowService = Context.getProgramWorkflowService();
					
					if (request.getParameter("identifierType_0") != null) {
						log.info(">>>>>VCT>>Result>>Reception>>From>>>> Trying to create identifier for Patient#" + p.getNames());
						boolean cont = true;
						int index = 0;
						while (cont) {
							if (cont && request.getParameter("identifierType_" + index) != null
							        && request.getParameter("identifierType_" + index).compareTo("") != 0) {
								if (true == Context.getPatientService().getPatientIdentifierType(
								    Integer.valueOf(request.getParameter("identifierType_" + index))).getRequired()) {
									
									p.addIdentifier(createPatientIdentifier(request, index));
									log.info(">>>>>VCT>>Result>>Reception>>From>>>> Identifier of type "
									        + Context.getPatientService().getPatientIdentifierType(
									            Integer.valueOf(request.getParameter("identifierType_" + index))).getName()
									        + " for Patient#" + p.getNames() + " created succesfully.");
									
								}
							} else
								cont = false;
							index += 1;
						}
						log.info(">>>>>VCT>>Result>>Reception>>From>>>> Identifier creation for Patient#" + p.getNames() + " is success");
					}
					
					log.info(">>>>>VCT>>Result>>Reception>>From>>>> Trying to save Patient#" + p.getNames() + "...");
					patientService.savePatient(p);
					log.info(">>>>>VCT>>Result>>Reception>>From>>>> Patient#" + p.getPatientId() + " saved succesfully");
					
					log.info(">>>>>VCT>>Result>>Reception>>From>>>> Trying to enroll Patient#" + p.getNames() + " to " + program.getName() + "...");
					patientProgram.setPatient(p);
					patientProgram.setProgram(program);
					patientProgram.setDateEnrolled(df.parse(request.getParameter("dateHivTestResultReceived")));
					patientProgram.setCreator(Context.getAuthenticatedUser());
					patientProgram.setDateCreated(new Date());
					programWorkFlowService.savePatientProgram(patientProgram);
					log.info(">>>>>VCT>>Result>>Reception>>From>>>> Enrollement finished successfully.");
					
					log.info(">>>>>VCT>>Result>>Reception>>From>>>> Trying to synchronize Person#" + person.getPersonId() + " and Person#" + p.getPersonId() + "...");
					service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
					service.synchronizePatientsAndClients(person.getPersonId(), p.getPatientId());
					person = p;
					log.info(">>>>>VCT>>Result>>Reception>>From>>>> Synchronization of patients finished successfully.");
					
				}

				if (request.getParameter("nextVisitDate") != null && request.getParameter("nextVisitDate") != "") {
					Obs obs = new Obs();
					obs.setConcept(Context.getConceptService().getConcept(VCTTracConstant.RETURN_VISIT_DATE));
					obs.setObsDatetime(df.parse(request.getParameter("nextVisitDate")));
					obs.setDateCreated(new Date());
					obs.setCreator(Context.getAuthenticatedUser());
					obs.setPerson(person);
					obs.setLocation(Context.getLocationService().getDefaultLocation());
					
					log.info(">>>>>VCT>>Result>>Reception>>From>>>> Trying to save a Next Visit Date for Patient#" + person.getPersonId() + "...");
					Context.getObsService().saveObs(obs, null);
					log.info(">>>>>VCT>>Result>>Reception>>From>>>> Saved successfully.");
				}
				
				if (request.getParameter("nextVisitDate") != null && request.getParameter("nextVisitDate") != "") {
					Obs modeOfAdmission = new Obs();
					modeOfAdmission.setConcept(Context.getConceptService().getConcept(VCTTracConstant.METHOD_OF_ENROLLMENT));
					modeOfAdmission.setValueCoded(Context.getConceptService().getConcept(VCTTracConstant.VCT_PROGRAM));
					modeOfAdmission.setDateCreated(new Date());
					modeOfAdmission.setObsDatetime(resultReceivedOn);
					modeOfAdmission.setCreator(Context.getAuthenticatedUser());
					modeOfAdmission.setPerson(person);
					modeOfAdmission.setLocation(Context.getLocationService().getDefaultLocation());
					
					log.info(">>>>>VCT>>Result>>Reception>>From>>>> Trying to save the Method of Enrollment for Patient#" + person.getPersonId() + "...");
					Context.getObsService().saveObs(modeOfAdmission, null);
					log.info(">>>>>VCT>>Result>>Reception>>From>>>> Saved successfully.");
				}
			} else */

			if (request.getParameter("clientDecision") != null && request.getParameter("clientDecision").compareTo("0") == 0) {
				Obs obs = new Obs();
				obs.setConcept(Context.getConceptService().getConcept(VCTTracConstant.TRANSFER_OUT_TO));
				obs.setValueAsString(request.getParameter("location"));
				obs.setDateCreated(new Date());
				obs.setCreator(Context.getAuthenticatedUser());
				obs.setObsDatetime(resultReceivedOn);
				obs.setPerson(client.getClient());
				obs.setLocation(Context.getLocationService().getDefaultLocation());
				
				log
				        .info(">>>>>VCT>>Result>>Reception>>From>>>> Trying to save the location where the client is being transferred to...");
				Context.getObsService().saveObs(obs, null);
				log.info(">>>>>VCT>>Result>>Reception>>From>>>> Saved successfully.");
			}
			
			if (request.getParameter("clientDecision") == null || request.getParameter("clientDecision").compareTo("1") != 0) {
				client.setArchived(true);
				vms.saveVCTClient(client);
			}
			
			if (client.getClient().isPatient()) {
				client.setArchived(true);
				log.info(">>>>>VCT>>Result>>Reception>>From>>>> Trying to archive Client#"
				        + client.getTracVctClientId() + "...");
				vms.saveVCTClient(client);
				log.info(">>>>>VCT>>Result>>Reception>>From>>>> Client Archived successfully.");
			}
			
			//			if (!client.getClient().isPatient() && request.getParameter("hivStatus").compareToIgnoreCase("0") == 0) {
			//				client.setArchived(true);
			//				service.saveVCTClient(client);
			//			}
			
			String msg = getMessageSourceAccessor().getMessage("Form.saved");
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
		}
		catch (Exception e) {
			log.error(">>>>>VCT>>Result>>Reception>>From>>>> An error occured : ");
			e.printStackTrace();
			String msg = getMessageSourceAccessor().getMessage("Form.not.saved");
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
		}
		
	}
	
}
