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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohtracportal.util.MohTracUtil;
import org.openmrs.module.vcttrac.VCTClient;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.FileExporter;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.module.vcttrac.util.VCTTracConstant;
import org.openmrs.module.vcttrac.util.VCTTracUtil;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class VCTClientListingController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		List<VCTClient> clientList=new ArrayList<VCTClient>();
		FileExporter fe=new FileExporter();
		String title="";
		
		mav.addObject("civilStatusAttributeTypeId", VCTConfigurationUtil.getCivilStatusAttributeTypeId());
		mav.addObject("mainActivityAttributeTypeId", VCTConfigurationUtil.getMainActivityAttributeTypeId());
		mav.addObject("educationLevelAttributeTypeId", VCTConfigurationUtil.getEducationLevelAttributeTypeId());
		mav.addObject("whyDidYouGetTestedConceptId", VCTConfigurationUtil.getWhyDidYouGetTestedConceptId());
		mav.addObject("dateResultOfHivTestedReceivedConceptId", VCTConfigurationUtil
		        .getDateResultOfHivTestReceivedConceptId());
		mav.addObject("resultOfHivTestConceptId", VCTConfigurationUtil.getResultOfHivTestConceptId());
		mav.addObject("positive", Context.getConceptService().getConcept(VCTTracConstant.POSITIVE_CID).getDisplayString());
		
		try {
			if (request.getParameter("type") != null && request.getParameter("type").trim().compareTo("") != 0) {
				String from = request.getParameter("dateFrom");
				String to = request.getParameter("dateTo");
				Integer admissionMode = Integer.valueOf(request.getParameter("type"));
				Integer locationId = Integer.valueOf(request.getParameter("location"));
				if (request.getParameter("type").trim().compareTo("2") != 0) {
					Integer minAge = Integer.valueOf(((request.getParameter("minAge") == null) ? 0 + "" : request
					        .getParameter("minAge")));
					Integer maxAge = Integer.valueOf(((request.getParameter("maxAge") == null) ? 0 + "" : request
					        .getParameter("maxAge")));
					String gender = request.getParameter("gender");
					
					if (request.getParameter("criteria").trim().compareToIgnoreCase("cct") == 0) {
						clientList=vms.getNewClientsCounseledAndTestedForHIV(from, to, locationId,
						    admissionMode, minAge, maxAge, gender);
						title=VCTTracUtil.getMessage("vcttrac.param.indicator.cct", null);
						mav.addObject("clients", clientList);
					} else if (request.getParameter("criteria").trim().compareToIgnoreCase("ctrr") == 0) {
						clientList=vms.getNewClientsTestedAndReceivedResults(from, to, locationId,
						    admissionMode, minAge, maxAge, gender);
						title=VCTTracUtil.getMessage("vcttrac.param.indicator.ctrr", null);
						mav.addObject("clients", clientList);
					} else if (request.getParameter("criteria").trim().compareToIgnoreCase("hivpositive") == 0) {
						clientList=vms.getHIVPositiveClients(from, to, locationId, admissionMode, minAge,
						    maxAge, gender);
						title=VCTTracUtil.getMessage("vcttrac.param.indicator.hivpositive", null);
						mav.addObject("clients", clientList);
					}
				} else if (request.getParameter("type").trim().compareTo("2") == 0) {
//					if (request.getParameter("criteria").trim().compareToIgnoreCase("cct") == 0) {
//						clientList=vms.getCouplesCounseledAndTested(from, to, locationId);
//						title=VCTTracUtil.getMessage("vcttrac.param.indicator.dc", null);
//						mav.addObject("clients", clientList);
//					} else if (request.getParameter("criteria").trim().compareToIgnoreCase("dc") == 0) {
//						clientList=vms.getDiscordantCouples(from, to, locationId);
//						title=VCTTracUtil.getMessage("vcttrac.param.indicator.dc", null);
//						mav.addObject("clients", clientList);
//					}
				}
			}
			
			if(request.getParameter("exportFormat")!=null && request.getParameter("exportFormat").trim().toLowerCase().compareTo("csv")==0){
				fe.exportToCSVFile(request, response, clientList, title.trim().toLowerCase().replace(" ", "_")+".csv", ((request.getParameter("type").trim().compareTo("1") == 0) ? VCTTracUtil.getMessage("vcttrac.pit",
				    null) : VCTTracUtil.getMessage("vcttrac.vct", null))+" : "+title, "");
			}
			
			buildRequestParameters(request, mav);
		}
		catch (Exception e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    getMessageSourceAccessor().getMessage("@MODULE_ID@.error.loadingData"));
			log.error(">>>>>>VCT>>TRACNET>>INDICATORS>> " + e.getMessage());
			e.printStackTrace();
		}
		return mav;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param mav
	 */
	private void buildRequestParameters(HttpServletRequest request, ModelAndView mav) {
		String parameters = "", parameterDescription = "", title = "";
		
		try {
			if (request.getParameter("dateFrom") != null && request.getParameter("dateFrom").trim().compareTo("") != 0) {
				parameters += "&dateFrom=" + request.getParameter("dateFrom");
				parameterDescription += MohTracUtil.getMessage("vcttrac.param.date.start", null)
				        + " : "
				        + (new SimpleDateFormat("dd-MMM-yyyy").format(Context.getDateFormat().parse(
				            request.getParameter("dateFrom")))) + "; ";
			}
			if (request.getParameter("dateTo") != null && request.getParameter("dateTo").trim().compareTo("") != 0) {
				parameters += "&dateTo=" + request.getParameter("dateTo");
				parameterDescription += MohTracUtil.getMessage("vcttrac.param.date.end", null)
				        + " : "
				        + (new SimpleDateFormat("dd-MMM-yyyy").format(Context.getDateFormat().parse(
				            request.getParameter("dateTo")))) + "; ";
			}
			if (request.getParameter("location") != null && request.getParameter("location").trim().compareTo("") != 0) {
				parameters += "&location=" + request.getParameter("location");
				parameterDescription += MohTracUtil.getMessage("vcttrac.param.location", null) + " : "
				        + Context.getLocationService().getLocation(Integer.valueOf(request.getParameter("location"))) + "; ";
			}
			if (request.getParameter("minAge") != null && request.getParameter("minAge").trim().compareTo("") != 0) {
				parameters += "&minAge=" + request.getParameter("minAge");
				parameterDescription += MohTracUtil.getMessage("vcttrac.param.age.min", null) + " : "
				        + request.getParameter("minAge") + "; ";
			}
			if (request.getParameter("maxAge") != null && request.getParameter("maxAge").trim().compareTo("") != 0) {
				parameters += "&maxAge=" + request.getParameter("maxAge");
				parameterDescription += MohTracUtil.getMessage("vcttrac.param.age.max", null) + " : "
				        + request.getParameter("maxAge") + "; ";
			}
			if (request.getParameter("gender") != null && request.getParameter("gender").trim().compareTo("") != 0) {
				parameters += "&gender=" + request.getParameter("gender");
				parameterDescription += MohTracUtil.getMessage("vcttrac.param.gender", null) + " : "
				        + request.getParameter("gender").toUpperCase() + "; ";
			}
			if (request.getParameter("type") != null && request.getParameter("type").trim().compareTo("") != 0) {
				parameters += "&type=" + request.getParameter("type");
				title += (request.getParameter("type").trim().compareTo("1") == 0) ? MohTracUtil.getMessage("vcttrac.pit",
				    null) : MohTracUtil.getMessage("vcttrac.vct", null);
				title += " : ";
			}
			if (request.getParameter("criteria") != null && request.getParameter("criteria").trim().compareTo("") != 0) {
				parameters += "&criteria=" + request.getParameter("criteria");
				//				parameterDescription += MohTracUtil.getMessage("vcttrac.param.indicator", null)
				//		        + " : ";
				title += (request.getParameter("criteria").trim().compareToIgnoreCase("cct") == 0) ? MohTracUtil.getMessage(
				    "vcttrac.param.indicator.cct", null) : "";
				title += (request.getParameter("criteria").trim().compareToIgnoreCase("ctrr") == 0) ? MohTracUtil
				        .getMessage("vcttrac.param.indicator.ctrr", null) : "";
				title += (request.getParameter("criteria").trim().compareToIgnoreCase("hivpositive") == 0) ? MohTracUtil
				        .getMessage("vcttrac.param.indicator.hivpositive", null) : "";
				title += (request.getParameter("criteria").trim().compareToIgnoreCase("dc") == 0) ? MohTracUtil.getMessage(
				    "vcttrac.param.indicator.dc", null) : "";
				//				parameterDescription+="; ";
			}
		}
		catch (Exception e) {

		}
		mav.addObject("parameters", parameters);
		mav.addObject("parameterDescription", parameterDescription);
		mav.addObject("title", title);
		
	}
	
}
