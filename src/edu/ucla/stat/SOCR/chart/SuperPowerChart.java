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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
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

import edu.ucla.stat.SOCR.gui.SOCROptionPane;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
  * A simple demonstration application showing how to create a bar chart.
 */
public class SuperPowerChart extends Chart implements PropertyChangeListener,ActionListener,  Observer  {

	protected String[] raw_x;
	protected double[] transformed_x;
	protected double power;
	protected int row_count;
	JPanel sliderPanel;
	edu.ucla.stat.SOCR.util.FloatSlider  powerSlider;
	protected boolean sliderSetted = false;
	
	
	protected void createActionComponents(JToolBar toolBar){
		JButton button = null;
       	
		toolBar.setFloatable(false);

		/**************** Demo Tab****************/
		if (useStaticExample) {
			exampleStaticAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
			// Create First Example
				reset();
				resetTable();
				resetMappingList();
				resetExample();
				if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==ALL) 
					setMixPanel();

				updateStatus(url);
				validate();
			}
				
		};
		button = toolBar.add(exampleStaticAction);
		button.setText(EXAMPLE);
		button.setToolTipText(chartDescription);
		}
		
		/**************** DO-CHART Tab ****************/
		computeAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				doChart();
			}
		};
	
		button = toolBar.add(computeAction);
		button.setText(DOCHART);
       	button.setToolTipText("Press this Button to Generate the Chart");
		
 		/**************** CLEAR Tab****************/
      	clearAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {

			/* somehow reset has to be called more than once to set to the correct header. I'll leave it like this before I figure out why. annie che 20051123 -_- */
				reset();	// Need to work out what this means
				//	reset();

				resetTable();
				resetMappingList();
				resetChart();
				ChartExampleData exampleNull = new ChartExampleData(0, 0);
				/* A Null Example (with no data) is used here
				to reset the table so that when "CLEAR" button is pressed, the cells of dataTable is NOT null. 
				annieche 20060110. */
				updateExample(exampleNull);
				if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==ALL) 
					setMixPanel();

				updateStatus("The Chart has been reset!");
				//updateExample(exampleNull);
			}
				
		};

       	button = toolBar.add(clearAction);
       	button.setText(CLEAR);
       	button.setToolTipText("Clears All Windows");
       	
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
		//button.setMinimumSize(new Dimension(110, 20));
       	button.setText(" WIKI_Activity ");
       	button.setToolTipText("Press this Button to go to SOCR_POWER_Activity wiki page");      
         
       	/**************** TEST Tab ****************/
		testAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				doTest();
			}
		};
		if (TEST_API){
			button = toolBar.add(testAction);
			button.setText(TEST);
			button.setToolTipText("Press this Button to test the API");	
		}
     
		       
 }
	public void init(){

		indLabel = new JLabel("X");
		depLabel = new JLabel("Y");

		super.init();
		
		sliderPanel = new JPanel();
		powerSlider = new edu.ucla.stat.SOCR.util.FloatSlider("Power", 1.0, -10.0, 10.0);	
         powerSlider.setPreferredSize(new Dimension(CHART_SIZE_X+150,80));
         powerSlider.addObserver(this);
         powerSlider.setToolTipText("Slider for adjusting the value of power.");
         sliderPanel.add(this.powerSlider);
  		/* toolBarPanel.add(sliderPanel);
		
		 toolBarPanel = new JPanel();
		 JScrollPane toolBarContainer = new JScrollPane(toolBarPanel);		
		 toolBarPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));*/
			// Create the toolBar
		
		 toolBar = new JToolBar();
		 createActionComponents(toolBar);
		 JPanel toolBarContainer  = new JPanel();
		 toolBarContainer.add(toolBar);
		 //toolBarPanel.add(toolBar);
		 JSplitPane	toolContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolBarContainer, new JScrollPane(sliderPanel));
		 toolContainer.setContinuousLayout(true);
		 toolContainer.setDividerLocation(0.6);
		 this.getContentPane().add(toolContainer,BorderLayout.NORTH);
		
			
		depMax = 50; // max number of dependent var
		indMax = 50; // max number of independent var

		updateStatus(url);	
	  
		resetExample();
		validate();
		
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
	 *  create chart using data from the dataTable
	 */
	public void doChart(){
		
		//set_slider();// need to recaculate the slider range
		
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			SOCROptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			resetChart();
			return;
		}

		isDemo = false;
		XYDataset dataset = createDataset(isDemo );	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, isDemo ); 
		setChart();
	}
	

	/**
	 *  sample code for generating chart using ChartGenerator_JTable
	 *  
	 *   not done yet
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SuperIndexChart doTest get called!");
		
		 int no_series = dataTable.getColumnCount();	//2 y column only 	
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 0;    // there is no x column
			 pairs[i][1] = 0;    //column y
		 }
		 chart = chartMaker.getXYChart("Power","Power Transform Chart", "Row", "Data", dataTable, no_series, pairs,"noshape");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
/*	public void set_slider(){
		powerSlider = new ValueSlider("Power", -10, 10, 1, true); //set minimumRange10 = true
		powerSlider.setPreferredSize(new Dimension(CHART_SIZE_X-10, 100));
		powerSlider.addObserver(this);
		powerSlider.setValue(power);
		sliderSetted = true;
	}*/
	
	protected void reset_slider(){
		sliderSetted = false;
		power = 1.0;
		powerSlider.setFloatValue(power);	
}
	
	protected void initGraphPanel(){
		//System.out.println("initGraphPanel called");
		graphPanel = new JPanel();
		
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

		JFreeChart chart = createEmptyChart(null);	//create a empty graph first
		chartPanel = new ChartPanel(chart, false); 
		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y-100));
		
	/*	sliderPanel = new JPanel();
    
		// ValueSetter(String title, int type int min, int max, int initial,boolean minimumRange10) 
         powerSlider = new ValueSlider("Power", -10, 10, 1,true);
	
         reset_slider();// the range will need to be reset
         powerSlider.setPreferredSize(new Dimension(CHART_SIZE_X-10,100));
         powerSlider.addObserver(this);
         sliderPanel.add(this.powerSlider);
         sliderPanel.setPreferredSize(new Dimension(CHART_SIZE_X-10,100));*/
         
		graphPanel.add(chartPanel);
	//	graphPanel.add(sliderPanel);
		//System.out.println("added SliderPanel");
		graphPanel.validate();
	}
    
	protected void initMixPanel(){
		dataPanel2 = new JPanel();
		dataPanel2.setLayout(new BoxLayout(dataPanel2, BoxLayout.Y_AXIS));

		graphPanel2 = new JPanel();
		graphPanel2.setLayout(new BoxLayout(graphPanel2, BoxLayout.Y_AXIS));

		mixPanel = new JPanel(new BorderLayout()); 
		//		resetChart();

		setMixPanel();
	}
	   /**
	 * make the show_all panel
	 */
	protected void setMixPanel(){
		/*super.setMixPanel();
		return;*/
		
		dataPanel2.removeAll();
		graphPanel2.removeAll();

		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
		graphPanel2.add(chartPanel);
	//	sliderPanel.removeAll();
	/*	sliderPanel.add(new JLabel("Bin Size"));
        sliderPanel.add(this.binSlider);
        sliderPanel.add(new JLabel(":"+this.binSlider.getValue()));*/
	/*	powerSlider.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,100));
        sliderPanel.add(this.powerSlider);
    	sliderPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,100));*/
	//	graphPanel2.add(sliderPanel); // the slider
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
			chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y-100));
		     graphPanel.add(chartPanel);
		   //  graphPanel.add(sliderPanel);
	}
	
	 public void setChart(){
			// update graph
			//System.out.println("setChart called");

			graphPanel.removeAll();
			//chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));
		//	sliderPanel.removeAll();
			/*sliderPanel.add(new JLabel("Bin Size"));
			sliderPanel.add(this.binSlider);
			sliderPanel.add(new JLabel(":"+this.binSlider.getValue()));*/
		/*	powerSlider.setPreferredSize(new Dimension(CHART_SIZE_X,100));
			sliderPanel.add(this.powerSlider);*/
			
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
				//powerSlider.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,100));
				//sliderPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,100));
				graphPanel2.add(chartPanel);
				//graphPanel2.add(sliderPanel);
				graphPanel2.validate();	
				summaryPanel.validate();
			}
	    }

	 protected void setTable(XYDataset ds){
			
			convertor.Power2Table(ds);
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
	      
	        // don't bring graph to the front
	        if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) {
			//	tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
	        }
	        else{
	        	dataPanel2.removeAll();
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
	        }
		}
	 /**
	   * 
	   * @param isDemo data come from demo(true) or dataTable(false)
	   * @return
	   */   
    protected  XYDataset createDataset(boolean isDemo) {
		if (isDemo)	{
				updateStatus("isDemo==true in "+this.getClass().getName()+" class! return null Dataset, check the code!");
			return null; }
		else{
			
			//System.out.println("createDatase false get callled");
			setArrayFromTable();
			
			double[] raw_xvalue;
			
			row_count = xyLength;
			//System.out.println("row_count="+row_count);
			raw_x= new String[row_count];
			raw_xvalue = new double[row_count];

			row_count = 0;
			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
					raw_x[i] = indepValues[i][index];
					//System.out.println("raw_x="+raw_x[i]);
					try{
						
						raw_xvalue[row_count]= Double.parseDouble(raw_x[i]);
						row_count ++;
					}catch(Exception e)
					{
					 System.out.println("wrong data " +raw_x[i]);
					}
				}
		   
			double[] y_freq= new double[row_count];
			
			for (int i =0; i<row_count; i++){				
				y_freq[i]=i+1;
			}
			
			XYSeriesCollection dataset = new XYSeriesCollection();
			XYSeries series1 = new XYSeries("X");
			for (int i=0; i<row_count;i++)
				  series1.add(y_freq[i], raw_xvalue[i]);
			dataset.addSeries(series1);
		
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
    protected JFreeChart createChart(XYDataset dataset) {
         // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle,      // chart title
            "Y",                      // x axis label
            "X",                      // y axis label
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
        plot.setBackgroundPaint(Color.lightGray);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        XYLineAndShapeRenderer renderer 
            = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);

        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
  
	}	
    

  
   public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();

		System.err.println("From RegCorrAnal:: propertyName =" +propertyName +"!!!");

		if(propertyName.equals("DataUpdate")) {
			//update the local version of the dataTable by outside source
			dataTable = (JTable)(e.getNewValue());
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));
			dataTable.doLayout();

			System.err.println("From RegCorrAnal:: data UPDATED!!!");
		}
		}
	
	public Container getDisplayPane() {
		return this.getContentPane();
	}
   
	/*public String getLocalAbout() {
		String about ="\n \t A scatterplot or scatter graph is a graph used in statistics to visually display and\n compare two or more sets of related quantitative, or numerical, data by displaying only finitely\n many points, each having a coordinate on a horizontal and a vertical axis.\n";

		return super.getLocalAbout()+about;
		}*/
	  /**
     * reset dataTable to default (demo data), and refesh chart
     */
  
	public void update(Observable arg0, Object arg1) {
	//	System.out.println("update called: power="+power+" slider="+this.powerSlider.getValue());
		if (this.power!=this.powerSlider.getFloatValue()){
			setPower(this.powerSlider.getFloatValue());
			redoChart();
		}
	}
		/**
	     * Applies the Power transform to the data.
	     * @param dataset  the data to be transformed.
	     * @return transformed dataset.
	     */
	protected XYDataset applyPowerTransform(String[] raw_x, int row_count) {
	    	XYSeries series1 = new XYSeries("Data");
	    
	    	for (int i=0; i<row_count; i++)
	    		series1.add(i+1,Double.parseDouble(raw_x[i]));
	    
	    	XYSeries outputData = new XYSeries("Transformed Data");
	    	
	        for (int i=0; i< row_count; i++) {
	        	
	        	if (power==0) 
	        		{outputData.add(i+1, Math.log(Math.abs(Double.parseDouble(raw_x[i]))));
	        	//	System.out.println("adding ="+Math.log(Math.abs(Double.parseDouble(raw_x[i]))));
	        		}
	        	else {
	        		outputData.add(i+1, (Math.pow(Math.abs(Double.parseDouble(raw_x[i])), power)-1)/power);
	        		//System.out.println("adding ="+(Math.pow(Math.abs(Double.parseDouble(raw_x[i])), power)-1)/power);
	        	}
	        }      
	        
	        XYSeriesCollection dataset = new XYSeriesCollection();
	        dataset.addSeries(series1);    
	        dataset.addSeries(outputData);    
	        return dataset;
	    }
	    
	protected void redoChart(){
			XYDataset ds = applyPowerTransform(raw_x, row_count);
			setTable(ds);
			JFreeChart chart = createChart(ds);	
			chartPanel = new ChartPanel(chart, false); 
			setChart();
		}
	
	
}
