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
import java.awt.FlowLayout;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.uah.math.devices.Coin;
import edu.uah.math.devices.Parameter;
import edu.uah.math.distributions.BinomialDistribution;
import edu.uah.math.distributions.LocationScaleDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperIndexChart;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategoryCellLabelGenerator;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
import edu.ucla.stat.SOCR.gui.SOCROptionPane;
import edu.ucla.stat.SOCR.util.EditableHeader;
import javax.swing.event.*;
import java.awt.event.*;

/**
 * A simple demonstration application showing how to create a line chart using
 * data from an {@link XYDataset}.  
 * 
 */
public class  PowerTransformationFamilyChart extends SuperIndexChart implements PropertyChangeListener, ActionListener,   Observer {
	protected double[] transformed_x;
	protected double power;
	protected int row_count;
	JPanel sliderPanel;
	edu.ucla.stat.SOCR.util.FloatSlider  powerSlider;
	
	String dataHeader= "Data";
	
	protected void createActionComponents(JToolBar toolBar){
		super.createActionComponents(toolBar);
		JButton button;
		
		/**************** wiki Tab ****************/
        Action linkAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
			
		    		try {
		    			//popInfo("SOCRChart: About", new java.net.URL("http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_PowerTransformFamily_Graphs"), "SOCR: Power Transform Graphing Activity");
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
	
	public void init(){
		
		sliderPanel = new JPanel();
	    
		// ValueSetter(String title, int type int min, int max, int initial,boolean minimumRange10) 
         powerSlider = new edu.ucla.stat.SOCR.util.FloatSlider("Power", 1.0, -10.0, 10.0);	
         powerSlider.setPreferredSize(new Dimension(CHART_SIZE_X/2+150,80));
         powerSlider.addObserver(this);
         powerSlider.setToolTipText("Slider for adjusting the value of power.");
         sliderPanel.add(this.powerSlider);
      
         mapDep=false;
         LEGEND_SWITCH= false;
         
		super.init();

		indLabel = new JLabel("X");
		depLabel = new JLabel("Y");
		
		 toolBar = new JToolBar();
		 createActionComponents(toolBar);
		 JPanel toolBarContainer  = new JPanel();
		 toolBarContainer.add(toolBar);
		 JSplitPane	toolContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolBarContainer, new JScrollPane(sliderPanel));
		 toolContainer.setContinuousLayout(true);
		 toolContainer.setDividerLocation(0.6);
		 this.getContentPane().add(toolContainer,BorderLayout.NORTH);
		
			
		depMax = 10; // max number of dependent var
		indMax = 10; // max number of independent var
		
	}
     /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected XYDataset createDataset(boolean isDemo) {
        if (isDemo){
        
        	row_count = 10;
        	raw_x = new String[row_count];   
        	
        	raw_x[0] = "1.0"; 	raw_x[1] = "2.0"; 	raw_x[2] = "3.0"; 	raw_x[3] = "4.0"; 	raw_x[4] = "5.0";
        	raw_x[5] = "6.0"; 	raw_x[6] = "76.0"; 	raw_x[7] = "78.0"; 	raw_x[8] = "7.0"; 	raw_x[9] = "8.0";
    
        	   
        	reset_PowerSlider();
        	cleanup_raw(raw_x, row_count);
        	XYDataset dataset = applyPowerTransform();
                
        	return dataset;}
        else{

			if (independentVarLength ==0){
				setMapping();
				independentVarLength =1;		
			}
		//	System.out.println("createDatase false get callled");
			setArrayFromTable();
			
			double[] raw_xvalue;
			
			row_count = xyLength;
			//System.out.println("1row_count="+row_count + " independentVarLength="+independentVarLength);
			String[] raw_x_tmp= new String[row_count];
			raw_xvalue = new double[row_count];

			
			dataHeader = independentHeaders[0];
			row_count = 0;
			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
					
					
					try{
						if (indepValues[i][index].length()!=0){
							raw_x_tmp[row_count] = indepValues[i][index];
							raw_xvalue[row_count]= Double.parseDouble(raw_x_tmp[row_count]);
							
							row_count ++;
						}
						
					}catch(Exception e)
					{
					 System.out.println("wrong data " +raw_x[i]);
					}
				}
		   
		
			double[] y_freq= new double[row_count];
			
			raw_x = new String[row_count];
			for (int i =0; i<row_count; i++){				
				y_freq[i]=i+1;
				raw_x[i]= raw_x_tmp[i];
				//System.out.println("raw_x="+raw_x[i]);
			}
			
			XYSeriesCollection dataset = new XYSeriesCollection();
			XYSeries series1 = new XYSeries("X");
			for (int i=0; i<row_count;i++)
				  series1.add(y_freq[i], raw_xvalue[i]);
			dataset.addSeries(series1);
			
		
			//System.out.println("2row_count="+row_count);
          return dataset; 
		}
        
    }
		
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    protected JFreeChart createChart(XYDataset dataset) {
        chartTitle = "Power Transform Chart";
      //   create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle,//"Power Transform Chart",      // chart title
            domainLabel,                      // x axis label 
            rangeLabel,                      // y axis label  
            dataset,                  // data
            PlotOrientation.VERTICAL,
            !legendPanelOn,                     // include legend
            true,                     // tooltips 
            false                     // urls
        );
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
    	
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
        renderer.setBaseShapesVisible(false);
        renderer.setBaseShapesFilled(false);
        renderer.setBaseLinesVisible(true);
       
		renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

        //change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.05);
        rangeAxis.setLowerMargin(0.05);
        

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setAutoRangeIncludesZero(false);     
      //  domainAxis.setTickLabelsVisible(false);
       // domainAxis.setTickMarksVisible(false);      
        domainAxis.setUpperMargin(0.05);
        domainAxis.setLowerMargin(0.05);

        
        // OPTIONAL CUSTOMISATION COMPLETED.
	    setYSummary(dataset);     
	 
	    return chart;
    }
 
    
    public void resetExample() {
      //  System.out.println("resetExample get called");
 	   XYDataset dataset= createDataset(true);	
		
 	   setPower(1.0);
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
    
    public void setChart(){
		// update graph

		graphPanel.removeAll();
	
		graphPanel.add(chartPanel);
	//	graphPanel.add(sliderPanel);
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
		
    	//System.out.println("setTable get Called");
		convertor.Power2Table(ds);
		//convertor.dataset2Table(dataset);				
		JTable tempDataTable = convertor.getTable();

		resetTableRows(tempDataTable.getRowCount());
		resetTableColumns(tempDataTable.getColumnCount());
		
		//System.out.println(tempDataTable.getRowCount());
		
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
	
    public void update(Observable arg0, Object arg1) {
    	//	System.out.println("update called: power="+power+" slider="+this.powerSlider.getValue());
    		if (this.power!=this.powerSlider.getFloatValue()){
    			setPower(this.powerSlider.getFloatValue());
    			doChart();
    		}
    	}

    private void cleanup_raw(String[] in_x, int in_count){
		String[] x = new String[in_count];
		
		int count=0;
		
		//System.out.println("applyPowerTransform row_count=" +row_count);
		for (int i=0; i<in_count; i++){
			if (in_x[i]!=null && in_x[i].length()!=0 ){
				x[count]= in_x[i];
				count++;
			}
		}
		
		raw_x= new String[count];
		
		row_count= count;
		
		for (int i=0; i<count; i++){
			raw_x[i]= x[i]; 
		}
	}
    
	/**
     * Applies the Power transform to the data.
     * @param dataset  the data to be transformed.
     * @return transformed dataset.
     */
protected XYDataset applyPowerTransform() {
	//System.out.println("in applyPowerTransform row_count="+row_count);
	 XYSeriesCollection dataset = new XYSeriesCollection();
	 XYSeries series1 = new XYSeries(dataHeader);
    
    	for (int i=0; i<row_count; i++){
    		series1.add(i+1,Double.parseDouble(raw_x[i]));
    	//	System.out.println("Adding "+(i+1) + ":"+raw_x[i]);
    	}
    
    //	System.out.println("\n");
    	dataset.addSeries(series1); 
    	   
    	XYSeries outputData = new XYSeries("Transformed Data");
    	
    	transformed_x = calculate_power(raw_x, row_count, this. power);
    
        for (int i=0; i<row_count; i++) {      
        	outputData.add(i+1, transformed_x[i]);
        	//System.out.println("Adding "+(i+1) + ":"+transformed_x[i]);
        }      
              
        dataset.addSeries(outputData);    
        return dataset;
    }
    
public void doChart(){
	if(dataTable.isEditing())
		dataTable.getCellEditor().stopCellEditing();
	if (! hasExample ) {
		SOCROptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
		resetChart();
		return;
	}
		isDemo= false;
		XYDataset dataset = createDataset(isDemo);
		
		cleanup_raw(raw_x, row_count);
		XYDataset ds = applyPowerTransform();
		
		setTable(ds);
//		 do the mapping
		addButtonIndependent();//Y
		
		JFreeChart chart = createChart(ds);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();
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
}
