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
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CompassPlot;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;

import edu.ucla.stat.SOCR.util.EditableHeader;

/**
  * for the chart has only a single value
 */
public class SuperValueChart extends Chart implements PropertyChangeListener {

	/**
	 * @uml.property  name="y" multiplicity="(0 -1)" dimension="1"
	 */
	double[] y;

	public void init(){

		indLabel = new JLabel("X");
		depLabel = new JLabel("Y");

		super.init();
		depMax = 50; // max number of dependent var
		indMax = 50; // max number of independent var

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
		 showMessageDialog("SuperValueChart doTest get called!");
		
		 int no_series = dataTable.getColumnCount();		 
		 int[][] pairs = new int[no_series][1];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = i;    //column x
		 }
	
		 chart = chartMaker.getCompassChart("CompassChart",  dataTable, no_series, pairs, "");	
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
		ValueDataset dataset = createDataset(isDemo);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, isDemo); 
		setChart();
	}

    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */    
    protected  ValueDataset createDataset(boolean isDemo) {
		if (isDemo){
            ValueDataset dataset = new DefaultValueDataset(new Double(45.0));       
			return dataset;
     
		}else{
			setXYArray();
			//			System.out.println("y="+y[0]);
			// create the dataset... 
			ValueDataset dataset = new DefaultValueDataset(new Double(y[0]));
            return dataset; 
		}
	
	} 

	protected void setXYArray(){
		//System.out.println("starting setXYArry");		
		//		independentVarLength = lModel3.getSize();
		dependentVarLength = lModelDep.getSize();


		//	int[] independentVar = new int[independentVarLength];	
		int[] dependentVar = new int[dependentVarLength];	


			dependentHeaders = new String[dependentVarLength];
			//	independentHeaders = new String[independentVarLength];

		
		//independentVars store the column index for indep
	    int indep = -1; int dep = -1;	
		for (int i = 0; i < listIndex.length; i++) {	
			
			if (listIndex[i]==2){
				dep++;	
				dependentHeaders[dep] = columnModel.getColumn(i).getHeaderValue().toString().trim();
				dependentVar[dep] = i;
			}

		}

		domainLabel = dependentHeaders[0];
		//		rangeLabel = independentHeaders[0];

		int xLength = 0;
		int yLength = 0;
		//resultPanelTextArea.append("\nRESULT:\n" );

		String cellValue = null;

		ArrayList<String> yList = new ArrayList<String>();

		try {
			
			//dependent Y
			yLength = dataTable.getRowCount();
			y = new double[yLength];
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

				for (int i = 0; i < yLength; i++) {
					y[i] = Double.parseDouble((String)yList.get(i));
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
    protected JFreeChart createChart(ValueDataset dataset) {
         // create the chart...
        // OPTIONAL CUSTOMISATION COMPLETED.
            CompassPlot plot = new CompassPlot(dataset);
        plot.setSeriesNeedle(7);
        plot.setSeriesPaint(0, Color.red);
        plot.setSeriesOutlinePaint(0, Color.red);
        plot.setRoseHighlightPaint(Color.CYAN);
        JFreeChart chart = new JFreeChart(plot);
            
        return chart;
  
	}	
   
    /**
     * reset dataTable to default (demo data), and refesh chart
     */
  public void resetExample() {

	    ValueDataset dataset= createDataset(true);	
		
		JFreeChart chart = createChart(dataset);	
	    chartPanel = new ChartPanel(chart, false); 
		setChart();

        hasExample = true;
		convertor.dataset2Table(dataset);				
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
  public void setDataTable (String input){
	  hasExample= true;
	 
	  resetTableColumns(1);
	  resetTableRows(1);
	  dataTable.setValueAt(input, 0,0);
     // this will update the mapping panel     
     resetTableColumns(dataTable.getColumnCount());
 }
  
  
  public void setXLabel(String xLabel){
		
  	domainLabel = xLabel;
 
  }
  
  public void setYLabel(String yLabel){
  	rangeLabel = yLabel;
  	TableColumnModel columnModel = dataTable.getColumnModel();
  
  		columnModel.getColumn(0).setHeaderValue(rangeLabel);
  }
  
  // this will update the mapping panel    
  public void setMapping(){

	  addButtonDependent();
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


}
