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
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.openmrs.api.context.Context;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.VCTTracConstant;
import org.openmrs.module.vcttrac.util.VCTTracUtil;

/**
 *
 */
public class VCTCreateBarChartView extends AbstractChartView {
	
	/**
	 * @see org.openmrs.module.vcttrac.web.view.chart.AbstractChartView#createChart(java.util.Map,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected JFreeChart createChart(Map<String, Object> model, HttpServletRequest request) {
		
		//JFreeChart chart = null;
		
		if (request.getParameter("type").compareTo("todayAndYesterday") == 0) {
			CategoryDataset dataset = createDataset();
			return createChart(dataset, 1);
		} else if (request.getParameter("type").compareTo("monthInYear") == 0) {
			CategoryDataset dataset1 = createMonthDataset();
			return createChart(dataset1, 2);
		} else if (request.getParameter("type").compareTo("years") == 0) {
			CategoryDataset dataset2 = createYearDataset();
			return createChart(dataset2, 3);
		}
		
		// OPTIONAL CUSTOMISATION COMPLETED.
		
		return ChartFactory.createBarChart(null, null, null, null, PlotOrientation.VERTICAL, true, true, false);
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	private static CategoryDataset createDataset() {
		
		Date curDate = new Date();
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		
		// create the dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		Date yesterday = new Date(curDate.getTime() - (VCTTracConstant.ONE_DAY_IN_MILLISECONDS * 1));
		double value = vms.getNumberOfClientByDateOfRegistration(yesterday);
		dataset.addValue(value, "todayVsYesterday", VCTTracUtil.getMessage("vcttrac.graph.statistic.yesterday", null));
		//		+ " ("
		//		        + value + ")");
		
		Date today = new Date(curDate.getTime());
		double value1 = vms.getNumberOfClientByDateOfRegistration(today);
		dataset.addValue(value1, "todayVsYesterday", VCTTracUtil.getMessage("vcttrac.graph.statistic.today", null));
		//		+ " ("
		//		        + value1 + ")");
		
		return dataset;
		
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	private static CategoryDataset createMonthDataset() {
		
		Date curDate = new Date();
		
		int numberOfRepetitions = curDate.getMonth() + 1;
		
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		
		// create the dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		int month = 0;
		
		while (month < numberOfRepetitions) {
			double value = vms.getNumberOfClientByMonthAndYearOfRegistration(month + 1, curDate.getYear() + 1900);
			dataset.addValue(value, "year", VCTTracUtil.getMessage("vcttrac.month." + (month + 1), null));
			month += 1;
		}
		
		return dataset;
		
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	private static CategoryDataset createYearDataset() {
		
		//		Date curDate = new Date();
		
		VCTModuleService vms = Context.getService(VCTModuleService.class);
		
		int minYear = vms.getMinYearOfRegistration();
		int maxYear = vms.getMaxYearOfRegistration();
		
		//		int numberOfRepetitions = maxYear-minYear;
		
		// create the dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		int year = minYear;
		
		while (year <= maxYear) {
			double value = vms.getNumberOfClientByYearOfRegistration(year);
			dataset.addValue(value, "year", "" + year);
			//			log.info(">>>>>>>>>>>>>>>>>>>> "+year+" - "+value);
			year += 1;
		}
		
		return dataset;
		
	}
	
	/**
	 * Returns an array of paint objects that will be used for the bar colors.
	 * 
	 * @return An array of paint objects.
	 */
	private static Paint[] createPaint() {
		Paint[] colors = new Paint[5];
		
		colors[0] = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f, 0.0f, new Color(64, 0, 0));
		colors[1] = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f, 0.0f, new Color(0, 0, 64));
		colors[2] = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f, 0.0f, new Color(0, 64, 0));
		colors[3] = new GradientPaint(0f, 0f, Color.orange, 0.0f, 0.0f, new Color(250, 128, 114));
		colors[4] = new GradientPaint(0f, 0f, Color.yellow, 0.0f, 0.0f, new Color(255, 215, 0));
		return colors;
	}
	
	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset the dataset.
	 * @return The chart.
	 */
	private JFreeChart createChart(CategoryDataset dataset, int typeOfChart) {
		String title = "";
		if (typeOfChart == 1)
			title = VCTTracUtil.getMessage("vcttrac.graph.statistic.compare.todayandyesterday", null);
		else if (typeOfChart == 2)
			title = VCTTracUtil.getMessage("vcttrac.year", null) + " : " + (new Date().getYear() + 1900);
		else if (typeOfChart == 3)
			title = VCTTracUtil.getMessage("vcttrac.graph.statistic.years", null);
		// create the chart...
		JFreeChart chart = ChartFactory.createBarChart(title, null, null, // chart title
		    dataset, // data
		    PlotOrientation.VERTICAL, // orientation
		    false, // include legend
		    true, // tooltips?
		    false // URLs?
		        );
		
		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);
		
		// get a reference to the plot for further customisation...
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);
		Paint[] colors = createPaint();
		CustomBarRenderer renderer = new CustomBarRenderer(colors);
		renderer.setGradientPaintTransformer(new StandardGradientPaintTransformer(
		        GradientPaintTransformType.CENTER_HORIZONTAL));
		plot.setRenderer(renderer);
		
		// set the range axis to display integers only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setUpperMargin(0.15);
		
		//		CategoryItemRenderer renderer = plot.getRenderer();
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setSeriesItemLabelsVisible(0, Boolean.TRUE);
		
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 12));
		if (typeOfChart < 2)
			domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
		else
			domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		
		return chart;
		
	}
	
	static class CustomBarRenderer extends BarRenderer {
		
		private Paint colors[];
		
		public Paint getItemPaint(int i, int j) {
			return colors[j % colors.length];
		}
		
		public CustomBarRenderer(Paint apaint[]) {
			colors = apaint;
		}
	}
	
}
