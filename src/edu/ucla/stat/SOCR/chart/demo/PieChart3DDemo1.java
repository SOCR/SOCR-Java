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
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperPieChart;
import edu.ucla.stat.SOCR.chart.gui.Rotator;

/**
 * A simple demonstration application showing how to create a pie chart using 
 * data from a {@link DefaultPieDataset}.
 */
public class PieChart3DDemo1 extends SuperPieChart implements PropertyChangeListener {


	public void init(){
		ThreeDPie= true;
		super.init();
	}
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("Piechart3DDemo1 doTest get called!");
		
		 int[][] pairs = new int[1][2];
		 pairs[0][0] = 1;   // value
		 pairs[0][1] = 0;   // name
		 chart = chartMaker.getPieChart("Pie Chart3DDemo1", dataTable, pairs,"3D");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    /**
     * Creates a sample dataset for the demo.
     * @param isDemo  true use demo data, false use data from JTable
     * @return A sample dataset.
     */
    protected DefaultPieDataset createDataset(boolean isDemo) {
        	if (isDemo){
        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Java", new Double(43.2));
        result.setValue("Visual Basic", new Double(10.0));
        result.setValue("C/C++", new Double(17.5));
        result.setValue("PHP", new Double(32.5));
        result.setValue("Perl", null);  //new Double(1.0));
        pulloutFlag = new String[5];
        for (int i=0; i<5; i++)
        	pulloutFlag[i]="0";
        pulloutFlag[2]="1";
        return result;
			}
			else return super.createDataset(false);
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    protected JFreeChart createChart(PieDataset dataset) {
        
        JFreeChart chart = ChartFactory.createPieChart3D(
            chartTitle,  // chart title
            dataset,                // data
            !legendPanelOn,                   // include legend
            true,
            false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);

     /*   for (int i=0; i<pulloutFlag.length; i++){
        	//System.out.println("\""+pulloutFlag[i]+"\"");
        	if (pulloutFlag[i].equals("1")){
        		Comparable key = dataset.getKey(i);
        		plot.setExplodePercent(key, 0.30);
        	}
        }*/
        
        plot.setNoDataMessage("No data to display");
       
        if(rotateOn){
        	Rotator rotator = new Rotator(plot);
        	rotator.start();
        }
        
		setCategorySummary(dataset);
        return chart;
        
    }

}
