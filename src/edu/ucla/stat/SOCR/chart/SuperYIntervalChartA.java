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

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.lang.NumberFormatException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;

import edu.ucla.stat.SOCR.chart.data.SimpleIntervalXYDataset2;
import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create a YInterval  chart using 
 * data from a {@link IntervalXYDataset}.
 */
public class SuperYIntervalChartA extends Chart implements PropertyChangeListener {

	IntervalXYDataset dataset;
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SuperYIntervalChart doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;		 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 2*i;    //column x
			 pairs[i][1] = 2*i+1;    //column y
		 }
		 chart = chartMaker.getYIntervalChart("YInterVal Chart", "X", "Y", dataTable, no_series, pairs,"");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
	public void init(){

		depLabel = new JLabel("Y");
		indLabel = new JLabel("X");

		super.init();
		depMax = 2; // max number of dependent var
		indMax = 1; // max number of independent var

		updateStatus(url);	
	  
		resetExample();
		validate();
		
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
		if (dependentIndex < 0 || independentIndex < 0 || independentLength == 0 ||dependentLength ==0) {
			showMessageDialog(VARIABLE_MISSING_MESSAGE);
			resetChart();
			return;
			}

		isDemo = false;
		dataset = createDataset(isDemo);	 // not a demo, so get data from the table
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, isDemo); 
		setChart();

	}

	/**
	   * 
	   * @param isDemo data come from demo(true) or dataTable(false)
	   * @return
	   */  
    protected IntervalXYDataset createDataset(boolean isDemo) {
		if(isDemo){
				updateStatus("isDemo==true in "+this.getClass().getName()+" class! return null Dataset, check the code!");
			return null; }
		else {
			try{
			double[] x;
			double[] y1, y2;

			setArrayFromTable();
			y1 = new double[xyLength];
			y2 = new double[xyLength];
			x = new double[xyLength];
			int[] skipx = new int[xyLength];
			int[] skipy = new int[xyLength];
			String yy[] = new String[2];	


			for (int i = 0; i < xyLength; i++) 
				if (indepValues[i][0]==null || indepValues[i][0].length()==0)
					skipx[i] =1;
				else	x[i]= Double.parseDouble(indepValues[i][0]);
		   

			for (int i = 0; i < xyLength; i++) {
				if (depValues[i][0]==null || depValues[i][0].length()==0||
						depValues[i][1]==null || depValues[i][1].length()==0){
					skipy[i]= 1;
				}
				else{
				/*	StringTokenizer st = new StringTokenizer(depValues[i][0],DELIMITERS);
					yy[0]= st.nextToken();
					yy[1]= st.nextToken();
					//yy = depValues[i][0].split("( *)+,+( *)") ;
					y1[i] = Double.parseDouble(yy[0]);
					y2[i] = Double.parseDouble(yy[1]);*/
					y1[i]=Double.parseDouble(depValues[i][0]);
					y2[i]=Double.parseDouble(depValues[i][1]);
				}
			}
		
			ArrayList<Double> yy1 = new ArrayList<Double>();
			ArrayList<Double> yy2 = new ArrayList<Double>();
			ArrayList<Double> xx = new ArrayList<Double>();
			for (int i=0; i<xyLength; i++)
				if(skipx[i]!=1&&skipy[i]!=1){
					yy1.add(y1[i]);
					yy2.add(y2[i]);
					xx.add(x[i]);
				}
					
			y1 = new double[yy1.size()];
			y2 = new double[yy1.size()];
			x = new double[yy1.size()];
			for (int i=0; i<yy1.size(); i++){
				y1[i]=yy1.get(i);
				y2[i]=yy2.get(i);
				x[i]=xx.get(i);
			}
			
			SimpleIntervalXYDataset2 ds = new SimpleIntervalXYDataset2(yy1.size(), x, y1, y2); 	
			if(dependentHeaders!=null &&dependentHeaders[0]!=null){
				String name = dependentHeaders[0];
				if(name.indexOf(":")!=-1)
					name = name.substring(0, name.indexOf(":"));
				ds.setSeriesKey(name);
			}
            return 	ds;		
			}catch(NumberFormatException e)
				{
					showMessageDialog("Data format error!");
					return null;}
		}

	} 


	protected void setXYArray(){


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
            "Date",                     // domain axis label
            true,
            "Y",                        // range axis label
            dataset,                    // data
            PlotOrientation.VERTICAL,
            !legendPanelOn,                       // include legend
            true,
            false
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new ClusteredXYBarRenderer());
        // OPTIONAL CUSTOMISATION COMPLETED.
        return chart;            
	}	
	protected JFreeChart createLegend(IntervalXYDataset dataset) {
        
		JFreeChart chart = ChartFactory.createXYBarChart(
	            chartTitle,      // chart title
	            "Date",                     // domain axis label
	            true,
	            "Y",                        // range axis label
	            dataset,                    // data
	            PlotOrientation.VERTICAL,
	            true,                       // include legend
	            true,
	            false
	        );
	 
	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		  XYPlot plot = chart.getXYPlot();
	        plot.setRenderer(new ClusteredXYBarRenderer());
	        return chart;
	        
	    }

	    protected JFreeChart createLegendChart(JFreeChart origchart) {
	    	
	    	JFreeChart legendChart = new JFreeChart("", null, new HiddenPlot(),false);
	    	
	    	legendChart.setBackgroundPaint(Color.white);
	        XYPlot plot = (XYPlot)origchart.getPlot();

	        LegendTitle legendTitle = new LegendTitle(plot, 
	                new ColumnArrangement(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, 0), 
	                new ColumnArrangement(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, 0)); 
	        legendChart.addLegend(legendTitle); 
	        
	        return legendChart;
	        
	    }
	   
	  protected  void setChart(){
	    	// update graph
	    //	System.out.println("setChart called");
		
	    	graphPanel.removeAll();
	    	graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

	    	chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

	    	if (legendPanelOn){   	
	    		JFreeChart chart2 = createLegendChart(createLegend(dataset));
	    		legendPanel = new ChartPanel(chart2,false);
		    	//legendPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y*2/3));
	    	}
	    		
	    	graphPanel.add(chartPanel);
	    	JScrollPane	 legendPane = new JScrollPane(legendPanel);
	    	if (legendPanelOn){
	    		legendPane.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/5));
	    		graphPanel.add(legendPane);
	    	}
	    	
	    	graphPanel.validate();

	    	// get the GRAPH panel to the front
	    	if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) {
	    		tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
	    		graphPanel.removeAll();
	    		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
				
				graphPanel.add(chartPanel);
				
		    	if (legendPanelOn){
		    		legendPane = new JScrollPane(legendPanel);
		    		legendPane.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/5));
		    		graphPanel.add(legendPane);
		    	}
		    	graphPanel.validate();
	    	}
	    	else {
	    		graphPanel2.removeAll();
	    		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
	    		//legendPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/5));
	    		graphPanel2.add(chartPanel);
	    		if (legendPanelOn) {
	    			legendPane = new JScrollPane(legendPanel);
	            	legendPane.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/5));           	
	        		graphPanel2.add(legendPane);
	    		}
	    		graphPanel2.validate();	
	    		summaryPanel.validate();
	    	}
	    }
    
    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
	/*    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
		}*/
     
   public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();

		System.err.println("From RegCorrAnal:: propertyName =" +propertyName +"!!!");

		if(propertyName.equals("DataUpdate")) {
			//update the local version of the dataTable by outside source
			dataTable = (JTable)(e.getNewValue());
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));

			System.err.println("From RegCorrAnal:: data UPDATED!!!");
		}
		}
	
	public Container getDisplayPane() {
		return this.getContentPane();
	}
	
	/**
     * reset dataTable to default (demo data), and refesh chart
     */
  public void resetExample() {
	    isDemo = true;
	    dataset= createDataset(isDemo);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();

        hasExample = true;
		convertor.YIntervalDataset2TableA(dataset);				
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
        dataPanel.add(new JScrollPane(dataTable));
		dataTable.setGridColor(Color.gray);
		dataTable.setShowGrid(true);

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
  
  public void setDataTable (String input){
	  hasExample= true;
	  StringTokenizer lnTkns = new StringTokenizer(input,"#");
	  String line;
	  int rowCt = lnTkns.countTokens();
	  resetTableRows(rowCt);
	  resetTableColumns(3);
	  int r = 0;
	  while(lnTkns.hasMoreTokens()) {
		  line = lnTkns.nextToken();
	     		
    //	String tb[] =line.split("\t");
         StringTokenizer cellTkns = new StringTokenizer(line, ";");// IE use "space" Mac use tab as cell separator
         int cellCnt = cellTkns.countTokens();
         String tb[]= new String[cellCnt];
         int r1= 0;
         for (int i=0; i<cellCnt; i++){
        	 tb[r1]=cellTkns.nextToken();
         	 r1++;
         }   
         for (int i=0; i<tb.length; i++){
        	 StringTokenizer cTkns = new StringTokenizer(tb[i], ",");
          	int cCnt = cTkns.countTokens();
             String ctb[]= new String[cCnt];
             
             int r2=0;
             while(cTkns.hasMoreTokens()) {
              ctb[r2]=cTkns.nextToken();
              r2++;
             }
             for (int j=0; j<ctb.length; j++){
             	dataTable.setValueAt(ctb[j], r,j+1);
             }
         }
     	r++;
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
  	columnModel.getColumn(1).setHeaderValue(rangeLabel+ " lower"); 
  	columnModel.getColumn(2).setHeaderValue(rangeLabel+ " upper"); 
  	dataTable.setTableHeader(new EditableHeader(columnModel));
  }
  
  // this will update the mapping panel    
  public void setMapping(){
	  addButtonIndependent();		
	  addButtonDependent();
	  addButtonDependent();
  }
	


}
