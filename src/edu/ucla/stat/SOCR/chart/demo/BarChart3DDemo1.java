/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the Internet.
 
SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.
 
http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
 It s Online, Therefore, It Exists! 
****************************************************/
package edu.ucla.stat.SOCR.chart.demo;

import java.awt.BorderLayout;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_Bar;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;

/**
 * A simple demonstration application showing how to create a vertical 3D bar 
 * chart using data from a {@link CategoryDataset}.
 */
public class BarChart3DDemo1 extends SuperCategoryChart_Bar implements PropertyChangeListener {

	
	
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+" doTest get called!");
		
		 int no_series = dataTable.getRowCount()-1;
		 int no_category = dataTable.getColumnCount()-2;
		 int[][] pairs = new int[no_category][2];
		 for (int i=0; i<no_category; i++){
			 pairs[i][0] = i+1;    //column i store category i
			 pairs[i][1] = 0;    //column 0 stores series name
		 }
		 chart = chartMaker.getCategoryChart("Bar", chartTitle, "Category", "value", dataTable, no_category, pairs, "3D");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
	public void init(){
		turnLegendPanelOn();
		super.init();
	}
	
	/**
	   * 
	   * @param isDemo data come from demo(true) or dataTable(false)
	   * @return
	   */  
    protected CategoryDataset createDataset(boolean isDemo) {
		if (isDemo){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10.0, "Series 1", "Category 1");
        dataset.addValue(4.0, "Series 1", "Category 2");
        dataset.addValue(15.0, "Series 1", "Category 3");
        dataset.addValue(14.0, "Series 1", "Category 4");
        dataset.addValue(-5.0, "Series 2", "Category 1");
        dataset.addValue(-7.0, "Series 2", "Category 2");
        dataset.addValue(14.0, "Series 2", "Category 3");
        dataset.addValue(-3.0, "Series 2", "Category 4");
        dataset.addValue(6.0, "Series 3", "Category 1");
        dataset.addValue(17.0, "Series 3", "Category 2");
        dataset.addValue(-12.0, "Series 3", "Category 3");
        dataset.addValue(7.0, "Series 3", "Category 4");
        dataset.addValue(7.0, "Series 4", "Category 1");
        dataset.addValue(15.0, "Series 4", "Category 2");
        dataset.addValue(11.0, "Series 4", "Category 3");
        dataset.addValue(0.0, "Series 4", "Category 4");
        dataset.addValue(-8.0, "Series 5", "Category 1");
        dataset.addValue(-6.0, "Series 5", "Category 2");
        dataset.addValue(10.0, "Series 5", "Category 3");
        dataset.addValue(-9.0, "Series 5", "Category 4");
        dataset.addValue(9.0, "Series 6", "Category 1");
        dataset.addValue(8.0, "Series 6", "Category 2");
        dataset.addValue(0.0, "Series 6", "Category 3");
        dataset.addValue(6.0, "Series 6", "Category 4");
        dataset.addValue(-10.0, "Series 7", "Category 1");
        dataset.addValue(9.0, "Series 7", "Category 2");
        dataset.addValue(7.0, "Series 7", "Category 3");
        dataset.addValue(7.0, "Series 7", "Category 4");
        dataset.addValue(11.0, "Series 8", "Category 1");
        dataset.addValue(13.0, "Series 8", "Category 2");
        dataset.addValue(9.0, "Series 8", "Category 3");
        dataset.addValue(9.0, "Series 8", "Category 4");
        dataset.addValue(-3.0, "Series 9", "Category 1");
        dataset.addValue(7.0, "Series 9", "Category 2");
        dataset.addValue(11.0, "Series 9", "Category 3");
        dataset.addValue(-10.0, "Series 9", "Category 4");
        return dataset;}
		else return super.createDataset(false);
    }
    
    /**
     * Creates a 3D bar chart.
     * 
     * @param dataset  the category dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {
        
        JFreeChart chart = ChartFactory.createBarChart3D(
            chartTitle,      // chart title
            domainLabel,               // domain axis label
            rangeLabel,                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            !legendPanelOn,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinesVisible(true);
        CategoryAxis axis = plot.getDomainAxis();
        axis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 8.0));
        BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
		renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());
        renderer.setDrawBarOutline(false);

		setCategorySummary(dataset);
        return chart;

    }
 /*
	protected void initMixPanel(){
		dataPanel2 = new JPanel();
		dataPanel2.setLayout(new BoxLayout(dataPanel2, BoxLayout.Y_AXIS));

		graphPanel2 = new JPanel();
		graphPanel2.setLayout(new BoxLayout(graphPanel2, BoxLayout.Y_AXIS));

		mixPanel = new JPanel(new BorderLayout());
		//		resetChart();

		setMixPanel();
	}*/

  
}
