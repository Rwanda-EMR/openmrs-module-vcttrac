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

import java.util.ArrayList;
import java.util.List;

import org.openmrs.api.context.ServiceContext;
import org.openmrs.module.vcttrac.VCTClient;
import org.openmrs.module.vcttrac.service.VCTModuleService;

/**
 *
 */
public class VCTTracAutocompleteUtils {
	
//	private Log log = LogFactory.getLog(getClass());
	
	private int totalFound, totalFoundForResult;
	
	private List<String> clientCodes, clientCodesForResult;
	
	VCTModuleService service = (VCTModuleService) ServiceContext.getInstance().getService(VCTModuleService.class);
	
	/**
	 * 
	 */
	public VCTTracAutocompleteUtils() {
		clientCodes = new ArrayList<String>();
		clientCodesForResult = new ArrayList<String>();
		clientCodes = service.getAllClientCodeWithoutHivTestResult();
		clientCodesForResult = service.getAllClientCodeForReceptionOfResult();
		
		totalFound = clientCodes.size();
		totalFoundForResult = clientCodesForResult.size();
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param query
	 * @return
	 */
	public List<String> getMatchingClientCode(String query) {
		String clientCode = null;
		query = query.toLowerCase();
		List<String> matched = new ArrayList<String>();
		
		for (int i = 0; i < totalFound; i++) {
			clientCode = clientCodes.get(i).toLowerCase();
			if (clientCode.startsWith(query))
				matched.add(clientCodes.get(i));
		}
		
		return matched;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param query
	 * @return
	 */
	public List<String> getMatchingClientCodeForReceptionOfResult(String query) {
		String clientCode = null;
		query = query.toLowerCase();
		List<String> matched = new ArrayList<String>();
		
		for (int i = 0; i < totalFoundForResult; i++) {
			clientCode = clientCodesForResult.get(i).toLowerCase();
			if (clientCode.startsWith(query))
				matched.add(clientCodesForResult.get(i));
		}
		
		return matched;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param query
	 * @return
	 */
	public VCTClient getClientInfosForReceptionOfResult(String query) {
		return service.getClientByCodeTest(query);
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param query
	 * @return
	 
	public VCTClient getClientForHivTest(String query) {
		return service.getClientByClientCode(query);
	}*/
	
}
