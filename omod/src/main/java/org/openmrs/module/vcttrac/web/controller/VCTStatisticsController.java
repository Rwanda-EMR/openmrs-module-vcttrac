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
import org.openmrs.api.context.ServiceContext;
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
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Yves GAKUBA
 */


/**
 * mapped to /module/@MODULE_ID@/vctStatisticsForm.jsp
 */
public class VCTStatisticsController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (Context.getAuthenticatedUser() == null)
			return new ModelAndView(new RedirectView(request.getContextPath() + "/login.htm"));
		
		ModelAndView mav = new ModelAndView(getViewName());
		try {
			loadUtils(mav, request);
			manageListing(request, response, mav);
		}catch (Exception e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "An error occured when trying to load this page, please contact your Administrator.");
			log.error(">>>>>>>>VCT>>Statistics>> " + e.getMessage());
			e.printStackTrace();
		}
		
		return mav;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param mav
	 */
	private void loadUtils(ModelAndView mav, HttpServletRequest request) throws Exception {
		
			mav.addObject("elTypeId", VCTConfigurationUtil.getEducationLevelAttributeTypeId());
			mav.addObject("maTypeId", VCTConfigurationUtil.getMainActivityAttributeTypeId());
			mav.addObject("csTypeId", VCTConfigurationUtil.getCivilStatusAttributeTypeId());
			
			mav.addObject("hivTestingDoneConceptId", VCTConfigurationUtil.getHivTestingDoneConceptId());
			mav.addObject("whyGetTestedConceptId", VCTConfigurationUtil.getWhyDidYouGetTestedConceptId());
			mav.addObject("programOrdererConceptId", VCTConfigurationUtil.getProgramThatOrderedTestConceptId());
			mav.addObject("resultOfHivTestConceptId", VCTConfigurationUtil.getResultOfHivTestConceptId());
			mav.addObject("positiveString", Context.getConceptService().getConcept(VCTTracConstant.POSITIVE_CID)
			        .getDisplayString());
			
			mav.addObject("educationLevels", VCTTracUtil.createCodedOptions(VCTTracConstant.EDUCATION_LEVEL));
			mav.addObject("mainActivities", VCTTracUtil.createCodedOptions(VCTTracConstant.MAIN_ACTIVITY));
			mav.addObject("civilStatus", VCTTracUtil.createCivilStatusOptions());
			
			mav.addObject("whyGetTestedConceptOptions", VCTTracUtil.createCodedOptions(VCTConfigurationUtil
			        .getWhyDidYouGetTestedConceptId()));
			mav.addObject("programOrdererConceptOptions", VCTTracUtil.createCodedOptions(VCTConfigurationUtil
			        .getProgramThatOrderedTestConceptId()));
			mav.addObject("resultOfHivTestConceptOptions", VCTTracUtil.createResultOfHivTestOptions());
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param mav
	 */
	private void manageListing(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		VCTModuleService service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
		
		String pageNumber = request.getParameter("page");
		StringBuilder parameters = new StringBuilder();
		parameters.append("");
		StringBuilder searchDescription = new StringBuilder();
		searchDescription.append("");
		int pageSize, pNumber;
		List<VCTClient> clients = new ArrayList<VCTClient>();
		
		List<Integer> res;
		
		List<Integer> numberOfPages;
		
		try {
			
			pNumber = Integer.valueOf(pageNumber).intValue();
			
			if (pageNumber == null || pageNumber.trim().compareToIgnoreCase("") == 0) {
				mav = new ModelAndView();
				mav.setViewName(getViewName());
//				mav.setViewName(getViewName() + "?page=1&dateFrom="+Context.getDateFormat().format(new Date()));
			} else
				mav.setViewName(getViewName());
			
//			mav.addObject("todayDate", Context.getDateFormat().format(new Date()));
			
			if (VCTConfigurationUtil.isConfigured()) {
				pageSize = Integer.valueOf(VCTConfigurationUtil.getNumberOfRecordPerPage());
				
				String dateFrom = (request.getParameter("dateFrom") == null || request.getParameter("dateFrom").trim()
				        .compareTo("") == 0) ? null : request.getParameter("dateFrom").trim();
//				dateFrom=((request.getParameter("dateFrom") == null )?Context.getDateFormat().format(new Date()):dateFrom);
				parameters.append((null != dateFrom) ? "&dateFrom=" + dateFrom : "");
				searchDescription.append((null != dateFrom) ? "registered from " + dateFrom + " " : "");
				
				String dateTo = (request.getParameter("dateTo") == null || request.getParameter("dateTo").trim().compareTo(
				    "") == 0) ? null : request.getParameter("dateTo").trim();
				parameters.append((null != dateTo) ? "&dateTo=" + dateTo : "");
				searchDescription.append((null != dateTo) ? ((null==dateFrom)?"registered until ":" to ") + dateTo+", " : "");
				
				Integer location = (request.getParameter("location") == null || request.getParameter("location").trim()
				        .compareTo("") == 0) ? null : Integer.valueOf(request.getParameter("location"));
				parameters.append((null != location) ? "&location=" + location : "");
				searchDescription.append((null != location) ? "at " + Context.getLocationService().getLocation(location).getName() +", " : "");
				
				String reference = (request.getParameter("reference") == null || request.getParameter("reference").trim()
				        .compareTo("2") == 0) ? null : request.getParameter("reference").trim();
				parameters.append((null != reference) ? "&reference=" + reference : "");
				searchDescription.append((null != reference) ? "referenced in " + ((reference.compareTo("0")==0)?"VCT":"PIT")+", " : "");
				
				String gender = (request.getParameter("gender") == null || request.getParameter("gender").trim().compareTo(
				    "") == 0) ? null : request.getParameter("gender").trim();
				parameters.append((null != gender) ? "&gender=" + gender : "");
				searchDescription.append((null != gender) ? "with gender '" + gender.toUpperCase()+"', " : "");
				
				Integer counselingType = (request.getParameter("counselingType") == null || request.getParameter(
				    "counselingType").trim().compareTo("") == 0) ? null : Integer.valueOf(request
				        .getParameter("counselingType"));
				parameters.append((null != counselingType) ? "&counselingType=" + counselingType : "");
				searchDescription.append((null != counselingType) ? "with counseling type '" + ((counselingType.intValue()==1)?"Individuel":(counselingType.intValue()==2)?"Couple":"Not yet counseled") +"', " : "");
				
				String tested = (request.getParameter("tested") == null || request.getParameter("tested").trim().compareTo(
				    "") == 0) ? null : request.getParameter("tested").trim();
				parameters.append((null != tested) ? "&tested=" + tested : "");
				searchDescription.append((null != tested) ? "tested, " : "");
				
				Integer minAge = (request.getParameter("minAge") == null || request.getParameter("minAge").trim().compareTo(
				    "") == 0) ? null : Integer.valueOf(request.getParameter("minAge"));
				parameters.append((null != minAge) ? "&minAge=" + minAge : "");
				searchDescription.append((null != minAge) ? "with a minimum age of " + minAge : "");
				
				Integer maxAge = (request.getParameter("maxAge") == null || request.getParameter("maxAge").trim().compareTo(
				    "") == 0) ? null : Integer.valueOf(request.getParameter("maxAge").trim());
				parameters.append((null != maxAge) ? "&maxAge=" + maxAge : "");
				searchDescription.append((null != maxAge) ? ((null==minAge)?"with":" and")+" a maximum age of " + maxAge : "");
				
				Integer civilStatus = (request.getParameter("civilStatus") == null || request.getParameter("civilStatus")
				        .trim().compareTo("") == 0) ? null : Integer.valueOf(request.getParameter("civilStatus"));
				parameters.append((null != civilStatus) ? "&civilStatus=" + civilStatus : "");
				searchDescription.append((null != civilStatus) ? " '" + Context.getConceptService().getConcept(civilStatus).getDisplayString()+"', " : "");
				
				Integer educationLevel = (request.getParameter("educationLevel") == null || request.getParameter(
				    "educationLevel").trim().compareTo("") == 0) ? null : Integer.valueOf(request.getParameter(
				    "educationLevel").trim());
				parameters.append((null != educationLevel) ? "&educationLevel=" + educationLevel : "");
				searchDescription.append((null != educationLevel) ? " '" + Context.getConceptService().getConcept(educationLevel).getDisplayString()+"', " : "");
				
				Integer mainActivity = (request.getParameter("mainActivity") == null || request.getParameter("mainActivity")
				        .trim().compareTo("") == 0) ? null : Integer.valueOf(request.getParameter("mainActivity").trim());
				parameters.append((null != mainActivity) ? "&mainActivity=" + mainActivity : "");
				searchDescription.append((null != mainActivity) ? " '" + Context.getConceptService().getConcept(mainActivity).getDisplayString()+"', " : "");
				
				Integer testOrderer = (request.getParameter("program") == null || request.getParameter("program").trim()
				        .compareTo("") == 0) ? null : Integer.valueOf(request.getParameter("program").trim());
				parameters.append((null != testOrderer) ? "&program=" + testOrderer : "");
				searchDescription.append((null != testOrderer) ? "test ordered by '" + Context.getConceptService().getConcept(testOrderer).getDisplayString()+"', " : "");
				
				Integer whyGetTested = (request.getParameter("whyTested") == null || request.getParameter("whyTested")
				        .trim().compareTo("") == 0) ? null : Integer.valueOf(request.getParameter("whyTested").trim());
				parameters.append((null != whyGetTested) ? "&whyGetTested=" + whyGetTested : "");
				searchDescription.append((null != whyGetTested) ? "tested to '" + Context.getConceptService().getConcept(whyGetTested).getDisplayString()+"', " : "");
				
				Integer testResult = (request.getParameter("testResult") == null || request.getParameter("testResult")
				        .trim().compareTo("") == 0) ? null : Integer.valueOf(request.getParameter("testResult").trim());
				parameters.append((null != testResult) ? "&testResult=" + testResult : "");
				searchDescription.append((null != testResult) ? "with result '" + Context.getConceptService().getConcept(testResult).getDisplayString()+"'," : "");
				
				Integer gotResult = (request.getParameter("goResult") == null || request.getParameter("gotResult").trim()
				        .compareTo("") == 0) ? null : Integer.valueOf(request.getParameter("gotResult").trim());
				parameters.append((null != gotResult) ? "&gotResult=" + gotResult : "");
				searchDescription.append((null != gotResult) ? "received result": "");
				
				if (1 == pNumber) {
					res = service.getVCTClientsBasedOn(reference, gender, counselingType, location, tested, dateFrom,
					    dateTo, maxAge, minAge, civilStatus, educationLevel, mainActivity, testOrderer, whyGetTested,
					    testResult, gotResult);
					request.getSession().setAttribute("stat_res", res);
					
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
					request.getSession().setAttribute("stat_numberOfPages", numberOfPages);
					//----------------
					
				} else {
					res = (List<Integer>) request.getSession().getAttribute("stat_res");
					numberOfPages = (List<Integer>) request.getSession().getAttribute("stat_numberOfPages");
					for (int i = (pageSize * (pNumber - 1)); i < pageSize * pNumber; i++) {
						if (i >= res.size())
							break;
						else
							clients.add(service.getClientById(res.get(i)));
					}
				}
				
				if (pNumber > 1)
					mav.addObject("prevPage", pNumber - 1);
				else
					mav.addObject("prevPage", -1);
				if (pNumber < numberOfPages.size())
					mav.addObject("nextPage", pNumber + 1);
				else
					mav.addObject("nextPage", -1);
				mav.addObject("lastPage", ((numberOfPages.size() >= 1) ? numberOfPages.size() : 1));
				
				String title = "";
				
				title = VCTTracUtil.getMessage("vcttrac.statistic.allclient", null);
				mav.addObject("title", title);
				String searchDescr=(searchDescription.toString().compareTo("")!=0)?"Client "+searchDescription.toString():"";
				mav.addObject("searchResultDescription", searchDescr);
				
				//page infos
				Object[] pagerInfos = new Object[3];
				pagerInfos[0] = (res.size() == 0) ? 0 : (pageSize * (pNumber - 1)) + 1;
				pagerInfos[1] = (pageSize * pNumber <= res.size()) ? pageSize * pNumber : res.size();
				pagerInfos[2] = res.size();
				
				mav.addObject("numberOfPages", numberOfPages);
				mav.addObject("clients", clients);
				mav.addObject("parameters", parameters.toString());
				mav.addObject("pageSize", pageSize);
				mav.addObject("pageInfos", MohTracUtil.getMessage("vcttrac.pagingInfo.showingResults", pagerInfos));
				
				FileExporter fexp = new FileExporter();
				
				if (request.getParameter("export") != null && request.getParameter("export").compareToIgnoreCase("csv") == 0) {
					List<VCTClient> clientList = new ArrayList<VCTClient>();
					for (Integer clientId : res) {
						clientList.add(service.getClientById(clientId));
					}
					fexp.exportToCSVFile(request, response, clientList, "list_of_clients_in_vct_program_" + title.trim().toLowerCase().replace(" ", "_") + ".csv",
					    title.trim().toUpperCase(),searchDescr);
				}
			}
			
		}
		catch (Exception e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "An error occured when trying to load data from database. \nView error in log file.");
			
			log.error(">>>>>>>>>>>>VCT>>Statistics>>>> An error occured : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
