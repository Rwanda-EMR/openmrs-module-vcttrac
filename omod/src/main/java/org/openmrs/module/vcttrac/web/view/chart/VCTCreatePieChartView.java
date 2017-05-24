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
package org.openmrs.module.vcttrac.web.view.chart;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.openmrs.api.context.Context;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.VCTTracUtil;

/**
 *
 */
public class VCTCreatePieChartView extends AbstractChartView {
	
	/**
	 * @see org.openmrs.module.vcttrac.web.view.chart.AbstractChartView#createChart(java.util.Map,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected JFreeChart createChart(Map<String, Object> model, HttpServletRequest request) {
		if (request.getParameter("type").trim().compareTo("registrationEntryPoint") == 0)
			return createRegistrationEntryPointGraph();
		else if (request.getParameter("type").trim().compareTo("gender") == 0)
			return createGenderPieChartView();
		else if (request.getParameter("type").trim().compareTo("counselingType") == 0)
			return createCounselingTypePieChartView();
		else
			return ChartFactory.createPieChart("No chart selected", null, true, true, false);
	}
	
	public static JFreeChart createRegistrationEntryPointGraph() {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		
		VCTModuleService service = Context.getService(VCTModuleService.class);
		
		try {
			Date reportingDate = new Date();
			
			int numberOfClientInVCT = service.getNumberOfClientsByRegistrationEntryPoint("VCT", reportingDate);
			int numberOfClientInPIT = service.getNumberOfClientsByRegistrationEntryPoint("PIT", reportingDate);
			int numberOfClientInMaleCircusmcision = service.getNumberOfClientsByRegistrationEntryPoint("MALE_CIRCUMCISION", reportingDate);
			int numberOfClientInPostExposure = service.getNumberOfClientsByRegistrationEntryPoint("POST_EXPOSURE", reportingDate);
			int numberOfClientInOther = service.getNumberOfClientsByRegistrationEntryPoint("OTHER", reportingDate);
			int all = numberOfClientInVCT + numberOfClientInPIT + numberOfClientInMaleCircusmcision + numberOfClientInPostExposure + numberOfClientInOther;
			Float percentageVCT = new Float(100 * numberOfClientInVCT / all);
			Float percentagePIT = new Float(100 * numberOfClientInPIT / all);
			Float percentageMaleC = new Float(100 * numberOfClientInMaleCircusmcision / all);
			Float percentagePostExpo = new Float(100 * numberOfClientInPostExposure / all);
			Float percentageOther = new Float(100 * numberOfClientInOther / all);
			
			pieDataset.setValue(VCTTracUtil.getMessage("vcttrac.home.vctclient", null) + " (" + numberOfClientInVCT + " , "
			        + percentageVCT + "%)", percentageVCT);
			pieDataset.setValue(VCTTracUtil.getMessage("vcttrac.home.pitclient", null) + " (" + numberOfClientInPIT + " , "
			        + percentagePIT + "%)", percentagePIT);
			pieDataset.setValue(VCTTracUtil.getMessage("vcttrac.home.circumcisionclient", null) + " (" + numberOfClientInMaleCircusmcision + " , "
			        + percentageMaleC + "%)", percentageMaleC);
			pieDataset.setValue(VCTTracUtil.getMessage("vcttrac.home.exposureclient", null) + " (" + numberOfClientInPostExposure + " , "
			        + percentagePostExpo + "%)", percentagePostExpo);
			pieDataset.setValue(VCTTracUtil.getMessage("vcttrac.home.otherclient", null) + " (" + numberOfClientInOther + " , "
			        + percentageOther + "%)", percentageOther);
			
			JFreeChart chart = ChartFactory.createPieChart(VCTTracUtil.getMessage("vcttrac.graph.registrationEntryPoints", null), pieDataset, true, true, false);
			
			return chart;
		}
		catch (Exception e) {
			log.error(">>VCT>>vs>>PIT>>PIE>>CHART>> " + e.getMessage());
			e.printStackTrace();
			return ChartFactory.createPieChart(VCTTracUtil.getMessage("vcttrac.graph.registrationEntryPoints", null), null, true, true, false);
		}
	}
	
	private JFreeChart createGenderPieChartView() {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		
		VCTModuleService service = Context.getService(VCTModuleService.class);
		
		try {
			Date reportingDate = new Date();
			int numberOffemale = service.getVCTClientsBasedOnGender("f", reportingDate).size();
			int numberOfMale = service.getVCTClientsBasedOnGender("m", reportingDate).size();
			
			int all = numberOffemale + numberOfMale;
			
			Float percentageFemale = new Float(100 * numberOffemale / all);
			pieDataset.setValue(VCTTracUtil.getMessage("vcttrac.person.female", null) + " (" + numberOffemale + " , "
			        + percentageFemale + "%)", percentageFemale);
			
			Float percentageMale = new Float(100 * numberOfMale / all);
			pieDataset.setValue(VCTTracUtil.getMessage("vcttrac.person.male", null) + " (" + numberOfMale + " , "
			        + percentageMale + "%)", percentageMale);
			
			JFreeChart chart = ChartFactory.createPieChart(VCTTracUtil.getMessage("vcttrac.person.gender", null),
			    pieDataset, true, true, false);
			
			return chart;
		}
		catch (Exception e) {
			log.error(">>GENDER>>PIE>>CHART>> " + e.getMessage());
			e.printStackTrace();
			return ChartFactory.createPieChart(VCTTracUtil.getMessage("vcttrac.person.gender", null), null, true, true, false);
		}
	}
	
	private JFreeChart createCounselingTypePieChartView() {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		
		VCTModuleService service = Context.getService(VCTModuleService.class);
		
		try {
			Date reportingDate = new Date();
			int numberOfIndividual = service.getVCTClientsBasedOnCounselingType(1, reportingDate).size();
			int numberOfCouples = service.getVCTClientsBasedOnCounselingType(2, reportingDate).size();
			int numberOfNotCounseled = service.getVCTClientsBasedOnCounselingType(3, reportingDate).size();
			
			int all = numberOfIndividual + numberOfCouples + numberOfNotCounseled;
			
			Float percentageIndividual = new Float(100 * numberOfIndividual / all);
			pieDataset.setValue("Individual (" + numberOfIndividual + " , " + percentageIndividual + "%)",
			    percentageIndividual);
			
			Float percentageCouple = new Float(100 * numberOfCouples / all);
			pieDataset.setValue("Couples (" + numberOfCouples + " , " + percentageCouple + "%)", percentageCouple);
			
			Float percentageNotCounseled = new Float(100 * numberOfNotCounseled / all);
			pieDataset.setValue("Not Counseled (" + numberOfNotCounseled + " , " + percentageNotCounseled + "%)",
			    percentageNotCounseled);
			
			JFreeChart chart = ChartFactory.createPieChart(VCTTracUtil.getMessage(
			    "vcttrac.statistic.graph.typeofcounseling", null), pieDataset, true, true, false);
			
			return chart;
		}
		catch (Exception e) {
			log.error(">>COUNSELING>>TYPE>>PIE>>CHART>> " + e.getMessage());
			e.printStackTrace();
			return ChartFactory.createPieChart(VCTTracUtil.getMessage(
			    "vcttrac.statistic.graph.typeofcounseling", null), null, true, true, false);
		}
	}
	
}
