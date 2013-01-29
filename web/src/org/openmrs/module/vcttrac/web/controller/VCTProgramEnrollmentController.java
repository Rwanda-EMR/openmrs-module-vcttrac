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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientProgram;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Program;
import org.openmrs.User;
import org.openmrs.api.DuplicateIdentifierException;
import org.openmrs.api.IdentifierNotUniqueException;
import org.openmrs.api.InsufficientIdentifiersException;
import org.openmrs.api.InvalidIdentifierFormatException;
import org.openmrs.api.PatientIdentifierException;
import org.openmrs.api.PatientService;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.module.vcttrac.VCTClient;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.ContextProvider;
import org.openmrs.module.vcttrac.util.FileExporter;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.module.vcttrac.util.VCTTracConstant;
import org.openmrs.module.vcttrac.util.VCTTracUtil;
import org.openmrs.web.WebConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class VCTProgramEnrollmentController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());		
		
		mav.addObject("locations", VCTTracUtil.createLocationOptions());
		mav.addObject("defaultLocationId", VCTConfigurationUtil.getDefaultLocationId());
		mav.addObject("providers", VCTTracUtil.createProviderOptions());
		mav.addObject("defaultProviderId", ((Context.getAuthenticatedUser().hasRole("Provider")) ? Context
		        .getAuthenticatedUser().getUserId() : ""));
		mav.addObject("resultOfHivTestId", VCTTracConstant.RESULT_OF_HIV_TEST);
		mav.addObject("positiveString", Context.getConceptService().getConcept(VCTTracConstant.POSITIVE_CID)
		        .getDisplayString());
		
		if (request.getParameter("code") != null) {
			if (request.getParameter("code").trim().compareToIgnoreCase("") != 0) {
				enrollPatientInHIVProgram(request, response, mav);
			}
		}
		
		manageListing(request, response, mav);
		
		return mav;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param mav
	 */
	private void manageListing(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		VCTModuleService service = Context.getService(VCTModuleService.class);
		
		String pageNumber = (request.getParameter("page") != null) ? request.getParameter("page") : "1";
		//		StringBuilder parameters = new StringBuilder();
		//		parameters.append("");
		int pageSize;//, type;
		List<VCTClient> clients = new ArrayList<VCTClient>();
		
		List<Integer> res;
		
		List<Integer> numberOfPages;
		
		try {
			if (VCTConfigurationUtil.isConfigured()) {
				//				type = Integer.valueOf(request.getParameter("type"));
				//				parameters.append("&type=" + request.getParameter("type"));
				
				pageSize = Integer.valueOf(VCTConfigurationUtil.getNumberOfRecordPerPage());
				if (pageNumber.compareToIgnoreCase("1") == 0 || pageNumber.compareToIgnoreCase("") == 0) {
					res = new ArrayList<Integer>();
					
					//					if (request.getParameter("type") != null && request.getParameter("type").compareToIgnoreCase("0") == 0) {
					res = service.getVCTClientsWaitingToBeEnrolledInHIVProgram();
					/*	type = 0;
					} else if (request.getParameter("type") != null
					        && request.getParameter("type").compareToIgnoreCase("1") == 0) {
						if (request.getParameter("from") != null
						        && request.getParameter("from").compareToIgnoreCase("pit") == 0)
							res = service.getVCTClientsFromPIT();
						else if (request.getParameter("from") != null
						        && request.getParameter("from").compareToIgnoreCase("vct") == 0)
							res = service.getVoluntaryClients();
						type = 1;
						parameters.append("&from=" + request.getParameter("from"));
					} else if (request.getParameter("type") != null
					        && request.getParameter("type").compareToIgnoreCase("2") == 0) {
						res = service.getVCTClientsBasedOnGender(request.getParameter("gender"));
						type = 2;
						parameters.append("&gender=" + request.getParameter("gender"));
					} else if (request.getParameter("type") != null
					        && request.getParameter("type").compareToIgnoreCase("3") == 0) {
						if (request.getParameter("attributeTypeId") != null && request.getParameter("value") != null)
							res = service.getVCTClientsBasedOnAttributeType(Integer.valueOf(request
							        .getParameter("attributeTypeId")), Integer.valueOf(request.getParameter("value")));
						
						type = 3;
						parameters.append("&attributeTypeId=" + request.getParameter("attributeTypeId"));
						parameters.append("&value=" + request.getParameter("value"));
					} else if (request.getParameter("type") != null
					        && request.getParameter("type").compareToIgnoreCase("4") == 0
					        && request.getParameter("tested") != null) {
						res = service.getVCTClientsTested((request.getParameter("tested").compareTo("yes") == 0) ? true
						        : false);
						
						type = 4;
						parameters.append("&tested=" + request.getParameter("tested"));
					} else if (request.getParameter("type") != null
					        && request.getParameter("type").compareToIgnoreCase("5") == 0) {
						if (request.getParameter("conceptId") != null && request.getParameter("value") != null
						        && request.getParameter("gotresult") != null) {
							res = service.getVCTClientsBasedOnConceptObs(Integer.valueOf(request.getParameter("conceptId")),
							    Integer.valueOf(request.getParameter("value")), (request.getParameter("gotresult")
							            .compareToIgnoreCase("true") == 0));
							
							parameters.append("&value=" + request.getParameter("value"));
						} else if (request.getParameter("gotresult") != null) {
							res = service.getVCTClientsBasedOnConceptObs(Integer.valueOf(request.getParameter("conceptId")),
							    null, (request.getParameter("gotresult").compareToIgnoreCase("true") == 0));
						}
						type = 5;
						parameters.append("&conceptId=" + request.getParameter("conceptId"));
						parameters.append("&gotresult=" + request.getParameter("gotresult"));
					} else if (request.getParameter("type") != null
					        && request.getParameter("type").compareToIgnoreCase("6") == 0) {
						if (request.getParameter("counselingType") != null)
							res = service.getVCTClientsBasedOnCounselingType(Integer.valueOf(request
							        .getParameter("counselingType")));
						
						type = 6;
						parameters.append("&counselingType=" + request.getParameter("counselingType"));
					}*/

					request.getSession().setAttribute("prg_enrollment_res", res);
					
					//data collection
					for (int i = 0; i < pageSize; i++) {
						if (res.size() == 0)
							break;
						if (i >= res.size() - 1) {
							clients.add(service.getClientById(res.get(i)));
							break;
						} else
							clients.add(service.getClientById(res.get(i)));
					}
					
					//---------paging-------navigation between pages--------
					int n = (res.size() == ((int) (res.size() / pageSize)) * pageSize) ? (res.size() / pageSize)
					        : ((int) (res.size() / pageSize)) + 1;
					numberOfPages = new ArrayList<Integer>();
					for (int i = 1; i <= n; i++) {
						numberOfPages.add(i);
					}
					
					request.getSession().setAttribute("prg_enrollment_numberOfPages", numberOfPages);
					
					if (Integer.valueOf(pageNumber) > 1)
						mav.addObject("prevPage", (Integer.valueOf(pageNumber)) - 1);
					else
						mav.addObject("prevPage", -1);
					if (Integer.valueOf(pageNumber) < numberOfPages.size())
						mav.addObject("nextPage", (Integer.valueOf(pageNumber)) + 1);
					else
						mav.addObject("nextPage", -1);
					mav.addObject("lastPage", ((numberOfPages.size() >= 1) ? numberOfPages.size() : 1));
					//----------------
					
				} else {
					res = (List<Integer>) request.getSession().getAttribute("prg_enrollment_res");
					numberOfPages = (List<Integer>) request.getSession().getAttribute("prg_enrollment_numberOfPages");
					for (int i = (pageSize * (Integer.parseInt(pageNumber) - 1)); i < pageSize
					        * (Integer.parseInt(pageNumber)); i++) {
						if (i >= res.size())
							break;
						else
							clients.add(service.getClientById(res.get(i)));
					}
				}
				
				String title = VCTTracUtil.getMessage("vcttrac.statistic.clientwaitingenrollment", null);
				
				mav.addObject("title", title);
				
				/*switch (type) {
					case 0: {
						title = "All Clients";
						mav.addObject("title", title);
						break;
					}
					case 1: {
						title = "Clients from " + request.getParameter("from").toUpperCase();
						mav.addObject("title", title);
						break;
					}
					case 2: {
						title = "Gender : " + ((request.getParameter("gender").compareTo("f") == 0) ? "Female" : "Male");
						mav.addObject("title", title);
						break;
					}
					case 3: {
						title = Context.getPersonService().getPersonAttribute(
						    Integer.valueOf(request.getParameter("attributeTypeId"))).getAttributeType().getName()
						        + " : "
						        + Context.getConceptService().getConcept(Integer.valueOf(request.getParameter("value")))
						                .getDisplayString();
						mav.addObject("title", title);
						break;
					}
					case 4: {
						title = "Tested : " + request.getParameter("tested").toUpperCase();
						
						mav.addObject("title", title);
						break;
					}
					case 5: {
						title = Context.getConceptService().getConcept(Integer.valueOf(request.getParameter("conceptId")))
						        .getDisplayString()
						        + " : "
						        + ((request.getParameter("value") != null) ? Context.getConceptService().getConcept(
						            Integer.valueOf(request.getParameter("value"))).getDisplayString() : "ALL RESULTS");
						
						mav.addObject("title", title);
						break;
					}
					case 6: {
						title = "Counseling Type : "
						        + ((request.getParameter("counselingType").compareToIgnoreCase("1") == 0) ? "Individual"
						                : "Couples");
						
						mav.addObject("title", title);
						break;
					}
					default: {
						title = "...";
						mav.addObject("title", "...");
						break;
					}
				}*/

				//page infos
				Object[] pagerInfos = new Object[3];
				pagerInfos[0] = (res.size() == 0) ? 0 : (pageSize * (Integer.parseInt(pageNumber) - 1)) + 1;
				pagerInfos[1] = (pageSize * (Integer.parseInt(pageNumber)) <= res.size()) ? pageSize
				        * (Integer.parseInt(pageNumber)) : res.size();
				pagerInfos[2] = res.size();
				
				ApplicationContext appContext = ContextProvider.getApplicationContext();
				
				mav.addObject("numberOfPages", numberOfPages);
				mav.addObject("clients", clients);
				//				mav.addObject("parameters", parameters.toString());
				mav.addObject("pageSize", pageSize);
				mav.addObject("pageInfos", appContext.getMessage("vcttrac.pagingInfo.showingResults", pagerInfos, Context
				        .getLocale()));
				
				FileExporter fexp = new FileExporter();
				
				if (request.getParameter("export") != null && request.getParameter("export").compareToIgnoreCase("csv") == 0) {
					List<VCTClient> clientList = new ArrayList<VCTClient>();
					for (Integer clientId : res) {
						clientList.add(service.getClientById(clientId));
					}
					fexp.exportToCSVFile(request, response, clientList, "list_of_clients_in_vct_program_" + title + ".csv",
					    VCTTracUtil.getMessage("vcttrac.statistic.clientlist", null) + " : " + title.toUpperCase(),
					    "Client waiting to be enrolled in Program");
				}
			}
			
		}
		catch (Exception e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    VCTTracUtil.getMessage("vcttrac.error.loadingData", null));
			
			log.error(">>>>>>>>>>>>VCT>>Statistics>>>> An error occured : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param mav
	 */
	private boolean enrollPatientInHIVProgram(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		/*
		 * trying to save patient in psy-social UI
		 * 
		 */

		VCTModuleService vms;
		DateFormat df = Context.getDateFormat();
		
		Date encounterDate;
		Location location;
		User provider;
		VCTClient client = null;
		Patient p = null;
		
		try {
			location = Context.getLocationService().getLocation(Integer.valueOf(request.getParameter("encounterLocation")));
			encounterDate = df.parse(request.getParameter("enrollmentDate"));
			provider = Context.getUserService().getUser(Integer.valueOf(request.getParameter("provider")));
			
			vms = Context.getService(VCTModuleService.class);
			client = vms.getClientByCodeTest(request.getParameter("code"));
			
			//			if (request.getParameter("clientDecision").compareTo("1") == 0) {
			Person person = client.getClient();
			
			if (!person.isPatient() && request.getParameter("hivStatus").compareToIgnoreCase("1") == 0) {
				p = new Patient();
				// person name
				PersonName pn = new PersonName(person.getPersonName().getGivenName(),
				        person.getPersonName().getMiddleName(), person.getPersonName().getFamilyName());
				
				//adding person's name, birtdate, gender and creator
				p.getNames().add(pn);
				p.setBirthdate(person.getBirthdate());
				p.setGender(person.getGender());//string
				p.setCreator(Context.getAuthenticatedUser());
				
				//creating a patientProgramHiv
				Program programHiv = Context.getProgramWorkflowService().getProgram(VCTConfigurationUtil.getHivProgramId());
				PatientProgram patientProgram_Hiv = new PatientProgram();
				PatientService patientService = Context.getPatientService();
				ProgramWorkflowService programWorkFlowService = Context.getProgramWorkflowService();
				
				if (request.getParameter("identifierType_0") != null) {
					log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to create identifier for Patient#"
					        + p.getNames());
					boolean cont = true;
					int index = 0;
					while (cont) {
						if (cont && request.getParameter("identifierType_" + index) != null
						        && request.getParameter("identifierType_" + index).compareTo("") != 0) {
							//							if (true == Context.getPatientService().getPatientIdentifierType(
							//							    Integer.valueOf(request.getParameter("identifierType_" + index))).getRequired()) {
							
							PatientIdentifier identifier = createPatientIdentifier(request, index);
							if (null != identifier) {
								p.addIdentifier(identifier);
								log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Identifier of type "
								        + Context.getPatientService().getPatientIdentifierType(
								            Integer.valueOf(request.getParameter("identifierType_" + index))).getName()
								        + " for Patient#" + p.getNames() + " created succesfully.");
							} else return false;
							
							//							}
						} else
							cont = false;
						index += 1;
					}
					log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Identifier creation for Patient#"
					        + p.getNames() + " succeded");
				}
				
				//				moved at the end of the function
				//				client.setArchived(true);
				//				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to archive Client#"
				//				        + client.getTracVctClientId() + "...");
				//				vms.saveVCTClient(client);
				//				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Client Archived successfully.");
				
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to save Patient#" + p.getNames() + "...");
				patientService.savePatient(p);
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Patient#" + p.getPatientId()
				        + " saved succesfully");
				
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to enroll Patient#" + p.getNames() + " to "
				        + programHiv.getName() + "...");
				patientProgram_Hiv.setPatient(p);
				patientProgram_Hiv.setProgram(programHiv);
				patientProgram_Hiv.setDateEnrolled(encounterDate);
				patientProgram_Hiv.setCreator(Context.getAuthenticatedUser());
				patientProgram_Hiv.setDateCreated(new Date());
				programWorkFlowService.savePatientProgram(patientProgram_Hiv);
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Enrollement finished successfully.");
				
				if (request.getParameter("enroll_in_pmtct") != null) {
					log.info(">>>>>VCT>>PMTCT>>Program>>Patient>>Enrollment>>>> Trying to enroll Patient#" + p.getNames()
					        + " to " + programHiv.getName() + "...");
					Program programPmtct = Context.getProgramWorkflowService().getProgram(
					    VCTConfigurationUtil.getPmtctProgramId());
					PatientProgram patientProgram_Pmtct = new PatientProgram();
					patientProgram_Pmtct.setPatient(p);
					patientProgram_Pmtct.setProgram(programPmtct);
					patientProgram_Pmtct.setDateEnrolled(encounterDate);
					patientProgram_Pmtct.setCreator(Context.getAuthenticatedUser());
					patientProgram_Pmtct.setDateCreated(new Date());
					programWorkFlowService.savePatientProgram(patientProgram_Pmtct);
					log.info(">>>>>VCT>>PMTCT>>Program>>Patient>>Enrollment>>>> Enrollement finished successfully.");
				}
				
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to synchronize Person#"
				        + person.getPersonId() + " and Person#" + p.getPersonId() + "...");
				vms = Context.getService(VCTModuleService.class);
				
				vms.synchronizePatientsAndClients(person.getPersonId(), p.getPatientId());
				person = p;
				log
				        .info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Synchronization of patients finished successfully.");
				
			}
			// move at the end of the function
			/*else {
				client.setArchived(true);
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to archive Client#"
				        + client.getTracVctClientId() + "...");
				vms.saveVCTClient(client);
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Client Archived successfully.");
			}*/

			// add the NID identifier in case he doesn't have one
			PatientIdentifier pi = VCTTracUtil.getPatientIdentifier(person.getPersonId(), vms
			        .getClientAtLastVisitByClientId(person.getPersonId()).getNid(), VCTConfigurationUtil
			        .getNIDIdentifierTypeId(), null);
			
			if (null == pi) {
				Patient p1 = Context.getPatientService().getPatient(person.getPersonId());
				pi = new PatientIdentifier();
				//pi.setPatient(p1);
				pi.setDateCreated(new Date());
				pi.setCreator(Context.getAuthenticatedUser());
				pi.setLocation(client.getLocation());
				pi.setIdentifier(client.getNid());
				pi.setIdentifierType(Context.getPatientService().getPatientIdentifierType(
				    VCTConfigurationUtil.getNIDIdentifierTypeId()));
				
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to save PatientIdentifier, type="
				        + Context.getPatientService()
				                .getPatientIdentifierType(VCTConfigurationUtil.getNIDIdentifierTypeId()).getName()
				        + " for Patient#" + p1.getPersonId() + "...");
				//				try {
				p1.addIdentifier(pi);
				Context.getPatientService().savePatient(p1);
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> PatientIdentifier saved successfully.");
				//				}
			}
			
			List<Obs> obsList = new ArrayList<Obs>();
			
			if (request.getParameter("nextVisitDate") != null && request.getParameter("nextVisitDate") != "") {
				Obs nextVisitDate = new Obs();
				nextVisitDate.setConcept(Context.getConceptService().getConcept(VCTTracConstant.RETURN_VISIT_DATE));
				nextVisitDate.setObsDatetime(encounterDate);
				nextVisitDate.setValueDatetime(df.parse(request.getParameter("nextVisitDate")));
				nextVisitDate.setDateCreated(new Date());
				nextVisitDate.setCreator(Context.getAuthenticatedUser());
				nextVisitDate.setPerson(person);
				nextVisitDate.setLocation(location);
				
				obsList.add(nextVisitDate);
				
				//				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to save a Next Visit Date for Patient#"
				//				        + person.getPersonId() + "...");
				//				Context.getObsService().saveObs(obs, null);
				//				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Saved successfully.");
				//			}
				//			
				//			if (request.getParameter("nextVisitDate") != null && request.getParameter("nextVisitDate") != "") {
				
				Obs modeOfAdmission = new Obs();
				modeOfAdmission.setConcept(Context.getConceptService().getConcept(VCTTracConstant.METHOD_OF_ENROLLMENT));
				modeOfAdmission.setValueCoded(Context.getConceptService().getConcept(VCTTracConstant.VCT_PROGRAM));
				modeOfAdmission.setDateCreated(new Date());
				modeOfAdmission.setObsDatetime(encounterDate);
				modeOfAdmission.setCreator(Context.getAuthenticatedUser());
				modeOfAdmission.setPerson(person);
				modeOfAdmission.setLocation(location);
				
				obsList.add(modeOfAdmission);
				
				//				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to save the Method of Enrollment for Patient#"+ person.getPersonId() + "...");
				//				Context.getObsService().saveObs(modeOfAdmission, null);
				//				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Saved successfully.");
				
				Encounter enc = VCTTracUtil.createEncounter(encounterDate, provider, location, Context.getPatientService()
				        .getPatient(person.getPersonId()), Context.getEncounterService().getEncounterType(
				    VCTTracConstant.ADULT_INITIAL_ENCOUNTER_TYPE), obsList);
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to save the first encounter for Patient#"
				        + person.getPersonId() + "...");
				Context.getEncounterService().saveEncounter(enc);
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Encounter saved successfully.");
			}
			
			//archive the client
			archiveClient(person.getPersonId());
			
			//String msg = getMessageSourceAccessor().getMessage();
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Form.saved");
		}
		catch (InvalidIdentifierFormatException iife) {
			log.error(iife);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.formatInvalid");
		}
		catch (IdentifierNotUniqueException inue) {
			log.error(inue);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.notUnique");
		}
		catch (DuplicateIdentifierException die) {
			log.error(die);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.duplicate");
		}
		catch (InsufficientIdentifiersException iie) {
			log.error(iie);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "PatientIdentifier.error.insufficientIdentifiers");
		}
		catch (PatientIdentifierException pie) {
			log.error(pie);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.general");
		}
		//		catch (ConstraintViolationException cve) {
		//			log.error(cve);
		//			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
		//			    "NID " + client.getNid() + " used by someone else...");
		//		}
		catch (Exception e) {
			log.error(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> An error occured : " + e.getMessage());
			e.printStackTrace();
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Form.not.saved");
		}
		return true;
	}
	
	/**
	 * Archive the client
	 * 
	 * @param request
	 */
	private void archiveClient(Integer clientId) throws DataIntegrityViolationException {
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		//try {
		VCTClient cl = vms.getClientAtLastVisitByClientId(clientId);
		cl.setArchived(true);
		cl.setClient(Context.getPersonService().getPerson(clientId));
		log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to archive Client#" + cl.getTracVctClientId()
		        + "..." + cl.getCodeClient() + "..." + cl.getClient().getPersonId());
		vms.saveVCTClient(cl);
		log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Client Archived successfully.");
		//		}
		//		catch (DataIntegrityViolationException dive) {
		//			log.error(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> An error occured : " + dive.getMessage());
		//			dive.printStackTrace();
		//			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Form.not.saved");
		//		}
		//		catch (Exception e) {
		//			log.error(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> An error occured : " + e.getMessage());
		//			e.printStackTrace();
		//			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Form.not.saved");
		//		}
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
			
			if (Context.getPatientService().isIdentifierInUseByAnotherPatient(pi)) {
				request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
				    "Identifier [" + request.getParameter("identifier_" + index) + "] is in use with someone else !");
				return null;
			}
		}
		catch (Exception ex) {
			String msg = "An error occured [" + ex.getMessage() + "], please check your log file.";
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
			log.error(">>>>>>>>>>>>VCT>>Result>>Reception>>Form>>>> An error occured : " + ex.getMessage());
			ex.printStackTrace();
		}
		return pi;
	}
	
}
