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

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperXYChart;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;

/**
 * A simple demonstration application showing how to use the 
 * {@link SymbolAxis} class with an {@link XYPlot}.
 */
public class SymbolAxisDemo1  extends SuperXYChart implements PropertyChangeListener {
    

	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SymboliAxisDemo doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;		 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 2*i;    //column x
			 pairs[i][1] = 2*i+1;    //column y
		 }
		 chart = chartMaker.getXYChart("SymbolicAxis","SymbolicAxis Chart", "X", "Y", dataTable, no_series, pairs,"");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }

	
    protected JFreeChart createChart(XYDataset dataset) {
        SymbolAxis domainAxis = new SymbolAxis("Domain", 
                new String[] {"A", "B", "C", "D"});
        SymbolAxis rangeAxis = new SymbolAxis("Range", 
                new String[] {"V", "X", "Y", "Z"});
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
        if (!legendPanelOn) 
        	renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

        XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
        JFreeChart chart = new JFreeChart(chartTitle, plot);

		setXSummary(dataset);
		
		if (legendPanelOn)
        	chart.removeLegend();
        return chart;
    }
    
    /**
     * Creates a sample dataset.
     */
    protected XYDataset createDataset(boolean isDemo) {
		if (isDemo){
        XYSeries s1 = new XYSeries("Series 1");
        s1.add(0, 3);
        s1.add(1, 3);
        s1.add(2, 0);
        s1.add(3, 1);
        XYSeries s2 = new XYSeries("Series 2");
        s2.add(0, 1);
        s2.add(1, 2);
        s2.add(2, 1);
        s2.add(3, 3);
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        return dataset;}
		else return super.createDataset(false);
    }

}
