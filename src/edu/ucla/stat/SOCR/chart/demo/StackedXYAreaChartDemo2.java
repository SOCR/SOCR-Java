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

import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.jfree.data.xy.TableXYDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperAreaChart_XY;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;


/**
 * A demo showing a stacked area chart created with the 
 * <code>CategoryTableXYDataset</code>.
 */
public class StackedXYAreaChartDemo2 extends SuperAreaChart_XY implements PropertyChangeListener {

	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+" doTest get called!");
		
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
    protected TableXYDataset createDataset(boolean isDemo) {
		if (isDemo){
        CategoryTableXYDataset dataset = new CategoryTableXYDataset();
        dataset.add(0.0, 0.0, "Series 1");
        dataset.add(10.0, 20.0, "Series 1");
        dataset.add(20.0, 15.0, "Series 1");
        dataset.add(30.0, 25.0, "Series 1");
        dataset.add(40.0, 21.0, "Series 1");

        dataset.add(10.0, 9.0, "Series 2");
        dataset.add(20.0, -7.0, "Series 2");
        dataset.add(30.0, 15.0, "Series 2");
        dataset.add(40.0, 11.0, "Series 2");
        dataset.add(45.0, -10.0, "Series 2");  // no matching value in series 1
        dataset.add(50.0, 0.0, "Series 2");  // likewise
        return dataset;}
		else
			{
				
			setArrayFromTable();
			
			if (independentVarLength != dependentVarLength){
			JOptionPane.showMessageDialog(this, "The number of X and Y doesn't match!");
			resetChart();
			return null;
			}

			String[][] x= new String[xyLength][independentVarLength];
			double[][] y= new double[xyLength][dependentVarLength];
			int[][] skipy= new int[xyLength][dependentVarLength];

			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++)
					x[i][index] = indepValues[i][index];
			   
		   
			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
					if (depValues[i][index]==null ||depValues[i][index]=="null" || depValues[i][index].length()==0)
						skipy[i][index]=1;
					else  y[i][index] = Double.parseDouble(depValues[i][index]);
				}

			// create the dataset... 
			CategoryTableXYDataset dataset = new CategoryTableXYDataset();

			for (int i=0; i<independentVarLength; i++){
				String serieName = independentHeaders[i];
				if (serieName.indexOf(':')!=-1)
					serieName = independentHeaders[i].substring(0, serieName.indexOf(':'));
				
				for (int j=0; j<dataTable.getRowCount()-1; j++){
					if (x[j][i]!=null && x[j][i].length()!=0 && skipy[j][i]!=1)
						//dataset.add(0.0, y[j][i], independentHeaders[i]);
					 dataset.add(Double.parseDouble(x[j][i]), y[j][i], serieName);
					//System.out.println("adding :("+x[j][i]+","+y[j][i]+","+independentHeaders[i]+")" );
				}
			}
		
            return dataset; 

			} 
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
