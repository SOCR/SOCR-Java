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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperPieChart;
import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;
import edu.ucla.stat.SOCR.chart.gui.Rotator;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;


/**
 * A simple demonstration application showing how to create a pie chart using data from a
 * {@link DefaultPieDataset}.  This chart has a lot of labels and rotates, so it is useful for
 * testing the label distribution algorithm.
 */
public class PieChartDemo4 extends SuperPieChart implements PropertyChangeListener {
	
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("PieChartDemo4 doTest get called!");
		
		 int[][] pairs = new int[1][2];
		 pairs[0][0] = 1;   // value
		 pairs[0][1] = 0;   // name
		 chart = chartMaker.getPieChart("Pie Chart Demo4", dataTable, pairs,"2D clockwise");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
	/* public void setChart(){
	    	// update graph
	    //	System.out.println("setChart called");
		 
	    	JFreeChart chart1 = createChart(dataset);	

	    	graphPanel.removeAll();
	    	graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

	    	chartPanel = new ChartPanel(chart1, false); 
	    	chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

	    	if (legendPanelOn){
	    		JFreeChart chart2 = createLegendChart(createLegend(dataset));
	    		legendPanel = new ChartPanel(chart2,false);
		    	legendPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y*2/3));
	    	}
	    		
	    	graphPanel.add(chartPanel);
	    	JScrollPane	 legendPane = new JScrollPane(legendPanel);
	    	if (legendPanelOn){
	    		legendPane.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/3));
	    		graphPanel.add(legendPane);
	    	}
	    	
	    	graphPanel.validate();

	    	// get the GRAPH panel to the front
	    	if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) {
	    		tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
	    		graphPanel.removeAll();
	    		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
				
				graphPanel.add(chartPanel);
				legendPane = new JScrollPane(legendPanel);
		    	legendPane.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/3));
		    	if (legendPanelOn) graphPanel.add(legendPane);
		    	graphPanel.validate();
	    	}
	    	else {
	    		graphPanel2.removeAll();
	    		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
	    		graphPanel2.add(chartPanel);
	    		legendPane = new JScrollPane(legendPanel);
	        	legendPane.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y/3));
	        	if (legendPanelOn) graphPanel2.add(legendPane);
	    		graphPanel2.validate();	
	    		summaryPanel.validate();
	    	}
	    }*/
	    
/*	 protected void setMixPanel(){
		    //	System.out.println("setMixPanel called");
				dataPanel2.removeAll();
				graphPanel2.removeAll();

				graphPanel2.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
				graphPanel2.setLayout(new BoxLayout(graphPanel2, BoxLayout.Y_AXIS));
				
				graphPanel2.add(chartPanel);
				if (legendPanelOn){
					JScrollPane	 legendPane = new JScrollPane(legendPanel);
					legendPane.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/3));
					graphPanel2.add(legendPane);
				}
				graphPanel2.validate();

				dataPanel2.add(new JLabel(" "));
				dataPanel2.add(new JLabel("Data"));
				JScrollPane dt = new JScrollPane(dataTable);
				dt.setPreferredSize(new Dimension(CHART_SIZE_X/3, CHART_SIZE_Y*3/8));

				dataPanel2.add(dt);
				JScrollPane st = new JScrollPane(summaryPanel);
				st.setPreferredSize(new Dimension(CHART_SIZE_X/3,CHART_SIZE_Y/6));
				dataPanel2.add(st);
				st.validate();

				dataPanel2.add(new JLabel(" "));
				dataPanel2.add(new JLabel("Mapping"));
				mapPanel.setPreferredSize(new Dimension(CHART_SIZE_X/3, CHART_SIZE_Y/2));
				dataPanel2.add(mapPanel);

				dataPanel2.validate();

				mixPanel.removeAll();
				mixPanel.add(graphPanel2, BorderLayout.WEST);
				mixPanel.add(new JScrollPane(dataPanel2), BorderLayout.CENTER);
				mixPanel.validate();	
			}*/
			
	
   protected JFreeChart createChart(PieDataset dataset) {
        // create the chart...
        JFreeChart chart = ChartFactory.createPieChart(
            chartTitle,  // chart title
            dataset,             // dataset
            !legendPanelOn,               // include legend
            true,
            false
        );

        // set the background color for the chart...
        chart.setBackgroundPaint(new Color(222, 222, 255));
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setCircular(true);
        for (int i=0; i<pulloutFlag.length; i++){
        	//System.out.println("\""+pulloutFlag[i]+"\"");
        	if (isPullout(i)){
        		Comparable key = dataset.getKey(i);
        		plot.setExplodePercent(key, 0.30);
        	}
        }
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} = {2}",
                NumberFormat.getNumberInstance(), 
                NumberFormat.getPercentInstance()));
        plot.setNoDataMessage("No data available");

        if(rotateOn){
        	Rotator rotator = new Rotator(plot);
        	rotator.start();
        }
		setCategorySummary(dataset);
		return chart;
    }

  

  
   
   public void doChart(){
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			showMessageDialog(DATA_MISSING_MESSAGE);
			resetChart();
			return;
		}
		if (dependentIndex < 0 || independentIndex < 0 || independentLength == 0) {
			showMessageDialog(VARIABLE_MISSING_MESSAGE);
			resetChart();
			return;
			}

		dataset = createDataset(false);	 // not a demo, so get data from the table
		JFreeChart chart1 = createChart(dataset);	
		chartPanel = new ChartPanel(chart1, false); 
		
		
		setChart();
		//updateStatus("Chart has been updated, click GRAPH to view it.");
	}

   
    /**
     * Creates a sample dataset.
     * 
     * @param sections  the number of sections.
     * 
     * @return A sample dataset.
     */
    protected  DefaultPieDataset createDataset(boolean isDemo) {
       	if (isDemo){
			dataset = createDataset(14);
			pulloutFlag = new String[14];
		        for (int i=0; i<14; i++)
		        	pulloutFlag[i]="0";
		        pulloutFlag[2]="1";
        return dataset;	
		}else 
			return super.createDataset(false);
    }

    protected DefaultPieDataset createDataset(int sections) {
        DefaultPieDataset result = new DefaultPieDataset();
        for (int i = 0; i < sections; i++) {
            double value = 100.0 * Math.random();
            result.setValue("Section " + i, value);
        }
        return result;
    }
}
 
