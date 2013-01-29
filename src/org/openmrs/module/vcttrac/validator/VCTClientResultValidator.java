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
package org.openmrs.module.vcttrac.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.vcttrac.VCTClientResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 */
public class VCTClientResultValidator implements Validator {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
    @Override
	public boolean supports(Class c) {
		return VCTClientResult.class.isAssignableFrom(c);
	}
	
	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object obj, Errors error) {
		VCTClientResult result = (VCTClientResult) obj;
		
		if (result.getDateOfResult() == null || result.getDateOfResult().equals(""))
			error.rejectValue("dateOfResult", "vcttrac.result.error.dateOfResult");
		if (result.getCodeTest() == null || result.getCodeTest().trim().compareTo("") == 0)
			error.rejectValue("codeTest", "vcttrac.result.error.codeTest");
		if (result.getHivTestResult() == 0)
			error.rejectValue("hivTestResult", "vcttrac.result.error.hivTestResult");
		if (result.getLocation() == null)
			error.rejectValue("location", "vcttrac.result.error.location");
	}
	
}
