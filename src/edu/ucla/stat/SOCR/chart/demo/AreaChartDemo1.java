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
import java.awt.Font;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;
import org.jfree.util.UnitType;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart;
import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;

/**
 * A simple demonstration application showing how to create an area chart 
 * using data from a {@link CategoryDataset}.
 */
public class AreaChartDemo1 extends SuperCategoryChart implements PropertyChangeListener {	
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable(); 
		 
		 resetChart();
		 showMessageDialog("AreaChartDemo1 doTest get called!");
		
		 int no_series = dataTable.getRowCount()-1;
		 int no_category = dataTable.getColumnCount()-2;
		 int[][] pairs = new int[no_category][2];
		 for (int i=0; i<no_category; i++){
			 pairs[i][0] = i+1;    //column i store category i
			 pairs[i][1] = 0;    //column 0 stores series name
		 }
		 chart = chartMaker.getCategoryChart("Area", "Area Chart", "Category", "value", dataTable, no_category, pairs, "");	
		 chartPanel = new ChartPanel(chart, false); 
		 dataset = createDataset(false);	
		 setChart();
	 }

	protected JFreeChart createLegend(CategoryDataset dataset) {
        
		JFreeChart chart = ChartFactory.createAreaChart(
	       // JFreeChart chart = ChartFactory.createLineChart(
	            chartTitle,             // chart title
	            domainLabel,               // domain axis label
	            rangeLabel,                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
	 
	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	        chart.setBackgroundPaint(Color.white);
	        CategoryPlot plot = chart.getCategoryPlot();

			AreaRenderer renderer = (AreaRenderer)plot.getRenderer();
	        renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());
	        return chart;
	        
	    }
	
	public void init(){
		turnLegendPanelOn();
		super.init();
	
	}
	
	/**
	   * 
	   * @param isDemo data come from demo(true) or dataTable(false)
	   * @return
	   */  
    protected  CategoryDataset createDataset(boolean isDemo) {
		if (isDemo){
       
		DefaultCategoryDataset	dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "Series 1", "Type 1");
        dataset.addValue(4.0, "Series 1", "Type 2");
        dataset.addValue(3.0, "Series 1", "Type 3");
        dataset.addValue(5.0, "Series 1", "Type 4");
        dataset.addValue(5.0, "Series 1", "Type 5");
        dataset.addValue(7.0, "Series 1", "Type 6");
        dataset.addValue(7.0, "Series 1", "Type 7");
        dataset.addValue(8.0, "Series 1", "Type 8");
        dataset.addValue(5.0, "Series 2", "Type 1");
        dataset.addValue(7.0, "Series 2", "Type 2");
        dataset.addValue(6.0, "Series 2", "Type 3");
        dataset.addValue(8.0, "Series 2", "Type 4");
        dataset.addValue(4.0, "Series 2", "Type 5");
        dataset.addValue(4.0, "Series 2", "Type 6");
        dataset.addValue(2.0, "Series 2", "Type 7");
        dataset.addValue(1.0, "Series 2", "Type 8");
        dataset.addValue(4.0, "Series 3", "Type 1");
        dataset.addValue(3.0, "Series 3", "Type 2");
        dataset.addValue(2.0, "Series 3", "Type 3");
        dataset.addValue(3.0, "Series 3", "Type 4");
        dataset.addValue(6.0, "Series 3", "Type 5");
        dataset.addValue(3.0, "Series 3", "Type 6");
        dataset.addValue(4.0, "Series 3", "Type 7");
        dataset.addValue(3.0, "Series 3", "Type 8");
        return dataset;}
		else return super.createDataset(false);
    }
    
    /**
     * Creates a Area chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {
                
        JFreeChart chart = ChartFactory.createAreaChart(
            chartTitle,             // chart title
            domainLabel,               // domain axis label
            rangeLabel,                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            !legendPanelOn,                     // include legend
            true,                     // tooltips
            false                     // urls
        );
 
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        if(isDemo){
        TextTitle subtitle = new TextTitle(
            "An area chart demonstration.  We use this subtitle as an "
            + "example of what happens when you get a really long title or "
            + "subtitle."
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setPosition(RectangleEdge.TOP);
        subtitle.setPadding(
            new RectangleInsets(UnitType.RELATIVE, 0.05, 0.05, 0.05, 0.05)
        );
        subtitle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        chart.addSubtitle(subtitle);
        }

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setForegroundAlpha(0.5f);
        
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        AreaRenderer renderer = (AreaRenderer)plot.getRenderer();
        renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        if(isDemo){
        	domainAxis.addCategoryLabelToolTip("Type 1", "The first type.");
        	domainAxis.addCategoryLabelToolTip("Type 2", "The second type.");
        	domainAxis.addCategoryLabelToolTip("Type 3", "The third type.");
        }
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLabelAngle(0 * Math.PI / 2.0);
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        setCategorySummary(dataset);
        return chart;
        
    }
    
   
    
    /*protected void setChart(){
		// update graph
		//System.out.println("setChart called");

		graphPanel.removeAll();
		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

		graphPanel.add(chartPanel);
		graphPanel.validate();


		// get the GRAPH panel to the front
		if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) 
			tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
		else {
			graphPanel2.removeAll();
			chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
			graphPanel2.add(chartPanel);
			graphPanel2.validate();	
			summaryPanel.validate();
		}
    }*/
    
   /* public void setChart(){
    	// update graph
    //	System.out.println("setChart called");
    	JFreeChart chart1 = createChart(dataset);	
    	JFreeChart chart2 = createLegendChart(createLegend(dataset));	

    	graphPanel.removeAll();
    	graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

    	chartPanel = new ChartPanel(chart1, false); 
    	chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

    	legendPanel = new ChartPanel(chart2,false);
    	legendPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y*2/3));
    		
    	graphPanel.add(chartPanel);
    	JScrollPane	 legendPane = new JScrollPane(legendPanel);
    	legendPane.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/3));
    	graphPanel.add(legendPane);
    	
    	graphPanel.validate();

    	// get the GRAPH panel to the front
    	if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) {
			tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
			graphPanel.removeAll();
			graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
				
			graphPanel.add(chartPanel);
			legendPane = new JScrollPane(legendPanel);
	    	legendPane.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/3));
	    	graphPanel.add(legendPane);
	    	graphPanel.validate();
		}
    	else {
    		graphPanel2.removeAll();
    		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
    		graphPanel2.add(chartPanel);
    		legendPane = new JScrollPane(legendPanel);
        	legendPane.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y/3));
        	graphPanel2.add(legendPane);
    		graphPanel2.validate();	
    		summaryPanel.validate();
    	}
    }*/
    
 /*   protected void setMixPanel(){
    //	System.out.println("setMixPanel called");
		dataPanel2.removeAll();
		graphPanel2.removeAll();

		graphPanel2.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
		graphPanel2.setLayout(new BoxLayout(graphPanel2, BoxLayout.Y_AXIS));
		if (chartPanel!=null)
			 graphPanel2.add(chartPanel);
		if (legendPanel!=null){
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
	
	 protected void setGraphPanel(){
	//	 System.out.println("setGraphPanel called");
		 
 		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));
		graphPanel.add(chartPanel);
		JScrollPane	 legendPane = new JScrollPane(legendPanel);
    	legendPane.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/3));
    	graphPanel.add(legendPane);
 }
 
	 public void doChart(){

		    //	System.out.println("dochart get called");
		    	
				if(dataTable.isEditing())
					dataTable.getCellEditor().stopCellEditing();
				if (! hasExample ) {
					showMessageDialog(DATA_MISSING_MESSAGE);
					resetChart();
					return;
				}

				CategoryDataset dataset = createDataset(false);
				JFreeChart chart = createChart(dataset);	
				chartPanel = new ChartPanel(chart, false); 
				setChart();
		    }
		    
}
