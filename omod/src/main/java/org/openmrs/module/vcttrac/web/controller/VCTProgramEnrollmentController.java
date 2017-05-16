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
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());

		mav.addObject("locations", VCTTracUtil.createLocationOptions());
		mav.addObject("defaultLocationId", VCTConfigurationUtil.getDefaultLocationId());
		mav.addObject("providers", VCTTracUtil.createProviderOptions());
		mav.addObject("defaultProviderId", ((Context.getAuthenticatedUser().hasRole("Provider"))
				? Context.getAuthenticatedUser().getUserId() : ""));
		mav.addObject("resultOfHivTestId", VCTTracConstant.RESULT_OF_HIV_TEST);
		mav.addObject("positiveString",
				Context.getConceptService().getConcept(VCTTracConstant.POSITIVE_CID).getDisplayString());

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
	@SuppressWarnings("unchecked")
	private void manageListing(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		VCTModuleService service = Context.getService(VCTModuleService.class);

		String pageNumber = (request.getParameter("page") != null) ? request.getParameter("page") : "1";
		int pageSize;// , type;
		List<VCTClient> clients = new ArrayList<VCTClient>();

		List<Integer> res;

		List<Integer> numberOfPages;

		try {
			if (VCTConfigurationUtil.isConfigured()) {

				pageSize = Integer.valueOf(VCTConfigurationUtil.getNumberOfRecordPerPage());
				if (pageNumber.compareToIgnoreCase("1") == 0 || pageNumber.compareToIgnoreCase("") == 0) {
					res = new ArrayList<Integer>();
					res = service.getVCTClientsWaitingToBeEnrolledInHIVProgram();
					request.getSession().setAttribute("prg_enrollment_res", res);

					// data collection
					for (int i = 0; i < pageSize; i++) {
						if (res.size() == 0)
							break;
						if (i >= res.size() - 1) {
							clients.add(service.getClientById(res.get(i)));
							break;
						} else
							clients.add(service.getClientById(res.get(i)));
					}

					// ---------paging-------navigation between pages--------
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
					// ----------------

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

				// page infos
				Object[] pagerInfos = new Object[3];
				pagerInfos[0] = (res.size() == 0) ? 0 : (pageSize * (Integer.parseInt(pageNumber) - 1)) + 1;
				pagerInfos[1] = (pageSize * (Integer.parseInt(pageNumber)) <= res.size())
						? pageSize * (Integer.parseInt(pageNumber)) : res.size();
				pagerInfos[2] = res.size();

				ApplicationContext appContext = ContextProvider.getApplicationContext();

				mav.addObject("numberOfPages", numberOfPages);
				mav.addObject("clients", clients);
				// mav.addObject("parameters", parameters.toString());
				mav.addObject("pageSize", pageSize);
				mav.addObject("pageInfos",
						appContext.getMessage("vcttrac.pagingInfo.showingResults", pagerInfos, Context.getLocale()));

				FileExporter fexp = new FileExporter();

				if (request.getParameter("export") != null
						&& request.getParameter("export").compareToIgnoreCase("csv") == 0) {
					List<VCTClient> clientList = new ArrayList<VCTClient>();
					for (Integer clientId : res) {
						clientList.add(service.getClientById(clientId));
					}
					fexp.exportToCSVFile(request, response, clientList,
							"list_of_clients_in_vct_program_" + title + ".csv",
							VCTTracUtil.getMessage("vcttrac.statistic.clientlist", null) + " : " + title.toUpperCase(),
							"Client waiting to be enrolled in Program");
				}
			}

		} catch (Exception e) {
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
	private boolean enrollPatientInHIVProgram(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mav) {
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
			location = Context.getLocationService()
					.getLocation(Integer.valueOf(request.getParameter("encounterLocation")));
			encounterDate = df.parse(request.getParameter("enrollmentDate"));
			provider = Context.getUserService().getUser(Integer.valueOf(request.getParameter("provider")));

			vms = Context.getService(VCTModuleService.class);
			client = vms.getClientByCodeTest(request.getParameter("code"));

			// if (request.getParameter("clientDecision").compareTo("1") == 0) {
			Person person = client.getClient();

			if (!person.isPatient() && request.getParameter("hivStatus").compareToIgnoreCase("1") == 0) {
				p = new Patient();
				// person name
				PersonName pn = new PersonName(person.getPersonName().getGivenName(),
						person.getPersonName().getMiddleName(), person.getPersonName().getFamilyName());

				// adding person's name, birtdate, gender and creator
				p.getNames().add(pn);
				p.setBirthdate(person.getBirthdate());
				p.setGender(person.getGender());// string
				p.setCreator(Context.getAuthenticatedUser());

				// creating a patientProgramHiv
				Program programHiv = Context.getProgramWorkflowService()
						.getProgram(VCTConfigurationUtil.getHivProgramId());
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
							PatientIdentifier identifier = createPatientIdentifier(request, index);
							if (null != identifier) {
								p.addIdentifier(identifier);
								log.info(
										">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Identifier of type "
												+ Context.getPatientService()
														.getPatientIdentifierType(Integer.valueOf(
																request.getParameter("identifierType_" + index)))
														.getName()
												+ " for Patient#" + p.getNames() + " created succesfully.");
							} else
								return false;

							// }
						} else
							cont = false;
						index += 1;
					}
					log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Identifier creation for Patient#"
							+ p.getNames() + " succeded");
				}

				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to save Patient#" + p.getNames()
						+ "...");
				patientService.savePatient(p);
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Patient#" + p.getPatientId()
						+ " saved succesfully");

				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to enroll Patient#" + p.getNames()
						+ " to " + programHiv.getName() + "...");
				patientProgram_Hiv.setPatient(p);
				patientProgram_Hiv.setProgram(programHiv);
				patientProgram_Hiv.setDateEnrolled(encounterDate);
				patientProgram_Hiv.setCreator(Context.getAuthenticatedUser());
				patientProgram_Hiv.setDateCreated(new Date());
				programWorkFlowService.savePatientProgram(patientProgram_Hiv);
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Enrollement finished successfully.");

				if (request.getParameter("enroll_in_pmtct") != null) {
					log.info(">>>>>VCT>>PMTCT>>Program>>Patient>>Enrollment>>>> Trying to enroll Patient#"
							+ p.getNames() + " to " + programHiv.getName() + "...");
					Program programPmtct = Context.getProgramWorkflowService()
							.getProgram(VCTConfigurationUtil.getPmtctProgramId());
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
				log.info(
						">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Synchronization of patients finished successfully.");

			}

			// add the NID identifier in case he doesn't have one
			PatientIdentifier pi = VCTTracUtil.getPatientIdentifier(person.getPersonId(),
					vms.getClientAtLastVisitByClientId(person.getPersonId()).getNid(),
					VCTConfigurationUtil.getNIDIdentifierTypeId(), null);

			if (null == pi) {
				Patient p1 = Context.getPatientService().getPatient(person.getPersonId());
				pi = new PatientIdentifier();
				// pi.setPatient(p1);
				pi.setDateCreated(new Date());
				pi.setCreator(Context.getAuthenticatedUser());
				pi.setLocation(client.getLocation());
				pi.setIdentifier(client.getNid());
				pi.setIdentifierType(Context.getPatientService()
						.getPatientIdentifierType(VCTConfigurationUtil.getNIDIdentifierTypeId()));

				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to save PatientIdentifier, type="
						+ Context.getPatientService()
								.getPatientIdentifierType(VCTConfigurationUtil.getNIDIdentifierTypeId()).getName()
						+ " for Patient#" + p1.getPersonId() + "...");
				// try {
				p1.addIdentifier(pi);
				Context.getPatientService().savePatient(p1);
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> PatientIdentifier saved successfully.");
				// }
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

				Obs modeOfAdmission = new Obs();
				modeOfAdmission
						.setConcept(Context.getConceptService().getConcept(VCTTracConstant.METHOD_OF_ENROLLMENT));
				modeOfAdmission.setValueCoded(Context.getConceptService().getConcept(VCTTracConstant.VCT_PROGRAM));
				modeOfAdmission.setDateCreated(new Date());
				modeOfAdmission.setObsDatetime(encounterDate);
				modeOfAdmission.setCreator(Context.getAuthenticatedUser());
				modeOfAdmission.setPerson(person);
				modeOfAdmission.setLocation(location);

				obsList.add(modeOfAdmission);

				Encounter enc = VCTTracUtil.createEncounter(encounterDate, provider, location,
						Context.getPatientService().getPatient(person.getPersonId()),
						Context.getEncounterService().getEncounterType(VCTTracConstant.ADULT_INITIAL_ENCOUNTER_TYPE),
						obsList);
				log.info(
						">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to save the first encounter for Patient#"
								+ person.getPersonId() + "...");
				Context.getEncounterService().saveEncounter(enc);
				log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Encounter saved successfully.");
			}

			// archive the client
			archiveClient(person.getPersonId());

			// String msg = getMessageSourceAccessor().getMessage();
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Form.saved");
		} catch (InvalidIdentifierFormatException iife) {
			log.error(iife);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.formatInvalid");
		} catch (IdentifierNotUniqueException inue) {
			log.error(inue);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.notUnique");
		} catch (DuplicateIdentifierException die) {
			log.error(die);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.duplicate");
		} catch (InsufficientIdentifiersException iie) {
			log.error(iie);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
					"PatientIdentifier.error.insufficientIdentifiers");
		} catch (PatientIdentifierException pie) {
			log.error(pie);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.general");
		} catch (Exception e) {
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
		// try {
		VCTClient cl = vms.getClientAtLastVisitByClientId(clientId);
		cl.setArchived(true);
		cl.setClient(Context.getPersonService().getPerson(clientId));
		log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Trying to archive Client#" + cl.getTracVctClientId()
				+ "..." + cl.getCodeClient() + "..." + cl.getClient().getPersonId());
		vms.saveVCTClient(cl);
		log.info(">>>>>VCT>>HIV>>Program>>Patient>>Enrollment>>>> Client Archived successfully.");
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
			pi.setIdentifierType(Context.getPatientService()
					.getPatientIdentifierType(Integer.valueOf(request.getParameter("identifierType_" + index))));
			pi.setIdentifier(request.getParameter("identifier_" + index));
			pi.setLocation(Context.getLocationService()
					.getLocation(Integer.valueOf(request.getParameter("identifierLocation_" + index))));

			if (Context.getPatientService().isIdentifierInUseByAnotherPatient(pi)) {
				request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Identifier ["
						+ request.getParameter("identifier_" + index) + "] is in use with someone else !");
				return null;
			}
		} catch (Exception ex) {
			String msg = "An error occured [" + ex.getMessage() + "], please check your log file.";
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
			log.error(">>>>>>>>>>>>VCT>>Result>>Reception>>Form>>>> An error occured : " + ex.getMessage());
			ex.printStackTrace();
		}
		return pi;
	}

}
