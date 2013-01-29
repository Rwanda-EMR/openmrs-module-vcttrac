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
import org.openmrs.User;

/**
 * @author Yves G
 */
public class VCTClientResult {
	
	private Date dateOfResult;
	
	private Location location;
	
	private int hivTestResult;
	
	private int hivTestResultObsId;
	
	private int hivTestDateObsId;
	
	private String codeTest;
	
	private Date dateCreated;
	
	private User createdBy;
	
	private VCTClient client;
	
	/**
	 * Gets the date of the result
	 * 
	 * @return the date of the result
	 */
	public Date getDateOfResult() {
		return dateOfResult;
	}
	
	/**
	 * Sets the date of the result
	 * 
	 * @param dateOfResult the date of the result to set
	 */
	public void setDateOfResult(Date dateOfResult) {
		this.dateOfResult = dateOfResult;
	}
	
	/**
	 * Gets the location the result get registered from
	 * 
	 * @return the location where the result get registered from
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Sets the location where the result get registered from
	 * 
	 * @param location the location where the result get registered from
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/**
	 * Gets the hiv test result
	 * 
	 * @return the hiv test result
	 */
	public int getHivTestResult() {
		return hivTestResult;
	}
	
	/**
	 * Sets the hiv test result
	 * 
	 * @param hivTestResult the hiv test result to set
	 */
	public void setHivTestResult(int hivTestResult) {
		this.hivTestResult = hivTestResult;
	}
	
	/**
	 * Gets the corresponding obs id of the hiv test result
	 * 
	 * @return the corresponding obs id of the hiv test result
	 */
	public int getHivTestResultObsId() {
		return hivTestResultObsId;
	}
	
	/**
	 * Sets the corresponding obs id of the hiv test result
	 * 
	 * @param hivTestResultObsId the corresponding obs id of the hiv test result to set
	 */
	public void setHivTestResultObsId(int hivTestResultObsId) {
		this.hivTestResultObsId = hivTestResultObsId;
	}
	
	/**
	 * Gets the corresponding obs id of the hiv test date
	 * 
	 * @return the corresponding obs id of the hiv test date
	 */
	public int getHivTestDateObsId() {
		return hivTestDateObsId;
	}
	
	/**
	 * Sets the corresponding obs id of the hiv test date
	 * 
	 * @param hivTestDateObsId the corresponding obs id of the hiv test date to set
	 */
	public void setHivTestDateObsId(int hivTestDateObsId) {
		this.hivTestDateObsId = hivTestDateObsId;
	}
	
	/**
	 * Gets the code test
	 * 
	 * @return the code test
	 */
	public String getCodeTest() {
		return codeTest;
	}
	
	/**
	 * Sets the code test
	 * 
	 * @param codeTest the code test to set
	 */
	public void setCodeTest(String codeTest) {
		this.codeTest = codeTest;
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
	 * @param dateCreated the date of creation to set
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
	 * @param createdBy the user who created the object to set
	 */
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	
	/**
	 * Gets the corresponding vct client to this object
	 * 
	 * @return the corresponding vct client to this object
	 */
	public VCTClient getClient() {
		return client;
	}
	
	/**
	 * Sets the corresponding vct client to this object
	 * 
	 * @param client the corresponding vct client to this object to set
	 */
	public void setClient(VCTClient client) {
		this.client = client;
	}
	
}
