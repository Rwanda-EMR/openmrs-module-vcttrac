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
package org.openmrs.module.vcttrac.db;

import java.util.Date;
import java.util.List;

import org.openmrs.Person;
import org.openmrs.module.vcttrac.VCTClient;

/**
 * @author Yves GAKUBA
 */
public interface VCTModuleDAO {
	
	public void saveVCTClient(VCTClient client);
	
	public void synchronizePatientsAndClients(Integer formerPersonId, Integer newPersonId);
	
	public List<Integer> getVCTClientsFromPIT();
	
	public List<Integer> getVoluntaryClients();
	
	public List<VCTClient> getAllClients();
	
	public VCTClient getClientAtLastVisitByClientId(Integer clientId);
	
	public List<String> getAllClientCodeWithoutHivTestResult();
	
	public List<String> getAllClientCodeForReceptionOfResult();
	
	public VCTClient getClientByCodeTest(String codeTest);
	
	public VCTClient getClientByClientCode(String clientCode);
	
	public List<Integer> getAllClientId();
	
	public VCTClient getClientById(Integer clientId);
	
	public VCTClient getClientByNID(String nid);
	
	public Integer getPersonIdByNID(String nid);	
	
	public List<VCTClient> getClientVisitByPersonId(Integer personId);
	
	public List<Integer> getVCTClientsBasedOnGender(String gender, Date registrationDate);
	
	public List<Integer> getVCTClientsBasedOnAttributeType(Integer attibuteTypeId, Integer value);
	
	public List<Integer> getVCTClientsBasedOnConceptObs(Integer conceptObsId, Integer value, Boolean gotResult);
	
	public List<Integer> getVCTClientsTested(Boolean tested);
	
	public List<Integer> getVCTClientsBasedOnCounselingType(Integer counselingType, Date registrationDate);
	
	public Integer getNumberOfClientByVCTOrPIT(Integer vctorpit, Date startingFrom);
	
	public Integer getNumberOfClientByDateOfRegistration(Date registrationDate);
	
	public Integer getNumberOfClientByMonthAndYearOfRegistration(Integer month, Integer year);
	
	public Integer getMinOrMaxYearOfRegistration(boolean minYear);
	
	public Integer getNumberOfClientByYearOfRegistration(Integer year);
	
	public List<Integer> getVCTClientsWaitingFromHIVTest();
	
	public List<Integer> getVCTClientsBasedOn(String reference, String gender, Integer counselingType, Integer location,
	                                          String tested, String dateFrom, String dateTo, Integer minAge, Integer maxAge,
	                                          Integer civilStatus, Integer educationLevel, Integer mainActivity,
	                                          Integer testOrderer, Integer whyGetTested, Integer testResult,
	                                          Integer gotResult)
	                                                                                                        throws Exception;
	
	public List<Integer> getVCTClientsWaitingToBeEnrolledInHIVProgram();
	
	//DWR
	public List<Person> getPeople(String name, Boolean dead, Boolean counseled);
	
	//Tracnet Indicators
	
	public Integer getNumberOfNewClientsCounseledAndTestedForHIV(String from, String to, Integer locationId,
	                                                             Integer admissionMode, Integer minAge, Integer maxAge,
	                                                             String gender);
	
	public Integer getNumberOfNewClientsTestedAndReceivedResults(String from, String to, Integer locationId,
	                                                             Integer admissionMode, Integer minAge, Integer maxAge,
	                                                             String gender);
	
	public Integer getNumberOfHIVPositiveClients(String from, String to, Integer locationId, Integer admissionMode,
	                                             Integer minAge, Integer maxAge, String gender);
	
	public Integer getNumberOfDiscordantCouples(String from, String to, Integer locationId);
	
	public List<VCTClient> getNewClientsCounseledAndTestedForHIV(String from, String to, Integer locationId,
	                                                             Integer admissionMode, Integer minAge, Integer maxAge,
	                                                             String gender);
	
	public List<VCTClient> getNewClientsTestedAndReceivedResults(String from, String to, Integer locationId,
	                                                             Integer admissionMode, Integer minAge, Integer maxAge,
	                                                             String gender);
	
	public List<VCTClient> getHIVPositiveClients(String from, String to, Integer locationId, Integer admissionMode,
	                                             Integer minAge, Integer maxAge, String gender);
	
	public List<VCTClient> getDiscordantCouples(String from, String to, Integer locationId);

	/**
     * Auto generated method comment
     * 
     * @param from
     * @param to
     * @param locationId
     * @return
     */
    public List<VCTClient> getCouplesCounseled(String from, String to, Integer locationId);
	
	public List<VCTClient> getCouplesCounseledAndTested(String from, String to, Integer locationId, int whoGetTested);
	
	public Integer getNumberOfCouplesCounseledAndTested(String from, String to, Integer locationId, int whoGetTested);
	
	//end Tracnet Indicators
}
