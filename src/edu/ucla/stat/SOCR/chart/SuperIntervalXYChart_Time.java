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
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;

import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
import edu.ucla.stat.SOCR.gui.SOCROptionPane;


/**
 * A simple demonstration application showing how to create a Bar chart using 
 * data from a {@link IntervalXYDataset}.
 */
public class SuperIntervalXYChart_Time extends Chart implements PropertyChangeListener {
	IntervalXYDataset dataset;
	
	public void init(){

		indLabel = new JLabel("Time");
		depLabel = new JLabel("Value");

		super.init();
		depMax = 50; // max number of dependent var
		indMax = 50; // max number of independent var

		updateStatus(url);	
	  
		resetExample();
		validate();
		
	}
	/**
	 * sample code for generating chart using ChartGenerator_JTable 
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SuperIntervalXYChart_Time doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = i*2;    //column 1 stores value
			 pairs[i][1] = i*2+1;    //column 0 stores time
		 }
		 chart = chartMaker.getXYChart("Bar","Category Chart", "Category", "value", dataTable, no_series, pairs,"Year");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
	 protected JFreeChart createLegend(IntervalXYDataset dataset) {
	        
	        JFreeChart chart = ChartFactory.createXYBarChart(
	        		  chartTitle,      // chart title
	                  domainLabel,                   // domain axis label
	                  true,
	                  rangeLabel,                        // range axis label
	                  dataset,                    // data
	                  PlotOrientation.VERTICAL,
	                  true,                       // include legend
	                  true,
	                  false
	        );
	 
	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	        chart.setBackgroundPaint(Color.white);
	        XYPlot plot = (XYPlot) chart.getPlot();

	        XYItemRenderer renderer = plot.getRenderer();
	        renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
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
	    
	    protected void setChart(){
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
	        
	        public void setXLabel(String xLabel){
	        	domainLabel = xLabel;
	        	TableColumnModel columnModel = dataTable.getColumnModel();
	        	for (int i=0; i<columnModel.getColumnCount()/2; i++)
	        		columnModel.getColumn(2*i).setHeaderValue(xLabel+":"+i);
	    		dataTable.setTableHeader(new EditableHeader(columnModel));
	        }
	        
	        public void setYLabel(String yLabel){
	        	rangeLabel = yLabel;
	        	TableColumnModel columnModel = dataTable.getColumnModel();
	        	for (int i=0; i<columnModel.getColumnCount()/2; i++)
	        		columnModel.getColumn(2*i+1).setHeaderValue(yLabel+":"+i);
	    		dataTable.setTableHeader(new EditableHeader(columnModel));    		
	    		
	        }
	        
	        // this will update the mapping panel    
	        public void setMapping(){
	     
		        int seriesCount = dataTable.getColumnCount()/2;
		        for (int i=0; i<seriesCount; i++){
					addButtonIndependent();
					addButtonDependent();
				}
	        }
	        
	        // this will update the mapping panel
	     
	    
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
		dataset = createDataset(isDemo);	
		
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
		if(isDemo)	{
				updateStatus("isDemo==true in "+this.getClass().getName()+" class! return null Dataset, check the code!");
			return null; }
		else {
			setArrayFromTable();
				
			String[][] x = new String[xyLength][independentVarLength] ;
			double[][] y = new double[xyLength][dependentVarLength];
			int[][] skipy = new int[xyLength][dependentVarLength];

			for (int index=0; index<independentVarLength; index++){
				for (int i = 0; i < xyLength; i++) {
					 x[i][index] = indepValues[i][index];
					// System.out.println("x["+i+"]["+index+"]="+x[i][index]);
				}
			}
		   			
			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
				 if(depValues[i][index]==null ||depValues[i][index]=="null"||depValues[i][index]=="NaN"||depValues[i][index].length()==0)
					 skipy[i][index] = 1; //skip it
				 else y[i][index] = Double.parseDouble(depValues[i][index]);
				// System.out.println("y["+i+"]["+index+"]="+y[i][index]);
				}

			
			TimeSeriesCollection collection = new TimeSeriesCollection();
			TimeSeries series;
			SimpleDateFormat df = new SimpleDateFormat();

			for (int ind =0; ind<independentVarLength; ind++){

				int start_ind   = independentHeaders[ind].lastIndexOf(":");
				if 	(start_ind< 0)
					start_ind =0;
				int start_dep   = dependentHeaders[ind].lastIndexOf(":");
				if 	(start_dep< 0)
					start_dep =0;

				String serieName = independentHeaders[ind].substring(0, start_ind);
				if (serieName.length()==0)
					serieName="Serie"+ind;
				if (start_ind>0)
					domainLabel = independentHeaders[ind].substring(0, start_ind);
				else domainLabel = independentHeaders[ind];
				
				if (start_dep>0)
					rangeLabel = dependentHeaders[ind].substring(0, start_dep);
				else rangeLabel = dependentHeaders[ind];

				//				series = new TimeSeries(serieName,indName,depName, Year.class);
				series = new TimeSeries(serieName,Year.class);
				//TimeSeries("Executions", "Year", "Count", Year.class);
				
				try{
					for (int i=0; i<xyLength; i++)
						if (x[i][ind]!=null && skipy[i][ind]!=1){
							series.add(new Year(Integer.parseInt(x[i][ind])), y[i][ind]);
							//System.out.println("adding year "+new Year(Integer.parseInt(x[i][ind]))+ " , "+y[i][ind]);
						}
				}catch(NumberFormatException e){
					SOCROptionPane.showMessageDialog(this, "Wrong data format, enter integer for Year please. Check the Mapping also.");
					return null;
				}
				//System.out.println("adding:"+serieName);
			//	collection.setDomainIsPointsInTime(false);
				collection.addSeries(series);
			}
			return collection;	
		}
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

		convertor.dataset2Table((TimeSeriesCollection)dataset);				
		JTable tempDataTable = convertor.getTable();
		int seriesCount = tempDataTable.getColumnCount()/2;
		resetTableRows(tempDataTable.getRowCount()+1);
		resetTableColumns(seriesCount*2);

        for(int i=0;i<seriesCount*2;i++) {
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

        setMapping();

		updateStatus(url);
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
            true,
            rangeLabel,                        // range axis label
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
	
}
