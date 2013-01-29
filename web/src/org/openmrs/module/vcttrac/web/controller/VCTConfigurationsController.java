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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.module.vcttrac.util.VCTModuleTag;
import org.openmrs.module.vcttrac.util.VCTTracUtil;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * @author Yves GAKUBA
 */
public class VCTConfigurationsController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());
		
		try {
			if (request.getParameter("save") != null && request.getParameter("save") != "true")
				saveVCTConfiguration(request, mav);
			
			mav.addObject("attributes", VCTTracUtil.createAttributesOptions());
			
			mav.addObject("vctConfigured", Context.getAdministrationService().getGlobalPropertyObject("vcttrac.configured"));
			
			mav.addObject("gp_vctProgramConstruct", VCTConfigurationUtil.getVctProgramConstructGlobalPropertie());
			mav.addObject("gp_vpcMembers", VCTConfigurationUtil.getVctProgramConstructMembers());
			
			mav.addObject("gp_hivTestConstruct", VCTConfigurationUtil.getHivTestConstructGlobalPropertie());
			mav.addObject("gp_htcMembers", VCTConfigurationUtil.getHivTestConstructMembers());
			
			mav.addObject("relationShipTypes", Context.getPersonService().getAllRelationshipTypes());
			mav.addObject("relationShipTypeId", VCTConfigurationUtil.getPartnerRelationShipTypeId());
			mav.addObject("gp_relationShipType", Context.getAdministrationService().getGlobalPropertyObject(
			    "vcttrac.relationshiptype.partners"));
		}
		catch (Exception e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "An error occured " + e.getMessage() + ". Check the log file for more details on the error.");
			
			log.error(">>>>>>>VCT>>Configuration>>Controller>> " + e.getMessage());
			e.printStackTrace();
		}
		return mav;
	}
	
	/**
	 * Save the VCT Configurations
	 * 
	 * @param request
	 * @param mav
	 */
	private void saveVCTConfiguration(HttpServletRequest request, ModelAndView mav) {
		AdministrationService as = Context.getAdministrationService();
		
		try {
			if (request.getParameter("config_chkbx") != null) {
				//config_chkbx
				GlobalProperty gpVctConfig = as.getGlobalPropertyObject("vcttrac.configured");
				gpVctConfig.setPropertyValue("" + (request.getParameter("config_chkbx") != null));
				as.saveGlobalProperty(gpVctConfig);
				
				//relationShipType
				GlobalProperty gpRelationShipTypeId = as.getGlobalPropertyObject("vcttrac.relationshiptype.partners");
				gpRelationShipTypeId.setPropertyValue((request.getParameter("relationShipType") != null) ? request
				        .getParameter("relationShipType") : "");
				as.saveGlobalProperty(gpRelationShipTypeId);
				
				//vct program construct & its members
				gpVctConfig = as.getGlobalPropertyObject("vcttrac.vpc.vctProgramConstruct");
				gpVctConfig.setPropertyValue((request.getParameter("vctProgramConstruct") != null) ? ""
				        + request.getParameter("vctProgramConstruct") : "");
				as.saveGlobalProperty(gpVctConfig);
				
				for (GlobalProperty gp : VCTConfigurationUtil.getVctProgramConstructMembers()) {
					gp
					        .setPropertyValue((request.getParameter(VCTModuleTag.globalPropertyParser(gp.getProperty())) != null) ? ""
					                + request.getParameter(VCTModuleTag.globalPropertyParser(gp.getProperty()))
					                : "");
					as.saveGlobalProperty(gp);
				}
				
				//hiv test construct & its members
				gpVctConfig = as.getGlobalPropertyObject("vcttrac.htc.hivTestConstruct");
				gpVctConfig.setPropertyValue((request.getParameter("hivTestConstruct") != null) ? ""
				        + request.getParameter("hivTestConstruct") : "");
				as.saveGlobalProperty(gpVctConfig);
				
				for (GlobalProperty gp : VCTConfigurationUtil.getHivTestConstructMembers()) {
					gp
					        .setPropertyValue((request.getParameter(VCTModuleTag.globalPropertyParser(gp.getProperty())) != null) ? ""
					                + request.getParameter(VCTModuleTag.globalPropertyParser(gp.getProperty()))
					                : "");
					as.saveGlobalProperty(gp);
				}
				
				String msg = getMessageSourceAccessor().getMessage("Form.saved");
				request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
			}
		}
		catch (Exception ex) {
			String msg = getMessageSourceAccessor().getMessage("Form.not.saved");
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
			
			log
			        .error(">>>>>>VCT>>Configuration>>Controller>> An error occured when trying to save the VCT Configurations: \n");
			ex.printStackTrace();
		}
		
	}
	
}
