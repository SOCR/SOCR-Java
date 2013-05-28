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
import java.util.StringTokenizer;

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
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.IntervalXYDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperHistogramChart;
import edu.ucla.stat.SOCR.chart.data.SimpleIntervalXYDataset;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.util.FloatSlider;

/**
 * A simple demonstration application showing how to create a bar chart using
 * an {@link XYPlot}.
 */
public class PowerTransformHistogramChart extends SuperHistogramChart implements PropertyChangeListener, ActionListener{

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
		
        binSlider2 = new FloatSlider("Bin Size", 5, 1, 10);
        super.reset_BinSlider();
        binSlider2.setPreferredSize(new Dimension(CHART_SIZE_X/2+150,80));
        binSlider2.addObserver(this);
        binSlider2.setToolTipText("Slider for adjusting the bin size.");
		sliderPanel.add(this.binSlider2);
		
		mapDep=false;
		  
		super.init();
		indLabel.setText("Data"); // Y
		
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		JPanel toolBarContainer  = new JPanel();
		toolBarContainer.add(toolBar);
		JSplitPane	toolContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolBarContainer, new JScrollPane(sliderPanel));
		toolContainer.setContinuousLayout(true);
		toolContainer.setDividerLocation(0.6);
		this.getContentPane().add(toolContainer,BorderLayout.NORTH);
	}

	   protected void initGraphPanel(){
			//System.out.println("initGraphPanel called");
			graphPanel = new JPanel();
			
			graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

			JFreeChart chart = createEmptyChart(null);	//create a empty graph first
			chartPanel = new ChartPanel(chart, false); 
			chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y-100));
	         
			graphPanel.add(chartPanel);
			graphPanel.add(sliderPanel);
			graphPanel.validate();
		}
	   protected void setGraphPanel(){
			chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y-100));
		     graphPanel.add(chartPanel);
		  //   graphPanel.add(sliderPanel);
	}
	 
	 protected void setMixPanel(){
	
			dataPanel2.removeAll();
			graphPanel2.removeAll();

			chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
			graphPanel2.add(chartPanel);
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

	
	
	public void update(Observable arg0, Object arg1) {
			//	System.out.println("update called: power="+power+" slider="+this.powerSlider.getValue());
		boolean binChanged = false;
		if (this.power!=this.powerSlider.getFloatValue()){
			setPower(this.powerSlider.getFloatValue());	
			this.createDataset(isDemo);
			redoChart(binChanged);
			return;
		}
					
		if (this.bin_size!=this.binSlider2.getFloatValue()){
			set_binSize(binSlider2.getFloatValue());	
			binChanged = true;
			redoChart(binChanged);
			return;
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
		
		IntervalXYDataset dataset= createDataset(false);	
		
		redoChart(true);
	}
	
	protected void redoChart(boolean binChanged){
		cleanup_raw(raw_x, row_count);
		IntervalXYDataset ds = applyPowerTransform();
		setTable(ds, binChanged);
//		 do the mapping
		addButtonIndependent();//Y
		
		JFreeChart chart = createChart(ds);	
		chartPanel = new ChartPanel(chart, false); 
		setChart(binChanged);
	}
	
	  protected void setChart(){
		this.setChart(true);  
	  }
	
	protected void setChart(boolean binChanged){
			// update graph

			graphPanel.removeAll();
			graphPanel.add(chartPanel);
			graphPanel.validate();

			// don't get the GRAPH panel to the front unless the bin size is changed
			if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) {
				if (binChanged)	
						tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
			}
			else {
				graphPanel2.removeAll();
				chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
				graphPanel2.add(chartPanel);
				graphPanel2.validate();	
				summaryPanel.validate();
			}
	    }
	 
	 protected void setTable(IntervalXYDataset ds, boolean binChanged){
			
		 convertor.data2Table(raw_x, transformed_x,"Data", "Transformed Data", row_count);			
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
	      
	        // don't bring graph to the front if the bin Size is not changed
	        if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) {
	        	if (binChanged)
	        		tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
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
	
		
    protected  JFreeChart createChart(IntervalXYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYBarChart(
            chartTitle,
            domainLabel,
            false,
            rangeLabel,
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        // then customise it a little...
		// chart.addSubtitle(new TextTitle("Source: http://www.amnestyusa.org/abolish/listbyyear.do"));
	    chart.setBackgroundPaint(Color.white);
       
	    XYPlot plot = chart.getXYPlot();
	    plot.setRenderer(new ClusteredXYBarRenderer());
	    //plot.setRenderer(new XYBarRenderer());
        XYItemRenderer renderer = plot.getRenderer();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	     
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
    //    domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	     
       // setXSummary(dataset);  //X  is time
        renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
        return chart;
    }
    
  
    protected void reset_PowerSlider(){
		power = 1.0;
		powerSlider.setFloatValue(power);	
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
    
protected IntervalXYDataset applyPowerTransform() {
    do_histogram(raw_x, row_count);	
	SimpleIntervalXYDataset dataset =  new SimpleIntervalXYDataset(bin_count, x_start, x_end, y_freq);
     
	transformed_x = calculate_power(raw_x, row_count, this.power);
	normalized_x = normalize(raw_x, transformed_x, row_count);
	do_histogram(normalized_x, row_count);	
	dataset.add(bin_count, x_start, x_end, y_freq);
	 
	return dataset;
    }


    
    /**
     * Creates a sample dataset.
     */
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected IntervalXYDataset createDataset(boolean isDemo) {
        if (isDemo){
      
        	row_count = 20;
		
        	raw_x= new String[row_count];

        	raw_x[0]="97"; raw_x[1]="98";raw_x[2]="92";raw_x[3]="94";raw_x[4]="93";
        	raw_x[5]="106"; raw_x[6]="94";raw_x[7]="109";raw_x[8]="102";raw_x[9]="96";
	
        	raw_x[10]="103"; raw_x[11]="98";raw_x[12]="92";raw_x[13]="94";raw_x[14]="93";
        	raw_x[15]="106"; raw_x[16]="94";raw_x[17]="109";raw_x[18]="102";raw_x[19]="95";
        	

        	default_bin = (int)Math.sqrt(row_count);  
        	set_binSize(default_bin); 
        	do_histogram(raw_x, row_count);
        	reset_BinSlider();
        	reset_PowerSlider();
        	cleanup_raw(raw_x, row_count);
        	IntervalXYDataset dataset = applyPowerTransform();
            
        	return dataset;}
        else return super.createDataset(false);
        
    }
    public void resetExample() {

        reset_BinSlider();
       // reset_PowerSlider();
       
	    IntervalXYDataset dataset= createDataset(true);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart(true);//binsize changed

        hasExample = true;
      
		setTable(dataset, true); //binsize changed

		// do the mapping
		setMapping();
		updateStatus(url);
  }
    
 
    public void setDataTable (String input){
  	  hasExample= true;
  	  StringTokenizer lnTkns = new StringTokenizer(input,"#,");
  	  String line;
  	  int lineCt = lnTkns.countTokens();
  	  resetTableRows(lineCt);
  	  resetTableColumns(1);
  	  int r = 0;
  	  while(lnTkns.hasMoreTokens()) {
  		  line = lnTkns.nextToken();
  		  
  		  dataTable.setValueAt(line, r, 0);
                      
  		  r++;
       }
       
       // this will update the mapping panel     
       resetTableColumns(dataTable.getColumnCount());
   }

}
