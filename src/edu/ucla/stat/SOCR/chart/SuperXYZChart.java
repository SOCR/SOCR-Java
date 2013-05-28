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
import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.util.PercentSlider;

/**
  * A simple demonstration application showing how to create a Bubble chart.
 */
public class SuperXYZChart extends Chart implements PropertyChangeListener, Observer {


	/**
	 * @uml.property  name="x" multiplicity="(0 -1)" dimension="1"
	 */
	double[] x;
	/**
	 * @uml.property  name="y" multiplicity="(0 -1)" dimension="1"
	 */
	double[] y;
	/**
	 * @uml.property  name="z" multiplicity="(0 -1)" dimension="1"
	 */
	double[] z;
	
	double zShrinkPercent = 100;
	JPanel sliderPanel;
	protected PercentSlider  zSlider;
	protected boolean sliderSetted = false;

	public void init(){

		LEGEND_SWITCH =false;
		indLabel = new JLabel("X");
		depLabel = new JLabel("Y/Z");
		
		super.init();
		depMax = 2; // max number of dependent var
		indMax = 1; // max number of independent var

		updateStatus(url);	
	  
		resetExample();
		validate();
		
	}
	

	/**
	 *  sample code for generating chart using ChartGenerator_JTable 
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SuperXYZChart doTest get called!");
		
		 int no_series = (dataTable.getColumnCount())/3;		 
		 int[][] pairs = new int[no_series][3];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 3*i;    //column x
			 pairs[i][1] = 3*i+1;    //column y
			 pairs[i][2] = 3*i+2;    //column y
		 }
	
		 chart = chartMaker.getXYZBubbleChart("Bubble Chart", "X", "Y", dataTable, no_series, pairs,"");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	/**
	 *  create chart using data from the dataTable
	 */
	public void doChart(){

		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			showMessageDialog(DATA_MISSING_MESSAGE);
			resetChart();
			return;
		}
		
		isDemo = false;
		XYZDataset dataset = createDataset(isDemo);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, isDemo); 
		setChart();
		//updateStatus("Chart has been updated, click GRAPH to view it.");
	}

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
			for (int i=0; i<len; i++){
				data[0][i]=x[i];
				data[1][i]=y[i];
				data[2][i]=z[i]*zShrinkPercent/100;
			}
			DefaultXYZDataset dataset = new DefaultXYZDataset();
			String serieName = independentHeaders[0];
			if (independentHeaders[0].indexOf(":")!=-1)
				serieName = independentHeaders[0].substring(0,independentHeaders[0].indexOf(":"));
			
			dataset.addSeries((Comparable)serieName, data);
            return dataset; 
		}

	} 

    protected void setZShrinkPercent(double zt){
    	zShrinkPercent = zt;
    	doChart();	
    }
    
	protected void setXYZArray(){
		//System.out.println("starting setXYArry");		
		independentVarLength = lModelIndep.getSize();
		dependentVarLength = lModelDep.getSize();
		
		int[] independentVar = new int[independentVarLength];	
		int[] dependentVar = new int[dependentVarLength];	


		dependentHeaders = new String[dependentVarLength];
		independentHeaders = new String[independentVarLength];

		//independentVars store the column index for indep
	    int indep = -1; int dep = -1;	
		for (int i = 0; i < listIndex.length; i++) {	
			
			if (listIndex[i]==3){
				indep++;	
				independentHeaders[indep] = columnModel.getColumn(i).getHeaderValue().toString().trim();
				independentVar[indep] = i;
			}else 	if (listIndex[i]==2){
				dep++;	
				dependentHeaders[dep] = columnModel.getColumn(i).getHeaderValue().toString().trim();
				dependentVar[dep] = i;
			}

		}

		domainLabel = independentHeaders[0];
		rangeLabel = dependentHeaders[0];
		
		int xLength = 0;
		int yLength = 0;
		//resultPanelTextArea.append("\nRESULT:\n" );

		String cellValue = null;
		ArrayList<String> xList = new ArrayList<String>(); 
		ArrayList<String> yList = new ArrayList<String>();

		try {
			
			//dependent Y,Z
			yLength = dataTable.getRowCount();
			y = new double[yLength];
			z = new double[yLength];

			for (int index2 = 0; index2 < dependentVarLength; index2++) { 
				yList = new ArrayList<String>();
				yLength =  0;
				for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
						cellValue = ((String)dataTable.getValueAt(k,dependentVar[index2])).trim();
						if (cellValue != null && !cellValue.equals("")) {
							yList.add(yLength , cellValue);
							yLength ++;
						}
						else {
							continue; // to the next for
						}
					} catch (Exception e) { // do nothing?					
					}
				}	
				try{
				for (int i = 0; i < yLength; i++) {
					if ((index2 % 2)==0)
						y[i]= Double.parseDouble((String)yList.get(i));
					else z[i]= Double.parseDouble((String)yList.get(i));
				}
				}catch(NumberFormatException e)
				{
					showMessageDialog("Data format error!");
					}
			}
		
			xyLength = yLength;
			//independents Time
			x = new double[yLength];
			for (int index2 = 0; index2 < independentVarLength; index2++) { 
				xList = new ArrayList<String>(); 
				// for each independent variable

				xLength = 0;
				for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
					cellValue = ((String)dataTable.getValueAt(k, independentVar[index2])).trim();
					if (cellValue != null && !cellValue.equals("")) {
						xList.add(xLength , cellValue); 
						xLength++;
					}
					else {
						continue; // to the next for
					}
					} catch (Exception e) {	
						showError("Exception: " + e );
					}
				}
				try{	
					for (int i = 0; i < xLength; i++) {
						x[i]= Double.parseDouble((String)xList.get(i));
						//Sy	stem.out.println("x["+i+"]["+index2+"]="+x[i][index2]);
				
					}
				}catch(NumberFormatException e)
				{
					showMessageDialog("Data format error!");
				   }
			}
		}catch (Exception e) {
			showError("Exception In outer catch: " + e );
		}
		//System.out.println("ending setXYArry");		
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
    
    /**
     * reset dataTable to default (demo data), and refesh chart
     */    
  public void resetExample() {
	  	reset_Slider();
	    XYZDataset dataset= createDataset(true);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();

		
        hasExample = true;
		convertor.dataset2Table(dataset);				
		JTable tempDataTable = convertor.getTable();
		//		resetTable();
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
		
		int seriesCount = dataset.getSeriesCount();
		for(int i=0; i<seriesCount; i++){
			addButtonIndependent();
			addButtonDependent();
			addButtonDependent();
		}
		//updateStatus(url);
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
	 public void setDataTable (String input){
		  hasExample= true;
		  StringTokenizer lnTkns = new StringTokenizer(input,";");
		  String line;
		  int rowCt = lnTkns.countTokens();
		  resetTableRows(rowCt);
		  resetTableColumns(3);
		  int r = 0;
		  while(lnTkns.hasMoreTokens()) {
			  line = lnTkns.nextToken();
		     		
	    //	String tb[] =line.split("\t");
	         StringTokenizer cellTkns = new StringTokenizer(line, ",");// IE use "space" Mac use tab as cell separator
	         int cellCnt = cellTkns.countTokens();
	         String tb[]= new String[cellCnt];
	         int r1=0;
	         while(cellTkns.hasMoreTokens()) {
	        	dataTable.setValueAt(cellTkns.nextToken(), r,3*r1);
	         	dataTable.setValueAt(cellTkns.nextToken(), r,3*r1+1);
	         	dataTable.setValueAt(cellTkns.nextToken(), r,3*r1+2);
	            r++;
	           }
	     }
		 
	     // this will update the mapping panel     
	     resetTableColumns(dataTable.getColumnCount());
	 }
	  
	  
	  public void setXLabel(String xLabel){			
	  	domainLabel = xLabel;
	  	TableColumnModel columnModel = dataTable.getColumnModel();	  	
	  	columnModel.getColumn(0).setHeaderValue(domainLabel);	  
	  	dataTable.setTableHeader(new EditableHeader(columnModel));
	  }
	  
	  public void setYLabel(String yLabel){
	  	rangeLabel = yLabel;
	  	TableColumnModel columnModel = dataTable.getColumnModel();
	  	columnModel.getColumn(1).setHeaderValue(rangeLabel);
		columnModel.getColumn(2).setHeaderValue("Z");
	 	dataTable.setTableHeader(new EditableHeader(columnModel));
	  }
	  
	  // this will update the mapping panel    
	  public void setMapping(){
		  addButtonIndependent();
		  addButtonDependent();
		  addButtonDependent();
	  }
	  
	  protected void initGraphPanel(){
		//	System.out.println("initGraphPanel called");
			graphPanel = new JPanel();
			
			graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

			JFreeChart chart = createEmptyChart(null);	//create a empty graph first
			chartPanel = new ChartPanel(chart, false); 
			chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y-100));
			
			sliderPanel = new JPanel();
			zSlider = new PercentSlider("Bubble Size Percent", 100, 1, 200);
		//	zSlider.setAll(1, 200, zThrinkPercent);

		
			reset_Slider();// the range will need to be reset
			zSlider.setPreferredSize(new Dimension(CHART_SIZE_X,100));
			//zSlider.setSliderSize(new Dimension(CHART_SIZE_X-10,100));
			zSlider.addObserver(this);
			sliderPanel.add(this.zSlider);
			sliderPanel.setPreferredSize(new Dimension(CHART_SIZE_X+100,80));
	         
			graphPanel.add(chartPanel);
			graphPanel.add(sliderPanel);
			graphPanel.validate();
		}
	  
	  protected void setChart(){
			// update graph
			//System.out.println("setChart called");

			graphPanel.removeAll();
			//chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));
			sliderPanel.removeAll();
		
			/*sliderPanel.add(new JLabel("Bin Size"));
			sliderPanel.add(this.binSlider);
			sliderPanel.add(new JLabel(":"+this.binSlider.getValue()));*/
			zSlider.setPreferredSize(new Dimension(CHART_SIZE_X,100));
			//zSlider.setSliderSize(new Dimension(CHART_SIZE_X-10,100));
			sliderPanel.add(this.zSlider);
			
			graphPanel.add(chartPanel);
			graphPanel.add(sliderPanel);
			graphPanel.validate();


			// get the GRAPH panel to the front
			if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) 
				tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
			else {
				graphPanel2.removeAll();
				chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
				zSlider.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,100));
				sliderPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,100));
				graphPanel2.add(chartPanel);
				graphPanel2.add(sliderPanel);
				graphPanel2.validate();	
				summaryPanel.validate();
			}
	    }
	    
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
			zSlider.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,100));
	        sliderPanel.add(this.zSlider);
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
	  
	    protected void reset_Slider(){
	    	zSlider.setFloatValue(100);
			zShrinkPercent = 100;
	    }
	    
	    public void update(Observable o, Object arg) {
			double d = new Double(zSlider.getFloatValue()).doubleValue();
			//System.out.println("update get called "+d +" zThrinkPercent="+zThrinkPercent);
			if (zShrinkPercent-d>1 || zShrinkPercent-d<-1)
				setZShrinkPercent(d);
			return;
	    }
}
