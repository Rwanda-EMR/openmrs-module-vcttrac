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
package org.openmrs.module.vcttrac;

import java.util.Date;

import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.User;

/**
 * @author Yves G
 */
public class VCTClient {
	
	public static final long serialVersionUID = 57332L;
	
	private Integer tracVctClientId;
	
	private String uuid;
	
	private String codeClient = null;
	
	private String nid = null;
	
	private Integer typeOfCounseling; // 1=individual; 2=couple
	
	private Integer clientDecision;
	
	private Person client;
	
	private Person partner = null;
	
	private boolean vctOrPit;//vct=0; pit=1;
	
	private Obs counselingObs;
	
	private Obs resultObs;
	
	private String codeTest = null;
	
	private boolean archived = false;
	
	private Date dateCreated;
	
	private Date dateChanged;
	
	private Date dateOfRegistration;
	
	private Location location;
	
	private User changedBy;
	
	private User createdBy;
	
	private boolean voided = false;
	
	private Date dateVoided;
	
	private User voidedBy;
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[" + getTracVctClientId() + "] " + getClient().getPersonId() + "-" + getClient().getPersonName() + " "
		        + getCodeTest() + " " + isVctOrPit() + " " + isVoided();
	}
	
	/**
	 * Gets the last date that the object has been changed
	 * 
	 * @return the last date that the object has been changed
	 */
	public Date getDateChanged() {
		return dateChanged;
	}
	
	/**
	 * Sets the last date that the object has been changed
	 * 
	 * @param dateChanged the dateChanged to set
	 */
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	
	/**
	 * Gets the last user who changed the object
	 * 
	 * @return last user who changed the object
	 */
	public User getChangedBy() {
		return changedBy;
	}
	
	/**
	 * Sets the last user who changed the object
	 * 
	 * @param changedBy the user who changed the object
	 */
	public void setChangedBy(User changedBy) {
		this.changedBy = changedBy;
	}
	
	/**
	 * Gets the nid associated to the object
	 * 
	 * @return the nid associated to the object
	 */
	public String getNid() {
		return nid;
	}
	
	/**
	 * Sets the nid associated to the object
	 * 
	 * @param nid the nid to set
	 */
	public void setNid(String nid) {
		this.nid = nid;
	}
	
	/**
	 * Gets the UUID associated to the object
	 * 
	 * @return the uuid UUID associated to the object
	 */
	public String getUuid() {
		return uuid;
	}
	
	/**
	 * Sets the UUID associated to the object
	 * 
	 * @param uuid the UUID associated to the object
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * Gets the id of the object
	 * 
	 * @return the tracVctClientId id of the object
	 */
	public Integer getTracVctClientId() {
		return tracVctClientId;
	}
	
	/**
	 * Sets the id of the object
	 * 
	 * @param tracVctClientId the id of the object
	 */
	public void setTracVctClientId(Integer tracVctClientId) {
		this.tracVctClientId = tracVctClientId;
	}
	
	/**
	 * Gets the location object associated to the vctclient object
	 * 
	 * @return the location location object associated to the object
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Sets the location associated to the vctclient object
	 * 
	 * @param location the location associated to the object
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/**
	 * Gets the code of the client
	 * 
	 * @return the codeClient the code of the client
	 */
	public String getCodeClient() {
		return codeClient;
	}
	
	/**
	 * Sets the code of the client
	 * 
	 * @param codeClient the code of the client
	 */
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}
	
	/**
	 * Return <b>NULL</b> when the reception of result by the client has not been done<br/>
	 * Return <b>1</b> when the client decided <i>to join the HIV program</i><br/>
	 * Return <b>0</b> when the client decided <i>to be transferred to another location</i><br/>
	 * Return <b>2</b> when the client <i>denied to join the HIV program</i><br/>
	 * 
	 * @return the clientDecision
	 */
	public Integer getClientDecision() {
		return clientDecision;
	}
	
	/**
	 * Sets the client decision
	 * 
	 * @param clientDecision the client decision to set
	 */
	public void setClientDecision(Integer clientDecision) {
		this.clientDecision = clientDecision;
	}
	
	/**
	 * Gets the Person object associated to the vctclient object
	 * 
	 * @return the person object associated to the vctclient object
	 */
	public Person getClient() {
		return client;
	}
	
	/**
	 * Sets the Person object associated to the vctclient object
	 * 
	 * @param client the person object associated to the vctclient object to set
	 */
	public void setClient(Person client) {
		this.client = client;
	}
	
	/**
	 * Tells if the vctclient object associated is of type vct or pit
	 * 
	 * @return <i>true</i> if the associated vctclient used pit. <i>false</i> if the associated
	 *         vctclient used vct.
	 */
	public boolean isVctOrPit() {
		return vctOrPit;
	}
	
	/**
	 * Sets the mode of enrollment in vct
	 * 
	 * @param vctOrPit <i>true</i> if the associated vctclient used pit. <i>false</i> if the
	 *            associated vctclient used vct.
	 */
	public void setVctOrPit(boolean vctOrPit) {
		this.vctOrPit = vctOrPit;
	}
	
	/**
	 * Gets the obs corresponding to counseling obs group
	 * 
	 * @return the obs group corresponding to counseling
	 */
	public Obs getCounselingObs() {
		return counselingObs;
	}
	
	/**
	 * Sets the obs corresponding to counseling obs group
	 * 
	 * @param counselingObs the obs group corresponding to counseling
	 */
	public void setCounselingObs(Obs counselingObs) {
		this.counselingObs = counselingObs;
	}
	
	/**
	 * Gets the obs corresponding to result obs group
	 * 
	 * @return the obs group corresponding to result
	 */
	public Obs getResultObs() {
		return resultObs;
	}
	
	/**
	 * Sets the obs corresponding to result obs group
	 * 
	 * @param resultObs the obs group corresponding to result to set
	 */
	public void setResultObs(Obs resultObs) {
		this.resultObs = resultObs;
	}
	
	/**
	 * Gets the code test of the object
	 * 
	 * @return the code test of the object
	 */
	public String getCodeTest() {
		return codeTest;
	}
	
	/**
	 * Sets the code test of the object
	 * 
	 * @param codeTest the code test to set
	 */
	public void setCodeTest(String codeTest) {
		this.codeTest = codeTest;
	}
	
	/**
	 * Tells if the object is archived or not
	 * 
	 * @return <i>true</i> if the object is archived <i>false</i> if the object is not archived
	 */
	public boolean isArchived() {
		return archived;
	}
	
	/**
	 * Archive the object or not
	 * 
	 * @param archived <i>true</i> if the object is archived <i>false</i> if the object is not
	 *            archived
	 */
	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	
	/**
	 * Gets the date of registration
	 * 
	 * @return the date of registration
	 */
	public Date getDateOfRegistration() {
		return dateOfRegistration;
	}
	
	/**
	 * Sets the date of registration
	 * 
	 * @param dateOfRegistration the date or registration to set
	 */
	public void setDateOfRegistration(Date dateOfRegistration) {
		this.dateOfRegistration = dateOfRegistration;
	}
	
	/**
	 * Gets the date of creation of the object
	 * 
	 * @return the date of creation of the object
	 */
	public Date getDateCreated() {
		return dateCreated;
	}
	
	/**
	 * Sets the date of creation of the object
	 * 
	 * @param dateCreated the date or creation to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	/**
	 * Gets the user who created the object
	 * 
	 * @return the user who created the object
	 */
	public User getCreatedBy() {
		return createdBy;
	}
	
	/**
	 * Sets the user who created the object
	 * 
	 * @param createdBy the user who created the object
	 */
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	
	/**
	 * Sets the object to category vct
	 */
	public void isFromVCT() {
		setVctOrPit(false);
	}
	
	/**
	 * Sets the object to category pit
	 */
	public void isFromPIT() {
		setVctOrPit(true);
	}
	
	/**
	 * Tells if the object is voided or not
	 * 
	 * @return <i>true</i> if the object is voided <i>false</i> if the object is not voided
	 */
	public boolean isVoided() {
		return voided;
	}
	
	/**
	 * Sets the object to be voided or not
	 * 
	 * @param voided the status (voided or not) of the object to set
	 */
	public void setVoided(boolean voided) {
		this.voided = voided;
	}
	
	/**
	 * Gets the date the object has been voided on
	 * 
	 * @return the date that the object has been voided on
	 */
	public Date getDateVoided() {
		return dateVoided;
	}
	
	/**
	 * Sets the date on what the object has been voided on
	 * 
	 * @param dateVoided the date when the object has been voided on to set
	 */
	public void setDateVoided(Date dateVoided) {
		this.dateVoided = dateVoided;
	}
	
	/**
	 * Gets the user who voided the object
	 * 
	 * @return the user who voided the object
	 */
	public User getVoidedBy() {
		return voidedBy;
	}
	
	/**
	 * Sets the user who voided the object
	 * 
	 * @param voidedBy the user who voided the object to set
	 */
	public void setVoidedBy(User voidedBy) {
		this.voidedBy = voidedBy;
	}
	
	/**
	 * Gets the type of counseling used
	 * 
	 * @return the type of counseling used <i>1</i> if the type used is individual counseling
	 *         <i>2</i> if the type used is couple counseling
	 */
	public Integer getTypeOfCounseling() {
		return typeOfCounseling;
	}
	
	/**
	 * Sets the type of counseling used
	 * 
	 * @param typeOfCounseling the type of counseling to set <i>1</i> if the type used is individual
	 *            counseling <i>2</i> if the type used is couple counseling
	 */
	public void setTypeOfCounseling(Integer typeOfCounseling) {
		this.typeOfCounseling = typeOfCounseling;
	}
	
	/**
	 * Gets the partner corresponding to the object
	 * 
	 * @return the partner (Person) associated to the object
	 */
	public Person getPartner() {
		return partner;
	}
	
	/**
	 * Sets the partner corresponding to the object
	 * 
	 * @param partner the partner corresponding to the object to set
	 */
	public void setPartner(Person partner) {
		this.partner = partner;
	}
	
	/**
	 * Change the state of the client, the result <i>have or have not</i> been received
	 * 
	 * @param received <i>true</i> if the client have received the result <i>false</i> if the client
	 *            have not yet received the result
	 */
	public void setResultHasBeenReceived(boolean received) {
		setArchived(received);
	}
	
	/**
	 * Tells if the vctclient has received the result
	 * 
	 * @return <i>true</i> if the client have received the result <i>false</i> if the client have
	 *         not yet received the result
	 */
	public boolean doesTheClientReceiveTheResult() {
		return isArchived();
	}
	
}
