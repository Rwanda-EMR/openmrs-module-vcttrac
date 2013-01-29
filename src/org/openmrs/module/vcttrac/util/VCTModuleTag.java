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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.RelationshipType;
import org.openmrs.api.context.Context;
import org.openmrs.module.vcttrac.service.VCTModuleService;

/**
 * @author Yves GAKUBA
 */
public class VCTModuleTag {
	
	private static Log log = LogFactory.getLog(VCTModuleTag.class);
	
	/**
	 * Auto generated method comment
	 * 
	 * @param personId
	 * @return
	 */
	public static String getPersonNames(Integer personId) {
		try {
			if (personId == null)
				return "";
			
			Person person = Context.getPersonService().getPerson(personId);
			String names = "";
			if (null != person) {
				names = (person.getGivenName().trim() + " " + person.getMiddleName()).trim() + " "
				        + person.getFamilyName().trim();
			}
			return names;
		}
		catch (Exception e) {
			log.info(e.getMessage());
			return "";
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param locationId
	 * @return
	 */
	public static String getLocationName(Integer locationId) {
		try {
			if (null == locationId)
				return "";
			Location loc = Context.getLocationService().getLocation(locationId);
			return (loc != null) ? loc.getDisplayString() : "";
		}
		catch (Exception e) {
			log.info(e.getMessage());
			return "";
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param conceptId
	 * @return
	 */
	public static String getConceptName(Integer conceptId) {
		try {
			if (null == conceptId)
				return "-";
			Concept concept = Context.getConceptService().getConcept(conceptId);
			return (concept != null) ? concept.getDisplayString() : "-";
		}
		catch (Exception e) {
			log.info(e.getMessage());
			return "-";
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param conceptId
	 * @return
	 */
	public static String getConceptNameByIdAsString(String conceptId) {
		String res = "-";
		try {
			if (conceptId.trim().compareTo("") == 0)
				return "-";
			Concept concept = Context.getConceptService().getConcept(Integer.parseInt(conceptId));
			res = (concept != null) ? concept.getDisplayString() : "-";
		}
		catch (Exception e) {
			log.info(e.getMessage());
		}
		return res;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param conceptId
	 * @return
	 */
	public static String checkIfConceptExistByIdAsString(String conceptId) {
		String res = "";
		try {
			if (conceptId.trim().compareTo("") == 0)
				return res;
			Concept concept = Context.getConceptService().getConcept(Integer.parseInt(conceptId));
			res = (concept != null) ? "" + concept.getConceptId() : "";
		}
		catch (Exception e) {
			log.info(e.getMessage());
		}
		return res;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param p
	 * @param attributeTypeId
	 * @return
	 */
	public static String personRelationShipType(Integer relationShipTypeId) {
		try {
			RelationshipType rt = null;
			if (relationShipTypeId != null) {
				rt = Context.getPersonService().getRelationshipType(relationShipTypeId);
				if (null != rt)
					return rt.getaIsToB() + " / " + rt.getbIsToA();
				else
					return "-";
			} else
				return "-";
		}
		catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>>VCT>>Module>>Tag>>>> An error occured : " + e.getMessage());
			e.printStackTrace();
			return "-";
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param p
	 * @param attributeTypeId
	 * @return
	 */
	public static String personAttribute(Person p, Integer attributeTypeId) {
		try {
			PersonAttribute pa = null;
			if (p != null)
				pa = p.getAttribute(attributeTypeId);
			if (pa != null) {
				return Context.getConceptService().getConcept(Integer.parseInt(pa.getValue())).getName().getName();
			} else
				return "-";
		}
		catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>>VCT>>Module>>Tag>>>> An error occured : " + e.getMessage());
			e.printStackTrace();
			return "-";
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param p
	 * @param addressTypeId
	 * @return
	 */
	public static String personAddress(Person p, Integer addressTypeId) {
		try {
			PersonAddress pa = null;
			if (p != null)
				pa = p.getPersonAddress();
			if (pa != null) {
				switch (addressTypeId) {
					case 1:
						return (pa.getAddress1().compareToIgnoreCase("") == 0) ? "-" : pa.getAddress1();
					case 2:
						return (pa.getAddress2().compareToIgnoreCase("") == 0) ? "-" : pa.getAddress2();
					case 3:
						return (pa.getCityVillage().compareToIgnoreCase("") == 0) ? "-" : pa.getCityVillage();
					default:
						return "-";
				}
			} else
				return "-";
		}
		catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>>VCT>>Module>>Tag>>>> An error occured : " + e.getMessage());
			e.printStackTrace();
			return "-";
		}
	}
	
	public static String getFullPersonAddress(Person p) {
		
		String address = "-/-/-/-/-/-";
		try {
			//			Country/Province/District/Sector/Cell/Umudugudu
			PersonAddress pa = null;
			if (p != null)
				pa = p.getPersonAddress();
			if (pa != null) {
				address = ((pa.getCountry() != null) ? pa.getCountry() : "-") + " / "
				        + ((pa.getStateProvince() != null) ? pa.getStateProvince() : "-") + " / "
				        + ((pa.getCountyDistrict() != null) ? pa.getCountyDistrict() : "-") + " / "
				        + ((pa.getCityVillage() != null) ? pa.getCityVillage() : "-") + " / "
				        + ((pa.getNeighborhoodCell() != null) ? pa.getNeighborhoodCell() : "-") + " / "
				        + ((pa.getAddress1() != null) ? pa.getAddress1() : "-");
			} else
				return address;
			return address;
		}
		catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>>VCT>>Module>>Tag>>>> An error occured : " + e.getMessage());
			e.printStackTrace();
			return address;
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param obs
	 * @param conceptId
	 * @return
	 */
	public static String convsetObsValueByConcept(Obs obs, Integer conceptId) {
		String res = "-";
		try {
			if (obs != null) {
				for (Obs ob : obs.getGroupMembers()) {
					if (ob.getConcept().getConceptId() == conceptId.intValue()) {
						res = ob.getValueAsString(Context.getLocale());
						break;
					}
					
				}
			}
		}
		catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>>VCT>>Module>>Tag>>>> An error occured : " + e.getMessage());
			e.printStackTrace();
		}
		
		return res;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param gp
	 * @return
	 */
	public static String globalPropertyParser(String gp) {
		String res = "-";
		try {
			res = gp.substring(gp.lastIndexOf(".") + 1);
		}
		catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>>VCT>>Module>>Tag>>>> An error occured : " + e.getMessage());
			e.printStackTrace();
		}
		
		return res;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param from
	 * @param to
	 * @param location
	 * @param admissionMode
	 * @param minAge
	 * @param maxAge
	 * @return
	 */
	public static String getNumberOfNewClientsCounseledAndTestedForHIV(String from, String to, Integer locationId,
	                                                                   Integer admissionMode, Integer minAge,
	                                                                   Integer maxAge, String gender) {
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		Integer res = vms.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, locationId, admissionMode, minAge, maxAge,
		    gender);
		
		return (res != null) ? "" + res : "-";
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param from
	 * @param to
	 * @param location
	 * @param admissionMode
	 * @param minAge
	 * @param maxAge
	 * @return
	 */
	public static String getNumberOfNewClientsTestedAndReceivedResults(String from, String to, Integer locationId,
	                                                                   Integer admissionMode, Integer minAge,
	                                                                   Integer maxAge, String gender) {
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		Integer res = vms.getNumberOfNewClientsTestedAndReceivedResults(from, to, locationId, admissionMode, minAge, maxAge,
		    gender);
		
		return (res != null) ? "" + res : "-";
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param from
	 * @param to
	 * @param location
	 * @param admissionMode
	 * @param minAge
	 * @param maxAge
	 * @return
	 */
	public static String getNumberOfHIVPositiveClients(String from, String to, Integer locationId, Integer admissionMode,
	                                                   Integer minAge, Integer maxAge, String gender) {
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		Integer res = vms.getNumberOfHIVPositiveClients(from, to, locationId, admissionMode, minAge, maxAge, gender);
		
		return (res != null) ? "" + res : "-";
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param from
	 * @param to
	 * @param locationId
	 * @return
	 */
	public static String getNumberOfDiscordantCouples(String from, String to, Integer locationId) {
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		Integer res = vms.getNumberOfDiscordantCouples(from, to, locationId);
		
		return (res != null) ? "" + res : "-";
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param from
	 * @param to
	 * @param locationId
	 * @return
	 */
	public static String getNumberOfCouplesCounseledAndTested(String from, String to, Integer locationId, Integer whoGetTested) {
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		Integer res = vms.getNumberOfCouplesCounseledAndTested(from, to, locationId,whoGetTested);
		
		return (res != null) ? "" + res : "-";
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param from
	 * @param to
	 * @param locationId
	 * @return
	 */
	public static String getNumberOfCouplesCounseled(String from, String to, Integer locationId) {
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		Integer res = vms.getCouplesCounseled(from, to, locationId).size();
		
		return (res != null) ? "" + res/2 : "-";
	}
	
}
