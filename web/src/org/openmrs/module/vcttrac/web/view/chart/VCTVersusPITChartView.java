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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.openmrs.api.context.Context;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.VCTTracUtil;

/**
 * View to render date range usage data as a chart image
 */
public class VCTVersusPITChartView extends AbstractChartView {
	
	private Log log = LogFactory.getLog(getClass());
	
	@Override
	protected JFreeChart createChart(Map<String, Object> model, HttpServletRequest request) {
		
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		
		VCTModuleService service = Context.getService(VCTModuleService.class);
		
		try {
			Date reportingDate = new Date();
			int numberOfClientInVCT = service.getNumberOfClientByVCTOrPIT(1, reportingDate);
			int numberOfClientInPIT = service.getNumberOfClientByVCTOrPIT(2, reportingDate);
			
			int all = numberOfClientInVCT + numberOfClientInPIT;
			
			Float percentageVCT = new Float(100 * numberOfClientInVCT / all);
			pieDataset.setValue(VCTTracUtil.getMessage("vcttrac.home.vctclient", null) + " (" + numberOfClientInVCT + " , "
			        + percentageVCT + "%)", percentageVCT);
			
			Float percentagePIT = new Float(100 * numberOfClientInPIT / all);
			pieDataset.setValue(VCTTracUtil.getMessage("vcttrac.home.pitclient", null) + " (" + numberOfClientInPIT + " , "
			        + percentagePIT + "%)", percentagePIT);
			
			JFreeChart chart = ChartFactory.createPieChart(VCTTracUtil.getMessage("vcttrac.home.vctclient", null) + " "
			        + VCTTracUtil.getMessage("vcttrac.graph.statistic.comparedto", null) + " "
			        + VCTTracUtil.getMessage("vcttrac.home.pitclient", null), pieDataset, true, true, false);
			
			return chart;
		}
		catch (Exception e) {
			log.error(">>VCT>>vs>>PIT>>PIE>>CHART>> " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
