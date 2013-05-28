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

import javax.swing.JLabel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CompassPlot;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperValueChart;

/**
 * A simple demonstration application showing how to create a compass chart
 */
public class CompassDemo1 extends SuperValueChart implements PropertyChangeListener {

	protected ValueDataset createDateset(boolean isDemo){
		if (isDemo){
			ValueDataset dataset = new DefaultValueDataset(new Double(45.0));
		return dataset;
		}	
		else return super.createDataset(false);
		
    }
	
	public void init(){
		LEGEND_SWITCH =false;
	//	indLabel = new JLabel("X");
		depLabel = new JLabel("Y");

		mapIndep=false;
		
		super.init();
		depMax = 1; // max number of dependent var
		indMax = 5; // max number of independent var

		updateStatus(url);	
	  
		resetExample();
		validate();
		
	}
	
	/**
	 * sample code showing how to create a  chart using ChartGenerator_JTable class
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+" doTest get called!");
		
		 int no_series = dataTable.getColumnCount();		 
		 int[][] pairs = new int[no_series][1];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = i;    //column x
		 }
	
		 chart = chartMaker.getCompassChart("CompassChart",  dataTable, no_series, pairs, "");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(ValueDataset dataset) {
       

        CompassPlot plot = new CompassPlot(dataset);
        plot.setSeriesNeedle(7);
        plot.setSeriesPaint(0, Color.red);
        plot.setSeriesOutlinePaint(0, Color.red);
        plot.setRoseHighlightPaint(Color.CYAN);
        JFreeChart chart = new JFreeChart(plot);
        return chart;
        
    }

}
