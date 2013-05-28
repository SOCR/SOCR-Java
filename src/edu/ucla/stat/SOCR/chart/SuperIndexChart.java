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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

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
public class SuperIndexChart extends Chart implements PropertyChangeListener {

	protected String[] raw_x;
	protected int row_count;
	
	public void init(){

		indLabel = new JLabel("X");
		//depLabel = new JLabel("Y");

		mapDep=false;
		
		super.init();
	//	depMax = 10; // max number of dependent var
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
			SOCROptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			resetChart();
			return;
		}

		isDemo = false;
		XYDataset dataset = createDataset(isDemo);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, isDemo); 
		setChart();
	}
	
	/**
	 *  sample code for generating chart using ChartGenerator_JTable 
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SuperIndexChart doTest get called!");
		
		 int no_series = dataTable.getColumnCount();	//one y column only 	
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 0;    // there is no x column
			 pairs[i][1] = 0;    //column y
		 }
		 chart = chartMaker.getXYChart("Index","Index Chart", "Row", "Data", dataTable, no_series, pairs,"noshape");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
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
						if (raw_x[i]!=null && raw_x[i].length()!=0){
							raw_xvalue[row_count]= Double.parseDouble(raw_x[i]);
							row_count ++;
						}
					}catch(Exception e)
					{
					 System.out.println("wrong data " +raw_x[i]);
					}
				}
			
		   //if(rangeLabel==null || rangeLabel.length()==0)
			   rangeLabel=independentHeaders[0];
		   
			double[] y_freq= new double[row_count];
			
			for (int i =0; i<row_count; i++){				
				y_freq[i]=i+1;
			}
			
			XYSeriesCollection dataset = new XYSeriesCollection();
			XYSeries series1 = new XYSeries(rangeLabel);
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
            domainLabel,                      // x axis label
            rangeLabel,                      // y axis label
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
  
      
      public void setMapping(){
      	addButtonIndependent();//Y
      }
      
      public void setXLabel(String xLabel){
  		
        	domainLabel = xLabel;
        	TableColumnModel columnModel = dataTable.getColumnModel();    	
        	columnModel.getColumn(0).setHeaderValue(xLabel);
        	dataTable.setTableHeader(new EditableHeader(columnModel));   
        }
        
        public void setYLabel(String yLabel){
        	rangeLabel = yLabel;
        	TableColumnModel columnModel = dataTable.getColumnModel();      	
        	columnModel.getColumn(0).setHeaderValue(yLabel);
        	dataTable.setTableHeader(new EditableHeader(columnModel));    		
      		
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
  

}
