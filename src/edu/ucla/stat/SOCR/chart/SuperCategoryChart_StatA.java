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
import java.beans.PropertyChangeListener;
import java.util.StringTokenizer;
import java.lang.NumberFormatException;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;

import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
  * A simple demonstration application showing how to create a statistical bar chart.
 */
public class SuperCategoryChart_StatA extends SuperCategoryChart implements PropertyChangeListener {

	protected DefaultStatisticalCategoryDataset dataset;
	/**
	 * sample code for generating chart using ChartGenerator_JTable 
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SuperCategoryChart_Stat doTest get called!");
		
		 int no_series = dataTable.getRowCount()-1;
		 int no_category = dataTable.getColumnCount()-2;
		 int[][] pairs = new int[no_category][2];
		 for (int i=0; i<no_category; i++){
			 pairs[i][0] = i+1;    //column i store category i
			 pairs[i][1] = 0;    //column 0 stores series name
		 }
		 chart = chartMaker.getCategoryChart("BarStat", "Category Chart", "Type", "value", dataTable, no_category, pairs, "");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }

	/**
	 * @param isdemo data come from demo(true) or dataTable(false)
	 */
    protected  CategoryDataset createDataset(boolean isDemo) {
	
		if (isDemo){
			return super.createDataset(true);
   		}
		else{
			try{
				
				setArrayFromTable();
		
				double[][] xmean = new double[xyLength][independentVarLength]; 
				double[][] xstdDev =  new double[xyLength][independentVarLength];
				String[][] y = new String[xyLength][dependentVarLength];
				String [] ss = new String[2] ;
		
				for (int index=0; index<independentVarLength; index++)
					for (int i = 0; i < xyLength; i++) {
						if (indepValues[i][index]==null || indepValues[i][index].length()==0)
						{
							if (index%2==0)
								xmean[i][index/2]=0.0;
							else
								xstdDev[i][index/2]=0.0;
						}
						else{
						
							if (index%2==0)
								xmean[i][index/2] = Double.parseDouble(indepValues[i][index]);
							else
								xstdDev[i][index/2] = Double.parseDouble(indepValues[i][index]);
						}
					}
				   
				for (int index=0; index<dependentVarLength; index++)
					for (int i = 0; i < xyLength; i++) 
							y[i][index] = depValues[i][index];
		
				// create the dataset... 
				dataset = new DefaultStatisticalCategoryDataset(); 
		
					//dependent 
					for (int j=0; j<xyLength; j++)
						for (int i=0; i<independentVarLength/2; i++){
							String header="C"+i;
							if (independentHeaders[i*2].length()!=0 &&independentHeaders[i*2].indexOf("Mean")!=-1)
								header = independentHeaders[i*2].substring(0, independentHeaders[i*2].indexOf("Mean"));
							if(y[j][0]!=null)
								dataset.add(xmean[j][i], xstdDev[j][i], y[j][0], header); 
							//System.out.println("adding :("+x[j][i]+","+y[j]+","+independentHeaders[i]+")" );
						}
					
		            return dataset; 
			}catch(NumberFormatException e)
				{
					showMessageDialog("Data format error!");
					return null;}
		}

	} 
    
    
    /**
     * reset dataTable to default (demo data), and refesh chart
     */
      public void resetExample() {

    	  dataset= (DefaultStatisticalCategoryDataset)createDataset(true);	
    		
    		JFreeChart chart = createChart(dataset);	
    		chartPanel = new ChartPanel(chart, false);
    		//		setSummary(dataset);
    		setChart();

            hasExample = true;
    		convertor.dataset2TableA(dataset);				
    		JTable tempDataTable = convertor.getTable();
    		resetTableRows(tempDataTable.getRowCount()+1);
    		resetTableColumns(tempDataTable.getColumnCount());

            for(int i=0;i<tempDataTable.getColumnCount();i++) {
                columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
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
    
  // this will update the mapping panel    
  public void setMapping(){

	  addButtonDependent();
		int columnCount = dataTable.getColumnCount();
		for(int i=0; i<columnCount; i++)
			addButtonIndependent();
  }
  
  
  /* read in data from html*/
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
         StringTokenizer cellTkns = new StringTokenizer(line, ";");// IE use "space" Mac use tab as cell separator
         int cellCnt = cellTkns.countTokens();
         String tb[]= new String[cellCnt];
         int r1=0;
           while(cellTkns.hasMoreTokens()) {
            tb[r1]=cellTkns.nextToken();
            r1++;
           }
         //System.out.println("tb.length="+tb.length);
     	int colCt=tb.length;
     	resetTableColumns(colCt*2-1);
     	
         for (int i=0; i<tb.length; i++){
         	//System.out.println(tb[i]);
         	if (tb[i].length()==0)
         		tb[i]="0";
         	
         	if(i==0)
         		 dataTable.setValueAt(tb[i], r,i);
         	else{
             	StringTokenizer cTkns = new StringTokenizer(tb[i], ",");
             	int cCnt = cTkns.countTokens();
                String ctb[]= new String[cCnt];
                int r2=0;
                while(cTkns.hasMoreTokens()) {
                 ctb[r2]=cTkns.nextToken();
                 r2++;
                }
                for (int j=0; j<ctb.length; j++){
                	dataTable.setValueAt(ctb[j], r,(i-1)*2+j+1);
                }
         	}
         }           
     	r++;
     }
     
     // this will update the mapping panel     
     resetTableColumns(dataTable.getColumnCount());
 }
  
  public void setXLabel(String xLabel){
		
	  	domainLabel = xLabel;
	  	
	  	for (int i=1; i<columnModel.getColumnCount(); i++)
	  		if(i%2==1)
	  		columnModel.getColumn(i).setHeaderValue(domainLabel+(i+1)/2+" Mean");
	  		else columnModel.getColumn(i).setHeaderValue(domainLabel+(i+1)/2+" StdDev");
	  
	  	dataTable.setTableHeader(new EditableHeader(columnModel));
	  }
	  
  public void setYLabel(String yLabel){
	  	rangeLabel = yLabel;
	  	TableColumnModel columnModel = dataTable.getColumnModel();
	  	columnModel.getColumn(0).setHeaderValue(rangeLabel);	
	  }
  
  protected JFreeChart createLegend(CategoryDataset dataset) {
      
		//  JFreeChart chart = ChartFactory.createAreaChart(
	        JFreeChart chart = ChartFactory.createLineChart(
	            chartTitle,             // chart title
	            domainLabel,               // domain axis label
	            rangeLabel,                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
	 
	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	        chart.setBackgroundPaint(Color.white);
	        CategoryPlot plot = chart.getCategoryPlot();

	        StatisticalBarRenderer renderer = new StatisticalBarRenderer();
	        renderer.setErrorIndicatorPaint(Color.black);
			//	renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());
	        plot.setRenderer(renderer);
	       // renderer.setDrawOutlines(true);
	       // renderer.setUseFillPaint(true);
	       // renderer.setFillPaint(Color.white);
	
	        return chart;
	        
	    }

	    protected JFreeChart createLegendChart(JFreeChart origchart) {
	    	
	    	JFreeChart legendChart = new JFreeChart("", null, new HiddenPlot(),false);
	    	
	    	legendChart.setBackgroundPaint(Color.white);
	        CategoryPlot plot = origchart.getCategoryPlot();

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
	    
}
