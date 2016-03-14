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
package org.openmrs.module.vcttrac.impl;

import java.util.Date;
import java.util.List;

import org.openmrs.Person;
import org.openmrs.module.vcttrac.VCTClient;
import org.openmrs.module.vcttrac.db.VCTModuleDAO;
import org.openmrs.module.vcttrac.service.VCTModuleService;

/**
 *
 */
public class VCTModuleServiceImpl implements VCTModuleService {
	
	private VCTModuleDAO vctDAO;
	
	/**
	 * @return the vctDAO
	 */
	public VCTModuleDAO getVctDAO() {
		return vctDAO;
	}
	
	/**
	 * @param vctDAO the vctDAO to set
	 */
	public void setVctDAO(VCTModuleDAO vctDAO) {
		this.vctDAO = vctDAO;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#saveVCTClient(org.openmrs.module.vcttrac.VCTClient)
	 */
	//@Override =>incompatible with jre5
	public void saveVCTClient(VCTClient client) {
		vctDAO.saveVCTClient(client);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getVCTClientsFromPIT()
	 */
	//@Override =>incompatible with jre5
	public List<Integer> getVCTClientsFromPIT() {
		return vctDAO.getVCTClientsFromPIT();
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getVoluntaryClients()
	 */
	//@Override =>incompatible with jre5
	public List<Integer> getVoluntaryClients() {
		return vctDAO.getVoluntaryClients();
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getAllClients()
	 */
	//@Override =>incompatible with jre5
	public List<VCTClient> getAllClients() {
		return vctDAO.getAllClients();
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getClientAtLastVisitByClientId(java.lang.Integer)
	 */
	//@Override =>incompatible with jre5
	public VCTClient getClientAtLastVisitByClientId(Integer clientId) {
		return vctDAO.getClientAtLastVisitByClientId(clientId);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getAllClientCodeWithoutHivTestResult()
	 */
	//@Override =>incompatible with jre5
	public List<String> getAllClientCodeWithoutHivTestResult() {
		return vctDAO.getAllClientCodeWithoutHivTestResult();
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getClientByCodeTest(java.lang.String)
	 */
	//@Override =>incompatible with jre5
	public VCTClient getClientByCodeTest(String clientCode) {
		return vctDAO.getClientByCodeTest(clientCode);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getAllClientCodeForReceptionOfResult()
	 */
	//@Override =>incompatible with jre5
	public List<String> getAllClientCodeForReceptionOfResult() {
		return vctDAO.getAllClientCodeForReceptionOfResult();
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getAllClientId()
	 */
	//@Override =>incompatible with jre5
	public List<Integer> getAllClientId() {
		return vctDAO.getAllClientId();
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getClientById(java.lang.Integer)
	 */
	//@Override =>incompatible with jre5
	public VCTClient getClientById(Integer clientId) {
		return vctDAO.getClientById(clientId);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getClientVisitByPersonId()
	 */
	//@Override =>incompatible with jre5
	public List<VCTClient> getClientVisitByPersonId(Integer personId) {
		return vctDAO.getClientVisitByPersonId(personId);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getVCTClientsBasedOnGender(java.lang.String)
	 */
	//@Override =>incompatible with jre5
	public List<Integer> getVCTClientsBasedOnGender(String gender, Date registrationDate) {
		return vctDAO.getVCTClientsBasedOnGender(gender, registrationDate);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getVCTClientsBasedOnAttributeType(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	//@Override =>incompatible with jre5
	public List<Integer> getVCTClientsBasedOnAttributeType(Integer attibuteTypeId, Integer value) {
		return vctDAO.getVCTClientsBasedOnAttributeType(attibuteTypeId, value);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getVCTClientsBasedOnConceptObs(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	//@Override =>incompatible with jre5
	public List<Integer> getVCTClientsBasedOnConceptObs(Integer conceptObsId, Integer value, Boolean gotResult) {
		return vctDAO.getVCTClientsBasedOnConceptObs(conceptObsId, value, gotResult);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getVCTClientsTested(java.lang.Boolean)
	 */
	//@Override =>incompatible with jre5
	public List<Integer> getVCTClientsTested(Boolean tested) {
		return vctDAO.getVCTClientsTested(tested);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getVCTClientsBasedOnCounselingType(java.lang.Integer)
	 */
	//@Override =>incompatible with jre5
	public List<Integer> getVCTClientsBasedOnCounselingType(Integer counselingType, Date registrationDate) {
		return vctDAO.getVCTClientsBasedOnCounselingType(counselingType, registrationDate);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#synchronizePatientsAndClients(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	//@Override =>incompatible with jre5
	public void synchronizePatientsAndClients(Integer formerPersonId, Integer newPersonId) {
		vctDAO.synchronizePatientsAndClients(formerPersonId, newPersonId);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getNumberOfClientByVCTOrPIT(java.lang.Integer,
	 *      java.util.Date)
	 */
	public Integer getNumberOfClientByVCTOrPIT(Integer vctorpit, Date startingFrom) {
		return vctDAO.getNumberOfClientByVCTOrPIT(vctorpit, startingFrom);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getPeople(java.lang.String,
	 *      java.lang.Boolean)
	 */
	public List<Person> getPeople(String name, Boolean dead, Boolean counseled) {
		return vctDAO.getPeople(name, dead, counseled);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getVCTClientsWaitingFromHIVTest()
	 */
	@Override
	public List<Integer> getVCTClientsWaitingFromHIVTest() {
		return vctDAO.getVCTClientsWaitingFromHIVTest();
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getClientByClientCode(java.lang.String)
	 */
	@Override
	public VCTClient getClientByClientCode(String clientCode) {
		return vctDAO.getClientByClientCode(clientCode);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getVCTClientsWaitingToBeEnrolledInHIVProgram()
	 */
	@Override
	public List<Integer> getVCTClientsWaitingToBeEnrolledInHIVProgram() {
		return vctDAO.getVCTClientsWaitingToBeEnrolledInHIVProgram();
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getNumberOfNewClientsCounseledAndTestedForHIV(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@Override
	public Integer getNumberOfNewClientsCounseledAndTestedForHIV(String from, String to, Integer locationId,
	                                                             Integer admissionMode, Integer minAge, Integer maxAge,
	                                                             String gender) {
		return vctDAO.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, locationId, admissionMode, minAge, maxAge,
		    gender);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getNumberOfNewClientsTestedAndReceivedResults(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@Override
	public Integer getNumberOfNewClientsTestedAndReceivedResults(String from, String to, Integer locationId,
	                                                             Integer admissionMode, Integer minAge, Integer maxAge,
	                                                             String gender) {
		return vctDAO.getNumberOfNewClientsTestedAndReceivedResults(from, to, locationId, admissionMode, minAge, maxAge,
		    gender);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getNumberOfHIVPositiveClients(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@Override
	public Integer getNumberOfHIVPositiveClients(String from, String to, Integer locationId, Integer admissionMode,
	                                             Integer minAge, Integer maxAge, String gender) {
		return vctDAO.getNumberOfHIVPositiveClients(from, to, locationId, admissionMode, minAge, maxAge, gender);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getNumberOfCouplesCounseledAndTested(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	@Override
	public Integer getNumberOfCouplesCounseledAndTested(String from, String to, Integer locationId, int whoGetTested) {
		return vctDAO.getNumberOfCouplesCounseledAndTested(from, to, locationId, whoGetTested);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getNumberOfDiscordantCouples(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	@Override
	public Integer getNumberOfDiscordantCouples(String from, String to, Integer locationId) {
		return vctDAO.getNumberOfDiscordantCouples(from, to, locationId);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getCouplesCounseledAndTested(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<VCTClient> getCouplesCounseledAndTested(String from, String to, Integer locationId, int whoGetTested) {
		return vctDAO.getCouplesCounseledAndTested(from, to, locationId, whoGetTested);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getDiscordantCouples(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<VCTClient> getDiscordantCouples(String from, String to, Integer locationId) {
		return vctDAO.getDiscordantCouples(from, to, locationId);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getHIVPositiveClients(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@Override
	public List<VCTClient> getHIVPositiveClients(String from, String to, Integer locationId, Integer admissionMode,
	                                             Integer minAge, Integer maxAge, String gender) {
		return vctDAO.getHIVPositiveClients(from, to, locationId, admissionMode, minAge, maxAge, gender);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getNewClientsCounseledAndTestedForHIV(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@Override
	public List<VCTClient> getNewClientsCounseledAndTestedForHIV(String from, String to, Integer locationId,
	                                                             Integer admissionMode, Integer minAge, Integer maxAge,
	                                                             String gender) {
		return vctDAO.getNewClientsCounseledAndTestedForHIV(from, to, locationId, admissionMode, minAge, maxAge, gender);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getNewClientsTestedAndReceivedResults(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@Override
	public List<VCTClient> getNewClientsTestedAndReceivedResults(String from, String to, Integer locationId,
	                                                             Integer admissionMode, Integer minAge, Integer maxAge,
	                                                             String gender) {
		return vctDAO.getNewClientsTestedAndReceivedResults(from, to, locationId, admissionMode, minAge, maxAge, gender);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getClientByNID(java.lang.String)
	 */
	@Override
	public VCTClient getClientByNID(String nid) {
		return vctDAO.getClientByNID(nid);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getPersonIdByNID(java.lang.String)
	 */
	@Override
	public Integer getPersonIdByNID(String nid) {
		return vctDAO.getPersonIdByNID(nid);
	}
	
	/**
	 * @throws Exception
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getVCTClientBasedOn(java.lang.String)
	 */
	@Override
	public List<Integer> getVCTClientsBasedOn(String reference, String gender, Integer counselingType, Integer location,
	                                          String tested, String dateFrom, String dateTo, Integer minAge, Integer maxAge,
	                                          Integer civilStatus, Integer educationLevel, Integer mainActivity,
	                                          Integer testOrderer, Integer whyGetTested, Integer testResult,
	                                          Integer gotResult) throws Exception {
		return vctDAO.getVCTClientsBasedOn(reference, gender, counselingType, location, tested, dateFrom, dateTo, minAge,
		    maxAge, civilStatus, educationLevel, mainActivity, testOrderer, whyGetTested, testResult, gotResult);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getNumberOfClientByDateOfRegistration(java.util.Date)
	 */
	@Override
	public Integer getNumberOfClientByDateOfRegistration(Date registrationDate) {
		return vctDAO.getNumberOfClientByDateOfRegistration(registrationDate);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getNumberOfClientByMonthAndYearOfRegistration(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	public Integer getNumberOfClientByMonthAndYearOfRegistration(Integer month, Integer year) {
		return vctDAO.getNumberOfClientByMonthAndYearOfRegistration(month, year);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getCouplesCounseled(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<VCTClient> getCouplesCounseled(String from, String to, Integer locationId) {
		return vctDAO.getCouplesCounseled(from, to, locationId);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getMaxYearOfRegistration()
	 */
	@Override
	public Integer getMaxYearOfRegistration() {
		return vctDAO.getMinOrMaxYearOfRegistration(false);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getMinYearOfRegistration()
	 */
	@Override
	public Integer getMinYearOfRegistration() {
		return vctDAO.getMinOrMaxYearOfRegistration(true);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.service.VCTModuleService#getNumberOfClientByYearOfRegistration(java.lang.Integer)
	 */
	@Override
	public Integer getNumberOfClientByYearOfRegistration(Integer year) {
		return vctDAO.getNumberOfClientByYearOfRegistration(year);
	}
	
}
