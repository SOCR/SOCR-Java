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

import edu.ucla.stat.SOCR.analyses.data.DataCase;
import edu.ucla.stat.SOCR.analyses.exception.DataException;
import edu.ucla.stat.SOCR.chart.data.Summary;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.util.RegressionLine;

/**
  * A simple demonstration application showing how to create a QQ data vs data chart.
 */
public class SuperXYChart_QQ_DD extends SuperXYChart implements PropertyChangeListener {
    protected int x_row_count, y_row_count, larger_row_count;
	protected double min_x, max_x;
	protected String[] raw_x, raw_y;
	protected double[] stdResiduals, normalQuantiles;

	public void init(){
		trimColumn = false;
		indLabel = new JLabel("Group 1"); //X
		depLabel = new JLabel("Group 2"); //Y

		super.init();
		depMax = 2; // max number of dependent var
		indMax = 2; // max number of independent var

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
		 showMessageDialog("SuperXYChart_QQ_DD doTest get called!");

		 int no_series = dataTable.getColumnCount()/2;
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 2*i;    //column x
			 pairs[i][1] = 2*i+1;    //column y
		 }
		 chart = chartMaker.getXYChart("LineQQDD","QQD2D Chart", "Group1", "Group 2", dataTable, no_series, pairs,"");
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
		XYDataset dataset = createDataset(isDemo);

		JFreeChart chart = createChart(dataset);
		chartPanel = new ChartPanel(chart, isDemo);
		setChart();
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
		else{

			setArrayFromTable();

			//System.out.println("ind="+independentVarLength +" dep="+dependentVarLength);

			if (independentVarLength != dependentVarLength){
			showMessageDialog("The number of X and Y doesn't match!");
			resetChart();
			return null;
			}

			
			// allow X and Y have different rowcount
			String[]  raw_x_tmp= new String[xyLength];
			String[] raw_y_tmp= new String[xyLength];

		//	System.out.println("xyLength="+xyLength);
			int k =0;
			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
					if (indepValues[i][index]!=null)
						if (indepValues[i][index]!="null" && indepValues[i][index].length()!=0){
							raw_x_tmp[k] = indepValues[i][index];
							k++;
						}
				}
			x_row_count=k;
			raw_x = new String[x_row_count];			
			for (int i=0; i<k ; i++)
				 raw_x[i]=raw_x_tmp[i];
				 
			k=0;				 
			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++)
					if (depValues[i][index]!=null)
						if (depValues[i][index]!="null" &&depValues[i][index].length()!=0){
							raw_y_tmp[k] = depValues[i][index];
							k++;
						}
			y_row_count=k;
			raw_y = new String[y_row_count];			
			for (int i=0; i<k ; i++)
				 raw_y[i]=raw_y_tmp[i];
			
			if (y_row_count>=x_row_count)
				larger_row_count= y_row_count;
			else 
				larger_row_count= x_row_count;
			
			do_dd();

			int len = normalQuantiles.length;
			// create the dataset...
			XYSeriesCollection dataset = new XYSeriesCollection();
			XYSeries series;

			series = new XYSeries("QQ");

			// Ivo Test:  
			for (int j=0; j<len; j++) {
			//for (int j=0; j<xyLength; j++){
				series.add(normalQuantiles[j], stdResiduals[j]);
			}
			
			dataset.addSeries(series);
			try{
			XYSeries series2 = new XYSeries("Reference Line");
			RegressionLine refLine = new RegressionLine(normalQuantiles, stdResiduals);
			double intercept = refLine.getIntercept();
			double slope = refLine.getSlope();

			min_x = Math.min(normalQuantiles[0], stdResiduals[0]);
			double min_y = slope*min_x +intercept;

			// Ivo Test: 
			//System.out.println("len="+len +" normalQ"+normalQuantiles[len-1] +" stdR"+stdResiduals[len-1]);
			max_x = Math.max (normalQuantiles[len-1],stdResiduals[len-1]);
			//max_x = Math.max (normalQuantiles[xyLength-1],stdResiduals[xyLength-1]);
			
			double max_y = slope*max_x+intercept;

			series2.add(min_x,min_y);
			series2.add(max_x,max_y);

			dataset.addSeries(series2);
			}	catch(DataException e){
				System.out.println("cought dataException" );
				e.printStackTrace();
			}

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

	    XYDataset dataset= createDataset(true);

		JFreeChart chart = createChart(dataset);
		chartPanel = new ChartPanel(chart, false);
		setChart();

        hasExample = true;
		convertor.XY2Table(raw_x, raw_y, larger_row_count);
		JTable tempDataTable = convertor.getTable();

		resetTableRows(tempDataTable.getRowCount()+1);
		resetTableColumns(tempDataTable.getColumnCount());

        for(int i=0;i<tempDataTable.getColumnCount();i++) {
            //columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
			columnModel.getColumn(i).setHeaderValue("Group "+(i+1));
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

	//Data vs Data
	public void do_dd(){
		//System.out.println("x_row_count"+x_row_count +" y_row_count"+y_row_count);
			double[] x = new double[x_row_count];
			double[] y = new double[y_row_count];

			try{
				for (int i=0; i<x_row_count; i++){
					if (raw_x[i].length()==0)
						x[i]=0.0;
					else x[i]= Double.parseDouble(raw_x[i]);
				}
				for (int i=0; i<y_row_count; i++){
					if (raw_y[i].length()==0)
						y[i]=0.0;
					y[i]= Double.parseDouble(raw_y[i]);
				}
			}catch(NumberFormatException e)
				{
					showMessageDialog("Data format error!");
				 }

			//stdResiduals = RandomGenerator.getGeneratedArray(x, RandomGenerator.NORMAL);
			stdResiduals = AnalysisUtility.getQuantileArray(x);
			DataCase[] sortedResidual = AnalysisUtility.getSortedRedidual(stdResiduals);
			
			//System.err.println("sortedResidual.length="+sortedResidual.length);
			
			for (int i = 0; i < sortedResidual.length; i++) {
				stdResiduals[i]= sortedResidual[i].getValue()	;
				}

			//normalQuantiles = RandomGenerator.getGeneratedArray(y, RandomGenerator.NORMAL);
			normalQuantiles = AnalysisUtility.getQuantileArray(y);
			DataCase[] sortedQuantiles = AnalysisUtility.getSortedRedidual(normalQuantiles);
			
			//System.err.println("sortedQuantiles.length="+sortedQuantiles.length);
			
			for (int i = 0; i < sortedQuantiles.length; i++) {
				normalQuantiles[i]= sortedQuantiles[i].getValue();
				}

	}

	protected void setQQSummary(XYDataset ds){
		summaryPanel.removeAll();
		summaryPanel.setText("Summary:\n");

		Summary s = new Summary(ds);
		int categoryCount = s.getCategoryCount();

		for (int i=0; i<categoryCount; i++)
			summaryPanel.append(s.getQQSummary(i));
		summaryPanel.validate();
	}
	
	 public void setDataTable (String input){
   	  hasExample= true;
   	  StringTokenizer lnTkns = new StringTokenizer(input,"#");
   	  String line;
   	  int lineCt = lnTkns.countTokens();
   	  resetTableRows(0);
   	  resetTableColumns(lineCt);
   	  int r = 0;
   	  while(lnTkns.hasMoreTokens()) {
   		  line = lnTkns.nextToken();
   	     		
       //	String tb[] =line.split("\t");
            StringTokenizer cTkns = new StringTokenizer(line, ";");
           
           
              
            if(r==0){
            	domainLabel=cTkns.nextToken();           	
            }
            if(r==1){
            	rangeLabel =  cTkns.nextToken();            	
            }
          
            StringTokenizer cellTkns = new StringTokenizer(cTkns.nextToken(), ",");
            int cellCnt = cellTkns.countTokens();           
            String tb[]= new String[cellCnt];
            int r1=0;
              while(cellTkns.hasMoreTokens()) {
               tb[r1]=cellTkns.nextToken();
               r1++;
              }
            //System.out.println("tb.length="+tb.length);
              
        	int colCt= tb.length;
        //	System.out.println("rowCount="+dataTable.getRowCount()+" input count="+colCt);
        	if (dataTable.getRowCount()< colCt)
        		resetTableRows(colCt);
            for (int i=0; i<tb.length; i++){
            	dataTable.setValueAt(tb[i], i, r);
            }           
        	r++;
        }
        
        // this will update the mapping panel     
        resetTableColumns(dataTable.getColumnCount());
        TableColumnModel columnModel = dataTable.getColumnModel(); 
        columnModel.getColumn(0).setHeaderValue(domainLabel);
        columnModel.getColumn(1).setHeaderValue(rangeLabel);
    }
     
     public void setMapping(){
    	 addButtonIndependent();
    	 addButtonDependent();
     }
     

}

