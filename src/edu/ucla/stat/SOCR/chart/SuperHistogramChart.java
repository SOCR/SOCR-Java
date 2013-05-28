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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import java.lang.NumberFormatException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.util.FloatSlider;
import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.util.ValueSlider;
import edu.ucla.stat.SOCR.chart.data.SimpleIntervalXYDataset;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
import edu.ucla.stat.SOCR.gui.SOCROptionPane;


/**
 * A simple demonstration application showing how to create a Bar chart using 
 * data from a {@link IntervalXYDataset}.
 */
public class SuperHistogramChart extends Chart implements PropertyChangeListener, ActionListener,  Observer {

	protected int row_count, bin_count;
	protected double bin_size;
	protected String[] raw_x;
	protected double[] x_start, x_end;
	protected int[] y_freq;
	//protected JSlider binSlider;
	protected double default_bin = 1;
	//protected int space_bin = 2;
	protected int min_bin=1, max_bin;
	JPanel sliderPanel;
	protected FloatSlider  binSlider2;
	protected boolean sliderSetted = false;
	//double  scale=1.0;

	double min_x=1000;
	double max_x=-1000;
	
	public void init(){

		LEGEND_SWITCH =false;
		indLabel = new JLabel("XValue");
	//	depLabel = new JLabel("Frequency");

		mapDep=false;
		
		super.init();
		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var

		updateStatus(url);	
	  
		resetExample();
		validate();
		
	}
	public void resetMappingList() {
		super.resetMappingList();
		reset_BinSlider();	
	}
	/**
	 * sample code for generating chart using ChartGenerator_JTable 
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 reset_BinSlider();// need to recaculate the slider range
		 showMessageDialog("SuperHistogramChart doTest get called!");
		
		 int no_series = dataTable.getColumnCount();	//one y column only 	 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 0;    // there is no x column
			 pairs[i][1] = 0;    //column y
		 }
		 chart = chartMaker.getXYChart("Histogram","Category Chart", "Value", "Frequency", dataTable, no_series, pairs, String.valueOf(bin_size));	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
	/**
	 *  create chart using data from the dataTable
	 */
	public void doChart(){
//System.out.println("doChart");
		reset_BinSlider();// need to recaculate the slider range
		
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			SOCROptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			resetChart();
			return;
		}

		isDemo = false;
		IntervalXYDataset dataset = createDataset(isDemo);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, isDemo); 
		setChart();
	}

	  
	protected void redoChart(){
		//System.out.println("redoChart");
		do_histogram(raw_x, row_count);
	//	System.out.println("bin_count="+bin_count);
		try{
			IntervalXYDataset dataset = new SimpleIntervalXYDataset(bin_count, x_start, x_end, y_freq);
			JFreeChart chart = createChart(dataset);	
			chartPanel = new ChartPanel(chart, false); 
			setChart();
		}catch(OutOfMemoryError e){
			
			SOCROptionPane.showMessageDialog(this, "out of memory, please increase bin_ size.");
			return;
		}
		
	}
	  /**
	   * 
	   * @param isDemo data come from demo(true) or dataTable(false)
	   * @return
	   */  
    protected IntervalXYDataset createDataset(boolean isDemo) {

		if(isDemo)	{
				updateStatus("isDemo==true in "+this.getClass().getName()+" class! return null Dataset, check the code!");
			return null; }
		else {
			if (independentVarLength==0){
				setMapping();
				independentVarLength=1;
			}
			setArrayFromTable();
			
			if (independentVarLength>0)
				domainLabel = independentHeaders[0];
			if (dependentVarLength>0)
				rangeLabel = dependentHeaders[0];
			
			rangeLabel= "Frequency";
			
			//System.out.println(domainLabel+" "+rangeLabel);
			
			row_count = xyLength;
			//System.out.println("row_count="+row_count);
			raw_x= new String[row_count];

			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
					raw_x[i] = indepValues[i][index];
				}

			do_histogram(raw_x, row_count);
//System.out.println("createDataset bin_size="+bin_size+ " bin_count="+bin_count);
			try{
				IntervalXYDataset dataset = new SimpleIntervalXYDataset(bin_count, x_start, x_end, y_freq);		
				return dataset; 
			}catch(OutOfMemoryError e){			
				SOCROptionPane.showMessageDialog(this, "out of memory, please increase bin_ size.");
				return null;
			}
		}
		
	} 
    
    protected void set_binSize(double size){
    	if (size==0.0)
    		this.bin_size = 0.1;
    	else 
    	   this.bin_size = size;
    	
    	binSlider2.setFloatValue(bin_size);	
    	updateStatus("Bin size is set to "+size);
    //	System.out.println("SuperHistogram: set binsize "+bin_size);
    }

    protected void do_histogram(double[] raw_x, int row_count){
   // 	System.out.println("doHistogram");
     //	System.out.println("row_count="+row_count);
    	double[] tmp_xvalue = new double[row_count];
    	int data_count = 0;
     	
	     	//System.out.println("row_count="+row_count);
	     	
	     	
	     		for (int i=0; i<row_count; i++){
	     		//	System.out.println("parsing:"+raw_x[i]);
	     				try{
	     					tmp_xvalue[data_count]= raw_x[i];
	     					data_count++;
	     					//System.out.println("row_xvalue["+data_count+"]="+tmp_xvalue[data_count]);
	     				}
	     				catch(Exception e)
	     				{
							//System.out.println("wrong data " +raw_x[i]);
	     					//showMessageDialog("Data format error! Skip:" +raw_x[i]+ "at #"+i);
	     					System.out.println("Data format error! Skip:" +raw_x[i]+ " at line" +(i+1));
	     				}
	     				
	     		}
		//	System.out.println("data_count=" +data_count);
			
	     double[] raw_xvalue = new double[data_count];
	     for (int i=0; i<data_count; i++)
	     	 raw_xvalue[i]=tmp_xvalue[i];
	     
	     histogram(raw_xvalue, data_count); 
	/*	System.out.println("sliderSetted="+sliderSetted);
			
		if (!sliderSetted){ // need to calculate the max and min, setRange for slider
		
			default_bin = (int)Math.sqrt(data_count);  // for range >1
			
			for (int i=0; i<data_count; i++){
				if (raw_xvalue[i]>max_x)
					max_x= raw_xvalue[i];
				if (raw_xvalue[i]<min_x)
					min_x= raw_xvalue[i];
			}
				//System.out.println("max_x="+max_x);
				//System.out.println("min_x="+min_x);
			double range = max_x - min_x;
			System.out.println("range="+range);
			if (range < 1){
			
				binSlider2 = new FloatSlider("Bin Size", 0.0, range); //set minimumRange10 = true
				binSlider2.setPreferredSize(new Dimension(CHART_SIZE_X-10, 100));
				binSlider2.addObserver(this);
		       
				 min_bin = 0;
				 max_bin = 1;
				 int t= 10;
				 for (int i=1; i<5; i++){
					
				 if (range>(double)1/t){
				    default_bin = (double)1/t;
				    bin_size = (double)1/t;
				    break;
				    }
				 else t*=10;
				 }	
				  
				 sliderSetted = true;
				 binSlider2.setFloatValue(default_bin);	
				 //setRange and setValue method doesn't work when max_bin-min_bin<=1
			}
			else {
				max_bin = (int)(Math.round(range)+1.0);
			
				sliderSetted = true; // it has to been set before calling the setRange method to prevent looping
				binSlider2.setAll(min_bin, max_bin, default_bin);
			}
			
		}
		//
		
		
		System.out.println("bin_size="+bin_size);
		bin_count = (int)((max_bin-min_bin)/this.bin_size)+1;
		
	    System.out.println("bin_count="+bin_count +"\n");
		
		y_freq= new int[bin_count];
		for (int i =0; i<data_count; i++){
			int j = (int)((raw_xvalue[i]-min_x)/this.bin_size);		
			y_freq[j]++;
		}
			
		x_start = new double[bin_count];
		x_end = new double[bin_count];
	
		for (int i=0; i<bin_count; i++){
		
		   x_start[i]=min_x+i*(this.bin_size);
		   x_end[i]=x_start[i]+(this.bin_size);
		//	System.out.println("x_bin["+i+"]="+x_bin[i]);
		//	System.out.println("y_freq["+i+"]="+y_freq[i]);
		}
		*/
    		return;
    }
    
    protected void do_histogram(String[] raw_x, int row_count){
	//System.out.println("do Histogram String input row_count="+row_count);
  //  Exception e2 = new Exception();
  //  e2.printStackTrace();
       	double[] tmp_xvalue = new double[row_count];
       	int data_count = 0;
        	
   	     	//System.out.println("row_count="+row_count);
   	     	
   	     	
       	for (int i=0; i<row_count; i++){
   	     			//System.out.println("parsing:"+raw_x[i]);
       		if (raw_x[i]!=null&&raw_x[i].length()!=0){
       			try{
       				tmp_xvalue[data_count]= Double.parseDouble(raw_x[i]);
   	     				//	System.out.println("row_xvalue["+data_count+"]="+tmp_xvalue[data_count]);
       				data_count++; 	     					
       			}
       			catch(Exception e)
       			{
       				//System.out.println("wrong data " +raw_x[i]);
       				//showMessageDialog("Data format error! Skip:" +raw_x[i]+ "at #"+i);
       				System.out.println("Data format error! Skip:" +raw_x[i]+ " at #"+(i+1));
       			}
   	     				
       		}else 	{
       			//System.out.println("skip: raw_x:" +raw_x[i]+" at #"+(i+1));
   	     			
       		}
       	}
   		//	System.out.println("data_count=" +data_count);
       	double[] raw_xvalue = new double[data_count];
       	for (int i=0; i<data_count; i++)
       		raw_xvalue[i]=tmp_xvalue[i];
     	     
   	   histogram(raw_xvalue, data_count);
   	   return;
       }
    
    protected void histogram(double[] raw_xvalue, int data_count){
    	
    	     
    		//	System.out.println("......");
    	//  System.out.println("sliderSetted="+sliderSetted);
    		if (!sliderSetted){ // need to calculate the max and min, setRange for slider
    		
    			default_bin = (int)Math.sqrt(data_count);  // for range >1
    			
    			max_x=0;
    			min_x=0;
    			for (int i=0; i<data_count; i++){
    				if (raw_xvalue[i]>max_x)
    					max_x= raw_xvalue[i];
    				if (raw_xvalue[i]<min_x)
    					min_x= raw_xvalue[i];
    			}
    				//System.out.println("max_x="+max_x);
    				//System.out.println("min_x="+min_x);
    			double range = max_x - min_x;
    		//	System.out.println("range="+range+ " "+min_x+"->"+max_x);
    			if (range < 1){
    			
    				binSlider2 = new FloatSlider("Bin Size", 0.0, range); //set minimumRange10 = true
    				binSlider2.setPreferredSize(new Dimension(CHART_SIZE_X-10, 100));
    				binSlider2.addObserver(this);
    		       
    				 min_bin = 0;
    				 max_bin = 1;
    				 int t= 10;
    				 for (int i=1; i<5; i++){
    					
    				 if (range>(double)1/t){
    				    default_bin = (double)1/t;
    				    bin_size = (double)1/t;
    				    break;
    				    }
    				 else t*=10;
    				 }	
    				  
    				 sliderSetted = true;
    				 binSlider2.setFloatValue(default_bin);	
    				 //setRange and setValue method doesn't work when max_bin-min_bin<=1
    			}
    			else {
    				default_bin =(int) (range)/Math.sqrt(data_count);  // for range >1
    				max_bin = (int)(Math.round(range)+1.0);
    				min_bin = 1;
    				bin_size = default_bin;
    				//System.out.println("range="+range+" "+ Math.sqrt(data_count)+" bins_size="+bin_size);
    			//	if(bin_size<range/100){
    			//		bin_size= range/100;   				
    			//	}
    				
    				sliderSetted = true; // it has to been set before calling the setRange method to prevent looping
    				binSlider2.setAll(min_bin, max_bin, bin_size);
    				binSlider2.setFloatValue(bin_size);	
    			}
    		}
    		//
    		
    		
    		//System.out.println("bin_size="+bin_size);
    		bin_count = (int)((max_bin-min_bin)/this.bin_size)+1;
    		
    		//System.out.println("bin_count="+bin_count +"\n");
    		
    		y_freq= new int[bin_count];
    		for (int i =0; i<data_count; i++){
    			int j = (int)((raw_xvalue[i]-min_x)/this.bin_size);		
    		//	System.out.println("j="+j);
    			y_freq[j]++;
    		}
    			
    		x_start = new double[bin_count];
    		x_end = new double[bin_count];
    	
    		for (int i=0; i<bin_count; i++){
    		
    		   x_start[i]=min_x+i*(this.bin_size);
    		   x_end[i]=x_start[i]+(this.bin_size);
    		//	System.out.println("x_bin["+i+"]="+x_bin[i]);
    		//	System.out.println("y_freq["+i+"]="+y_freq[i]);
    		}
    		
    }
    /**
     * reset dataTable to default (demo data), and refesh chart
     */
    public void resetExample() {

        reset_BinSlider();
       
	    IntervalXYDataset dataset= createDataset(true);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();

        hasExample = true;
      
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

    public void setXLabel(String xLabel){
      	domainLabel = xLabel;
      	TableColumnModel columnModel = dataTable.getColumnModel();
      	for (int i=0; i<columnModel.getColumnCount(); i+=2)
      		columnModel.getColumn(i).setHeaderValue(xLabel);
    		dataTable.setTableHeader(new EditableHeader(columnModel));
      }
      
      public void setYLabel(String yLabel){
      	rangeLabel = yLabel;
      
      }
      
 // this will update the mapping panel    
    public void setMapping(){

  			addButtonIndependent();
  	
    }
    
    public void setDataTable (String input){
      	
    	StringTokenizer lnTkns = new StringTokenizer(input,"#");
    	String line;
        int lineCt = lnTkns.countTokens();
        resetTableRows(lineCt);
        int r = 0;
        while(lnTkns.hasMoreTokens()) {
        	line = lnTkns.nextToken();
        	
       //	String tb[] =line.split("\t");
            StringTokenizer cellTkns = new StringTokenizer(line, " \t\f,");// IE use "space" Mac use tab as cell separator
            int cellCnt = cellTkns.countTokens();
            String tb[]= new String[cellCnt];
            int r1=0;
              while(cellTkns.hasMoreTokens()) {
               tb[r1]=cellTkns.nextToken();
               r1++;
              }
            //System.out.println("tb.length="+tb.length);
        	int colCt=tb.length;
        	resetTableColumns(colCt);
            for (int i=0; i<tb.length; i++){
            	//System.out.println(tb[i]);
            	if (tb[i].length()==0)
            		tb[i]="0";
            	dataTable.setValueAt(tb[i], r,i);
            }           
        	r++;
        }
        
        // this will update the mapping panel     
        resetTableColumns(dataTable.getColumnCount());
        // comment out, let setXLabel and setYLabel take care of this
   /*     resetTableColumns(dataTable.getColumnCount());
        TableColumnModel columnModel= dataTable.getColumnModel();
        dataTable.setTableHeader(new EditableHeader(columnModel));
        System.out.println(dataTable.getColumnCount() +" "+dataTable.getRowCount());
        
        int seriesCount = dataTable.getColumnCount()/2;
        for (int i=0; i<seriesCount; i++){
   			addButtonIndependent();
   			addButtonDependent();
   		}*/
    }

    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a chart.
     */
    protected JFreeChart createChart(IntervalXYDataset dataset) {
          JFreeChart chart = ChartFactory.createXYBarChart(
            chartTitle,      // chart title
            domainLabel,                     // domain axis label
            false,
            rangeLabel,                        // range axis label
            dataset,                    // data
            PlotOrientation.VERTICAL,
           // !legendPanelOn,                       // include legend
            false, //no legend
            true,
            false
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new ClusteredXYBarRenderer());
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	     
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
        // OPTIONAL CUSTOMISATION COMPLETED.
        return chart;            
	}
    
    protected void initGraphPanel(){
		//System.out.println("initGraphPanel called");
		graphPanel = new JPanel();
		
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

		JFreeChart chart = createEmptyChart(null);	//create a empty graph first
		chartPanel = new ChartPanel(chart, false); 
		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y-100));
		
		sliderPanel = new JPanel();
	
      /*   binSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 10, default_bin);
         binSlider.setPaintLabels(true);
         binSlider.setPaintTicks(true);
         binSlider.setMajorTickSpacing(space_bin);
        // binSlider.setMinorTickSpacing(5);
        // binSlider.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
         binSlider.addChangeListener(this);
         sliderPanel.add(new JLabel("Bin Size"));
         sliderPanel.add(this.binSlider);
         sliderPanel.add(new JLabel(":"+this.binSlider.getValue()));*/
         
		// ValueSetter(String title, int type int min, int max, int initial,boolean minimumRange10) 
         binSlider2 = new FloatSlider("Bin Size", 1, 10, 5,true);

	
         reset_BinSlider();// the range will need to be reset
         binSlider2.setPreferredSize(new Dimension(CHART_SIZE_X-10,100));
         binSlider2.addObserver(this);
         sliderPanel.add(this.binSlider2);
         sliderPanel.setPreferredSize(new Dimension(CHART_SIZE_X+100,80));
         
		graphPanel.add(chartPanel);
		graphPanel.add(sliderPanel);
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
     * 
     *
     */
    protected void setChart(){
		// update graph
		//System.out.println("setChart called");

		graphPanel.removeAll();
		//chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));
				sliderPanel.removeAll();
	
		/*sliderPanel.add(new JLabel("Bin Size"));
		sliderPanel.add(this.binSlider);
		sliderPanel.add(new JLabel(":"+this.binSlider.getValue()));*/
		binSlider2.setPreferredSize(new Dimension(CHART_SIZE_X,100));
		sliderPanel.add(this.binSlider2);
		
		graphPanel.add(chartPanel);
		graphPanel.add(sliderPanel);
		graphPanel.validate();


		// get the GRAPH panel to the front
		if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) 
			tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
		else {
			graphPanel2.removeAll();
			chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
			binSlider2.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,100));
			sliderPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,100));
			graphPanel2.add(chartPanel);
			graphPanel2.add(sliderPanel);
			graphPanel2.validate();	
			summaryPanel.validate();
		}
    }
    
    protected void setGraphPanel(){
		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y-100));
	     graphPanel.add(chartPanel);
	     graphPanel.add(sliderPanel);
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
		sliderPanel.removeAll();
	/*	sliderPanel.add(new JLabel("Bin Size"));
        sliderPanel.add(this.binSlider);
        sliderPanel.add(new JLabel(":"+this.binSlider.getValue()));*/
		binSlider2.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,100));
        sliderPanel.add(this.binSlider2);
		sliderPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,100));
		graphPanel2.add(sliderPanel); // the slider
		graphPanel2.validate();

		dataPanel2.add(new JLabel(" "));
		dataPanel2.add(new JLabel("Data"));
		JScrollPane dt = new JScrollPane(dataTable);
		dt.setRowHeaderView(headerTable);
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

    /**
     * Receives JSlider change events and updates the chart accordingly.
     * 
     * @param event  the event.
     */
 
	protected void reset_BinSlider(){
			sliderSetted = false;
			min_x= 1000;
			max_x = -1000;
	}
	
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
		if (this.bin_size!=this.binSlider2.getFloatValue()){
			set_binSize(binSlider2.getFloatValue());	
			redoChart();
			return;
		}
	}

	
}
