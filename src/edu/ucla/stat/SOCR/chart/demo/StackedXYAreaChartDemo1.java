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

import java.beans.PropertyChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYSeries;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperAreaChart_XY;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;

/**
 * This demo shows the creation of a stacked XY area chart.  
 * 
 */
public class StackedXYAreaChartDemo1 extends SuperAreaChart_XY implements PropertyChangeListener {

	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+ " doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;		 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 2*i;    //column x
			 pairs[i][1] = 2*i+1;    //column y
		 }
		 chart = chartMaker.getTableXYAreaChart("Area Chart", "X Vaue", "Y Value", dataTable, no_series, pairs, "");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected  TableXYDataset createDataset(boolean isDemo) {
        if (isDemo){
        DefaultTableXYDataset dataset = new DefaultTableXYDataset();
        
        XYSeries s1 = new XYSeries("Series 1", true, false);
        s1.add(5.0, 5.0);
        s1.add(10.0, 15.5);
        s1.add(15.0, 9.5);
        s1.add(20.0, 7.5);
        dataset.addSeries(s1);
        
        XYSeries s2 = new XYSeries("Series 2", true, false);
        s2.add(5.0, 5.0);
        s2.add(10.0, 15.5);
        s2.add(15.0, 9.5);
        s2.add(20.0, 3.5);
        dataset.addSeries(s2);
        
        return dataset;}
		else return super.createDataset(false);
        
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset for the chart.
     * 
     * @return a sample chart.
     */
    protected JFreeChart createChart(TableXYDataset dataset) {

        JFreeChart chart = ChartFactory.createStackedXYAreaChart(
            chartTitle,  // chart title
            domainLabel,                       // domain axis label
            rangeLabel,                       // range axis label
            dataset,                         // data
            PlotOrientation.VERTICAL,        // the plot orientation
            !legendPanelOn,                            // legend
            true,                            // tooltips
            false                            // urls
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        StackedXYAreaRenderer2 renderer = new StackedXYAreaRenderer2(); 
        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        plot.setRenderer(0, renderer);  

		renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

		setXSummary(dataset);
        return chart;
        
    }

  
}
