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
package edu.ucla.stat.SOCR.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import java.lang.NumberFormatException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
//import org.jfree.data.contour.DefaultContourDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;

import edu.ucla.stat.SOCR.chart.data.SampleXYZDataset;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYZSeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.util.PercentSlider;

/**
  * A simple demonstration application showing how to create a Bubble chart.
 */
public class SuperBubbleChart extends SuperXYZChart {

	protected double zScale = 1;

	/**
	   * 
	   * @param isDemo data come from demo(true) or dataTable(false)
	   * @return
	   */  
    protected  XYZDataset createDataset(boolean isDemo) {
		if (isDemo){
			XYZDataset dataset = new SampleXYZDataset();
			domainLabel = "X";
			rangeLabel = "Y";
			return dataset;
     
		}else{
			setXYZArray();
			int len = Array.getLength(x);

		/*	Object[] xData = new Object[len]; 
			Object[] yData = new Object[len]; 
			Object[] zData = new Object[len]; 

			for (int i=0; i<len; i++){
				xData[i]=new Double(x[i]);
				yData[i]=new Double(y[i]);
				zData[i]=new Double(z[i]);
			}
			// create the dataset... 
			DefaultContourDataset dataset = new DefaultContourDataset((Comparable)independentHeaders[0].substring(0,independentHeaders[0].indexOf(":")), xData, yData, zData); */
			
			double[][] data = new double[3][len];
			
			double zMax = z[0];
			double xMax = x[0];
			double yMax = y[0];
			
			for (int i=1; i<len; i++){
				if (x[i]>xMax)
					xMax = x[i];
				if (y[i]>yMax)
					yMax = y[i];
				if (z[i]>zMax)
					zMax = z[i];
			}
			
			if (xMax>yMax)
				xMax = yMax;
			
			if (zMax > xMax*0.75)
				zScale = (xMax*0.75)/zMax;
				 
		
			zScale = zScale*zShrinkPercent/100;
			//System.out.println("zScale="+zScale);
			for (int i=0; i<len; i++){
				data[0][i]=x[i];
				data[1][i]=y[i];
				data[2][i]=z[i]*zScale;
			}
			DefaultXYZDataset dataset = new DefaultXYZDataset();
			String serieName = independentHeaders[0];
			if (independentHeaders[0].indexOf(":")!=-1)
				serieName = independentHeaders[0].substring(0,independentHeaders[0].indexOf(":"));
			
			dataset.addSeries((Comparable)serieName, data);
            return dataset; 
		}

	} 

    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a chart.
     */
    protected JFreeChart createChart(XYZDataset dataset) {
         // create the chart...
        JFreeChart chart = ChartFactory.createBubbleChart(
            chartTitle,      // chart titl                          
            rangeLabel,    // y axis label
            domainLabel,    // x axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            !legendPanelOn,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setForegroundAlpha(0.65f);
        
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
    	renderer.setLegendItemLabelGenerator(new SOCRXYZSeriesLabelGenerator());           

        // increase the margins to account for the fact that the auto-range 
        // doesn't take into account the bubble size...
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setLowerMargin(0.15);
        domainAxis.setUpperMargin(0.15);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLowerMargin(0.15);
        rangeAxis.setUpperMargin(0.15);
                      
        return chart;
	}	
    
  
}
