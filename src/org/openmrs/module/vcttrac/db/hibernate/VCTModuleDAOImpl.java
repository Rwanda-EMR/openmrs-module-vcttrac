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
package org.openmrs.module.vcttrac.db.hibernate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Obs;
import org.openmrs.PatientIdentifier;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohtracportal.util.MohTracUtil;
import org.openmrs.module.vcttrac.VCTClient;
import org.openmrs.module.vcttrac.db.VCTModuleDAO;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.module.vcttrac.util.VCTModuleTag;
import org.openmrs.module.vcttrac.util.VCTTracConstant;

/**
 *
 */
public class VCTModuleDAOImpl implements VCTModuleDAO {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	private Session getSession() {
		//		if (getSessionFactory().isClosed())
		//			log.info(">>>>VCT_DAO>> sessionFactory is closed!");
		Session session = getSessionFactory().getCurrentSession();
		if (session == null) {
			//			log.info(">>>>VCT_DAO>> Trying to close the existing session...");
			Context.closeSession();
			//			log.info(">>>>VCT_DAO>> Session closed.");
			//			log.info(">>>>VCT_DAO>> Trying to open new session...");
			Context.openSession();
			//			log.info(">>>>VCT_DAO>> New Session created.");
			try {
				session = getSessionFactory().getCurrentSession();
			}
			catch (Exception e) {
				log.error(">>>>>>>>VCT_DAO>> Session Error : " + session);
				e.printStackTrace();
			}
		}
		return session;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#saveVCTClient(org.openmrs.module.vcttrac.VCTClient)
	 */
	@Override
	public void saveVCTClient(VCTClient client) {
		if (client.getTracVctClientId() == null) {
			UUID uid = UUID.randomUUID();
			client.setUuid(uid.toString());
		} else {
			client.setDateChanged(new Date());
			client.setChangedBy(Context.getAuthenticatedUser());
		}
		getSession().saveOrUpdate(client);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getVCTClientsFromPIT()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getVCTClientsFromPIT() {
		List<Integer> clientsCode = getSession().createSQLQuery(
		    "SELECT trac_vct_client_id FROM trac_vct_client WHERE vct_or_pit=1 AND archived IS FALSE AND voided IS FALSE")
		        .list();
		
		return clientsCode;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getVoluntaryClients()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getVoluntaryClients() {
		List<Integer> clientsCode = getSession().createSQLQuery(
		    "SELECT trac_vct_client_id FROM trac_vct_client WHERE vct_or_pit=0 AND archived IS FALSE AND voided IS FALSE")
		        .list();
		
		return clientsCode;
	}
	
	@SuppressWarnings("unchecked")
	public List<Person> getPeople(String name, Boolean dead, Boolean counseled) {
		name = name.replace(", ", " ");
		String[] names = name.split(" ");
		List<Integer> personIds = null;
		
		String query = "";
		
		query = "SELECT DISTINCT(pn.person_id) FROM person_name pn INNER JOIN person p ON pn.person_id=p.person_id "
		        + "INNER JOIN trac_vct_client t ON p.person_id=t.client_id " + "WHERE t.code_client='" + name + "'";
		if (counseled == false) {
			query += " AND t.obs_id_counseling IS NULL";
		}
		
		//log.info(">>>>>>>>>>>>>>>>>>>QUERY : " + query);
		
		personIds = getSession().createSQLQuery(query).list();
		
		if (personIds == null || personIds.size() == 0) {
			query = "SELECT DISTINCT(pn.person_id) FROM person_name pn INNER JOIN person p ON pn.person_id=p.person_id "
			        + "INNER JOIN trac_vct_client t ON p.person_id=t.client_id " + "WHERE ";
			
			if (counseled == false) {
				query += "t.obs_id_counseling IS NULL ";
			}
			
			int i = 0;
			for (String n : names) {
				if (n != null && n.length() > 0) {
					if (i > 0 || counseled == false)
						query += "AND ";
					query += "pn.given_name LIKE '" + n + "%' OR pn.middle_name LIKE '" + n + "%' OR pn.family_name LIKE '"
					        + n + "%' ";
					i++;
				}
			}
			
			query += "ORDER BY pn.given_name;";
			//			log.info(">>>>>>>>>>>>>>>>>>>QUERY 1 : " + query);
			personIds = getSession().createSQLQuery(query).list();
		}
		List<Person> personList = new ArrayList<Person>();
		for (Integer id : personIds)
			personList.add(Context.getPersonService().getPerson(id));
		
		return personList;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getAllClients()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<VCTClient> getAllClients() {
		List<VCTClient> allClients = getSession().createCriteria(VCTClient.class).list();
		
		return allClients;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getClientById()
	 */
	@Override
	public VCTClient getClientAtLastVisitByClientId(Integer clientId) {
		Integer i = (Integer) getSession().createSQLQuery(
		    "SELECT MAX(trac_vct_client_id) FROM trac_vct_client WHERE client_id=" + clientId).uniqueResult();
		
		VCTClient client = (VCTClient) getSession().load(VCTClient.class, i);
		
		return client;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getAllClientCodeWithoutHivTestResult()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getAllClientCodeWithoutHivTestResult() {
		List<String> clientsCode = getSession()
		        .createSQLQuery(
		            "SELECT code_test FROM trac_vct_client WHERE code_test IS NOT NULL AND obs_id_result IS NULL AND archived IS FALSE AND voided IS FALSE")
		        .list();
		
		return clientsCode;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getClientByCodeTest(java.lang.String)
	 */
	@Override
	public VCTClient getClientByCodeTest(String clientCode) {
		
		VCTClient client = (VCTClient) getSession().createCriteria(VCTClient.class).add(
		    Restrictions.eq("codeTest", clientCode)).uniqueResult();
		
		return client;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getAllClientCodeForReceptionOfResult()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getAllClientCodeForReceptionOfResult() {
		List<String> clientsCode = new ArrayList<String>();
		//		getSession()
		//		        .createSQLQuery(
		//		            "SELECT code_test FROM trac_vct_client WHERE code_test IS NOT NULL AND obs_id_result IS NOT NULL AND archived IS FALSE AND voided IS FALSE")
		//		        .list();
		
		List<VCTClient> clientList = getSession().createCriteria(VCTClient.class).add(Restrictions.isNotNull("codeTest"))
		        .add(Restrictions.isNotNull("resultObs")).add(Restrictions.eq("archived", false)).add(
		            Restrictions.eq("voided", false)).list();
		
		for (VCTClient c : clientList)
			if (VCTModuleTag.convsetObsValueByConcept(c.getResultObs(),
			    VCTConfigurationUtil.getDateResultOfHivTestReceivedConceptId()).compareTo("-") == 0)
				clientsCode.add(c.getCodeTest());
		
		return clientsCode;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getAllClientId()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getAllClientId() {
		List<Integer> clientsCode = getSession().createSQLQuery(
		    "SELECT trac_vct_client_id FROM trac_vct_client WHERE archived IS FALSE AND voided IS FALSE").list();
		
		return clientsCode;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getClientById(java.lang.Integer)
	 */
	@Override
	public VCTClient getClientById(Integer clientId) {
		VCTClient client = (VCTClient) getSession().load(VCTClient.class, clientId);
		
		return client;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getClientVisitByPersonId()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<VCTClient> getClientVisitByPersonId(Integer personId) {
		
		List<VCTClient> client = getSession().createCriteria(VCTClient.class).add(
		    Restrictions.eq("client", Context.getPersonService().getPerson(personId))).list();
		
		return client;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getVCTClientsBasedOnGender(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getVCTClientsBasedOnGender(String gender, Date registrationDate) {
		String query = "SELECT trac_vct_client_id FROM trac_vct_client cl INNER JOIN person p ON cl.client_id=p.person_id "
		        + "WHERE p.gender='" + gender + "'";//+ "' AND cl.archived IS FALSE AND cl.voided IS FALSE ";
		
		query += (registrationDate != null) ? " AND date_registration='"
		        + MohTracUtil.getMySQLDateFormat().format(registrationDate) + "'" : "";
		
		List<Integer> clientsCode = getSession().createSQLQuery(query).list();
		
		return clientsCode;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getVCTClientsBasedOnAttributeType(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getVCTClientsBasedOnAttributeType(Integer attibuteTypeId, Integer value) {
		List<Integer> clientsCode = getSession().createSQLQuery(
		    "SELECT DISTINCT trac_vct_client_id FROM trac_vct_client cl "
		            + "INNER JOIN person_attribute pa ON cl.client_id=pa.person_id "
		            + "WHERE cl.archived IS FALSE AND cl.voided IS FALSE AND pa.person_attribute_type_id=" + attibuteTypeId
		            + " AND pa.value=" + value + ";").list();
		
		return clientsCode;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getVCTClientsBasedOnConceptObs(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getVCTClientsBasedOnConceptObs(Integer conceptObsId, Integer value, Boolean gotResult) {
		String query = "";
		if (null != value) {
			query = "select distinct trac_vct_client_id from trac_vct_client cl INNER JOIN obs o ON cl.client_id=o.person_id "
			        + "WHERE cl.voided IS FALSE AND o.voided IS FALSE AND o.concept_id="
			        + conceptObsId
			        + " AND o.value_coded=" + value;
			
			query += (gotResult) ? " AND cl.archived=1" : " AND cl.archived IS FALSE";
		} else {
			query = "select distinct trac_vct_client_id from trac_vct_client cl INNER JOIN obs o ON cl.client_id=o.person_id "
			        + "WHERE cl.voided IS FALSE AND o.voided IS FALSE AND o.concept_id="
			        + conceptObsId
			        + " AND o.value_coded IS NOT NULL";
			
			query += (gotResult) ? " AND cl.archived=1" : " AND cl.archived IS FALSE";
		}
		
		List<Integer> clientsCode = getSession().createSQLQuery(query).list();
		
		return clientsCode;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getVCTClientsTested(java.lang.Boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getVCTClientsTested(Boolean tested) {
		List<Integer> clientsCode = getSession().createSQLQuery(
		    "SELECT DISTINCT trac_vct_client_id FROM trac_vct_client WHERE archived IS FALSE AND voided IS FALSE "
		            + ((tested) ? " AND code_test IS NOT NULL" : " AND code_test IS NULL")).list();
		
		return clientsCode;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getVCTClientsBasedOnCounselingType(java.lang.Integer)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getVCTClientsBasedOnCounselingType(Integer counselingType, Date registrationDate) {
		String query = "";
		if (counselingType.intValue() < 3)
			query = "SELECT trac_vct_client_id FROM trac_vct_client WHERE type_of_counseling=" + counselingType;
		//			        + " AND archived IS FALSE AND voided IS FALSE";
		else
			query = "SELECT trac_vct_client_id FROM trac_vct_client WHERE type_of_counseling IS NULL";// AND archived IS FALSE AND voided IS FALSE";
			
		query += (registrationDate != null) ? " AND date_registration='"
		        + MohTracUtil.getMySQLDateFormat().format(registrationDate) + "'" : "";
		
		List<Integer> clientsCode = getSession().createSQLQuery(query).list();
		
		return clientsCode;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#synchronizePatientsAndClients(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	public void synchronizePatientsAndClients(Integer formerPersonId, Integer newPersonId) {
		int i = 0;
		i = getSession().createSQLQuery(
		    "UPDATE person_address SET person_id=" + newPersonId + " WHERE person_id=" + formerPersonId).executeUpdate();
		log.info("----------------> Update Person_address : " + i + " rows");
		i = getSession().createSQLQuery(
		    "UPDATE person_attribute SET person_id=" + newPersonId + " WHERE person_id=" + formerPersonId).executeUpdate();
		log.info("----------------> Update Person_attribute : " + i + " rows");
		i = getSession().createSQLQuery("UPDATE obs SET person_id=" + newPersonId + " WHERE person_id=" + formerPersonId)
		        .executeUpdate();
		log.info("----------------> Update Obs : " + i + " rows");
		i = getSession().createSQLQuery(
		    "UPDATE relationship SET person_a=" + newPersonId + " WHERE person_a=" + formerPersonId).executeUpdate();
		log.info("----------------> Update Relationship : " + i + " rows");
		i = getSession().createSQLQuery(
		    "UPDATE relationship SET person_b=" + newPersonId + " WHERE person_b=" + formerPersonId).executeUpdate();
		log.info("----------------> Update Relationship : " + i + " rows");
		i = getSession().createSQLQuery(
		    "UPDATE trac_vct_client SET client_id=" + newPersonId + " WHERE client_id=" + formerPersonId).executeUpdate();
		log.info("----------------> Update VCT Client : " + i + " rows");
		i = getSession().createSQLQuery(
		    "UPDATE trac_vct_client SET partner_id=" + newPersonId + " WHERE partner_id=" + formerPersonId).executeUpdate();
		log.info("----------------> Update VCT Client : " + i + " rows");
		i = getSession().createSQLQuery("DELETE FROM person_name WHERE person_id=" + formerPersonId).executeUpdate();
		log.info("----------------> DELETE Person_name : " + i + " rows");
		i = getSession().createSQLQuery("DELETE FROM person WHERE person_id=" + formerPersonId).executeUpdate();
		log.info("----------------> Delete Person : " + i + " rows");
		
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getNumberOfClientByVCTOrPIT(java.lang.Integer,
	 *      java.util.Date)
	 */
	public Integer getNumberOfClientByVCTOrPIT(Integer vctorpit, Date startingFrom) {
		
		String query = "SELECT COUNT(c.client_id) FROM trac_vct_client c";
		
		if (vctorpit.intValue() == 1)
			query += " WHERE c.vct_or_pit=0";
		else if (vctorpit.intValue() == 2)
			query += " WHERE c.vct_or_pit=1";
		if (startingFrom != null && vctorpit.intValue() > 0)
			query += " AND c.date_registration='" + MohTracUtil.getMySQLDateFormat().format(startingFrom) + "'";
		else if (startingFrom != null && vctorpit.intValue() == 0)
			query += " WHERE c.date_registration='" + MohTracUtil.getMySQLDateFormat().format(startingFrom) + "'";
		
		//		log.info(">>>>>>>>>>>>>GETTING>>NUMBER>>OF>>CLIENTS>>>> Query : "+vctorpit+" - "+startingFrom+" - "+query);
		String s = (getSession().createSQLQuery(query).uniqueResult()).toString();
		//log.info(">>>>>>>>>>>>>GETTING>>NUMBER>>OF>>CLIENTS>>>> Result : " + vctorpit + " - " + startingFrom + s);
		
		return Integer.valueOf(s);
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getVCTClientsWaitingFromHIVTest()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getVCTClientsWaitingFromHIVTest() {
		List<Integer> clientsCode = getSession()
		        .createSQLQuery(
		            "SELECT DISTINCT trac_vct_client_id FROM trac_vct_client WHERE archived IS FALSE AND voided IS FALSE "
		                    + " AND obs_id_counseling IS NOT NULL AND client_decision IS NULL ORDER BY date_registration DESC, code_test ASC")
		        .list();
		
		return clientsCode;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getClientByClientCode(java.lang.String)
	 */
	@Override
	public VCTClient getClientByClientCode(String clientCode) {
		
		VCTClient client = (VCTClient) getSession().createCriteria(VCTClient.class).add(
		    Restrictions.eq("codeClient", clientCode)).uniqueResult();
		
		return client;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getVCTClientsWaitingToBeEnrolledInHIVProgram()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getVCTClientsWaitingToBeEnrolledInHIVProgram() {
		List<Integer> clientsCode = getSession().createSQLQuery(
		    "SELECT DISTINCT trac_vct_client_id FROM trac_vct_client WHERE archived IS FALSE AND voided IS FALSE "
		            + " AND client_decision=1 ORDER BY date_registration DESC").list();
		
		return clientsCode;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getNumberOfNewClientsCounseledAndTestedForHIV(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@SuppressWarnings( { "unchecked" })
	@Override
	public Integer getNumberOfNewClientsCounseledAndTestedForHIV(String from, String to, Integer locationId,
	                                                             Integer admissionMode, Integer minAge, Integer maxAge,
	                                                             String gender) {
		
		List<VCTClient> result = new ArrayList<VCTClient>();
		
		try {
			List<VCTClient> clientList = getSession().createCriteria(VCTClient.class).add(
			    Restrictions.eq("vctOrPit", (admissionMode.intValue() == 1))).add(
			    Restrictions.eq("location", Context.getLocationService().getLocation(locationId))).add(
			    Restrictions.isNotNull("counselingObs")).add(Restrictions.isNotNull("codeTest")).add(
			    Restrictions.between("dateOfRegistration", Context.getDateFormat().parse(from), Context.getDateFormat()
			            .parse(to))).list();
			
			for (VCTClient c : clientList) {
				Person p = c.getClient();
				if (p.getGender().compareToIgnoreCase(gender) == 0 && (p.getAge() >= minAge))
					if (maxAge > 0) {
						if (p.getAge() < maxAge)
							result.add(c);
					} else
						result.add(c);
			}
		}
		catch (Exception e) {
			log.error(">>>VCT>>Number>>of>>new>>clients>>counseled>>and>>tested>>for>>hiv>> from: " + from + ", to: " + to
			        + ", location: " + locationId + ", admissionMode: " + admissionMode + ", minAge: " + minAge
			        + ", maxAge: " + maxAge + ", gender: " + gender);
			e.printStackTrace();
		}
		return result.size();
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getNumberOfNewClientsTestedAndReceivedResults(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@SuppressWarnings( { "unchecked" })
	@Override
	public Integer getNumberOfNewClientsTestedAndReceivedResults(String from, String to, Integer locationId,
	                                                             Integer admissionMode, Integer minAge, Integer maxAge,
	                                                             String gender) {
		
		List<VCTClient> result = new ArrayList<VCTClient>();
		
		try {
			List<VCTClient> clientList = getSession().createCriteria(VCTClient.class).add(
			    Restrictions.eq("vctOrPit", (admissionMode.intValue() == 1))).add(
			    Restrictions.eq("location", Context.getLocationService().getLocation(locationId))).add(
			    Restrictions.isNotNull("codeTest")).add(Restrictions.isNotNull("resultObs")).add(
			    Restrictions.between("dateOfRegistration", Context.getDateFormat().parse(from), Context.getDateFormat()
			            .parse(to))).list();
			
			for (VCTClient c : clientList) {
				Person p = c.getClient();
				if (p.getGender().compareToIgnoreCase(gender) == 0 && (p.getAge() >= minAge))
					if (maxAge > 0) {
						if (p.getAge() < maxAge) {
							if (VCTModuleTag.convsetObsValueByConcept(c.getResultObs(),
							    VCTConfigurationUtil.getDateResultOfHivTestReceivedConceptId()).compareTo("-") != 0)
								result.add(c);
						}
					} else {
						if (VCTModuleTag.convsetObsValueByConcept(c.getResultObs(),
						    VCTConfigurationUtil.getDateResultOfHivTestReceivedConceptId()).compareTo("-") != 0)
							result.add(c);
					}
			}
		}
		catch (Exception e) {
			log.error(">>>VCT>>Number>>of>>new>>clients>>counseled>>and>>tested>>for>>hiv>> from: " + from + ", to: " + to
			        + ", location: " + locationId + ", admissionMode: " + admissionMode + ", minAge: " + minAge
			        + ", maxAge: " + maxAge + ", gender: " + gender);
			e.printStackTrace();
		}
		return result.size();
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getNumberOfHIVPositiveClients(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@SuppressWarnings( { "unchecked" })
	@Override
	public Integer getNumberOfHIVPositiveClients(String from, String to, Integer locationId, Integer admissionMode,
	                                             Integer minAge, Integer maxAge, String gender) {
		
		List<VCTClient> result = new ArrayList<VCTClient>();
		
		try {
			List<VCTClient> clientList = getSession().createCriteria(VCTClient.class).add(
			    Restrictions.eq("vctOrPit", (admissionMode.intValue() == 1))).add(
			    Restrictions.eq("location", Context.getLocationService().getLocation(locationId))).add(
			    Restrictions.isNotNull("resultObs")).add(
			    Restrictions.between("dateOfRegistration", Context.getDateFormat().parse(from), Context.getDateFormat()
			            .parse(to))).list();
			
			for (VCTClient c : clientList) {
				Person p = c.getClient();
				if (p.getGender().compareToIgnoreCase(gender) == 0 && (p.getAge() >= minAge))
					if (maxAge > 0) {
						if (p.getAge() < maxAge) {
							if (VCTModuleTag.convsetObsValueByConcept(c.getResultObs(), VCTTracConstant.RESULT_OF_HIV_TEST)
							        .compareTo(
							            Context.getConceptService().getConcept(VCTTracConstant.POSITIVE_CID)
							                    .getDisplayString()) == 0)
								result.add(c);
						}
					} else {
						if (VCTModuleTag.convsetObsValueByConcept(c.getResultObs(), VCTTracConstant.RESULT_OF_HIV_TEST)
						        .compareTo(
						            Context.getConceptService().getConcept(VCTTracConstant.POSITIVE_CID).getDisplayString()) == 0)
							result.add(c);
					}
			}
		}
		catch (Exception e) {
			log.error(">>>VCT>>Number>>of>>new>>clients>>counseled>>and>>tested>>for>>hiv>> from: " + from + ", to: " + to
			        + ", location: " + locationId + ", admissionMode: " + admissionMode + ", minAge: " + minAge
			        + ", maxAge: " + maxAge + ", gender: " + gender);
			e.printStackTrace();
		}
		return result.size();
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getNumberOfCouplesCounseledAndTested(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	@Override
	public Integer getNumberOfCouplesCounseledAndTested(String from, String to, Integer locationId, int whoGetTested) {
		//		try {
		//			List<VCTClient> couples = new ArrayList<VCTClient>();
		//			
		//			List<VCTClient> clientList = getSession().createCriteria(VCTClient.class).add(
		//			    Restrictions.eq("typeOfCounseling", 2)).add(
		//			    Restrictions.eq("location", Context.getLocationService().getLocation(locationId))).add(
		//			    Restrictions.isNotNull("counselingObs")).add(Restrictions.isNotNull("codeTest")).add(
		//			    Restrictions.between("dateOfRegistration", Context.getDateFormat().parse(from), Context.getDateFormat()
		//			            .parse(to))).list();
		//			couples = clientList;
		//			
		//		}
		//		catch (Exception e) {
		//			log.error(">>>VCT>>Number>>of>>Couples>>Counseled>>And>>Tested>>for>>hiv>> from: " + from + ", to: " + to
		//			        + ", location: " + locationId);
		//			e.printStackTrace();
		//		}
		
		List<VCTClient> result = new ArrayList<VCTClient>();
		int all = 0, oneOfThem = 0, noneOfThem = 0, res = 0;
		
		try {
			List<VCTClient> couplesCounseled = getCouplesCounseled(from, to, locationId);
			//			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Couples counseled       : "+couplesCounseled.size());
			
			//for (VCTClient c : couplesCounseled) {
			int index = 0;
			while (couplesCounseled.size() > index) {
				res = checkIfClientAndPartnerGetTested(couplesCounseled, couplesCounseled.get(index));
				
				if (res == 0)
					noneOfThem += 1;
				else if (res == 1)
					oneOfThem += 1;
				else if (res == 2)
					all += 1;
				
				index += 1;
			}
			//}
			
			//			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>All get tested          : "+all);
			//			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>One of them get tested  : "+oneOfThem);
			//			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>None of them get tested : "+noneOfThem);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if (whoGetTested == 0)
			return noneOfThem;
		else if (whoGetTested == 1)
			return oneOfThem;
		else
			return all;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getNumberOfDiscordantCouples(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	@Override
	public Integer getNumberOfDiscordantCouples(String from, String to, Integer locationId) {
		return 0;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getCouplesCounseled(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<VCTClient> getCouplesCounseled(String from, String to, Integer locationId) {
		List<VCTClient> couplesCounseled = new ArrayList<VCTClient>();
		try {
			couplesCounseled = getSession().createCriteria(VCTClient.class).add(Restrictions.eq("typeOfCounseling", 2)).add(
			    Restrictions.isNotNull("counselingObs")).add(
			    Restrictions.eq("location", Context.getLocationService().getLocation(locationId))).add(
			    Restrictions.between("dateOfRegistration", Context.getDateFormat().parse(from), Context.getDateFormat()
			            .parse(to))).list();
		}
		catch (Exception e) {
			log.error(">>>VCT>>Number>>of>>Couples>>Counseled>>And>>Tested>>for>>hiv>> from: " + from + ", to: " + to
			        + ", location: " + locationId);
			e.printStackTrace();
		}
		return couplesCounseled;
	}
	
	private int checkIfClientAndPartnerGetTested(List<VCTClient> couplesCounseled, VCTClient client) {
		boolean clientTested = false, partnerTested = false, found = false;
		int partnersTested = 0;
		
		for (VCTClient c : couplesCounseled) {
			if (c.getTracVctClientId() == client.getTracVctClientId()) {
				clientTested = (c.getCodeTest() != null) ? true : false;
				for (VCTClient cl : couplesCounseled) {
					if (c.getPartner().getPersonId() == cl.getClient().getPersonId()
					        && cl.getPartner().getPersonId() == c.getClient().getPersonId()) {
						partnerTested = (cl.getCodeTest() != null) ? true : false;
						couplesCounseled.remove(cl);
						found = true;
					}
					if (found)
						break;
				}
			}
			if (found)
				break;
		}
		
		partnersTested += (clientTested) ? 1 : 0;
		partnersTested += (partnerTested) ? 1 : 0;
		
		return partnersTested;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getCouplesCounseledAndTested(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<VCTClient> getCouplesCounseledAndTested(String from, String to, Integer locationId, int whoGetTested) {
		List<VCTClient> result = new ArrayList<VCTClient>();
		int all = 0, oneOfThem = 0, noneOfThem = 0, res = 0;
		
		try {
			List<VCTClient> couplesCounseled = getCouplesCounseled(from, to, locationId);
			//			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Couples counseled       : " + couplesCounseled.size());
			
			//for (VCTClient c : couplesCounseled) {
			int index = 0;
			while (couplesCounseled.size() > index) {
				res = checkIfClientAndPartnerGetTested(couplesCounseled, couplesCounseled.get(index));
				
				if (res == 0)
					noneOfThem += 1;
				else if (res == 1)
					oneOfThem += 1;
				else if (res == 2)
					all += 1;
				
				index += 1;
			}
			//}
			
			//			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>All get tested          : "+all);
			//			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>One of them get tested  : "+oneOfThem);
			//			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>None of them get tested : "+noneOfThem);
			
			//			getSession().createCriteria(VCTClient.class).add(Restrictions.eq("typeOfCounseling", 2)).add(
			//			    Restrictions.eq("location", Context.getLocationService().getLocation(locationId))).add(
			//			    Restrictions.isNotNull("counselingObs")).add(Restrictions.isNotNull("codeTest")).add(
			//			    Restrictions.between("dateOfRegistration", Context.getDateFormat().parse(from), Context.getDateFormat()
			//			            .parse(to))).addOrder(Order.asc("dateOfRegistration")).list();
			//			result = couplesCounseled;
			
		}
		catch (Exception e) {
			log.error(">>>VCT>>Number>>of>>Couples>>Counseled>>And>>Tested>>for>>hiv>> from: " + from + ", to: " + to
			        + ", location: " + locationId);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getDiscordantCouples(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<VCTClient> getDiscordantCouples(String from, String to, Integer locationId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getHIVPositiveClients(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VCTClient> getHIVPositiveClients(String from, String to, Integer locationId, Integer admissionMode,
	                                             Integer minAge, Integer maxAge, String gender) {
		List<VCTClient> result = new ArrayList<VCTClient>();
		
		try {
			List<VCTClient> clientList = getSession().createCriteria(VCTClient.class).add(
			    Restrictions.eq("vctOrPit", (admissionMode.intValue() == 1))).add(
			    Restrictions.eq("location", Context.getLocationService().getLocation(locationId))).add(
			    Restrictions.isNotNull("resultObs")).add(
			    Restrictions.between("dateOfRegistration", Context.getDateFormat().parse(from), Context.getDateFormat()
			            .parse(to))).list();
			
			for (VCTClient c : clientList) {
				Person p = c.getClient();
				if (p.getGender().compareToIgnoreCase(gender) == 0 && (p.getAge() >= minAge))
					if (maxAge > 0) {
						if (p.getAge() < maxAge) {
							if (VCTModuleTag.convsetObsValueByConcept(c.getResultObs(), VCTTracConstant.RESULT_OF_HIV_TEST)
							        .compareTo(
							            Context.getConceptService().getConcept(VCTTracConstant.POSITIVE_CID)
							                    .getDisplayString()) == 0)
								result.add(c);
						}
					} else {
						if (VCTModuleTag.convsetObsValueByConcept(c.getResultObs(), VCTTracConstant.RESULT_OF_HIV_TEST)
						        .compareTo(
						            Context.getConceptService().getConcept(VCTTracConstant.POSITIVE_CID).getDisplayString()) == 0)
							result.add(c);
					}
			}
		}
		catch (Exception e) {
			log.error(">>>VCT>>Number>>of>>new>>clients>>counseled>>and>>tested>>for>>hiv>> from: " + from + ", to: " + to
			        + ", location: " + locationId + ", admissionMode: " + admissionMode + ", minAge: " + minAge
			        + ", maxAge: " + maxAge + ", gender: " + gender);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getNewClientsCounseledAndTestedForHIV(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VCTClient> getNewClientsCounseledAndTestedForHIV(String from, String to, Integer locationId,
	                                                             Integer admissionMode, Integer minAge, Integer maxAge,
	                                                             String gender) {
		List<VCTClient> result = new ArrayList<VCTClient>();
		
		try {
			List<VCTClient> clientList = getSession().createCriteria(VCTClient.class).add(
			    Restrictions.eq("vctOrPit", (admissionMode.intValue() == 1))).add(
			    Restrictions.eq("location", Context.getLocationService().getLocation(locationId))).add(
			    Restrictions.isNotNull("counselingObs")).add(Restrictions.isNotNull("codeTest")).add(
			    Restrictions.between("dateOfRegistration", Context.getDateFormat().parse(from), Context.getDateFormat()
			            .parse(to))).list();
			
			for (VCTClient c : clientList) {
				Person p = c.getClient();
				if (p.getGender().compareToIgnoreCase(gender) == 0 && (p.getAge() >= minAge))
					if (maxAge > 0) {
						if (p.getAge() < maxAge)
							result.add(c);
					} else
						result.add(c);
			}
		}
		catch (Exception e) {
			log.error(">>>VCT>>Number>>of>>new>>clients>>counseled>>and>>tested>>for>>hiv>> from: " + from + ", to: " + to
			        + ", location: " + locationId + ", admissionMode: " + admissionMode + ", minAge: " + minAge
			        + ", maxAge: " + maxAge + ", gender: " + gender);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getNewClientsTestedAndReceivedResults(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VCTClient> getNewClientsTestedAndReceivedResults(String from, String to, Integer locationId,
	                                                             Integer admissionMode, Integer minAge, Integer maxAge,
	                                                             String gender) {
		List<VCTClient> result = new ArrayList<VCTClient>();
		
		try {
			List<VCTClient> clientList = getSession().createCriteria(VCTClient.class).add(
			    Restrictions.eq("vctOrPit", (admissionMode.intValue() == 1))).add(
			    Restrictions.eq("location", Context.getLocationService().getLocation(locationId))).add(
			    Restrictions.isNotNull("codeTest")).add(Restrictions.isNotNull("resultObs")).add(
			    Restrictions.between("dateOfRegistration", Context.getDateFormat().parse(from), Context.getDateFormat()
			            .parse(to))).list();
			
			for (VCTClient c : clientList) {
				Person p = c.getClient();
				if (p.getGender().compareToIgnoreCase(gender) == 0 && (p.getAge() >= minAge))
					if (maxAge > 0) {
						if (p.getAge() < maxAge) {
							if (VCTModuleTag.convsetObsValueByConcept(c.getResultObs(),
							    VCTConfigurationUtil.getDateResultOfHivTestReceivedConceptId()).compareTo("-") != 0)
								result.add(c);
						}
					} else {
						if (VCTModuleTag.convsetObsValueByConcept(c.getResultObs(),
						    VCTConfigurationUtil.getDateResultOfHivTestReceivedConceptId()).compareTo("-") != 0)
							result.add(c);
					}
			}
		}
		catch (Exception e) {
			log.error(">>>VCT>>Number>>of>>new>>clients>>counseled>>and>>tested>>for>>hiv>> from: " + from + ", to: " + to
			        + ", location: " + locationId + ", admissionMode: " + admissionMode + ", minAge: " + minAge
			        + ", maxAge: " + maxAge + ", gender: " + gender);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getClientByNID(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public VCTClient getClientByNID(String nid) {
		VCTClient client = (VCTClient) getSession().createCriteria(VCTClient.class).add(Restrictions.eq("nid", nid))
		        .uniqueResult();
		if (null == client) {
			try {
				List<PatientIdentifier> piList = Context.getPatientService().getPatientIdentifiers(nid,
				    Context.getPatientService().getPatientIdentifierType(VCTConfigurationUtil.getNIDIdentifierTypeId()));
				
				if (piList != null && piList.size() > 0) {
					client = (VCTClient) getSession().load(VCTClient.class, piList.get(0).getPatient().getPersonId());
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return client;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getPersonIdByNID(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Integer getPersonIdByNID(String nid) {
		try {
			VCTClient client = null;
			
			List<VCTClient> clientList = (List<VCTClient>) getSession().createCriteria(VCTClient.class).add(
			    Restrictions.eq("nid", nid)).list();
			if (clientList != null && clientList.size() > 0)
				client = clientList.get(0);
			
			if (null == client) {
				
				List<PatientIdentifier> piList = Context.getPatientService().getPatientIdentifiers(nid,
				    Context.getPatientService().getPatientIdentifierType(VCTConfigurationUtil.getNIDIdentifierTypeId()));
				
				if (piList != null && piList.size() > 0) {
					return piList.get(0).getPatient().getPersonId();
				}
				return null;
				
			}
			
			return client.getClient().getPersonId();
		}
		catch (Exception e) {
			log.error(">>>VCT>>GET>>CLIENT>>ID>>BY>>NID>> An error occured : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	private String whereOrAnd() {
		if (whereCreated == true)
			return " AND ";
		else {
			whereCreated = true;
			return " WHERE ";
		}
	}
	
	private boolean whereCreated = false;
	
	/**
	 * @throws Exception
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getVCTClientBasedOn(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getVCTClientsBasedOn(String reference, String gender, Integer counselingType, Integer location,
	                                          String tested, String dateFrom, String dateTo, Integer minAge, Integer maxAge,
	                                          Integer civilStatus, Integer educationLevel, Integer mainActivity,
	                                          Integer testOrderer, Integer whyGetTested, Integer testResult,
	                                          Integer gotResult) throws Exception {
		List<Integer> clientIds = new ArrayList<Integer>();
		List<Integer> tempIds = new ArrayList<Integer>();
		String query = "";
		whereCreated = false;
		DateFormat df = MohTracUtil.getMySQLDateFormat();
		
		query = "SELECT c.trac_vct_client_id FROM trac_vct_client c";
		//		boolean whereCreated = false;
		
		//location
		if (location != null)
			query += whereOrAnd() + " c.location=" + location;
		//date from
		if (dateFrom != null) {
			query += whereOrAnd() + " c.date_registration>='" + df.format(Context.getDateFormat().parse(dateFrom)) + "'";
		}
		//date to
		if (dateTo != null) {
			query += whereOrAnd() + " c.date_registration<='" + df.format(Context.getDateFormat().parse(dateTo)) + "'";
		}
		//from vct or pit
		if (reference != null)
			query += whereOrAnd() + " c.vct_or_pit=" + reference;
		//counseling
		if (counselingType != null) {
			if (counselingType < 3)
				query += whereOrAnd() + " c.type_of_counseling=" + counselingType;
			else
				query += whereOrAnd() + " c.type_of_counseling IS NULL";
		}
		//hiv test
		if (tested != null) {
			if (tested.compareToIgnoreCase("yes") == 0)
				query += whereOrAnd() + " c.code_test IS NOT NULL";
			else
				query += whereOrAnd() + " c.code_test IS NULL";
		}
		
		//ordering
		query += " ORDER BY c.date_registration DESC";
		
		//log.info(">>>>STATISTICS>>QUERY>> " + query);
		
		tempIds = getSession().createSQLQuery(query).list();
		clientIds = tempIds;
		
		VCTClient c;
		boolean addToTheList;
		
		if (gender != null || minAge != null || maxAge != null || civilStatus != null || educationLevel != null
		        || mainActivity != null || testOrderer != null || whyGetTested != null || testResult != null
		        || gotResult != null) {
			clientIds = new ArrayList<Integer>();
			for (Integer cid : tempIds) {
				c = (VCTClient) getSession().load(VCTClient.class, cid);
				addToTheList = true;
				
				//gender
				if (gender != null) {
					if (c.getClient().getGender().compareToIgnoreCase(gender) != 0) {
						addToTheList = false;
					}
				}
				//minAge : minAge is included
				if (minAge != null) {
					if (c.getClient().getAge() > minAge) {
						addToTheList = false;
					}
				}
				//maxAge : maxAge is included
				if (maxAge != null) {
					if (c.getClient().getAge() < maxAge) {
						addToTheList = false;
					}
				}
				
				//civil status
				if (civilStatus != null) {
					PersonAttribute pa = new PersonAttribute();
					pa = c.getClient().getAttribute(VCTConfigurationUtil.getCivilStatusAttributeTypeId());
					if (pa != null) {
						if (pa.getValue().trim().compareToIgnoreCase("") != 0) {
							if (civilStatus.toString().compareToIgnoreCase(pa.getValue()) != 0)
								addToTheList = false;
						} else
							addToTheList = false;
					} else
						addToTheList = false;
				}
				
				//education level
				if (educationLevel != null) {
					PersonAttribute pa1 = c.getClient()
					        .getAttribute(VCTConfigurationUtil.getEducationLevelAttributeTypeId());
					if (pa1 != null) {
						if (pa1.getValue().trim().compareToIgnoreCase("") != 0) {
							if (educationLevel.toString().compareToIgnoreCase(pa1.getValue()) != 0)
								addToTheList = false;
						} else
							addToTheList = false;
					} else
						addToTheList = false;
				}
				
				//main activity
				if (mainActivity != null) {
					PersonAttribute pa2 = c.getClient().getAttribute(VCTConfigurationUtil.getMainActivityAttributeTypeId());
					if (pa2 != null) {
						if (pa2.getValue().trim().compareToIgnoreCase("") != 0) {
							if (mainActivity.toString().compareToIgnoreCase(pa2.getValue()) != 0)
								addToTheList = false;
						} else
							addToTheList = false;
					} else
						addToTheList = false;
				}
				
				//program that ordered test
				if (testOrderer != null) {
					Obs o = c.getCounselingObs();
					if (o != null) {
						if (VCTModuleTag.convsetObsValueByConcept(o,
						    VCTConfigurationUtil.getProgramThatOrderedTestConceptId()).compareToIgnoreCase(
						    Context.getConceptService().getConcept(testOrderer).getDisplayString()) != 0)
							addToTheList = false;
					} else
						addToTheList = false;
				}
				
				//why get tested for hiv
				if (whyGetTested != null) {
					Obs o = c.getCounselingObs();
					if (o != null) {
						if (VCTModuleTag
						        .convsetObsValueByConcept(o, VCTConfigurationUtil.getWhyDidYouGetTestedConceptId())
						        .compareToIgnoreCase(Context.getConceptService().getConcept(whyGetTested).getDisplayString()) != 0)
							addToTheList = false;
					} else
						addToTheList = false;
				}
				
				//result of test
				if (testResult != null) {
					Obs o = c.getResultObs();
					if (o != null) {
						if (VCTModuleTag.convsetObsValueByConcept(o, VCTTracConstant.RESULT_OF_HIV_TEST)
						        .compareToIgnoreCase(Context.getConceptService().getConcept(testResult).getDisplayString()) != 0)
							addToTheList = false;
					} else
						addToTheList = false;
				}
				
				//got result of test
				if (gotResult != null) {
					Obs o = c.getResultObs();
					if (o != null) {
						String dateOfReception = VCTModuleTag.convsetObsValueByConcept(o, VCTConfigurationUtil
						        .getDateResultOfHivTestReceivedConceptId());
						if (gotResult.intValue() == 1) {
							if (dateOfReception.compareToIgnoreCase("-") == 0)
								addToTheList = false;
						} else if (gotResult.intValue() == 0) {
							if (dateOfReception.compareToIgnoreCase("-") != 0)
								addToTheList = false;
						}
					} else
						addToTheList = false;
				}
				
				if (addToTheList)
					clientIds.add(c.getTracVctClientId());
			}
		}
		
		return clientIds;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getNumberOfClientByDateOfRegistration(java.util.Date)
	 */
	@Override
	public Integer getNumberOfClientByDateOfRegistration(Date registrationDate) {
		String query = "SELECT COUNT(trac_vct_client_id) FROM trac_vct_client WHERE date_registration='"
		        + MohTracUtil.getMySQLDateFormat().format(registrationDate) + "'";
		//		log.info(">>>>>>>>> " + query);
		int numberOfClient = Integer.valueOf("" + getSession().createSQLQuery(query).uniqueResult());
		
		return numberOfClient;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getNumberOfClientByMonthAndYearOfRegistration(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	public Integer getNumberOfClientByMonthAndYearOfRegistration(Integer month, Integer year) {
		String query = "SELECT COUNT(trac_vct_client_id) FROM trac_vct_client WHERE MONTH(date_registration)=" + month
		        + " AND YEAR(date_registration)=" + year;
		//		log.info(">>>>>>>>> " + query);
		int numberOfClient = Integer.valueOf("" + getSession().createSQLQuery(query).uniqueResult());
		
		return numberOfClient;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getNumberOfClientByYearOfRegistration(java.lang.Integer)
	 */
	@Override
	public Integer getNumberOfClientByYearOfRegistration(Integer year) {
		String query = "SELECT COUNT(trac_vct_client_id) FROM trac_vct_client WHERE  YEAR(date_registration)=" + year;
		
		int numberOfClient = Integer.valueOf("" + getSession().createSQLQuery(query).uniqueResult());
		
		return numberOfClient;
	}
	
	/**
	 * @see org.openmrs.module.vcttrac.db.VCTModuleDAO#getMinOrMaxYearOfRegistration(boolean)
	 */
	@Override
	public Integer getMinOrMaxYearOfRegistration(boolean minYear) {
		String query = "";
		if (minYear)
			query = "SELECT MIN(YEAR(date_registration)) FROM trac_vct_client";
		else
			query = "SELECT MAX(YEAR(date_registration)) FROM trac_vct_client";
		
		int year = Integer.valueOf("" + getSession().createSQLQuery(query).uniqueResult());
		
		return year;
	}
}
