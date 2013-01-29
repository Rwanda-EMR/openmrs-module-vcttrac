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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ServiceContext;
import org.openmrs.module.vcttrac.VCTClient;
import org.openmrs.module.vcttrac.VCTClientResult;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.module.vcttrac.util.VCTTracUtil;
import org.openmrs.propertyeditor.LocationEditor;
import org.openmrs.propertyeditor.UserEditor;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.WebConstants;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Yves GAKUBA
 */
public class VCTAutoCompleteListController extends SimpleFormController {
	
	private Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest,
	 *      org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Location.class, new LocationEditor());
		binder.registerCustomEditor(User.class, new UserEditor());
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(OpenmrsUtil.getDateFormat(), true));
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		VCTModuleService service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
		VCTClient client = null;
		VCTClientResult result = null;
		
		if (request.getParameter("testCode") != null) {
			client = service.getClientByCodeTest(request.getParameter("testCode"));
			result = getClientResultFromVCTClientObject(client, request);
		} else {
			result = new VCTClientResult();
			result.setLocation(Context.getLocationService().getLocation(
			    Integer.valueOf(VCTConfigurationUtil.getDefaultLocationId())));
			result.setDateOfResult(new Date());
			if (request.getParameter("tcode") != null)
				result.setCodeTest(request.getParameter("tcode"));
		}		
		
		request.setAttribute("hivTestResultObsId", result.getHivTestResultObsId());
		result.setClient(client);
		
		return result;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param client
	 * @return
	 */
	private VCTClientResult getClientResultFromVCTClientObject(VCTClient client,HttpServletRequest request) throws Exception{
		VCTClientResult result = null;
		if (null == client)
			result = new VCTClientResult();
		else {
			result = new VCTClientResult();
			result.setCodeTest(client.getCodeTest());
			if (client.getResultObs() != null) {
				Obs ob = client.getResultObs();
				for (Obs o : ob.getGroupMembers()) {
					if (o.getConcept().getConceptId() == VCTConfigurationUtil.getResultOfHivTestConceptId()) {
						result.setHivTestResult(o.getValueCoded().getConceptId());
						result.setCreatedBy(o.getCreator());
						result.setDateCreated(o.getDateCreated());
						result.setDateOfResult(o.getObsDatetime());
						result.setLocation(o.getLocation());
						result.setHivTestResultObsId(o.getObsId());
					} else if (o.getConcept().getConceptId() == VCTConfigurationUtil.getHivTestDateConceptId()) {
						result.setHivTestDateObsId(o.getObsId());
					} 
//					else if(o.getConcept().getConceptId() == VCTConfigurationUtil.getDateResultOfHivTestReceivedConceptId()) {
//						request.setAttribute("resultReceived", true);
//					}
				}
			}else{
				result.setLocation(Context.getLocationService().getLocation(
				    Integer.valueOf(VCTConfigurationUtil.getDefaultLocationId())));
//				result.setDateOfResult(new Date());
				result.setDateOfResult(client.getDateOfRegistration());
			}
		}
		
		return result;
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest,
	 *      java.lang.Object, org.springframework.validation.Errors)
	 */
	protected Map<String, Object> referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		VCTModuleService service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
		
		try {
			map.put("resultOfHivTestOptions", VCTTracUtil.createResultOfHivTestOptions());
			map.put("clientCodes", service.getAllClientCodeWithoutHivTestResult());
			
		}
		catch (Exception ex) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "An error occured when trying to load data. Find the error in the log file.");
			
			ex.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
	                                BindException errors) throws Exception {
		ModelAndView mav = new ModelAndView(new RedirectView(request.getContextPath() + getSuccessView()));
		
		if (request.getParameter("edit") == null)
			saveClientHivTestResultObs(request, command);
		else {
			mav = editTestCode(request);
		}
		
		return mav;
	}
	
	private ModelAndView editTestCode(HttpServletRequest request) {
		VCTModuleService service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
		//		if(request.getParameter("edit")!=null){
		VCTClient client = service.getClientByCodeTest(request.getParameter("testCode"));
		client.setCodeTest(request.getParameter("editedTestCode"));
		service.saveVCTClient(client);
		//			client = null;
		//			client = service.getClientByCodeTest(request.getParameter("editedTestCode"));
		//		}
		
		ModelAndView mav = new ModelAndView(new RedirectView(request.getContextPath() + getSuccessView() + "?testCode="
		        + request.getParameter("editedTestCode")));
		return mav;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 */
	private void saveClientHivTestResultObs(HttpServletRequest request, Object command) {
		VCTModuleService service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
		
		VCTClientResult result = (VCTClientResult) command;
		
		VCTClient client = (result.getClient() != null) ? result.getClient() : service.getClientByCodeTest(result
		        .getCodeTest());
		
		Obs hivTestConstruct = new Obs();
		Obs dateOfHivTest = new Obs();
		Obs resultOfHivTest = new Obs();
		//		Obs dateResultOfHivTestReceived = new Obs();
		
		User creator = Context.getAuthenticatedUser();
		Date createdOn = (result.getClient() != null) ? result.getDateCreated() : new Date();
		Date obsDatetime = result.getDateOfResult();
		Location location = result.getLocation();
		
		try {
			
			if (client.getResultObs() != null) {
				hivTestConstruct = Context.getObsService().getObs(client.getResultObs().getObsId());
			}
			hivTestConstruct.setCreator(creator);
			hivTestConstruct.setDateCreated(createdOn);
			hivTestConstruct.setLocation(location);
			hivTestConstruct.setPerson(client.getClient());
			hivTestConstruct.setConcept(Context.getConceptService().getConcept(
			    VCTConfigurationUtil.getVctHivTestConstructConceptId()));
			hivTestConstruct.setObsDatetime(obsDatetime);
			
			if (client.getResultObs() != null) {
				dateOfHivTest = Context.getObsService().getObs(result.getHivTestDateObsId());
			}
			dateOfHivTest.setCreator(creator);
			dateOfHivTest.setDateCreated(createdOn);
			dateOfHivTest.setLocation(location);
			dateOfHivTest.setPerson(client.getClient());
			dateOfHivTest.setConcept(Context.getConceptService().getConcept(VCTConfigurationUtil.getHivTestDateConceptId()));
			dateOfHivTest.setValueDatetime(client.getCounselingObs().getObsDatetime());
			dateOfHivTest.setObsDatetime(obsDatetime);
			
			if (client.getResultObs() != null) {
				resultOfHivTest = Context.getObsService().getObs(result.getHivTestResultObsId());
			}
			resultOfHivTest.setCreator(creator);
			resultOfHivTest.setDateCreated(createdOn);
			resultOfHivTest.setLocation(location);
			resultOfHivTest.setPerson(client.getClient());
			resultOfHivTest.setConcept(Context.getConceptService().getConcept(
			    VCTConfigurationUtil.getResultOfHivTestConceptId()));
			resultOfHivTest.setValueCoded(Context.getConceptService().getConcept(result.getHivTestResult()));
			resultOfHivTest.setObsDatetime(obsDatetime);
			
			//update
			if (client.getResultObs() != null) {
				log.info("--------------------------------------->> Trying to update the dateOfHivTest...."
				        + result.getHivTestDateObsId());
				dateOfHivTest.setObsId(result.getHivTestDateObsId());
				Context.getObsService().saveObs(dateOfHivTest, "Update");
				log.info("--------------------------------------->> dateOfHivTest updated!");
			}
			
			if (client.getResultObs() != null) {
				log.info("--------------------------------------->> Trying to update the resultOfHivTest...."
				        + result.getHivTestResultObsId());
				resultOfHivTest.setObsId(result.getHivTestResultObsId());
				Context.getObsService().saveObs(resultOfHivTest, "Update");
				log.info("--------------------------------------->> resultOfHivTest updated!");
			}
			
			if (client.getResultObs() != null) {
				log.info("--------------------------------------->> Trying to update the hivTestConstruct...."
				        + client.getResultObs().getObsId());
				hivTestConstruct.setObsId(client.getResultObs().getObsId());
				Context.getObsService().saveObs(hivTestConstruct, "Update");
				log.info("--------------------------------------->> hivTestConstruct updated!"
				        + hivTestConstruct.getObsDatetime());
			}
			
			/* end of update*/

			if (client.getResultObs() == null) {
				log.info("--------------------------------------->> Trying to save new obs....");
				hivTestConstruct.addGroupMember(dateOfHivTest);
				hivTestConstruct.addGroupMember(resultOfHivTest);
				
				Context.getObsService().saveObs(hivTestConstruct, null);
				log.info("--------------------------------------->> obs saved");
				
				log.info("--------------------------------------->> Trying to save new obs....");
				client.setResultObs(hivTestConstruct);
				
				service.saveVCTClient(client);
				log.info("--------------------------------------->> obs saved");
				
				log.info("--------------------------------------->> Trying to save Sample Test");
				//				MohTracPortalService serv = Context.getService(MohTracPortalService.class);
				//				SampleCode sc=serv.getSampleTestBySampleCode(result.getCodeTest());
				//				sc.setTestTaken(resultOfHivTest);
				//				serv.saveSampleTest(sc);
				
				VCTTracUtil.updateSampleCodeResult(resultOfHivTest, result.getCodeTest());
				
				log.info("--------------------------------------->> Sample Test saved");
			}
			String msg = getMessageSourceAccessor().getMessage("Form.saved");
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
		}
		catch (Exception e) {
			e.printStackTrace();
			String msg = getMessageSourceAccessor().getMessage("Form.not.saved");
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
		}
		
	}
}
