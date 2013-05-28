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
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.chart.SuperXYChart;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.util.FloatSlider;

/**
 * A simple demonstration application showing how to create a line chart using
 * data from an {@link XYDataset}.  
 * 
 */
public class PowerTransformXYScatterChart extends SuperXYChart implements PropertyChangeListener, ActionListener, Observer  {

	protected int row_count;
	protected String[] raw_x, raw_y;
	private double powerX=1.0, powerY=1.0;
	protected double[] transformed_x, transformed_y;
	protected double[] norm_x, norm_y;
	JPanel sliderPanel;
	FloatSlider powerXSlider, powerYSlider;

	
	public void init(){

		sliderPanel = new JPanel();
	
         powerXSlider = new FloatSlider("PowerX", 1.0, -10.0, 10.0);	
         powerXSlider.setPreferredSize(new Dimension(CHART_SIZE_X/2+150,80));
         powerXSlider.addObserver(this);
         powerXSlider.setToolTipText("Slider for adjusting the value of power for the axis.");
         sliderPanel.add(this.powerXSlider);
		
         powerYSlider = new FloatSlider("PowerY", 1.0, -10.0, 10.0);	
         powerYSlider.setPreferredSize(new Dimension(CHART_SIZE_X/2+150,80));
         powerYSlider.addObserver(this);
         powerYSlider.setToolTipText("Slider for adjusting the value of power for the Y axis.");
         sliderPanel.add(this.powerYSlider);
		
         super.init();
     
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
	
	
	
	public void update(Observable arg0, Object arg1) {
			//System.out.println("update called: ");
			if (this.powerX!=this.powerXSlider.getFloatValue() || this.powerY!=this.powerYSlider.getFloatValue()){
				setPowerX(this.powerXSlider.getFloatValue());
				setPowerY(this.powerYSlider.getFloatValue());
				this.createDataset(isDemo);
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
		if (dependentIndex < 0 || independentIndex < 0 || independentLength == 0) {
			showMessageDialog(VARIABLE_MISSING_MESSAGE);
			resetChart();
			return;
			}
		
		dataset = createDataset(false);	
		redoChart();
	}
	
	protected void redoChart(){
		XYDataset ds = applyPowerTransform();
		setTable(ds);
//		 do the mapping		
        addButtonIndependent();
        addButtonDependent();
        
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
			
		   convertor.Y2Table(raw_x, raw_y, transformed_x, transformed_y, row_count);			
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
	
     /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected XYDataset createDataset(boolean isDemo) {
        if (isDemo){
        	row_count = 8;
        	
        	raw_x = new String[row_count];
        	raw_y = new String[row_count];
        	transformed_x = new double[row_count];
        	transformed_y = new double[row_count];
		
        	raw_x[0]= "1.0"; raw_x[1]= "2.0"; raw_x[2]= "3.0"; raw_x[3]= "4.0"; raw_x[4]= "5.0"; raw_x[5]= "6.0"; raw_x[6]= "7.0"; raw_x[7]= "8.0"; 
        	raw_y[0]= "1.0"; raw_y[1]= "4.0"; raw_y[2]= "3.0"; raw_y[3]= "5.0"; raw_y[4]= "5.0"; raw_y[5]= "7.0"; raw_y[6]= "7.0"; raw_y[7]= "8.0"; 
			reset_slider();
			pairup_raw(raw_x, raw_y, row_count);
			XYDataset  dataset = applyPowerTransform();       
     
        return dataset;}		//else return super.createDataset(false);
        else {
        	
        	//only take first 2 columns
			if(independentVarLength == 0 ||dependentVarLength==0){
				resetMappingList();
				setMapping();
				independentVarLength = 1;
				dependentVarLength = 1;
				
			}
			
        	setArrayFromTable();
        	
			row_count = xyLength;
			
			raw_x= new String[row_count];
			raw_y= new String[row_count];
			
			
			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
					raw_x[i] = indepValues[i][index];
				}

			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++){
					raw_y[i] = depValues[i][index];
				}
			
			pairup_raw(raw_x,raw_y, row_count);
			
			if (independentVarLength != dependentVarLength){
			showMessageDialog("The number of X and Y doesn't match!");
			resetChart();
			return null;
		}
/*
			String[][] x= new String[xyLength][independentVarLength];
			double[][] y= new double[xyLength][dependentVarLength];


			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					x[i][index] = indepValues[i][index];
		   
			try{
				for (int index=0; index<dependentVarLength; index++)
					for (int i = 0; i < xyLength; i++) 
						if (skipy[i]!=1) 
							y[i][index] = Double.parseDouble(depValues[i][index]);
			}catch(NumberFormatException e)
				{
					showMessageDialog("Data format error! Y");
					return null;}
*/
			// create the dataset... 
			XYSeriesCollection dataset = new XYSeriesCollection(); 
			XYSeries series;	

			//dependent 
			try{
			for (int i=0; i<independentVarLength; i++){
				
				String serieName;
				if (independentHeaders[i].lastIndexOf(":")!=-1)
					serieName= independentHeaders[i].substring(0, independentHeaders[i].lastIndexOf(":"));
				else serieName = "serie";
				
				series = new XYSeries(serieName);
				for (int j=0; j<row_count; j++){
					if (raw_x[j]!=null && raw_y[j]!=null)
						series.add(Double.parseDouble(raw_x[j]), Double.parseDouble(raw_y[j]));
					//System.out.println("adding :("+x[j][i]+","+y[j][i]+","+independentHeaders[i]+")" );
				}
				dataset.addSeries(series);
			}}catch(NumberFormatException e)
				{
					showMessageDialog("Data format error! ");
					return null;}
		
            return dataset; 
			
        }
        
    }
    
    protected void reset_slider(){
		powerX = 1.0;
		powerXSlider.setFloatValue(powerX);	
		powerY = 1.0;
		powerYSlider.setFloatValue(powerY);	
}
    
	/**
	* This method sets the PowerX parameter. 
	* @param p the change event
	*/
	protected void setPowerX(double pX){
		powerX = pX;
	}
	/**
	* This method gets the current PowerX parameter. 
	*/
	protected double getPowerX(){
		return powerX;
	}

	/**
	* This method sets the PowerY parameter. 
	* @param p the change event
	*/
	protected void setPowerY(double pY){
		powerY = pY;
	}
	/**
	* This method gets the current PowerY parameter. 
	*/
	protected double getPowerY(){
		return powerY;
	}

	private void pairup_raw(String[] in_x, String[] in_y, int in_count){
		String[] x = new String[in_count];
		String[] y = new String[in_count];
		int count=0;
		
		//System.out.println("applyPowerTransform row_count=" +row_count);
		for (int i=0; i<in_count; i++){
			if (in_x[i]!=null && in_x[i].length()!=0 && in_y[i]!=null && in_y[i].length()!=0){
				x[count]= in_x[i];
				y[count]= in_y[i];
				count++;
			}
		}
		
		raw_x= new String[count];
		raw_y= new String[count];
		row_count= count;
		
		for (int i=0; i<count; i++){
			raw_x[i]= x[i]; raw_y[i]=y[i];
		}
	}
	
	protected XYDataset applyPowerTransform() {
		//System.out.println("applyPowerTransform count="+count);
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("Data");
		
		for (int i=0; i<row_count; i++){
			//System.out.println("i="+i+" normalQ="+normalQuantiles[i]+" stdRes="+stdResiduals[i]);	
			series1.add(Double.parseDouble(raw_x[i]), Double.parseDouble(raw_y[i]));}
	
		dataset.addSeries(series1);
		transformed_x = calculate_power(raw_x, row_count, powerX);
		transformed_y = calculate_power(raw_y, row_count, powerY);
		norm_x= normalize(raw_x, transformed_x, row_count);  
		norm_y= normalize(raw_y, transformed_y, row_count);   
		
		XYSeries series2 = new XYSeries("Transformed Data");
		for (int i=0; i<row_count; i++){
			//System.out.println("i="+i+" normalQ="+normalQuantiles[i]+" stdRes="+stdResiduals[i]);
			series2.add(norm_x[i], norm_y[i]);}
	    	
		dataset.addSeries(series2);
		return dataset;
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
            chartTitle, //"Power Transformed XYScatter Chart",      // chart title
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
        plot.setBackgroundPaint(Color.lightGray);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        XYLineAndShapeRenderer renderer 
            = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
       // renderer.setLinesVisible(false);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(0, true);
		renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRangeIncludesZero(false);
		rangeAxis.setUpperMargin(0);
        rangeAxis.setLowerMargin(0);

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setAutoRangeIncludesZero(false);
        domainAxis.setUpperMargin(0);
        domainAxis.setLowerMargin(0);

        // OPTIONAL CUSTOMISATION COMPLETED.
		setXSummary(dataset);     
        return chart;
        
    }
    
 
    public void resetExample() {

    	reset_slider();
	    XYDataset dataset= createDataset(true);	
			    
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();

        hasExample = true;
		//		convertor.dataset2Table((TimeSeriesCollection)dataset);				
		//convertor.dataset2Table(dataset);	
      
        setTable(dataset);

		// do the mapping			
        setMapping();
        
		updateStatus(url);
  }

    public void setMapping(){	
    	addButtonIndependent();
    	addButtonDependent();			
	  }
}
