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

import java.awt.Color;
import java.awt.Paint;
import java.beans.PropertyChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_Bar;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;
import edu.ucla.stat.SOCR.chart.gui.CustomBarRenderer; 

/**
 * A bar chart that uses a custom renderer to display different colors within a
 * series.  No legend is displayed because there is only one series but the 
 * colors are not consistent.  Also, a domain marker is used to highlight one
 * category.
 */
public class BarChartDemo3 extends SuperCategoryChart_Bar implements PropertyChangeListener {

	/**
	 * sample code showing how to create a  chart using ChartGenerator_JTable class
	 */
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
		 chart = chartMaker.getCategoryChart("Bar", chartTitle, "Category", "value", dataTable, no_category, pairs, "");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
   
  
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {
		if (isDemo){
        double[][] data = new double[][] {{4.0, 3.0, -2.0, 3.0, 6.0}};
        return DatasetUtilities.createCategoryDataset("Series ", "Category ",
													  data);}
		else return super.createDataset(false);
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a sample chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {
    
        JFreeChart chart = ChartFactory.createBarChart(
            chartTitle,       // chart title
            domainLabel,               // domain axis label
            rangeLabel,                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // the plot orientation
            !legendPanelOn,                    // include legend
            true,
            false
        );

        chart.setBackgroundPaint(Color.lightGray);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setNoDataMessage("NO DATA!");

        CategoryItemRenderer renderer = new CustomBarRenderer(
            new Paint[] {Color.red, Color.blue, Color.green,
                Color.yellow, Color.orange, Color.cyan,
                Color.magenta, Color.blue}
        );
        renderer.setBaseItemLabelGenerator(
            new StandardCategoryItemLabelGenerator()
        );
        renderer.setBaseItemLabelsVisible(true);
        ItemLabelPosition p = new ItemLabelPosition(
            ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 45.0
        );
        renderer.setBasePositiveItemLabelPosition(p);
        plot.setRenderer(renderer);

        CategoryMarker marker = new CategoryMarker("Category 3");
        marker.setLabel("Special");
        marker.setPaint(new Color(0xDD, 0xFF, 0xDD, 0x80));
        marker.setAlpha(0.5f);
        marker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        marker.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        marker.setLabelOffsetType(LengthAdjustmentType.CONTRACT);
        plot.addDomainMarker(marker, Layer.BACKGROUND);
        
        // change the margin at the top of the range axis...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLowerMargin(0.15);
        rangeAxis.setUpperMargin(0.15);

        BarRenderer seriesRenderer = (BarRenderer) plot.getRenderer();
		seriesRenderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

		setCategorySummary(dataset);
        return chart;

    }
        
}
