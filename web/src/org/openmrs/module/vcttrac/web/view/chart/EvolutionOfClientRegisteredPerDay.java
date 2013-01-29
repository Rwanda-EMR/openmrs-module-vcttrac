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

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openmrs.api.context.Context;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.VCTTracConstant;
import org.openmrs.module.vcttrac.util.VCTTracUtil;

/**
 *
 */
public class EvolutionOfClientRegisteredPerDay extends AbstractChartView {
	
//	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see org.openmrs.module.vcttrac.web.view.chart.AbstractChartView#createChart(java.util.Map,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected JFreeChart createChart(Map<String, Object> model, HttpServletRequest request) {
		
		String categoryAxisLabel = VCTTracUtil.getMessage("vcttrac.graph.evolution.yAxis.days", null);
		String valueAxisLabel = VCTTracUtil.getMessage("vcttrac.graph.evolution.xAxis.numberofclient", null);
		
		int numberOfDays = 7, dayFormat = 1;
		
		if (request.getParameter("graphCategory") != null) {
			if (request.getParameter("graphCategory").trim().compareTo("2") == 0) {
				Date d = new Date();
				d.setDate(1);
				d.setMonth((new Date()).getMonth());
				d.setYear((new Date()).getYear());
				
				numberOfDays = (int) (((new Date()).getTime() - d.getTime()) / VCTTracConstant.ONE_DAY_IN_MILLISECONDS);
				
				categoryAxisLabel = VCTTracUtil.getMessage("vcttrac.month." + ((new Date()).getMonth() + 1), null);
				dayFormat = 2;
				
			}
		}
		
		String title = "";
		title = VCTTracUtil.getMessage("vcttrac.graph.evolution.client.received", null);
		
		JFreeChart chart = ChartFactory.createLineChart(title, categoryAxisLabel, valueAxisLabel, createDataset(
		    numberOfDays, dayFormat), // data
		    PlotOrientation.VERTICAL, true, false, false);
		
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);
		
		// customise the range axis...   
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setUpperMargin(0.15);
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		// customise the renderer...   
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		renderer.setBaseShapesVisible(true);
		renderer.setBaseShapesFilled(true);
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setItemLabelsVisible(true);
		
		return chart;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	private CategoryDataset createDataset(int numberOfDays, int dayFormat) {
		
		// row keys...
		String series1 = VCTTracUtil.getMessage("vcttrac.graph.evolution.client.received", null);
		
		Date today = new Date();
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		
		// create the dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		int daysInWeeks = numberOfDays;
		
		while (daysInWeeks >= 0) {
			Date curDate = new Date(today.getTime() - (VCTTracConstant.ONE_DAY_IN_MILLISECONDS * daysInWeeks));
			String dayLabel = (dayFormat == 1) ? new SimpleDateFormat("EEE dd").format(curDate) : new SimpleDateFormat("dd")
			        .format(curDate);
			double value = vms.getNumberOfClientByDateOfRegistration(curDate);
			dataset.addValue(value, series1, dayLabel);
			
			daysInWeeks -= 1;
		}
		
		return dataset;
		
	}
	
}