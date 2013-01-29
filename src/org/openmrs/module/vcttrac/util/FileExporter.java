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

import java.text.DateFormat; //import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.vcttrac.VCTClient;

/**
 * @author Yves GAKUBA
 */
public class FileExporter {
	
	private Log log = LogFactory.getLog(this.getClass());
		
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param patientList
	 * @param filename
	 * @param title
	 * @throws Exception
	 */
	public void exportToCSVFile(HttpServletRequest request, HttpServletResponse response, List<VCTClient> clientList,
	                            String filename, String title, String searchDescr) throws Exception {
		ServletOutputStream outputStream = null;
		
		try {
			outputStream = response.getOutputStream();
			//			Person p;
			
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			outputStream.println(VCTTracUtil.getMessage("vcttrac.export.title.bigtitle", null));
			outputStream.println(VCTTracUtil.getMessage("vcttrac.export.report.title", null)+", "+title);
			outputStream.println(VCTTracUtil.getMessage("vcttrac.export.report.description", null)+", " + searchDescr);
			outputStream.println();
			outputStream.println(VCTTracUtil.getMessage("vcttrac.export.title.generatedby", null) + ", "
			        + Context.getAuthenticatedUser().getPersonName());
			outputStream.println(VCTTracUtil.getMessage("vcttrac.export.title.generatedon", null) + ", " + (new SimpleDateFormat("dd-MMM-yyyy HH:mm").format(new Date())));
			outputStream.println(VCTTracUtil.getMessage("vcttrac.export.title.numberofclients", null) + ", "
			        + clientList.size());
			outputStream.println();
			//			outputStream
			//			        .println("No., Names, Gender, BirthDay, Registration Date, Counseling Done ?, Got Résultat ?, Result of HIV Test");
			setColumnTitles(outputStream);
			outputStream.println();
			
			int ids = 0;
			
			for (VCTClient client : clientList) {
				ids += 1;
				/*p = client.getClient();
				outputStream.println(ids
				        + ","
				        + p.getPersonName()
				        + ","
				        + p.getGender()
				        + ","
				        + df.format(p.getBirthdate())
				        + ","
				        + df.format(client.getDateOfRegistration())
				        + ","
				        + ((client.getCounselingObs() != null) ? "Yes" : "No")
				        + ","
				        + ((client.getResultObs() != null) ? "Yes" : "No")
				        + ","
				        + VCTModuleTag.convsetObsValueByConcept(client.getResultObs(), VCTTracUtil
				                .getResultOfHivTestConceptId()));*/

				displayRowValues(outputStream, client, ids);
			}
			
			outputStream.flush();
		}
		catch (Exception e) {
			log.error(">>VCT>>Export>>in>>CSV>>Format>> " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (null != outputStream)
				outputStream.close();
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param outputStream
	 * @param showNames
	 */
	private void setColumnTitles(ServletOutputStream outputStream) {
		try {

			boolean hasPrivilegeToViewPatientNames=Context.getAuthenticatedUser().hasPrivilege("View Patient Names");
			boolean hasPrivilegeToViewClientResult=Context.getAuthenticatedUser().hasPrivilege("View VCT Client result");
			
			outputStream.println(VCTTracUtil.getMessage("vcttrac.export.column.registrationdate", null) + ", "
			        + VCTTracUtil.getMessage("vcttrac.export.column.number", null) + ", "
			        + VCTTracUtil.getMessage("vcttrac.export.column.clientcode", null) + ", "
			        + ((hasPrivilegeToViewPatientNames) ? VCTTracUtil.getMessage("vcttrac.export.column.names", null) + ", " : "")
			        + VCTTracUtil.getMessage("vcttrac.export.column.gender", null) + ", "
			        + VCTTracUtil.getMessage("vcttrac.export.column.birthday", null) + ", "
			        + VCTTracUtil.getMessage("vcttrac.export.column.counselingdone", null) + ", "
			        + VCTTracUtil.getMessage("vcttrac.export.column.gotresult", null) + ", "
			        + ((hasPrivilegeToViewClientResult) ? VCTTracUtil.getMessage("vcttrac.export.column.resultoftest", null) : ""));
			
		}
		catch (Exception e) {
			log.error(">>VCT>>Export>>in>>CSV>>Format>> Fail to set column titles >> " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param outputStream
	 * @param showNames
	 * @param showResult
	 * @param id
	 */
	private void displayRowValues(ServletOutputStream outputStream, VCTClient client,
	                              Integer id) {
		try {
			
			boolean hasPrivilegeToViewPatientNames=Context.getAuthenticatedUser().hasPrivilege("View Patient Names");
			boolean hasPrivilegeToViewClientResult=Context.getAuthenticatedUser().hasPrivilege("View VCT Client result");
			
			DateFormat df = Context.getDateFormat();
			
			Person p = client.getClient();
			outputStream.println(df.format(client.getDateOfRegistration())
			        + ", "
			        + id
			        + ", "
			        + client.getCodeClient()
			        + ", "
			        + ((hasPrivilegeToViewPatientNames) ? p.getPersonName() + ", " : "")
			        + p.getGender()
			        + ", "
			        + df.format(p.getBirthdate())
			        + ", "
			        + ((client.getCounselingObs() != null) ? VCTTracUtil.getMessage("vcttrac.export.column.yes", null)
			                : VCTTracUtil.getMessage("vcttrac.export.column.no", null))
			        + ", "
			        + ((client.getResultObs() != null) ? VCTTracUtil.getMessage("vcttrac.export.column.yes", null)
			                : VCTTracUtil.getMessage("vcttrac.export.column.no", null))
			        + ", "
			        + ((hasPrivilegeToViewClientResult) ? VCTModuleTag.convsetObsValueByConcept(client.getResultObs(), VCTConfigurationUtil
			                .getResultOfHivTestConceptId()) : ""));
			
		}
		catch (Exception e) {
			log.error(">>VCT>>Export>>in>>CSV>>Format>> Fail to display row values #" + id + " >> " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param filename
	 * @param title
	 * @throws Exception
	 */
	public void exportToCSVTracNetIndicators(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ServletOutputStream outputStream = null;
//		DateFormat df = Context.getDateFormat();
		try {
			outputStream = response.getOutputStream();
						
			String title = VCTTracUtil.getMessage("vcttrac.tracnetIndicators", null);
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\""
			        + title.trim().toLowerCase().replace(" ", "_") + ".csv" + "\"");
			outputStream.println(title);
			
			String location=request.getParameter("location");
			Date dateFrom=Context.getDateFormat().parse(request.getParameter("dateFrom"));
			Date dateTo=Context.getDateFormat().parse(request.getParameter("dateTo"));
			
			outputStream.println(VCTTracUtil.getMessage("Encounter.location", null) + " , "+((location == null || location.compareTo("") == 0)?"":Context.getLocationService().getLocation(Integer.valueOf(location)).getName()));
			outputStream.println(VCTTracUtil.getMessage("vcttrac.from", null) + " , "
			        + (new SimpleDateFormat("dd-MMM-yyyy").format(dateFrom)));
			outputStream.println(VCTTracUtil.getMessage("vcttrac.to", null) + " , "
			        + (new SimpleDateFormat("dd-MMM-yyyy").format(dateTo)));
			outputStream.println();
			outputStream.println(VCTTracUtil.getMessage("vcttrac.export.title.generatedby", null) + " , "
			        + Context.getAuthenticatedUser().getPersonName());
			outputStream.println(VCTTracUtil.getMessage("vcttrac.export.title.generatedon", null) + " , "
			        + new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
			outputStream.println();
			outputStream.println();
			
			Integer loc=Integer.parseInt(location);
			String from=request.getParameter("dateFrom");
			String to=request.getParameter("dateTo");
			
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.vct.indicators", null));
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.criteria", null)+", "
								+VCTTracUtil.getMessage("vcttrac.person.female", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" <15, "
								+VCTTracUtil.getMessage("vcttrac.person.male", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" <15, "
								+VCTTracUtil.getMessage("vcttrac.person.female", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" 15-24, "
								+VCTTracUtil.getMessage("vcttrac.person.male", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" 15-24, "
								+VCTTracUtil.getMessage("vcttrac.person.female", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" 25+, "
								+VCTTracUtil.getMessage("vcttrac.person.male", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" 25+");
			
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.criteria.numberOfNewClientsCounseledAndTestedForHiv", null)
								+", "+VCTModuleTag.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, loc, 0, 0, 15, "f")
								+", "+VCTModuleTag.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, loc, 0, 0, 15, "m")
								+", "+VCTModuleTag.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, loc, 0, 15, 25, "f")
								+", "+VCTModuleTag.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, loc, 0, 15, 25, "m")
								+", "+VCTModuleTag.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, loc, 0, 25, 0, "f")
								+", "+VCTModuleTag.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, loc, 0, 25, 0, "m"));
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.criteria.numberOfNewClientsTestedAndReceivedResults", null)
								+", "+VCTModuleTag.getNumberOfNewClientsTestedAndReceivedResults(from, to, loc, 0, 0, 15, "f")
								+", "+VCTModuleTag.getNumberOfNewClientsTestedAndReceivedResults(from, to, loc, 0, 0, 15, "m")
								+", "+VCTModuleTag.getNumberOfNewClientsTestedAndReceivedResults(from, to, loc, 0, 15, 25, "f")
								+", "+VCTModuleTag.getNumberOfNewClientsTestedAndReceivedResults(from, to, loc, 0, 15, 25, "m")
								+", "+VCTModuleTag.getNumberOfNewClientsTestedAndReceivedResults(from, to, loc, 0, 25, 0, "f")
								+", "+VCTModuleTag.getNumberOfNewClientsTestedAndReceivedResults(from, to, loc, 0, 25, 0, "m"));
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.criteria.numberOfHivPositiveClients", null)
								+", "+VCTModuleTag.getNumberOfHIVPositiveClients(from, to, loc, 0, 0, 15, "f")
								+", "+VCTModuleTag.getNumberOfHIVPositiveClients(from, to, loc, 0, 0, 15, "m")
								+", "+VCTModuleTag.getNumberOfHIVPositiveClients(from, to, loc, 0, 15, 25, "f")
								+", "+VCTModuleTag.getNumberOfHIVPositiveClients(from, to, loc, 0, 15, 25, "m")
								+", "+VCTModuleTag.getNumberOfHIVPositiveClients(from, to, loc, 0, 25, 0, "f")
								+", "+VCTModuleTag.getNumberOfHIVPositiveClients(from, to, loc, 0, 25, 0, "m"));
			
			outputStream.println();
			outputStream.println();
			
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.pit.indicators", null));
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.criteria", null)+", "
								+VCTTracUtil.getMessage("vcttrac.person.female", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" <15, "
								+VCTTracUtil.getMessage("vcttrac.person.male", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" <15, "
								+VCTTracUtil.getMessage("vcttrac.person.female", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" 15-24, "
								+VCTTracUtil.getMessage("vcttrac.person.male", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" 15-24, "
								+VCTTracUtil.getMessage("vcttrac.person.female", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" 25+, "
								+VCTTracUtil.getMessage("vcttrac.person.male", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" 25+");
			
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.criteria.numberOfNewClientsCounseledAndTestedForHiv", null)
								+", "+VCTModuleTag.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, loc, 1, 0, 15, "f")
								+", "+VCTModuleTag.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, loc, 1, 0, 15, "m")
								+", "+VCTModuleTag.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, loc, 1, 15, 25, "f")
								+", "+VCTModuleTag.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, loc, 1, 15, 25, "m")
								+", "+VCTModuleTag.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, loc, 1, 25, 0, "f")
								+", "+VCTModuleTag.getNumberOfNewClientsCounseledAndTestedForHIV(from, to, loc, 1, 25, 0, "m"));
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.criteria.numberOfNewClientsTestedAndReceivedResults", null)
								+", "+VCTModuleTag.getNumberOfNewClientsTestedAndReceivedResults(from, to, loc, 1, 0, 15, "f")
								+", "+VCTModuleTag.getNumberOfNewClientsTestedAndReceivedResults(from, to, loc, 1, 0, 15, "m")
								+", "+VCTModuleTag.getNumberOfNewClientsTestedAndReceivedResults(from, to, loc, 1, 15, 25, "f")
								+", "+VCTModuleTag.getNumberOfNewClientsTestedAndReceivedResults(from, to, loc, 1, 15, 25, "m")
								+", "+VCTModuleTag.getNumberOfNewClientsTestedAndReceivedResults(from, to, loc, 1, 25, 0, "f")
								+", "+VCTModuleTag.getNumberOfNewClientsTestedAndReceivedResults(from, to, loc, 1, 25, 0, "m"));
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.criteria.numberOfHivPositiveClients", null)
								+", "+VCTModuleTag.getNumberOfHIVPositiveClients(from, to, loc, 1, 0, 15, "f")
								+", "+VCTModuleTag.getNumberOfHIVPositiveClients(from, to, loc, 1, 0, 15, "m")
								+", "+VCTModuleTag.getNumberOfHIVPositiveClients(from, to, loc, 1, 15, 25, "f")
								+", "+VCTModuleTag.getNumberOfHIVPositiveClients(from, to, loc, 1, 15, 25, "m")
								+", "+VCTModuleTag.getNumberOfHIVPositiveClients(from, to, loc, 1, 25, 0, "f")
								+", "+VCTModuleTag.getNumberOfHIVPositiveClients(from, to, loc, 1, 25, 0, "m"));
			
			outputStream.println();
			outputStream.println();
			
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.couple.indicators", null));
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.criteria", null)+", "
								+VCTTracUtil.getMessage("vcttrac.tracnet.couple.counseled", null)+", "
								+VCTTracUtil.getMessage("vcttrac.tracnet.couple.tested", null)+" : "+VCTTracUtil.getMessage("vcttrac.tracnet.couple.all", null)+", "
								+VCTTracUtil.getMessage("vcttrac.tracnet.couple.tested", null)+" : "+VCTTracUtil.getMessage("vcttrac.tracnet.couple.oneoftwo", null)+", "
								+VCTTracUtil.getMessage("vcttrac.tracnet.couple.tested", null)+" : "+VCTTracUtil.getMessage("vcttrac.tracnet.couple.none", null));
				//				+VCTTracUtil.getMessage("vcttrac.person.male", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" 15-24, "
				//				+VCTTracUtil.getMessage("vcttrac.person.female", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" 25+, "
				//				+VCTTracUtil.getMessage("vcttrac.person.male", null)+" : "+VCTTracUtil.getMessage("vcttrac.age", null)+" 25+");
			
			outputStream.println(VCTTracUtil.getMessage("vcttrac.tracnet.criteria.numberOfCouples", null)
								+", "+VCTModuleTag.getNumberOfCouplesCounseled(from, to, loc)
								+", "+VCTModuleTag.getNumberOfCouplesCounseledAndTested(from, to, loc, 2)
								+", "+VCTModuleTag.getNumberOfCouplesCounseledAndTested(from, to, loc, 1)
								+", "+VCTModuleTag.getNumberOfCouplesCounseledAndTested(from, to, loc, 0));
			
			outputStream.println();
			
			outputStream.flush();
		}
		catch (Exception e) {
			log.error(">>VCT>>Export>>in>>CSV>>Format>>Tracnet>>Indicators>> " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (null != outputStream)
				outputStream.close();
		}
		
	}
	
}
