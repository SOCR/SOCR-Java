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

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.util.EditableHeader;

/**
  * A simple demonstration application showing how to create a Line chart.
 */
public class SuperXYChart_Time extends SuperXYChart implements PropertyChangeListener {


	public void init(){

		super.init();
		depMax = 50; // max number of dependent var
		indMax = 50; // max number of independent var
	}
	
	/**
	 *  sample code for generating chart using ChartGenerator_JTable 
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SuperXYChart_Time doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;		 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 2*i;    //column x
			 pairs[i][1] = 2*i+1;    //column y
		 }
		 String time_type = "Day";
		 chart = chartMaker.getXYChart("LineTime", "Line Chart", "X", "Y", dataTable, no_series, pairs, time_type);	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }

	public void doChart(){
		super.doChart();
	}

    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    
    protected  XYDataset createDataset(boolean isDemo) {
		if (isDemo)	{
				updateStatus("isDemo==true in "+this.getClass().getName()+" class! return null Dataset, check the code!");
			return null; }
		else   		return super.createDataset(false);
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
            PlotOrientation.VERTICAL,
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
    

    /**
     * reset dataTable to default (demo data), and refesh chart
     */
      public void resetExample() {

	    dataset= createDataset(true);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();

        hasExample = true;
		convertor.dataset2Table((TimeSeriesCollection)dataset);				
		JTable tempDataTable = convertor.getTable();
		//resetTable();
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
      
      public void setDataTable (String input){
      	
      	hasExample= true;
      	 StringTokenizer lnTkns = new StringTokenizer(input,"#");
      	String line;
          int lineCt = lnTkns.countTokens();
          resetTableRows(lineCt);
          int r = 0;
          while(lnTkns.hasMoreTokens()) {
          	line = lnTkns.nextToken();
          	
         //	String tb[] =line.split("\t");
              StringTokenizer serieTkns = new StringTokenizer(line, ";");// IE use "space" Mac use tab as cell separator
              int serieCnt = serieTkns.countTokens();
              String tb[]= new String[serieCnt];
              int r1=0;
              while(serieTkns.hasMoreTokens()) {
              	tb[r1]=serieTkns.nextToken();
              	r1++;
              }
              
              //System.out.println("tb.length="+tb.length);
          	int colCt=tb.length*2;
          	resetTableColumns(colCt);
              for (int i=0; i<tb.length; i++){
              	StringTokenizer cellTkns = new StringTokenizer(tb[i], ",");
              	dataTable.setValueAt(cellTkns.nextToken(), r, 2*i);
              	dataTable.setValueAt(cellTkns.nextToken(), r, 2*i+1);
              }           
          	r++;
          }
          
          // this will update the mapping panel     
          resetTableColumns(dataTable.getColumnCount());
        
      }


   public void propertyChange(PropertyChangeEvent e) {
	   super.propertyChange(e);
		}
	
	public Container getDisplayPane() {
		return this.getContentPane();
	}


}
