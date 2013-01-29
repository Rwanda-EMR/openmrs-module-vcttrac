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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.vcttrac.VCTPreCounselingInfo;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.module.vcttrac.util.VCTTracUtil;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * @author Yves GAKUBA
 */
public class VCTCounselingFormController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());
		
		try {
			mav.addObject("reasonOfHivTest", VCTTracUtil.createCodedOptions(VCTConfigurationUtil
			        .getWhyDidYouGetTestedConceptId()));
			mav.addObject("programOrderers", VCTTracUtil.createCodedOptions(VCTConfigurationUtil
			        .getProgramThatOrderedTestConceptId()));
			
			VCTPreCounselingInfo pci = new VCTPreCounselingInfo();
			pci.setEncounterDate(request.getParameter("encounterDate"));
			pci.setLocationId(Integer.parseInt(request.getParameter("location")));
			pci.setProviderId(Integer.parseInt(request.getParameter("provider")));
			pci.setCounselingTypeId(Integer.parseInt(request.getParameter("counselingType")));
			
			List<Integer> personIds = new ArrayList<Integer>();
			for (int i = 1; i <= pci.getCounselingTypeId(); i++)
				personIds.add(i);
			
			mav.addObject("personIds", personIds);
			mav.addObject("pci", pci);
			mav.addObject("providerName", Context.getUserService().getUser(pci.getProviderId()).getPersonName());
		}
		catch (Exception ex) {
			String msg = "An error occured [" + ex.getMessage() + "], please check your log file.";
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
			log.error(">>>>>>>>>>>>VCT>>Counseling>>Form>>>> An errer occured : " + ex.getMessage());
			ex.printStackTrace();
		}
		return mav;
	}
	
}
