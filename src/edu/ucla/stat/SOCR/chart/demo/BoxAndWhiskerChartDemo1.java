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
import java.beans.PropertyChangeListener;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperBoxAndWhiskerChart;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategoryCellLabelGenerator;

/**
 * A simple demonstration application showing how to create a box-and-whisker 
 * chart.
 */
public class BoxAndWhiskerChartDemo1 extends SuperBoxAndWhiskerChart implements PropertyChangeListener {
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
		 chart = chartMaker.getBoxAndWhiskerCategoryChart(chartTitle, "Category", "value", dataTable, no_category, pairs,"");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    /**
     * Returns a sample dataset.
     * 
     * @return The dataset.
     */
    protected BoxAndWhiskerCategoryDataset createDataset(boolean isDemo) {
		if (isDemo){
        SERIES_COUNT = 3;
        CATEGORY_COUNT = 2;
        VALUE_COUNT = 10;
		values_storage = new String[SERIES_COUNT][CATEGORY_COUNT];
   

        DefaultBoxAndWhiskerCategoryDataset result 
            = new DefaultBoxAndWhiskerCategoryDataset();

        for (int s = 0; s < SERIES_COUNT; s++) {
            for (int c = 0; c < CATEGORY_COUNT; c++) {
                List values = createValueList(0, 20.0, VALUE_COUNT);
				values_storage[s][c]= vs;
                result.add(values, "Series " + s, "Category " + c);
            }
        }
        return result;}
		else return super.createDataset(false);
    }
    
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(BoxAndWhiskerCategoryDataset dataset) {
        
        CategoryAxis domainAxis = new CategoryAxis(null);
        NumberAxis rangeAxis = new NumberAxis(rangeLabel);
        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        CategoryPlot plot = new CategoryPlot(
            dataset, domainAxis, rangeAxis, renderer
        );
        JFreeChart chart = new JFreeChart(chartTitle, plot);
        
        chart.setBackgroundPaint(Color.white);

        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		renderer.setLegendItemLabelGenerator(new SOCRCategoryCellLabelGenerator(dataset, values_storage,SERIES_COUNT, CATEGORY_COUNT));
		
		//RowCount -- serie count
		if(dataset.getColumnCount()* dataset.getRowCount()<5){

			domainAxis.setLowerMargin(0.2);
			domainAxis.setUpperMargin(0.2);
	   		if (dataset.getColumnCount()==1)
				renderer.setItemMargin(0.5);
			//	domainAxis.setCategoryMargin(domainAxis.getCategoryMargin()*2);
			/*		   
			System.out.println("1lowerMargin="+domainAxis.getLowerMargin());
			System.out.println("ItemMargin="+renderer.getItemMargin());
			System.out.println("CategoryMargin="+domainAxis.getCategoryMargin());*/

		}

		else if(dataset.getColumnCount()* dataset.getRowCount()<10){
			domainAxis.setLowerMargin(domainAxis.getLowerMargin()*2);
			domainAxis.setUpperMargin(domainAxis.getUpperMargin()*2);
	   		if (dataset.getColumnCount()==1)
				renderer.setItemMargin(renderer.getItemMargin()*2);
			else 			
				domainAxis.setCategoryMargin(domainAxis.getCategoryMargin()*2);

			/*System.out.println("2lowerMargin="+domainAxis.getLowerMargin());
			System.out.println("ItemMargin="+renderer.getItemMargin());
			System.out.println("CategoryMargin="+domainAxis.getCategoryMargin());*/
		
		}
	
		if (legendPanelOn)
			chart.removeLegend();
        return chart;
        
    }
 
}
