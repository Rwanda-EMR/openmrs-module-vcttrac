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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.vcttrac.VCTClient;
import org.openmrs.module.vcttrac.VCTCounselingInfo;
import org.openmrs.module.vcttrac.VCTPreCounselingInfo;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * @author Yves GAKUBA
 */
public class VCTSaveCounselingViewController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());
		List<VCTCounselingInfo> ciList = new ArrayList<VCTCounselingInfo>();
		
		try {
			VCTPreCounselingInfo pci = new VCTPreCounselingInfo();
			pci.setLocationId(Integer.valueOf(request.getParameter("location")));
			pci.setEncounterDate(request.getParameter("encounterDate"));
			pci.setProviderId(Integer.valueOf(request.getParameter("provider")));
			pci.setCounselingTypeId(Integer.valueOf(request.getParameter("counselingType")));
			
			for (int index = 1; index <= pci.getCounselingTypeId(); index++) {
				ciList.add(getVCTCouncelingInfoByPatient(pci, index, request));
			}
			
			//save the relationship
			if (pci.getCounselingTypeId() > 1) {
				Person husband = Context.getPersonService().getPerson(Integer.valueOf(request.getParameter("personId_1")));
				Person temp = Context.getPersonService().getPerson(Integer.valueOf(request.getParameter("personId_2")));
				Person wife = null;
				
				if (temp.getGender().compareToIgnoreCase("f") == 0)
					wife = temp;
				else {
					wife = husband;
					husband = temp;
				}
				
				Relationship r = new Relationship();
				r.setCreator(Context.getAuthenticatedUser());
				r.setDateCreated(new Date());
				r.setRelationshipType(Context.getPersonService().getRelationshipType(
				    VCTConfigurationUtil.getPartnerRelationShipTypeId()));
				r.setPersonA(husband);
				r.setPersonB(wife);
				
				Context.getPersonService().saveRelationship(r);
			}
			
			mav.addObject("pci", pci);
			mav.addObject("ciList", ciList);
		}
		catch (Exception ex) {
			String msg = "An error occured [" + ex.getMessage() + "], please check your log file.";
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
			log.error(">>>>>>>>>>>>VCT>>Save>>Counseling>>Form>>>> An error occured : " + ex.getMessage());
			ex.printStackTrace();
		}
		return mav;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 */
	//	private void updatePartnersTestCodes(HttpServletRequest request) {
	//		VCTModuleService service = Context.getService(VCTModuleService.class);
	//		
	//		VCTClient client = service.getClientAtLastVisitByClientId(Integer.parseInt(request.getParameter("personId_" + 1)));
	//		client.setPartnerCodeTest(request.getParameter("codeClient_" + 2));
	//		service.saveVCTClient(client);
	//		
	//		VCTClient partner = service.getClientAtLastVisitByClientId(Integer.parseInt(request.getParameter("personId_" + 2)));
	//		partner.setPartnerCodeTest(request.getParameter("codeClient_" + 1));
	//		service.saveVCTClient(partner);
	//		
	//	}
	/**
	 * Auto generated method comment
	 * 
	 * @param pci
	 * @param index
	 * @param request
	 * @return
	 */
	private VCTCounselingInfo getVCTCouncelingInfoByPatient(VCTPreCounselingInfo pci, int index, HttpServletRequest request) {
		
		VCTCounselingInfo ci = new VCTCounselingInfo(pci);
		ObsService os = Context.getObsService();
		ConceptService cs = Context.getConceptService();
		DateFormat df = Context.getDateFormat();
		
		try {
			//			MohTracPortalService serv = Context.getService(MohTracPortalService.class);
			
			ci.setPatientId(Integer.valueOf(request.getParameter("personId_" + index)));
			ci.setCodeClient(request.getParameter("codeClient_" + index));
			//ci.setWhatHandicap(Integer.parseInt(request.getParameter("handicap_" + index)));
			ci.setWhyTesting(Integer.valueOf(request.getParameter("whyTesting_" + index)));
			ci.setRefereDuService(Integer.valueOf(request.getParameter("refereDuService_" + index)));
//			ci.setNumberOfCondomTaken(Integer.valueOf(request.getParameter("numberOfCondom_" + index)));
			//			ci.setHivTested((request.getParameter("hivTestedCkbx_" + index) != null) ? 1 : 0);
			ci.setClinicalComment(request.getParameter("comment_" + index));
			
			//			if (pci.getCounselingTypeId() == 2) {
			//				if (index == 1)
			//					ci.setCodeDuPartenaire(request.getParameter("codeClient_" + (index + 1)));
			//				else
			//					ci.setCodeDuPartenaire(request.getParameter("codeClient_" + (index - 1)));
			//			}
			
			Obs parentObs = new Obs();
			Date obsDatetime = df.parse(pci.getEncounterDate());
			Concept vctProgramConcept = Context.getConceptService().getConcept(
			    VCTConfigurationUtil.getVctProgramConstructConceptId());
			Person person = Context.getPersonService().getPerson(ci.getPatientId());
			
			parentObs.setPerson(person);
			parentObs.setConcept(vctProgramConcept);
			parentObs.setCreator(Context.getAuthenticatedUser());
			parentObs.setDateCreated(new Date());
			parentObs.setLocation(Context.getLocationService().getLocation(pci.getLocationId()));
			parentObs.setObsDatetime(obsDatetime);
			
			Obs obs1 = new Obs();
			obs1.setPerson(person);
			obs1.setCreator(Context.getAuthenticatedUser());
			obs1.setDateCreated(new Date());
			obs1.setLocation(Context.getLocationService().getLocation(pci.getLocationId()));
			obs1.setObsDatetime(obsDatetime);
			obs1.setConcept(cs.getConcept(VCTConfigurationUtil.getWhyDidYouGetTestedConceptId()));
			obs1.setValueCoded(cs.getConcept(ci.getWhyTesting()));
			
			Obs obs2 = new Obs();
			obs2.setPerson(person);
			obs2.setCreator(Context.getAuthenticatedUser());
			obs2.setDateCreated(new Date());
			obs2.setLocation(Context.getLocationService().getLocation(pci.getLocationId()));
			obs2.setObsDatetime(obsDatetime);
			obs2.setConcept(cs.getConcept(VCTConfigurationUtil.getProgramThatOrderedTestConceptId()));
			obs2.setValueCoded(cs.getConcept(ci.getRefereDuService()));
			
			Obs obs3 = new Obs();
			obs3.setPerson(person);
			obs3.setCreator(Context.getAuthenticatedUser());
			obs3.setDateCreated(new Date());
			obs3.setLocation(Context.getLocationService().getLocation(pci.getLocationId()));
			obs3.setObsDatetime(obsDatetime);
			obs3.setConcept(cs.getConcept(VCTConfigurationUtil.getClinicalImpressionCommentConceptId()));
			obs3.setValueText(ci.getClinicalComment());
			
			/*Obs obs4 = new Obs();
			obs4.setPerson(person);
			obs4.setCreator(Context.getAuthenticatedUser());
			obs4.setDateCreated(new Date());
			obs4.setLocation(Context.getLocationService().getLocation(pci.getLocationId()));
			obs4.setObsDatetime(obsDatetime);
			obs4.setConcept(cs.getConcept(VCTTracUtil.getHivTestingDoneConceptId()));
			obs4.setValueNumeric(Double.valueOf(ci.getHivTested()));*/

			/*Obs obs5 = new Obs();
			obs5.setPerson(person);
			obs5.setCreator(Context.getAuthenticatedUser());
			obs5.setDateCreated(new Date());
			obs5.setLocation(Context.getLocationService().getLocation(pci.getLocationId()));
			obs5.setObsDatetime(obsDatetime);
			obs5.setConcept(cs.getConcept(VCTConfigurationUtil.getNumberOfCondomsTakenConceptId()));
			obs5.setValueNumeric(Double.valueOf(ci.getNumberOfCondomTaken()));*/
			
			parentObs.addGroupMember(obs1);
			parentObs.addGroupMember(obs2);
			parentObs.addGroupMember(obs3);
			//			parentObs.addGroupMember(obs4);
//			parentObs.addGroupMember(obs5);
			
			os.saveObs(parentObs, null);
			
			VCTModuleService service = Context.getService(VCTModuleService.class);
			VCTClient client = service.getClientAtLastVisitByClientId(parentObs.getPersonId());
			client.setCounselingObs(parentObs);
			client.setTypeOfCounseling(pci.getCounselingTypeId());
			
			if (pci.getCounselingTypeId() == 2) {
				if (index == 1)
					client.setPartner(Context.getPersonService().getPerson(
					    Integer.valueOf(request.getParameter("personId_" + (index + 1)))));
				else
					client.setPartner(Context.getPersonService().getPerson(
					    Integer.valueOf(request.getParameter("personId_" + (index - 1)))));
			}
			service.saveVCTClient(client);
			
			String msg = getMessageSourceAccessor().getMessage("Form.saved");
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
		}
		catch (Exception e) {
			String msg = getMessageSourceAccessor().getMessage("Form.not.saved");
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
			log.error(">>>>>>>>>>>>VCT>>Save>>Counseling>>Form>>>> An error occured : " + e.getMessage());
			e.printStackTrace();
		}
		return ci;
	}
}
