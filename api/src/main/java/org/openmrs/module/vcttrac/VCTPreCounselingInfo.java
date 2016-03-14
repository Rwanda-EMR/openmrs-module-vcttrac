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

/**
 * @author Yves G
 */
public class VCTPreCounselingInfo {
	
	private String encounterDate;
	
	private int locationId;
	
	private int providerId;
	
	private int counselingTypeId;
	
	/**
	 * Gets the encounter date
	 * 
	 * @return the encounter date
	 */
	public String getEncounterDate() {
		return encounterDate;
	}
	
	/**
	 * Sets the encounter date
	 * 
	 * @param encounterDate the encounter date to set
	 */
	public void setEncounterDate(String encounterDate) {
		this.encounterDate = encounterDate;
	}
	
	/**
	 * Gets the location id
	 * 
	 * @return the location id
	 */
	public int getLocationId() {
		return locationId;
	}
	
	/**
	 * Sets the location id
	 * 
	 * @param locationId the location id to set
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
	/**
	 * Gets the provider id
	 * 
	 * @return the provider id
	 */
	public int getProviderId() {
		return providerId;
	}
	
	/**
	 * Sets the provider id
	 * 
	 * @param providerId the provider id to set
	 */
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}
	
	/**
	 * Gets the counseling type id
	 * 
	 * @return the counseling type id
	 */
	public int getCounselingTypeId() {
		return counselingTypeId;
	}
	
	/**
	 * Sets the counseling type id
	 * 
	 * @param counselingTypeId the counseling type id to set
	 */
	public void setCounselingTypeId(int counselingTypeId) {
		this.counselingTypeId = counselingTypeId;
	}
	
}
