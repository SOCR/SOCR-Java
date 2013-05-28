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
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.chart.SuperXYChart_QQ;
import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.util.FloatSlider;

/**
 * A simple demonstration application showing how to create a line chart using
 * data from an {@link XYDataset}.  
 * 
 */
public class PowerTransformQQNormalPlotChart extends SuperXYChart_QQ implements PropertyChangeListener, ActionListener,Observer  {

	private double power=1.0;
	protected double[] transformed_x, normalized_x;
	JPanel sliderPanel;
	FloatSlider  powerSlider;

	public void init(){

		sliderPanel = new JPanel();
         powerSlider = new FloatSlider("Power", 1.0, -10.0, 10.0);	
         powerSlider.setPreferredSize(new Dimension(CHART_SIZE_X/2+150,80));
         powerSlider.addObserver(this);
         powerSlider.setToolTipText("Slider for adjusting the value of power.");
         sliderPanel.add(this.powerSlider);
		
		super.init();
		depLabel.setText("Data"); // Y
			  
		 toolBar = new JToolBar();
		 createActionComponents(toolBar);
		 JPanel toolBarContainer  = new JPanel();
		 toolBarContainer.add(toolBar);

		 JSplitPane	toolContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolBarContainer, new JScrollPane(sliderPanel));
		 toolContainer.setContinuousLayout(true);
		 toolContainer.setDividerLocation(0.6);
		 this.getContentPane().add(toolContainer,BorderLayout.NORTH);
	}
	
	protected void createActionComponents(JToolBar toolBar){
		super.createActionComponents(toolBar);
		JButton button;
		
		/**************** wiki Tab ****************/
        Action linkAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
			
		    		try {
		    			parentApplet.getAppletContext().showDocument(
		                        new java.net.URL("http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_PowerTransformFamily_Graphs"), 
		                			"SOCR: Power Transform Graphing Activity");
		               } catch (MalformedURLException Exc) {
		            	   JOptionPane.showMessageDialog(null, Exc,
		           				"MalformedURL Error",JOptionPane.ERROR_MESSAGE);
		                        Exc.printStackTrace();
		               }
		    	
			}
		};
	
		button = toolBar.add(linkAction);
       	button.setText(" WIKI_Activity ");
       	button.setToolTipText("Press this Button to go to SOCR_POWER_Activity wiki page");  
	}
	
	   /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected XYDataset createDataset(boolean isDemo) {
        if (isDemo){

			row_count = 10;
		
			raw_y= new String[row_count];

			raw_y[0]="97"; raw_y[1]="98";raw_y[2]="92";raw_y[3]="94";raw_y[4]="93";
			raw_y[5]="106"; raw_y[6]="94";raw_y[7]="109";raw_y[8]="102";raw_y[9]="96";
		
			reset_PowerSlider();
			cleanup_raw(raw_y, row_count);
			XYDataset  dataset = applyPowerTransform(raw_y, row_count);
			return dataset;}
		else return super.createDataset(false);
        
    }
    
	public void update(Observable arg0, Object arg1) {
			if (this.power!=this.powerSlider.getFloatValue()){
				setPower(this.powerSlider.getFloatValue());
				this.createDataset(isDemo); // update row_count raw_y in case the dataTable is changed
				redoChart();
			}
		}
	
	public void doChart(){

		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			showMessageDialog(DATA_MISSING_MESSAGE);
			resetChart();
			return;
		}
		
		dataset= createDataset(false);	
		
		redoChart();
	}
	
	protected void redoChart(){
		//System.out.println("row_count "+row_count);
		cleanup_raw(raw_y, row_count);
		XYDataset ds = applyPowerTransform(raw_y, row_count);
		setTable(ds);
//		 do the mapping
		addButtonDependent();//Y
		
		JFreeChart chart = createChart(ds);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();
	}
	
	 protected void setChart(){
			// update graph

			graphPanel.removeAll();
			graphPanel.add(chartPanel);
			graphPanel.validate();

			// don't get the GRAPH panel to the front
			if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) {
				//tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
			}
			else {
				graphPanel2.removeAll();
				chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
				graphPanel2.add(chartPanel);
				graphPanel2.validate();	
				summaryPanel.validate();
			}
	    }
	 
	 protected void setTable(XYDataset ds){
			
		   convertor.data2Table(raw_y, transformed_x, "Data", "Transformed Data", row_count);
			//convertor.dataset2Table(dataset);				
			JTable tempDataTable = convertor.getTable();

			resetTableRows(tempDataTable.getRowCount());
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
	       JScrollPane dt = new JScrollPane(dataTable);
	       dataPanel.add(dt);
	       dt.setRowHeaderView(headerTable);
	       dataTable.setGridColor(Color.gray);
	       dataTable.setShowGrid(true);
	       dataTable.doLayout();
			// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
			try { 
				dataTable.setDragEnabled(true);  
			} catch (Exception e) {
			}

	        dataPanel.validate();
	      
	        // don't bring graph to the front
	        if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) {
			//	tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
	        }
	        else{
	        	dataPanel2.removeAll();
	        	dataPanel2.add(new JLabel(" "));
	    		dataPanel2.add(new JLabel("Data"));
	    		JScrollPane dt2 = new JScrollPane(dataTable);
	    		dt2.setPreferredSize(new Dimension(CHART_SIZE_X/3, CHART_SIZE_Y*3/8));
	    		dt2.setRowHeaderView(headerTable);
	    		dataPanel2.add(dt2);
	    		JScrollPane st = new JScrollPane(summaryPanel);
	    		st.setPreferredSize(new Dimension(CHART_SIZE_X/3,CHART_SIZE_Y/6));
	    		dataPanel2.add(st);
	    		st.validate();

	    		dataPanel2.add(new JLabel(" "));
	    		dataPanel2.add(new JLabel("Mapping"));
	    		mapPanel.setPreferredSize(new Dimension(CHART_SIZE_X/3, CHART_SIZE_Y/2));
	    		dataPanel2.add(mapPanel);

	    		dataPanel2.validate();
	        }
		}
	 
  
    
    protected XYDataset applyPowerTransform(String[] raw_y, int row_count){
    	
    	//System.out.println("row_count="+row_count);
    	XYSeriesCollection dataset = new XYSeriesCollection();

		transformed_x = calculate_power(raw_y, row_count, this.power);
		normalized_x = normalize(raw_y, transformed_x, row_count);
    	do_normalQQ(raw_y, row_count);

		XYSeries series1 = new XYSeries("QQ");
		for (int i=0; i<row_count; i++){
			//System.out.println("i="+i+" normalQ="+normalQuantiles[i]+" stdRes="+stdResiduals[i]);
			series1.add(normalQuantiles[i], stdResiduals[i]);}

		XYSeries series2 = new XYSeries("Reference Line");
		min_x = Math.min(normalQuantiles[0], stdResiduals[0]);
		min_x = min_x-0.125;
		max_x = Math.max (normalQuantiles[row_count-1],stdResiduals[row_count-1]);
		max_x = max_x+0.125;
		series2.add(min_x,min_x);
		series2.add(max_x,max_x);

		
		dataset.addSeries(series1);
		dataset.addSeries(series2);

		transformed_x = calculate_power(raw_y, row_count, this.power);
		normalized_x = normalize(raw_y, transformed_x,  row_count);   
		
		do_normalQQ(normalized_x, row_count);
		
		XYSeries series3 = new XYSeries("Transformed Data");
		for (int i=0; i<row_count; i++){
			//System.out.println("i="+i+" normalQ="+normalQuantiles[i]+" stdRes="+stdResiduals[i]);
			series3.add(normalQuantiles[i], stdResiduals[i]);}
        	
    	dataset.addSeries(series3);
    	return dataset;
    }
   

	protected void reset_PowerSlider(){
		power = 1.0;
		powerSlider.setFloatValue(power);	
}
 
	/**
	* This method sets the Power parameter. 
	* @param p the change event
	*/
	public void setPower(double p){
		power = p;
	}

	/**
	* This method gets the current Power parameter. 
	*/
	public double getPower(){
		return power;
	}
	
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    protected JFreeChart createChart(XYDataset dataset) {
        
        // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle, //"Power Tranfomed Normal Q-Q plot",      // chart title
            domainLabel,                      // x axis label
            rangeLabel,                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
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
		// renderer.setShapesVisible(true);
		renderer.setBaseShapesFilled(true);
		//  renderer.setLinesVisible(false);
		renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesLinesVisible(2, true);
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(0, true);
       
		//renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0);
        rangeAxis.setLowerMargin(0);

		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(false);
        domainAxis.setUpperMargin(0);
        domainAxis.setLowerMargin(0);

		// domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // OPTIONAL CUSTOMISATION COMPLETED.
		//setQQSummary(dataset);    // very confusing   
        return chart;
        
    }

    /**
     * reset dataTable to default (demo data), and refesh chart
     */
   public void resetExample() {

	    XYDataset dataset= createDataset(true);

	    reset_PowerSlider();
	 	   
		JFreeChart chart = createChart(dataset);
		chartPanel = new ChartPanel(chart, false);
		setChart();

        hasExample = true;
        setTable(dataset);

		// do the mapping
		setMapping();
		updateStatus(url);
  }
}
