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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ServiceContext;
import org.openmrs.module.vcttrac.VCTClient;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * @author Yves GAKUBA
 */
public class VCTClientDashboardFormController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());
		Person p = null;
		
		try {
			VCTModuleService service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
			List<VCTClient> clientVisits = service.getClientVisitByPersonId(Integer
			        .valueOf(request.getParameter("clientId")));
			mav.addObject("clientVisits", clientVisits);
			mav.addObject("clientNID", clientVisits.get(0).getNid());
			mav.addObject("NID_title", Context.getPatientService().getPatientIdentifierType(VCTConfigurationUtil.getNIDIdentifierTypeId()).getName());
			mav.addObject("nidIdentifierTypeId", VCTConfigurationUtil.getNIDIdentifierTypeId());
			p = Context.getPersonService().getPerson(Integer.valueOf(request.getParameter("clientId")));
			mav.addObject("client", p);
			mav.addObject("isAPatient", p.isPatient());
			if (p.isPatient()) {
				mav.addObject("clientIdentifiers", Context.getPatientService().getPatient(p.getPersonId())
				        .getActiveIdentifiers());
			}
			
			mav.addObject("civilStatusAttibuteTypeId", VCTConfigurationUtil.getCivilStatusAttributeTypeId());
			mav.addObject("educationLevelAttibuteTypeId", VCTConfigurationUtil.getEducationLevelAttributeTypeId());
			mav.addObject("mainActivityAttibuteTypeId", VCTConfigurationUtil.getMainActivityAttributeTypeId());
			mav.addObject("dateResultOfHivTestReceived", VCTConfigurationUtil.getDateResultOfHivTestReceivedConceptId());
		}
		catch (Exception e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, e.getMessage());
			log.error(">>>>>>>>>>>>>>>VCT>>Dashboard>>>>>>>> An error occured : " + e.getMessage());
			e.printStackTrace();
		}
		return mav;
	}
	
}
