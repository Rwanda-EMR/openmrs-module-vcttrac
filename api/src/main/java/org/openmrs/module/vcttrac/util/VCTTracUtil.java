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
package org.openmrs.module.vcttrac.util;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.User;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohtracportal.SampleCode;
import org.openmrs.module.mohtracportal.service.MohTracPortalService;
import org.openmrs.module.mohtracportal.util.MohTracUtil;
import org.openmrs.util.OpenmrsUtil;

/**
 * @author Yves GAKUBA
 */
public class VCTTracUtil {
	
	private static Log log = LogFactory.getLog(VCTTracUtil.class);
	
	/**
	 * Creates a map of all providers in the system
	 * 
	 * @return
	 */
	public static HashMap<Integer, String> createProviderOptions() {
		return MohTracUtil.createProviderOptions();
	}
	
	/**
	 * Creates a map of all attributeTypes in the system
	 * 
	 * @return
	 */
	public static HashMap<Integer, String> createAttributesOptions() {
		return MohTracUtil.createAttributesOptions();
	}
	
	/**
	 * Creates a map of all patientIdentifierTypes in the system
	 * 
	 * @return
	 */
	public static HashMap<Integer, String> createPatientIdentifierTypesOptions() {
		return MohTracUtil.createIdentifierTypesOptions();
	}
	
	/**
	 * Creates a map of all locations in the system.
	 * 
	 * @return
	 */
	public static HashMap<Integer, String> createLocationOptions() {
		return MohTracUtil.createLocationOptions();
	}
	
	/**
	 * Retrieve a hashmap of concept answers (concept id, bestname) given the id of the coded
	 * concept question
	 * 
	 * @param codedConceptQuestionId
	 * @return
	 */
	public static HashMap<Integer, String> createCodedOptions(int codedConceptQuestionId) {
		return MohTracUtil.createConceptCodedOptions(codedConceptQuestionId);
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param patientId
	 * @param identifier
	 * @param patientIdentifierTypeId
	 * @param locationId
	 * @return
	 */
	public static PatientIdentifier getPatientIdentifier(Integer patientId, String identifier,
	                                                     Integer patientIdentifierTypeId, Integer locationId) {
		
		try {
			if (identifier == null)
				return null;
			else if (identifier.trim().compareTo("") == 0)
				return null;
			
			if (patientId == null && locationId == null)
				return null;
			
			List<PatientIdentifier> piList = Context.getPatientService().getPatientIdentifiers(identifier,
			    Context.getPatientService().getPatientIdentifierType(patientIdentifierTypeId));
			
			for (PatientIdentifier pi : piList) {
				if (locationId != null && patientId != null) {
					if (pi.getLocation().getLocationId() == locationId && pi.getPatient().getPatientId() == patientId)
						return pi;
				} else {
					if (locationId != null) {
						if (pi.getLocation().getLocationId().intValue() == locationId.intValue())
							return pi;
					} else if (patientId != null) {
						if (pi.getLocation().getLocationId().intValue() == locationId.intValue())
							return pi;
					}
				}
			}
		}
		catch (Exception e) {
			log.error(">>VCT>>UTIL>> getPatientIdentifier>> An error occured : " + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Return a map of 3 possible result of hiv test {indeterminate, negative, positive}
	 * 
	 * @return
	 */
	public static HashMap<Integer, String> createResultOfHivTestOptions() {
		HashMap<Integer, String> hivResultOptions = new HashMap<Integer, String>();
		for (int conceptId : VCTTracConstant.RESULT_OF_HIV_TEST_POSSIBLE_OPTIONS) {
			Concept c = Context.getConceptService().getConcept(conceptId);
			hivResultOptions.put(c.getConceptId().intValue(), c.getDisplayString());
		}
		return hivResultOptions;
	}
	
	/**
	 * Retrieve a hashmap of concept answers (concept id, bestname) given the id of the coded
	 * concept question
	 * 
	 * @param codedConceptQuestionId
	 * @return
	 */
	public static HashMap<Integer, String> createCivilStatusOptions() {
		HashMap<Integer, String> civilStatusOptions = new HashMap<Integer, String>();
		for (int conceptId : VCTTracConstant.CIVIL_STATUS_POSSIBLE_OPTIONS) {
			Concept c = Context.getConceptService().getConcept(conceptId);
			civilStatusOptions.put(c.getConceptId(), c.getDisplayString().toLowerCase());
		}
		return civilStatusOptions;
	}
	
	/**
	 * Retrieve a hashmap of concept answers (concept id, bestname) given the id of the coded
	 * concept question
	 * 
	 * @param codedConceptQuestionId
	 * @return
	 */
	public static HashMap<Integer, String> createEducationLevelOptions() {
		HashMap<Integer, String> educationLevelOptions = new HashMap<Integer, String>();
		for (int conceptId : VCTTracConstant.EDUCATION_LEVEL_POSSIBLE_OPTIONS) {
			Concept c = Context.getConceptService().getConcept(conceptId);
			educationLevelOptions.put(c.getConceptId(), c.getDisplayString().toLowerCase());
		}
		return educationLevelOptions;
	}
	
	/**
	 * Retrieve a hashmap of concept answers (concept id, bestname) given the id of the coded
	 * concept question
	 * 
	 * @param codedConceptQuestionId
	 * @return
	 */
	public static HashMap<Integer, String> createMainActivityOptions() {
		HashMap<Integer, String> mainActivityOptions = new HashMap<Integer, String>();
		for (int conceptId : VCTTracConstant.MAIN_ACTIVITY_POSSIBLE_OPTIONS) {
			Concept c = Context.getConceptService().getConcept(conceptId);
			mainActivityOptions.put(c.getConceptId(), c.getDisplayString().toLowerCase());
		}
		return mainActivityOptions;
	}
	
	/**
	 * Get a message from message propertie file
	 * 
	 * @param messageId
	 * @param parameters
	 * @return
	 */
	public static String getMessage(String messageId, Object[] parameters) {
		String msg = MohTracUtil.getMessage(messageId, parameters);
		return msg;
	}
	
	/**
	 * A convenience method for creating an obs with a value given the id for the concept question
	 * boolean answer.
	 * 
	 * @param conceptQuestionId
	 * @param answer
	 * @param patient
	 * @param encounterDate
	 * @param encounterLocation
	 * @return
	 */
	public static Obs createObsBoolean(int conceptQuestionId, Boolean answer, Patient patient, Date encounterDate,
	                                   Location encounterLocation) {
		Obs obs = null;
		if (answer != null) {
			ConceptService conceptService = Context.getConceptService();
			obs = new Obs(patient, conceptService.getConcept(conceptQuestionId), encounterDate, encounterLocation);
			if (answer) {
				obs.setValueNumeric(1.0);
			} else {
				obs.setValueNumeric(0.0);
			}
		}
		return obs;
	}
	
	/**
	 * A convenience method for creating an obs with a value given the id for the concept question
	 * and the coded answer.
	 * 
	 * @param conceptQuestionId
	 * @param conceptAnswerId
	 * @param patient
	 * @param encounterDate
	 * @param encounterLocation
	 * @return
	 */
	public static Obs createObsCoded(int conceptQuestionId, int conceptAnswerId, Patient patient, Date encounterDate,
	                                 Location encounterLocation) {
		Obs obs = null;
		if (conceptAnswerId != -1 && conceptAnswerId != 0) {
			ConceptService conceptService = Context.getConceptService();
			obs = new Obs(patient, conceptService.getConcept(conceptQuestionId), encounterDate, encounterLocation);
			obs.setValueCoded(conceptService.getConcept(conceptAnswerId));
		}
		return obs;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param conceptQuestionId
	 * @param conceptAnswerValue
	 * @param patient
	 * @param encounterDate
	 * @param encounterLocation
	 * @return
	 */
	public static Obs createObsNumeric(int conceptQuestionId, double conceptAnswerValue, Patient patient,
	                                   Date encounterDate, Location encounterLocation) {
		Obs obs = null;
		ConceptService conceptService = Context.getConceptService();
		obs = new Obs(patient, conceptService.getConcept(conceptQuestionId), encounterDate, encounterLocation);
		obs.setValueNumeric(conceptAnswerValue);
		
		return obs;
	}
	
	/**
	 * A convenience method for creating an obs with a value given the id for the concept question
	 * and the value for the date
	 * 
	 * @param conceptQuestionId
	 * @param answerDate
	 * @param patient
	 * @param encounterDate
	 * @param encounterLocation
	 * @return
	 */
	public static Obs createObsDate(int conceptQuestionId, String answerDate, Patient patient, Date encounterDate,
	                                Location encounterLocation) {
		Obs obs = null;
		try {
			if (answerDate != null && answerDate.trim().length() > 0) {
				ConceptService conceptService = Context.getConceptService();
				obs = new Obs(patient, conceptService.getConcept(conceptQuestionId), encounterDate, encounterLocation);
				obs.setValueDatetime(OpenmrsUtil.getDateFormat().parse(answerDate));
			}
		}
		catch (ParseException pe) {
			pe.printStackTrace();
		}
		return obs;
	}
	
	/**
	 * A convenience method for creating an obs with a value given the id for the concept question
	 * and the value for the text
	 * 
	 * @param conceptQuestionId
	 * @param answerText
	 * @param patient
	 * @param encounterDate
	 * @param encounterLocation
	 * @return
	 */
	public static Obs createObsText(int conceptQuestionId, String answerText, Patient patient, Date encounterDate,
	                                Location encounterLocation) {
		Obs obs = null;
		if (answerText != null && answerText.trim().length() > 0) {
			ConceptService conceptService = Context.getConceptService();
			obs = new Obs(patient, conceptService.getConcept(conceptQuestionId), encounterDate, encounterLocation);
			obs.setValueText(answerText);
		}
		return obs;
	}
	
	/**
	 * Create an encounter
	 * 
	 * @param encounterDate Date of the encounter
	 * @param provider Provider Provider of the encounter
	 * @param location Location Location of the encounter
	 * @param patient Patient Patient who did the encounter
	 * @param encounterType The type of that encounter
	 * @param obsList The list of obs related to that encounter
	 * @return Encounter
	 */
	public static Encounter createEncounter(Date encounterDate, User provider, Location location, Patient patient,
	                                        EncounterType encounterType, List<Obs> obsList) {
		Encounter enc = new Encounter();
		
		try {
			enc.setDateCreated(new Date());
			enc.setEncounterDatetime(encounterDate);
			enc.setProvider(provider);
			enc.setLocation(location);
			enc.setPatient(patient);
			enc.setEncounterType(encounterType);
			enc.setCreator(Context.getAuthenticatedUser());
			
			for (Obs o : obsList) {
				if (null != o)
					enc.addObs(o);
				else
					log.error("VCT-TRAC : An observation has not been saved because it was null.");
			}
		}
		catch (Exception e) {
			log.error(">>>>>>>>>>>>VCT>>UTILS>> " + e.getMessage());
			e.printStackTrace();
			enc = null;
		}
		return enc;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param sc
	 * @throws SQLException 
	 */
	public static void saveSampleCode(SampleCode sc) throws SQLException {
		MohTracPortalService service = Context.getService(MohTracPortalService.class);
		service.saveSampleTest(sc);
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param resultOfHivTest
	 * @param sampleCode
	 * @throws Exception
	 */
	public static void updateSampleCodeResult(Obs resultOfHivTest, String sampleCode) throws Exception {
		MohTracPortalService serv = Context.getService(MohTracPortalService.class);
		SampleCode sc = serv.getSampleTestBySampleCode(sampleCode);
		sc.setTestTaken(resultOfHivTest);
		serv.saveSampleTest(sc);
	}
}
