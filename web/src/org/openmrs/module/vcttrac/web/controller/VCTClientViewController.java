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

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ServiceContext;
import org.openmrs.module.mohtracportal.SampleCode;
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
 * @author Yves GAKUBA
 */
public class VCTClientViewController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());
		
		if (request.getParameter("save") != null)
			saveCodeTest(request, mav);
		
		if (request.getParameter("edit") != null)
			editCodeTest(request, mav);
		
		Concept c = Context.getConceptService().getConcept(VCTTracConstant.POSITIVE_CID);
		mav.addObject("positiveString", c.getDisplayString());
		mav.addObject("resultOfHivTest", VCTTracConstant.RESULT_OF_HIV_TEST);
		
		manageListing(request, mav, response);
		
		return mav;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param mav
	 * @throws ParseException
	 */
	private void saveCodeTest(HttpServletRequest request, ModelAndView mav) {
		DateFormat df = Context.getDateFormat();
		boolean cseCaught = false;
		try {
			VCTModuleService service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
			
			if (request.getParameter("clientId") != null && request.getParameter("clientCode") != null
			        && request.getParameter("clientCode").compareTo("") != 0) {
				VCTClient client = service.getClientById(Integer.valueOf(request.getParameter("clientId")));
				if (null != client) {
					client.setCodeTest(request.getParameter("testCode"));
					Obs counselingObs = client.getCounselingObs();
					
					Date obsDatetime = df.parse(request.getParameter("obsDate"));
					
					Obs hivTestingDone = new Obs();
					hivTestingDone.setPerson(client.getClient());
					hivTestingDone.setCreator(Context.getAuthenticatedUser());
					hivTestingDone.setDateCreated(new Date());
					hivTestingDone.setLocation(counselingObs.getLocation());
					hivTestingDone.setObsDatetime(obsDatetime);
					hivTestingDone.setConcept(Context.getConceptService().getConcept(
					    VCTConfigurationUtil.getHivTestingDoneConceptId()));
					hivTestingDone.setValueNumeric(1.0);
					
					counselingObs.addGroupMember(hivTestingDone);
					
					Context.getObsService().saveObs(hivTestingDone, "Client has been tested");
					
					//					client.setCounselingObs(newCounselingObs);
					service.saveVCTClient(client);
					
					//save sample code
					saveSampleCode(client);
				}
			}
			
			String msg = getMessageSourceAccessor().getMessage("Form.saved");
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
		}
		catch (ConstraintViolationException cve) {
			cseCaught = true;
			log.info(">>>>>>>VCT>>Save>>Code>>Test>> " + cve.getMessage());
			cve.printStackTrace();
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "The CODE TEST '" + request.getParameter("testCode") + "' is arleady in use.");
			
		}
		catch (DataIntegrityViolationException dive) {
			cseCaught = true;
			log.info(">>>>>>>VCT>>Save>>Code>>Test>> " + dive.getMessage());
			dive.printStackTrace();
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "The CODE TEST '" + request.getParameter("testCode") + "' is arleady in use.");
			
		}
		catch (Exception e) {
			log.info(">>>>>>>VCT>>Save>>Code>>Test>> " + e.getMessage());
			e.printStackTrace();
			//if (!cseCaught)
				request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
				    "An error occured when trying to save the CODE TEST, please check your log file.");
			
		}
	}
	
	private void editCodeTest(HttpServletRequest request, ModelAndView mav) {
//		DateFormat df = Context.getDateFormat();
//		boolean cseCaught = false;
		try {
			VCTModuleService service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
			
			if (request.getParameter("clientCode") != null && request.getParameter("testCode") != null
			        && request.getParameter("testCode").compareTo("") != 0) {
				VCTClient client = service.getClientById(Integer.valueOf(request.getParameter("clientCode")));
				if (null != client) {
					client.setCodeTest(request.getParameter("testCode"));
					client.setDateChanged(new Date());
					client.setChangedBy(Context.getAuthenticatedUser());
					service.saveVCTClient(client);
					
					//save sample code
					//saveSampleCode(client);
				}
			}
			
//			String msg = getMessageSourceAccessor().getMessage();
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Form.saved");
		}
		catch (ConstraintViolationException cve) {
//			cseCaught = true;
			log.info(">>>>>>>VCT>>Save>>Code>>Test>> " + cve.getMessage());
			cve.printStackTrace();
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "The CODE TEST '" + request.getParameter("testCode") + "' is arleady in use.");
			
		}
		catch (DataIntegrityViolationException dive) {
//			cseCaught = true;
			log.info(">>>>>>>VCT>>Save>>Code>>Test>> " + dive.getMessage());
			dive.printStackTrace();
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "The CODE TEST '" + request.getParameter("testCode") + "' is arleady in use.");
			
		}
		catch (Exception e) {
			log.info(">>>>>>>VCT>>Save>>Code>>Test>> " + e.getMessage());
			e.printStackTrace();
			//if (!cseCaught)
				request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
				    "An error occured when trying to save the CODE TEST, please check your log file.");
			
		}
	}
	
	private void saveSampleCode(VCTClient client) throws SQLException {
		//		if (ci.getHivTested() == 1) {
		//			client.setCodeTest(ci.getCodeClient());
		
		//the next codes should replace the codes under this comment
		//MohTracPortalService serv = Context.getService(MohTracPortalService.class);
		SampleCode sc_client = new SampleCode();
		sc_client.setDateCreated(new Date());
		sc_client.setCreator(Context.getAuthenticatedUser());
		sc_client.setComment("VCT");
		sc_client.setSampleCode(client.getCodeTest());
		//sc.setTestTaken(null);
		//			service.getMohTracPortalService().saveSampleTest(sc_client);
		VCTTracUtil.saveSampleCode(sc_client);
		// end of codes
		//		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param mav
	 */
	private void manageListing(HttpServletRequest request, ModelAndView mav, HttpServletResponse response) {
		VCTModuleService service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
		int pageSize;
		String pageNumber = request.getParameter("page");
		List<VCTClient> clients = new ArrayList<VCTClient>();
		
		List<Integer> res;
		
		List<Integer> numberOfPages;
		
		try {
			
			if (VCTConfigurationUtil.isConfigured()) {
				pageSize = VCTConfigurationUtil.getNumberOfRecordPerPage();
				
				if (pageNumber.compareToIgnoreCase("1") == 0 || pageNumber.compareToIgnoreCase("") == 0) {
					res = new ArrayList<Integer>();
					
					res = service.getVCTClientsWaitingFromHIVTest();
					request.getSession().setAttribute("cl_view_res", res);
					
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
					
					request.getSession().setAttribute("cl_view_numberOfPages", numberOfPages);
					//----------------
					
				} else {
					res = (List<Integer>) request.getSession().getAttribute("cl_view_res");
					numberOfPages = (List<Integer>) request.getSession().getAttribute("cl_view_numberOfPages");
					for (int i = (pageSize * (Integer.parseInt(pageNumber) - 1)); i < pageSize
					        * (Integer.parseInt(pageNumber)); i++) {
						if (i >= res.size())
							break;
						else
							clients.add(service.getClientById(res.get(i)));
					}
				}
				
				if (Integer.valueOf(pageNumber).intValue() > 1)
					mav.addObject("prevPage", (Integer.valueOf(pageNumber)) - 1);
				else
					mav.addObject("prevPage", -1);
				if (Integer.valueOf(pageNumber).intValue() < numberOfPages.size())
					mav.addObject("nextPage", (Integer.valueOf(pageNumber)) + 1);
				else
					mav.addObject("nextPage", -1);
				mav.addObject("lastPage", ((numberOfPages.size() >= 1) ? numberOfPages.size() : 1));
				
				
				String title = VCTTracUtil.getMessage("vcttrac.clientWaitingForResult", null);
				mav.addObject("title", title);
				
				//page infos
				Object[] pagerInfos = new Object[3];
				pagerInfos[0] = (res.size() == 0) ? 0 : (pageSize * (Integer.parseInt(pageNumber) - 1)) + 1;
				pagerInfos[1] = (pageSize * (Integer.parseInt(pageNumber)) <= res.size()) ? pageSize
				        * (Integer.parseInt(pageNumber)) : res.size();
				pagerInfos[2] = res.size();
				
				ApplicationContext appContext = ContextProvider.getApplicationContext();
				
				mav.addObject("numberOfPages", numberOfPages);
				mav.addObject("clients", clients);
				mav.addObject("pageSize", pageSize);
				mav.addObject("pageInfos", appContext.getMessage("vcttrac.pagingInfo.showingResults", pagerInfos, Context
				        .getLocale()));
				
				FileExporter fexp = new FileExporter();
				
				if (request.getParameter("export") != null && request.getParameter("export").compareToIgnoreCase("csv") == 0) {
					List<VCTClient> clientList = new ArrayList<VCTClient>();
					for (Integer clientId : res) {
						clientList.add(service.getClientById(clientId));
					}
					fexp.exportToCSVFile(request, response, clientList, "list_of_clients_in_vct_program_" + title + ".csv",
					    title.toUpperCase(), "");
				}
				
			}
		}
		catch (Exception e) {
			log.error(">>>>>>>VCT>>Client>>waiting>>for>>HIV>>Test>> " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
