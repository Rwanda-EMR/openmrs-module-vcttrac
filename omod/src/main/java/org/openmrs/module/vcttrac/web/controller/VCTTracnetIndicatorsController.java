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
import org.openmrs.api.context.Context;
import org.openmrs.module.vcttrac.util.FileExporter;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class VCTTracnetIndicatorsController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(VCTTracnetIndicatorsController.class);
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());
		
		mapParameters(request, response, mav);
		
		if (request.getParameter("exportFormat") != null
		        && request.getParameter("exportFormat").trim().toLowerCase().compareTo("csv") == 0) {
			FileExporter fe = new FileExporter();
			
			fe.exportToCSVTracNetIndicators(request, response);
		}
		
		return mav;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param mav
	 */
	private void mapParameters(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		DateFormat df = Context.getDateFormat();
		try {
			Date today = new Date();
			if (request.getParameter("location") == null || request.getParameter("location").compareTo("") == 0)
				mav.addObject("defaultLoc", VCTConfigurationUtil.getDefaultLocationId());
			else
				mav.addObject("defaultLoc", request.getParameter("location"));
			
			if (request.getParameter("dateFrom") == null || request.getParameter("dateFrom").compareTo("") == 0)
				mav.addObject("from", df.format(new Date(today.getYear(),today.getMonth(),1)));
			else
				mav.addObject("from", request.getParameter("dateFrom"));
			
			if (request.getParameter("dateTo") == null || request.getParameter("dateTo").compareTo("") == 0)
				mav.addObject("to", df.format(today));
			else
				mav.addObject("to", request.getParameter("dateTo"));
			
//			VCTModuleService vms=Context.getService(VCTModuleService.class);
//			vms.getCouplesCounseledAndTested(request.getParameter("dateFrom"), request.getParameter("dateTo"), VCTConfigurationUtil.getDefaultLocationId());
			
		}
		catch (Exception e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    getMessageSourceAccessor().getMessage("@MODULE_ID@.error.loadingData"));
			log.error(">>>>>>VCT>>TRACNET>>INDICATORS>> " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
