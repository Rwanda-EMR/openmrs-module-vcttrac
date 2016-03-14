package org.openmrs.module.vcttrac.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohtracportal.util.ContextProvider;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.module.vcttrac.util.VCTTracUtil;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

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

/**
 * @author Yves GAKUBA
 */
public class VCTPreRegistrationCheckupController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(VCTPreRegistrationCheckupController.class);
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//check if the user is logged in
		if (Context.getAuthenticatedUser() == null) {
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, ContextProvider.getMessage("require.login"));
			return new ModelAndView(new RedirectView(request.getContextPath() + "/login.htm"));
		}
		
		ModelAndView mav = new ModelAndView();
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		
		//initially the client doesn't exists in the database
		boolean found = false;
		
		//if the type of counseling has not been specified, redirect to home page to start over
		if (request.getParameter("type") == null) {
			mav.setView(new RedirectView("vctHome.htm"));
			log
			        .info(">>>VCT>>PRE>>REGISTRATION>>CHECKUP>>CONTROLLER>> Type of counseling has not been specified, redirect to home page...");
			return mav;
		} else
			mav.setViewName(getViewName());
		
		//load utilities
		loadUtils(mav);
		
		if (request.getParameter("type") != null) {
			Integer pId = null;//patientId
			
			//attempting to find client using nid
			if (request.getParameter("nid") != null && request.getParameter("nid").trim().compareTo("") != 0) {
				
				Integer cId = vms.getPersonIdByNID(request.getParameter("nid"));//clientId
				
				//if the client is found, change found = true and set the pId to the one corresponding to the person
				if (cId != null) {
					found = true;
					pId = cId;
				}
								
				//redict the view accordingly
				if (request.getParameter("type").trim().compareToIgnoreCase("vct") == 0)
					mav.setView(new RedirectView("vctRegistration.form?type=vct&select="
					        + (((found)) ? ("choose&clientId=" + pId) : "new")));
				else
					mav.setView(new RedirectView("vctRegistration.form?type=pit&select="
					        + (((found)) ? ("choose&clientId=" + pId) : "new")));
				
				//pack the nid in the model object
				mav.addObject("nid", request.getParameter("nid"));
			} else if (request.getParameter("idType") != null && request.getParameter("idType").trim().compareTo("") != 0) {
				
				List<PatientIdentifierType> identifierTypes = new ArrayList<PatientIdentifierType>();
				identifierTypes.add(Context.getPatientService().getPatientIdentifierType(
				    Integer.valueOf(request.getParameter("idType"))));
				
				List<Location> identifierLocs = new ArrayList<Location>();
				identifierLocs.add(Context.getLocationService().getLocation(
				    Integer.valueOf(request.getParameter("identifierLocation"))));
				
				List<PatientIdentifier> piList = Context.getPatientService().getPatientIdentifiers(
				    request.getParameter("ptIdentifier"), identifierTypes, identifierLocs, null, null);
				
				if (piList != null && piList.size() > 0) {
					
					found = true;
					pId = piList.get(0).getPatient().getPersonId();
					
					// check for an existing NID for this client
					identifierTypes = new ArrayList<PatientIdentifierType>();
					identifierTypes.add(Context.getPatientService().getPatientIdentifierType(
					    VCTConfigurationUtil.getNIDIdentifierTypeId()));
					
					List<Patient> patients = new ArrayList<Patient>();
					patients.add(piList.get(0).getPatient());
					
					piList = Context.getPatientService().getPatientIdentifiers(null, identifierTypes, null, patients, null);
					
					if (piList != null && piList.size() > 0) {
						found = true;
						mav.addObject("nid", piList.get(0).getIdentifier());
					} else
						mav.addObject("nid", request.getParameter("nid"));
					
				}
				
				//redict the view accordingly
				if (request.getParameter("type").trim().compareToIgnoreCase("vct") == 0)
					mav.setView(new RedirectView("vctRegistration.form?type=vct&select="
					        + (((found)) ? ("choose&clientId=" + pId) : "new")));
				else
					mav.setView(new RedirectView("vctRegistration.form?type=pit&select="
					        + (((found)) ? ("choose&clientId=" + pId) : "new")));
			}
		}
		return mav;
	}
	
	/**
	 * Loads utils, a map of patient_identifier_type, a list of locations and set the NID
	 * patient_identifier_type_id
	 * 
	 * @param mav Model object
	 * @throws Exception
	 */
	private void loadUtils(ModelAndView mav) throws Exception {
		mav.addObject("patientIdentifierTypes", VCTTracUtil.createPatientIdentifierTypesOptions());
		mav.addObject("locations", VCTTracUtil.createLocationOptions());
		mav.addObject("nationalIdType", VCTConfigurationUtil.getNIDIdentifierTypeId());
		mav.addObject("defaultLocationId", VCTConfigurationUtil.getDefaultLocationId());
	}
	
}
