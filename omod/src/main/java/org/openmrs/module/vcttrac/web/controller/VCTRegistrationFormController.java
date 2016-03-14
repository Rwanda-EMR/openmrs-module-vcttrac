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
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonName;
import org.openmrs.api.DuplicateIdentifierException;
import org.openmrs.api.IdentifierNotUniqueException;
import org.openmrs.api.InsufficientIdentifiersException;
import org.openmrs.api.InvalidIdentifierFormatException;
import org.openmrs.api.PatientIdentifierException;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohtracportal.util.MohTracConfigurationUtil;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.module.vcttrac.util.VCTModuleTag;
import org.openmrs.module.vcttrac.VCTClient;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.VCTTracUtil;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Yves GAKUBA
 */
public class VCTRegistrationFormController extends ParameterizableViewController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(getViewName());
		
		Location loc = new Location();
		mav.addObject("location", loc);
		
		try {
			if (request.getParameter("save") != null) {
				saveVCTClient(request);
				mav.setView(new RedirectView("vctHome.htm"));
			}
			
			mav.addObject("educationLevels", VCTTracUtil.createEducationLevelOptions());
			mav.addObject("mainActivities", VCTTracUtil.createMainActivityOptions());
			mav.addObject("civilStatus", VCTTracUtil.createCivilStatusOptions());
			mav.addObject("locationId", VCTConfigurationUtil.getDefaultLocationId());
			mav.addObject("todayDate", Context.getDateFormat().format(new Date()));
			mav.addObject("nid", request.getParameter("nid"));
			
			if (request.getParameter("codeClient") != null && request.getParameter("save") == null) {
				initRegisteredClientForEdit(request, mav);
			}
			
		}
		catch (Exception ex) {
			String msg = "An error occured [" + ex.getMessage() + "], please check your log file.";
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
			log.error(">>>>>>>>>>>>VCT>>Pre>>Counseling>>Form>>>> An error occured : " + ex.getMessage());
			ex.printStackTrace();
		}
		return mav;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @return
	 */
	private PersonAddress createPersonAddress(HttpServletRequest request, PersonAddress pAddress) {
		PersonAddress pa = (pAddress == null) ? new PersonAddress() : pAddress;
		try {
			if (request.getParameter("country") != null)
				pa.setCountry(request.getParameter("country").trim());
			pa.setStateProvince(request.getParameter("stateProvince").trim());
			pa.setCountyDistrict(request.getParameter("countyDistrict").trim());
			pa.setCityVillage(request.getParameter("cityVillage").trim());
			pa.setNeighborhoodCell(request.getParameter("neighborhoodCell").trim());
			pa.setAddress1(request.getParameter("address1").trim());
			
			if (pAddress == null) {
				pa.setCreator(Context.getAuthenticatedUser());
				pa.setDateCreated(new Date());
			}
		}
		catch (Exception ex) {
			log.error(">>>>VCT>>REGISTRATION>> Fail to create/update person_address : " + ex.getMessage());
			ex.printStackTrace();
			pa = null;
		}
		return pa;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param p
	 */
	private void createPersonAttributes(HttpServletRequest request, Person p) {
		PersonService ps = Context.getPersonService();
		PersonAttribute pa_civilStatus = null, paCs = null, pa_educationLevel = null, paEl = null, pa_mainActivity = null, paMa = null;
		try {
			paCs = p.getAttribute(VCTConfigurationUtil.getCivilStatusAttributeTypeId());
			pa_civilStatus = (paCs==null) ? new PersonAttribute() : paCs;
//			log.info("-----------civilStatus------------");
//			log.info(null == paCs);
//			log.info(paCs);
//			log.info(pa_civilStatus);
//			log.info("-----------------------");
			
			if (request.getParameter("civilStatus").compareToIgnoreCase("0") != 0) {
				if (null == paCs) {
					pa_civilStatus = new PersonAttribute();
					pa_civilStatus.setAttributeType(ps.getPersonAttributeType(VCTConfigurationUtil
					        .getCivilStatusAttributeTypeId()));
					pa_civilStatus.setCreator(Context.getAuthenticatedUser());
					pa_civilStatus.setDateCreated(new Date());
					pa_civilStatus.setValue(request.getParameter("civilStatus"));
					pa_civilStatus.setUuid(UUID.randomUUID().toString());
					
					p.addAttribute(pa_civilStatus);
				} else {
					pa_civilStatus.setValue(request.getParameter("civilStatus"));
					pa_civilStatus.setDateChanged(new Date());
					pa_civilStatus.setChangedBy(Context.getAuthenticatedUser());
				}
				
			}
			
			paEl = p.getAttribute(VCTConfigurationUtil.getEducationLevelAttributeTypeId());
//			pa_educationLevel = (null == paEl) ? new PersonAttribute() : paEl;
//			log.info("----------educationLevel-------------");
//			log.info(null == paEl);
//			log.info(paEl);
//			log.info(pa_educationLevel);
//			log.info("-----------------------");
			
			if (request.getParameter("educationLevel").compareToIgnoreCase("0") != 0) {
				if (paEl==null) {
//					log.info("creating an education level...................................");
					pa_educationLevel=new PersonAttribute();
					pa_educationLevel.setAttributeType(ps.getPersonAttributeType(VCTConfigurationUtil
					        .getEducationLevelAttributeTypeId()));
					pa_educationLevel.setCreator(Context.getAuthenticatedUser());
					pa_educationLevel.setDateCreated(new Date());
					pa_educationLevel.setValue(request.getParameter("educationLevel"));
					pa_educationLevel.setUuid(UUID.randomUUID().toString());
					
					p.addAttribute(pa_educationLevel);
				}else {
//					log.info("Education level modification...................................");
					pa_educationLevel = paEl;
					pa_educationLevel.setValue(request.getParameter("educationLevel"));
					pa_educationLevel.setDateChanged(new Date());
					pa_educationLevel.setChangedBy(Context.getAuthenticatedUser());
				}
			}
			
			paMa = p.getAttribute(VCTConfigurationUtil.getMainActivityAttributeTypeId());
			pa_mainActivity = (null == paMa) ? new PersonAttribute() : paMa;
//			log.info("----------mainActivity-------------");
//			log.info(null == paMa);
//			log.info(paMa == null);
//			log.info(paMa);
//			log.info(pa_mainActivity);
//			log.info("-----------------------");
			
			if (request.getParameter("mainActivity").compareToIgnoreCase("0") != 0) {
				if (paMa == null) {
					pa_mainActivity=new PersonAttribute();
					pa_mainActivity.setAttributeType(ps.getPersonAttributeType(VCTConfigurationUtil
					        .getMainActivityAttributeTypeId()));
					pa_mainActivity.setCreator(Context.getAuthenticatedUser());
					pa_mainActivity.setDateCreated(new Date());
					pa_mainActivity.setValue(request.getParameter("mainActivity"));
					pa_mainActivity.setUuid(UUID.randomUUID().toString());
					
					p.addAttribute(pa_mainActivity);
				}else{
					pa_mainActivity.setValue(request.getParameter("mainActivity"));
					pa_mainActivity.setDateChanged(new Date());
					pa_mainActivity.setChangedBy(Context.getAuthenticatedUser());
				}
			}
		}
		catch (Exception ex) {
			log.error(">>>>VCT>>REGISTRATION>> Fail to create/update person_attributes : " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 */
	private void saveVCTClient(HttpServletRequest request) {
		Person p;
		PersonService ps = Context.getPersonService();
		PersonName pn;
		DateFormat df = Context.getDateFormat();
		VCTClient client = new VCTClient();
		//		boolean cseCaught = false;
		
		try {
			if (request.getParameter("clientId") != null) {
				client = Context.getService(VCTModuleService.class).getClientById(
				    Integer.valueOf(request.getParameter("clientId")));
				client.getClient().getPersonName().setFamilyName(request.getParameter("familyName").trim());
				client.getClient().getPersonName().setGivenName(request.getParameter("givenName").trim());
				client.getClient().getPersonName().setMiddleName(request.getParameter("middleName").trim());
				client.getClient().setGender(request.getParameter("gender"));
				client.getClient().setBirthdate(df.parse(request.getParameter("birthdate")));
				
				//attributes
				createPersonAttributes(request, client.getClient());
				
				//address
				createPersonAddress(request, client.getClient().getPersonAddress());
				
				client.setVctOrPit((request.getParameter("vctOrPit").compareToIgnoreCase("pit") == 0));
				if (request.getParameter("registrationDate") != null
				        && request.getParameter("registrationDate").trim().compareTo("") != 0) {
					client.setDateOfRegistration(df.parse(request.getParameter("registrationDate")));
				}
				
				if (request.getParameter("location") != null && request.getParameter("location").trim().compareTo("") != 0) {
					client.setLocation(Context.getLocationService().getLocation(
					    Integer.valueOf(request.getParameter("location"))));
				}
				
				if (request.getParameter("codeClient") != null
				        && request.getParameter("codeClient").trim().compareTo("") != 0) {
					client.setCodeClient(request.getParameter("codeClient"));
				}
			} else {
				if (request.getParameter("existOrNew").compareTo("0") == 0) {
					p = new Person();
					pn = new PersonName(request.getParameter("givenName").trim(), request.getParameter("middleName").trim(),
					        request.getParameter("familyName").trim().toUpperCase());
					p.getNames().add(pn);
					p.setGender(request.getParameter("gender"));
					p.setBirthdate(df.parse(request.getParameter("birthdate")));
					
					//attributes
					createPersonAttributes(request, p);
					
					//address
					p.addAddress(createPersonAddress(request, null));
					
					//save the person
					ps.savePerson(p);
					
					log.info(">>>>>>>VCT>>Client>>Registration>>Form>>>> Person created successfully !");
				} else
					p = ps.getPerson(Integer.valueOf(request.getParameter("client")));
				
				client.setClient(p);
				client.setVctOrPit((request.getParameter("vctOrPit").compareToIgnoreCase("pit") == 0));
				client.setCounselingObs(null);
				client.setResultObs(null);
				client.setCreatedBy(Context.getAuthenticatedUser());
				
				if (request.getParameter("registrationDate") != null
				        && request.getParameter("registrationDate").trim().compareTo("") != 0) {
					client.setDateOfRegistration(df.parse(request.getParameter("registrationDate")));
				}
				
				if (request.getParameter("registrationDate_A") != null
				        && request.getParameter("registrationDate_A").trim().compareTo("") != 0) {
					client.setDateOfRegistration(df.parse(request.getParameter("registrationDate_A")));
				}
				
				if (request.getParameter("location") != null && request.getParameter("location").trim().compareTo("") != 0) {
					client.setLocation(Context.getLocationService().getLocation(
					    Integer.valueOf(request.getParameter("location"))));
				}
				
				if (request.getParameter("location_A") != null
				        && request.getParameter("location_A").trim().compareTo("") != 0) {
					client.setLocation(Context.getLocationService().getLocation(
					    Integer.valueOf(request.getParameter("location_A"))));
				}
				
				if (request.getParameter("codeClient") != null
				        && request.getParameter("codeClient").trim().compareTo("") != 0) {
					client.setCodeClient(request.getParameter("codeClient"));
				}
				
				if (request.getParameter("codeClient_A") != null
				        && request.getParameter("codeClient_A").trim().compareTo("") != 0) {
					client.setCodeClient(request.getParameter("codeClient_A"));
				}
				
				if (request.getParameter("nid") != null && request.getParameter("nid").trim().compareTo("") != 0) {
					client.setNid(request.getParameter("nid"));
				}
				
				if (request.getParameter("input_nid") != null && request.getParameter("input_nid").trim().compareTo("") != 0) {
					// adding the NID to the client object
					client.setNid(request.getParameter("input_nid"));
					
					// creating NID patientIdentifier for the patient
					PatientIdentifier pi = new PatientIdentifier();
					pi.setCreator(Context.getAuthenticatedUser());
					pi.setDateCreated(new Date());
					pi.setIdentifier(request.getParameter("input_nid"));
					pi.setIdentifierType(Context.getPatientService().getPatientIdentifierType(
					    VCTConfigurationUtil.getNIDIdentifierTypeId()));
					pi.setLocation(Context.getLocationService().getLocation(VCTConfigurationUtil.getDefaultLocationId()));
					
					try {
						Patient patient = Context.getPatientService().getPatient(client.getClient().getPersonId());
						patient.addIdentifier(pi);
						Context.getPatientService().savePatient(patient);
					}
					catch (InvalidIdentifierFormatException iife) {
						log.error(iife);
						request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
						    "PatientIdentifier.error.formatInvalid");
					}
					catch (IdentifierNotUniqueException inue) {
						log.error(inue);
						request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
						    "PatientIdentifier.error.notUnique");
					}
					catch (DuplicateIdentifierException die) {
						log.error(die);
						request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
						    "PatientIdentifier.error.duplicate");
					}
					catch (InsufficientIdentifiersException iie) {
						log.error(iie);
						request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
						    "PatientIdentifier.error.insufficientIdentifiers");
					}
					catch (PatientIdentifierException pie) {
						log.error(pie);
						request.getSession()
						        .setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.general");
					}
				}
				
				client.setDateCreated(new Date());
				log.info(">>>>>>>VCT>>Client>>Registration>>Form>>>> " + client.getDateOfRegistration());
			}
			VCTModuleService vms = Context.getService(VCTModuleService.class);
			vms.saveVCTClient(client);
			log.info(">>>>>>>VCT>>Client>>Registration>>Form>>>> Client created successfully !");
			
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Form.saved");
		}
		catch (ConstraintViolationException cve) {
			//			cseCaught = true;
			String msg = "The CODE CLIENT " + client.getCodeClient() + " is arleady in use.";
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
			cve.printStackTrace();
		}
		catch (Exception ex) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Form.not.saved");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param mav
	 */
	private void initRegisteredClientForEdit(HttpServletRequest request, ModelAndView mav) {
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		VCTClient client = vms.getClientByClientCode(request.getParameter("codeClient"));
		
		mav.addObject("clientId", client.getTracVctClientId());
		
		mav.addObject("registeredClient", client);
		
		mav.addObject("type", (client.isVctOrPit()) ? "pit" : "vct");
		
		if (null != client.getClient().getPersonAddress())
			mav.addObject("location", client.getClient().getPersonAddress());
		else
			mav.addObject("location", new Location());
		
		mav.addObject("locationId", client.getLocation().getLocationId());
		
		mav.addObject("todayDate", Context.getDateFormat().format(client.getDateOfRegistration()));
		
		mav.addObject("currentCivilStatus", VCTModuleTag.personAttribute(client.getClient(), MohTracConfigurationUtil
		        .getCivilStatusAttributeTypeId()));
		mav.addObject("currentEducationLevel", VCTModuleTag.personAttribute(client.getClient(), MohTracConfigurationUtil
		        .getEducationLevelAttributeTypeId()));
		mav.addObject("currentMainActivity", VCTModuleTag.personAttribute(client.getClient(), MohTracConfigurationUtil
		        .getMainActivityAttributeTypeId()));
		
		mav.addObject("nid", client.getNid());
		
		PersonName pn = client.getClient().getPersonName();
		mav.addObject("familyName", pn.getFamilyName());
		mav.addObject("givenName", pn.getGivenName());
		mav.addObject("middleName", pn.getMiddleName());
		
		mav.addObject("gender", client.getClient().getGender().toUpperCase());
		
		mav.addObject("birthdate", Context.getDateFormat().format(client.getClient().getBirthdate()));
		
	}
	
}
