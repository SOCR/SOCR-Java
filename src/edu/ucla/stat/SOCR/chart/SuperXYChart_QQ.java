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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
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
import edu.ucla.stat.SOCR.analyses.result.LinearModelResult;
import edu.ucla.stat.SOCR.chart.data.Summary;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
  * A simple demonstration application showing how to create a QQ data vs normalized data chart.
 */
public class SuperXYChart_QQ extends SuperXYChart implements PropertyChangeListener {

	public static String NORMAL="NORMAL";
	public static String POISSON="POISSON";

	protected String disChoice=NORMAL;

    protected int row_count;
	protected double min_x, max_x;
	protected String[] raw_x, raw_y;
	protected double[] stdResiduals, normalQuantiles;

	public void init(){

		indLabel = new JLabel("X"); //X
		depLabel = new JLabel("Y"); //Y

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
		 showMessageDialog("SuperXYChart_QQ doTest get called!");

		 int no_series = dataTable.getColumnCount();	//one y column only
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 0;    // there is no x column
			 pairs[i][1] = 0;    //column y
		 }
		 chart = chartMaker.getXYChart("LineQQ","QQNormalPlot Chart", "Data", "NormalDistribution of Data", dataTable, no_series, pairs,"");
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

		isDemo= false;
		XYDataset dataset = createDataset(isDemo);

		JFreeChart chart = createChart(dataset);
		chartPanel = new ChartPanel(chart, isDemo);
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

			if(dependentVarLength==0){
				setMapping();
				dependentVarLength=1;
			}
			
			setArrayFromTable();
			//System.out.println("after setArrayFromTable "+ row_count);
			row_count = xyLength;
			
			raw_x= new String[row_count];
			raw_y= new String[row_count];

			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
					raw_x[i] = indepValues[i][index];
				}

			row_count=0;
				
			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++)
					if (depValues[i][index]!=null&&depValues[i][index]!="null") {
						raw_y[row_count] = depValues[i][index];
						row_count++;
					}
			
			cleanup_raw(raw_y, row_count);			
			do_normalQQ(raw_y, row_count);
			int len = normalQuantiles.length;
			
			// create the dataset...
			XYSeriesCollection dataset = new XYSeriesCollection();
			XYSeries series;

			series = new XYSeries("QQ");
			min_x = Math.min(normalQuantiles[0], stdResiduals[0]);
			min_x = min_x-0.125;
			max_x = Math.max (normalQuantiles[len-1],stdResiduals[len-1]);
			//max_x = Math.max (normalQuantiles[xyLength-1],stdResiduals[xyLength-1]);
			max_x = max_x+0.125;

			for (int j=0; j<len; j++){
			//for (int j=0; j<xyLength; j++){
				series.add(normalQuantiles[j], stdResiduals[j]);
			}
			dataset.addSeries(series);

			XYSeries series2 = new XYSeries("Reference Line");
			series2.add(min_x,min_x);
			series2.add(max_x,max_x);

			dataset.addSeries(series2);
            return dataset;
		}
	}

    protected void cleanup_raw(String[] in_y, int in_count){
		String[] x = new String[in_count];
		
		int count=0;
		
		//System.out.println("applyPowerTransform row_count=" +row_count);
		for (int i=0; i<in_count; i++){
			if (in_y[i]!=null && in_y[i].length()!=0 ){
				x[count]= in_y[i];
				count++;
			}
		}
		
		raw_y= new String[count];
		
		row_count= count;
		
		for (int i=0; i<count; i++){
			raw_y[i]= x[i]; 
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
		convertor.Y2Table(raw_y, row_count);
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

	// with lm regression same as in R
	public void do_lm(){
		try{
		double[] x = new double[row_count];
		double[] y = new double[row_count];

		try{
			for (int i=0; i<row_count; i++){
				x[i]= Double.parseDouble(raw_x[i]);
				y[i]= Double.parseDouble(raw_y[i]);
			}
		}catch(NumberFormatException e)
			{
				showMessageDialog("Data format error!");
			}

		int sampleSize = x.length;

		double meanX = AnalysisUtility.mean(x);
		double meanY = AnalysisUtility.mean(y);
		double ssTotal = AnalysisUtility.sumOfSquares(y); // corrected total SS
		double sXX=  AnalysisUtility.sampleVariance(x);
		double sYY=  AnalysisUtility.sampleVariance(y);
		double sXY=  AnalysisUtility.sampleCovariance(x, y);
		double beta = sXY / sXX ;
		double alpha = meanY - beta * meanX;
		double rho = sXY / Math.sqrt(sXX * sYY);

		double[] predicted = new double [sampleSize];
		double[] residuals = new double [sampleSize];
		double ssError = 0;
		for (int i = 0; i < sampleSize; i++) {
			predicted[i] = alpha + beta * x[i];
			residuals[i] = y[i]- predicted[i];
			ssError = ssError + (residuals[i] * residuals[i]);
		}

		int df = residuals.length - 2; // number of data points - 2 (2 parameters for SLR)
		HashMap residualMap = AnalysisUtility.getResidualNormalQuantiles(residuals, df);

		double[] sortedResiduals = (double[])residualMap.get(LinearModelResult.SORTED_RESIDUALS);
		int[] sortedResidualsIndex = (int[])residualMap.get(LinearModelResult.SORTED_RESIDUALS_INDEX);
		stdResiduals = (double[])residualMap.get(LinearModelResult.SORTED_STANDARDIZED_RESIDUALS);

		double[] sortedNormalQuantiles = (double[])residualMap.get(LinearModelResult.SORTED_NORMAL_QUANTILES);
		normalQuantiles = (double[])residualMap.get(LinearModelResult.SORTED_STANDARDIZED_NORMAL_QUANTILES);

		}catch(DataException e){
			System.out.println(e);
		}
	}

	// Normal vs Data
	public void do_normalQQ(String[] raw_y, int row_count){
		
			//double[] x = new double[row_count];
			double[] y = new double[row_count];

		for (int i=0; i<row_count; i++){
			//	x[i]= Double.parseDouble(raw_x[i]);
			if(raw_y[i].length()==0)
				y[i]=0.0;
			else y[i]= Double.parseDouble(raw_y[i]);
		}
			do_normalQQ(y, row_count);
	}

	/*  This works, But ivo is trying to fix a bug 10/20/07! **********************
	public void do_normalQQ(double[] y, int row_count){
		try{
		//X
		normalQuantiles = new double[row_count];
		DataCase[] sorted = AnalysisUtility.getSortedRedidual(y);
		for (int i = 0; i < sorted.length; i++) {
				normalQuantiles[i]= sorted[i].getValue()	;
			}
		//Y
		if (disChoice == NORMAL)
			stdResiduals = RandomGenerator.getGeneratedArray(y, RandomGenerator.NORMAL);
		else if (disChoice == POISSON)
				stdResiduals = RandomGenerator.getGeneratedArray(y, RandomGenerator.POISSON);
		sorted = AnalysisUtility.getSortedRedidual(stdResiduals);
		for (int i = 0; i < sorted.length; i++) {
			stdResiduals[i]= sorted[i].getValue()	;
		}


		}catch(DataException e){
			System.out.println(e);
		}catch(NumberFormatException e)
			{
				showMessageDialog("Date format error!");
			  }
	}
	*/
	
	public void do_normalQQ(double[] y, int row_count){
		try{
			double mean = 0.0, sd=0.0;
		//X - DATA
		normalQuantiles = new double[row_count];
		stdResiduals = new double[row_count];
		
		DataCase[] sorted = AnalysisUtility.getSortedRedidual(y);
		
		//Y - Model Distribution (Normal or Poisson)
		if (disChoice == NORMAL) {

			for (int i = 0; i < sorted.length; i++) {
				mean+= sorted[i].getValue();
				sd += sorted[i].getValue()*sorted[i].getValue();
			}
			mean /= sorted.length;
			sd = Math.sqrt(sd/sorted.length - mean*mean);
			//System.err.println("mean="+mean+"\tSD="+sd+"\tsorted.length="+sorted.length);
		
			for (int i = 0; i < sorted.length; i++) {
				normalQuantiles[i]= (sorted[i].getValue()-mean)/sd;
			}
		
			edu.ucla.stat.SOCR.distributions.NormalDistribution ND = 
				new edu.ucla.stat.SOCR.distributions.NormalDistribution(0,1);
			//System.err.println("ND.inverseCDF(0.5)="+ND.inverseCDF(0.5));
			
			for (int i = 0; i < sorted.length; i++) {
				//System.err.println("stdResiduals["+i+"]="+stdResiduals[i]);
				stdResiduals[i]= ND.inverseCDF((1.0*i)/sorted.length);
				//System.err.println("stdResiduals["+i+"]="+stdResiduals[i]);
			}
		}
		else if (disChoice == POISSON){
			mean = 0.0;
			for (int i = 0; i < sorted.length; i++) {
				normalQuantiles[i]= sorted[i].getValue();
				mean+= normalQuantiles[i];
			}
			mean /= sorted.length;

			edu.ucla.stat.SOCR.distributions.PoissonDistribution PD = 
				new edu.ucla.stat.SOCR.distributions.PoissonDistribution(
						(int)sorted[0].getValue(), (int)(mean));
			for (int i = 0; i < sorted.length; i++) {
				if (i==0) stdResiduals[i]= PD.inverseCDF((1.0*i)/sorted.length);
				else stdResiduals[i]= PD.inverseCDF((1.0*i)/sorted.length) - sorted[0].getValue();
			}
		}
		
		sorted = AnalysisUtility.getSortedRedidual(stdResiduals);
		for (int i = 0; i < sorted.length; i++) {
			stdResiduals[i]= sorted[i].getValue()	;
		}

		}catch(NumberFormatException e)
			{	showMessageDialog("Data format error!"); }
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

    public void initMapPanel() {
        listIndex = new int[dataTable.getColumnCount()];
        for(int j=0;j<listIndex.length;j++)
            listIndex[j]=1;
        bPanel = new JPanel(new BorderLayout());
        mapPanel = new JPanel(new GridLayout(2,3,50, 50));
        bPanel.add(mapPanel,BorderLayout.CENTER);

        addButton1.addActionListener(this);
        addButton2.addActionListener(this);
        removeButton1.addActionListener(this);
        removeButton2.addActionListener(this);

        lModelAdded = new DefaultListModel();
        lModelDep = new DefaultListModel();
        lModelIndep = new DefaultListModel();

        int cellWidth = 10;

        listAdded = new JList(lModelAdded);
        listAdded.setSelectedIndex(0);
        listDepRemoved = new JList(lModelDep);
        listIndepRemoved = new JList(lModelIndep);

        paintMappingLists(listIndex);
        listAdded.setFixedCellWidth(cellWidth);
        listDepRemoved.setFixedCellWidth(cellWidth);
        listIndepRemoved.setFixedCellWidth(cellWidth);

        tools1 = new JToolBar(JToolBar.VERTICAL);
        tools2 = new JToolBar(JToolBar.VERTICAL);

        tools1.add(depLabel);
    //    tools2.add(indLabel);

		tools1.setFloatable(false);
		tools2.setFloatable(false);
        tools1.add(addButton1);
        tools1.add(removeButton1);
      //  tools2.add(addButton2);
     //   tools2.add(removeButton2);

		JRadioButton disChoices_normal;
		JRadioButton disChoices_poisson;

		//
		JPanel choicesPanel = new JPanel();
		disChoices_normal = new JRadioButton("Normal");
		disChoices_normal.addActionListener(this);
		disChoices_normal.setActionCommand(NORMAL);
		disChoices_normal.setSelected(true);
		disChoice = NORMAL;

		disChoices_poisson = new JRadioButton("Poisson");
		disChoices_poisson.addActionListener(this);
		disChoices_poisson.setActionCommand(POISSON);

		ButtonGroup group = new ButtonGroup();
		group.add(disChoices_normal);
		group.add(disChoices_poisson);
		choicesPanel.add(new JLabel("Choices of distribution:"));
		choicesPanel.add(disChoices_normal);
		choicesPanel.add(disChoices_poisson);
		choicesPanel.setPreferredSize(new Dimension(200,100));

        mapPanel.add(new JScrollPane(listAdded));
        mapPanel.add(tools1);
        mapPanel.add(new JScrollPane(listDepRemoved));
		//		JPanel emptyPanel = new JPanel(new GridLayout(0,1));
		//   mapPanel.add(emptyPanel);
		mapPanel.add(choicesPanel);
        mapPanel.add(tools2);
        mapPanel.add(new JScrollPane(listIndepRemoved));

    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals(NORMAL)) {
            try {
				disChoice = NORMAL;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else  if (evt.getActionCommand().equals(POISSON)) {
            try {
				disChoice = POISSON;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(evt.getSource() == addButton1) {
    		addButtonDependent();
    	}	
    	else if (evt.getSource() == removeButton1) {
    		removeButtonDependent();
    	}	

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
    	addButtonDependent();//Y
    }
    public void setXLabel(String xLabel){
  		
    	domainLabel = xLabel;
    	TableColumnModel columnModel = dataTable.getColumnModel();    	
    	columnModel.getColumn(0).setHeaderValue(xLabel);
    	dataTable.setTableHeader(new EditableHeader(columnModel));   
    }
    
    public void setYLabel(String yLabel){
    	rangeLabel = yLabel; 		
  		
    }
}

