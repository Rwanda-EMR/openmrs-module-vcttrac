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
public class VCTCounselingInfo {
	
	private VCTPreCounselingInfo preCouncelingInfo;
	
	private int patientId;
	
	private String codeClient;
	
	private String codeDuPartenaire;
	
	private int whatHandicap;
	
	private int whyTesting;
	
	private int refereDuService;
	
	private int numberOfCondomTaken;
	
	private int hivTested;
	
	private String clinicalComment;
	
	/**
	 * constructor
	 */
	public VCTCounselingInfo() {
	}
	
	/**
	 * Constructor
	 * 
	 * @param pci pre-counseling information
	 */
	public VCTCounselingInfo(VCTPreCounselingInfo pci) {
		setPreCouncelingInfo(pci);
	}
	
	/**
	 * Gets the pre-counseling information
	 * 
	 * @return the pre-counseling information
	 */
	public VCTPreCounselingInfo getPreCouncelingInfo() {
		return preCouncelingInfo;
	}
	
	/**
	 * Sets the pre-counseling information
	 * 
	 * @param preCouncelingInfo the pre-counceling information to set
	 */
	public void setPreCouncelingInfo(VCTPreCounselingInfo preCouncelingInfo) {
		this.preCouncelingInfo = preCouncelingInfo;
	}
	
	/**
	 * Gets the patient id
	 * 
	 * @return the patient id
	 */
	public int getPatientId() {
		return patientId;
	}
	
	/**
	 * Sets the patient id
	 * 
	 * @param patientId the patient id to set
	 */
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	
	/**
	 * Gets the code of the client
	 * 
	 * @return the code of the client
	 */
	public String getCodeClient() {
		return codeClient;
	}
	
	/**
	 * Sets the code of the client
	 * 
	 * @param codeClient the code of the client to set
	 */
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}
	
	/**
	 * Gets the code of the partner
	 * 
	 * @return the code of the partner
	 */
	public String getCodeDuPartenaire() {
		return codeDuPartenaire;
	}
	
	/**
	 * Sets the code of the partner
	 * 
	 * @param codeDuPartenaire the code of the partner to set
	 */
	public void setCodeDuPartenaire(String codeDuPartenaire) {
		this.codeDuPartenaire = codeDuPartenaire;
	}
	
	/**
	 * Gets the handicap of the client
	 * 
	 * @return the handicap of the client
	 */
	public int getWhatHandicap() {
		return whatHandicap;
	}
	
	/**
	 * Sets the handicap of the client
	 * 
	 * @param whatHandicap the handicap of the client to set
	 */
	public void setWhatHandicap(int whatHandicap) {
		this.whatHandicap = whatHandicap;
	}
	
	/**
	 * Gets the reason of getting tested of the client
	 * 
	 * @return the reason of getting tested of the client
	 */
	public int getWhyTesting() {
		return whyTesting;
	}
	
	/**
	 * Sets the reason of getting tested of the client
	 * 
	 * @param whyTesting the the reason of getting tested of the client to set
	 */
	public void setWhyTesting(int whyTesting) {
		this.whyTesting = whyTesting;
	}
	
	/**
	 * Gets the corresponding service which referred the client for test
	 * 
	 * @return the service which referred the client for test
	 */
	public int getRefereDuService() {
		return refereDuService;
	}
	
	/**
	 * Sets the service which referred the client for test
	 * 
	 * @param refereDuService the refereDuService to set
	 */
	public void setRefereDuService(int refereDuService) {
		this.refereDuService = refereDuService;
	}
	
	/**
	 * Gets the number of condoms taken by the client after the test
	 * 
	 * @return the number of condoms taken by the client
	 */
	public int getNumberOfCondomTaken() {
		return numberOfCondomTaken;
	}
	
	/**
	 * Sets the number of condoms taken by the client
	 * 
	 * @param numberOfCondomTaken the number of condoms taken by the client to set
	 */
	public void setNumberOfCondomTaken(int numberOfCondomTaken) {
		this.numberOfCondomTaken = numberOfCondomTaken;
	}
	
	/**
	 * Gets the status of hiv test
	 * 
	 * @return the status of hiv test
	 */
	public int getHivTested() {
		return hivTested;
	}
	
	/**
	 * Sets the status of hiv test
	 * 
	 * @param hivTested the status of hiv test to set
	 */
	public void setHivTested(int hivTested) {
		this.hivTested = hivTested;
	}
	
	/**
	 * Gets the clinical comments of the counselor
	 * 
	 * @return the clinical comments of the counselor
	 */
	public String getClinicalComment() {
		return clinicalComment;
	}
	
	/**
	 * Sets the clinical comments of the counselor
	 * 
	 * @param clinicalComment the clinical comments of the counselor to set
	 */
	public void setClinicalComment(String clinicalComment) {
		this.clinicalComment = clinicalComment;
	}
	
}
