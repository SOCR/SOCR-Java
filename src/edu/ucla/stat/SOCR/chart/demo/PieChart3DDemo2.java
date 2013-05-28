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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.Timer;

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
 * A rotating 3D pie chart.
 */
public class PieChart3DDemo2 extends SuperPieChart implements PropertyChangeListener {
	

	public void init(){
		ThreeDPie= true;
		super.init();
	}
	
	
	/**
	 * sample code showing how to use ChartGenerator_JTable to create chart
	 */
	
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("Piechart3DDemo2 doTest get called!");
		
		 int[][] pairs = new int[1][2];
		 pairs[0][0] = 1;   // value
		 pairs[0][1] = 0;   // name
		 chart = chartMaker.getPieChart("Pie Chart3DDemo2", dataTable, pairs,"3D counter_clockwise");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    /**
     * Creates a demo chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    protected JFreeChart createChart(PieDataset dataset) {
        // create the chart...
        JFreeChart chart = ChartFactory.createPieChart3D(
            chartTitle,  // chart title
            dataset,                // dataset
            !legendPanelOn,                   // include legend
            false,
            false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(270);
        plot.setDirection(Rotation.ANTICLOCKWISE);
        plot.setForegroundAlpha(0.60f);
        plot.setInteriorGap(0.05);  // 0.33 used to work with JFreeChart 1.0.05

        for (int i=0; i<pulloutFlag.length; i++){
        	//System.out.println("\""+pulloutFlag[i]+"\"");
        	if (pulloutFlag[i].equals("1")){
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

    /**
     * Creates a sample dataset for the demo.
     *  @param isDemo  true use demo data, false use data from JTable
     * @return A sample dataset.
     */
    protected DefaultPieDataset createDataset(boolean isDemo) {
		if (isDemo){
        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Java", new Double(43.2));
        result.setValue("Visual Basic", new Double(10.0));
        result.setValue("C/C++", new Double(17.5));
        result.setValue("PHP", new Double(32.5));
        result.setValue("Perl", new Double(12.5));
        pulloutFlag = new String[5];
        for (int i=0; i<5; i++)
        	pulloutFlag[i]="0";
        pulloutFlag[2]="1";
       
        return result;
		}
		else return super.createDataset(isDemo);
    }

}

