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
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperDotChart;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create a line chart using
 * data from an {@link XYDataset}.  
 * 
 */
public class  DotChart extends SuperDotChart implements PropertyChangeListener {
	ChartPanel chartPanel1;
	ChartPanel chartPanel2;
	NumberAxis common_rangeAxis;
	
	public void init(){

		LEGEND_SWITCH= false;
		super.init();
		
	}
	
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+" doTest get called!");
		
		 int no_series = dataTable.getColumnCount();	 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 0;    //no column x
			 pairs[i][1] = 0;    //column y
		 }
		 chart = chartMaker.getXYChart("Dot","Dot Chart", "", "Data", dataTable, no_series, pairs,"noline horizontal");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 super.setChart();
	 }

     /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected XYDataset createDataset1(boolean isDemo) {
        if (isDemo){
        
        	row_count = 20;
        	raw_x = new String[row_count];      
        	raw_x[0] = "3.0"; 	raw_x[1] = "-2.0"; 	raw_x[2] = "4.0"; 	raw_x[3] = "4.0"; 	raw_x[4] = "5.0";
        	raw_x[5] = "6.0"; 	raw_x[6] = "6.0"; 	raw_x[7] = "7.0"; 	raw_x[8] = "1.0"; 	raw_x[9] = "1.0";
        	raw_x[10] = "1.0"; 	raw_x[11] = "2.0"; 	raw_x[12] = "3.0"; 	raw_x[13] = "4.0"; 	raw_x[14] = "3.0";
        	raw_x[15] = "4.0"; 	raw_x[16] = "3.0"; 	raw_x[17] = "10.0"; 	raw_x[18] = "7.0"; 	raw_x[19] = "6.0";
        	
        XYSeries series1 = new XYSeries("Data");
        series1.add(0.1, 3.0);
        series1.add(0.1, -2.0);
        series1.add(0.1,  4.0);
        series1.add(0.2, 4.0);
        series1.add(0.1, 5.0);
        series1.add(0.1, 6.0);
        series1.add(0.2, 6.0);
        series1.add(0.1, 7.0);
        series1.add(0.1, 1.0);
        series1.add(0.2, 1.0);
        series1.add(0.3, 1.0);
        series1.add(0.1, 2.0);
        series1.add(0.2, 3.0);
        series1.add(0.3, 4.0);
        series1.add(0.3, 3.0);
        series1.add(0.4, 4.0);
        series1.add(0.4, 3.0);
        series1.add(0.1, 10.0);
        series1.add(0.2, 7.0);
        series1.add(0.3, 6.0); 

       
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
                
        return dataset;}
		else return super.createDataset1(false);
        
    }
    
    protected BoxAndWhiskerCategoryDataset createDataset2(boolean isDemo) {
    	if (isDemo){
            SERIES_COUNT = 1;
            CATEGORY_COUNT = 1;
            VALUE_COUNT = 20;
            values_storage = new String[SERIES_COUNT][CATEGORY_COUNT];
       

            DefaultBoxAndWhiskerCategoryDataset result 
                = new DefaultBoxAndWhiskerCategoryDataset();
            List values = createValueList("3, -2, 4, 4, 5, 6, 6, 7, 1, 1, 1, 2,3, 4, 3, 4, 3, 10, 7, 6");
            
            result.add(values, "" , "" );
            
            values_storage[0][0]="3, -2, 4, 4, 5, 6, 6, 7, 1, 1, 1, 2,3, 4, 3, 4, 3, 10, 7, 6";
           
            return result;}
    		else return super.createDataset2(false);
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    protected JFreeChart createChart1(XYDataset dataset) {
    	//System.out.println("createChart1 called");
      //   create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle,      // chart title
            "",                      // x axis label domain
            rangeLabel,                      // y axis label  range
            dataset,                  // data
            PlotOrientation.HORIZONTAL,
            !legendPanelOn,                     // include legend
            true,                     // tooltips
            false                     // urls
        );
    	
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
       chart.setBackgroundPaint(Color.white);
        
       // get a reference to the plot for further customisation...
        XYPlot plot = (XYPlot) chart.getPlot();
     
        plot.setBackgroundPaint(Color.white);
	    plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        
        XYLineAndShapeRenderer renderer 
            = (XYLineAndShapeRenderer) plot.getRenderer();
      //  renderer.setSeriesShape(0, java.awt.Shape.round);
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        renderer.setBaseLinesVisible(false);
       
		renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

        //change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        rangeAxis.setUpperMargin(0.01);
        rangeAxis.setLowerMargin(0.01);
        
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        //domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setAutoRangeIncludesZero(true);     
        domainAxis.setTickLabelsVisible(false);
        domainAxis.setTickMarksVisible(false);      
        domainAxis.setUpperMargin(5);
        domainAxis.setLowerMargin(0.01);

        // OPTIONAL CUSTOMISATION COMPLETED.
	    setYSummary(dataset);     
	 
	    try{
        //	System.out.println("setting the common RangeAxis to null");
        		common_rangeAxis = null;	
        		common_rangeAxis = (NumberAxis)rangeAxis.clone();
        	//	System.out.println("creating the common RangeAxis");
        }catch(CloneNotSupportedException e){
        		System.out.println("CloneNotSupportedException!, exception caught");
           }

	    return chart;
    }
    
protected JFreeChart createChart2(BoxAndWhiskerCategoryDataset dataset) {
	//System.out.println("createChart2 called");
        CategoryAxis domainAxis = new CategoryAxis(null);
       // NumberAxis rangeAxis = new NumberAxis("X");
        
      //  System.out.println("using the common RangeAxis\n");
        common_rangeAxis.setAutoRange(false);
       // NumberAxis rangeAxis = common_rangeAxis;
        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        CategoryPlot plot = new CategoryPlot(
            dataset, domainAxis, common_rangeAxis, renderer
        );
        JFreeChart chart = new JFreeChart("", plot);
        
        chart.setBackgroundPaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setOrientation( PlotOrientation.HORIZONTAL);

        //rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        renderer.setFillBox(false);
       // renderer.setLegendItemLabelGenerator(new SOCRCategoryCellLabelGenerator(dataset, values_storage,SERIES_COUNT, CATEGORY_COUNT));
	
	    domainAxis.setLowerMargin(0.46);
		domainAxis.setUpperMargin(0.46);
	 	chart.removeLegend();
        return chart;
        
    }

public void setChart(){
	// update graph
	/*System.out.println("setChart called");
	Exception e = new Exception();
	e.printStackTrace();*/

	
	graphPanel.removeAll();
	graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

	
	graphPanel.add(chartPanel1);
	graphPanel.add(chartPanel2);
	graphPanel.validate();

	// get the GRAPH panel to the front
	if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) 
		tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
	else {
		graphPanel2.removeAll();
		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
		graphPanel2.add(chartPanel1);
		graphPanel2.add(chartPanel2);
		graphPanel2.validate();	
		summaryPanel.validate();
	}
}

    public void doChart(){

    	//System.out.println("dochart get called");
    	
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			showMessageDialog(DATA_MISSING_MESSAGE);
			resetChart();
			return;
		}

		isDemo= false;
		XYDataset dataset1 = createDataset1(isDemo);
		JFreeChart chart1 = createChart1(dataset1);	
		BoxAndWhiskerCategoryDataset dataset2 = createDataset2(isDemo);
		JFreeChart chart2 = createChart2(dataset2);	

		graphPanel.removeAll();
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

		chartPanel1 = new ChartPanel(chart1, false); 
		chartPanel1.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/2));

		chartPanel2 = new ChartPanel(chart2, false); 
		chartPanel2.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/3));
			
		graphPanel.add(chartPanel1);
		graphPanel.add(chartPanel2);
		graphPanel.validate();


		// get the GRAPH panel to the front
		if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) {
			tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
			graphPanel.removeAll();
			graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
				
			graphPanel.add(chartPanel1);
			graphPanel.add(chartPanel2);
			graphPanel.validate();
		}
		else {
			graphPanel2.removeAll();
			chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
			graphPanel2.add(chartPanel1);
			graphPanel2.add(chartPanel2);
			graphPanel2.validate();	
			summaryPanel.validate();
		}
    }
    
    public void resetExample() {
       // System.out.println("resetExample get called");
 	   XYDataset dataset= createDataset1(true);	
		
	//   JFreeChart chart = createChart1(dataset);	
 	//   chartPanel1 = new ChartPanel(chart, false); 
 	   XYDataset dataset1 = createDataset1(true);
 		JFreeChart chart1 = createChart1(dataset1);	

 		BoxAndWhiskerCategoryDataset dataset2 = createDataset2(true);
 	    JFreeChart chart2 = createChart2(dataset2);	
 	   chartPanel1 = new ChartPanel(chart1, false); 
 		chartPanel1.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y*2/3));

 		chartPanel2 = new ChartPanel(chart2, false); 
 		chartPanel2.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/3));
 			
 		
	   this.setChart();

       hasExample = true;
   
   //  System.out.println("row_count="+row_count);
   //  System.out.println("raw+x="+raw_x[0]);
		convertor.Y2Table(raw_x, row_count);
		//convertor.dataset2Table(dataset);				
		JTable tempDataTable = convertor.getTable();

		resetTableRows(tempDataTable.getRowCount()+1);
		resetTableColumns(tempDataTable.getColumnCount());
				
     for(int i=0;i<tempDataTable.getColumnCount();i++) {
         columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
			//  System.out.println("updateExample tempDataTable["+i+"] = " +tempDataTable.getColumnName(i));
         }

		columnModel = dataTable.getColumnModel();
		dataTable.setTableHeader(new EditableHeader(columnModel));

     for(int i=0;i<tempDataTable.getRowCount();i++)
         for(int j=0;j<tempDataTable.getColumnCount();j++) {
             dataTable.setValueAt(tempDataTable.getValueAt(i,j),i,j);
		 }
   
         dataPanel.removeAll();
         dataPanel.add(new JScrollPane(dataTable));
		dataTable.setGridColor(Color.gray);
		dataTable.setShowGrid(true);
		dataTable.doLayout();
		// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
		try { 
			dataTable.setDragEnabled(true);  
		} catch (Exception e) {
		}

        dataPanel.validate();

		// do the mapping
        setMapping();
		updateStatus(url);
}
    
	protected void setMixPanel(){
		dataPanel2.removeAll();
		graphPanel2.removeAll();

		graphPanel2.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
		graphPanel2.setLayout(new BoxLayout(graphPanel2, BoxLayout.Y_AXIS));
		if (chartPanel1!=null)
			 graphPanel2.add(chartPanel1);
		if (chartPanel2!=null)
		    graphPanel2.add(chartPanel2);
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
	}
	
	 protected void setGraphPanel(){
 		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));
		graphPanel.add(chartPanel1);
		graphPanel.add(chartPanel2);
 }
}
