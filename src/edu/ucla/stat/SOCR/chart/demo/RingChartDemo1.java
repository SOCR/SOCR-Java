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

import java.awt.Font;
import java.beans.PropertyChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.RingPlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperPieChart;
import edu.ucla.stat.SOCR.chart.gui.Rotator;


/**
 * A simple demonstration application showing how to create a ring chart using 
 * data from a {@link DefaultPieDataset}.
 */
public class RingChartDemo1  extends SuperPieChart implements PropertyChangeListener {
	
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+" doTest get called!");
		
		 int[][] pairs = new int[1][2];
		 pairs[0][0] = 1;   // value
		 pairs[0][1] = 0;   // name
		 chart = chartMaker.getPieChart(chartTitle, dataTable, pairs,"ring");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }

    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected DefaultPieDataset createDataset(boolean isDemo) {
		if (isDemo){
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("One", new Double(43.2));
        dataset.setValue("Two", new Double(10.0));
        dataset.setValue("Three", new Double(27.5));
        dataset.setValue("Four", new Double(17.5));
        dataset.setValue("Five", new Double(11.0));
        dataset.setValue("Six", new Double(19.4));
        pulloutFlag = new String[6];
        for (int i=0; i<6; i++)
        	pulloutFlag[i]="0";
        pulloutFlag[2]="1";
        return dataset; }
		else return super.createDataset(isDemo);      
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a chart.
     */
    protected JFreeChart createChart(PieDataset dataset) {
        
        JFreeChart chart = ChartFactory.createRingChart(
            chartTitle,  // chart title
            dataset,             // data
            !legendPanelOn,               // include legend
            true,
            false
        );

        RingPlot plot = (RingPlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        for (int i=0; i<pulloutFlag.length; i++){
        	//System.out.println("\""+pulloutFlag[i]+"\"");
        	if (isPullout(i)){
        		Comparable key = dataset.getKey(i);
        		plot.setExplodePercent(key, 0.30);
        	}
        }
       
        if(rotateOn){
        	Rotator rotator = new Rotator(plot);
        	rotator.start();
        }
        
		setCategorySummary(dataset);
        return chart;
        
    }
    
 }
