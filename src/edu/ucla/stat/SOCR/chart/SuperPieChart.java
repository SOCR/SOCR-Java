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
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.NumberFormatException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;

import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;


/**
 * A simple demonstration application showing how to create a pie chart using 
 * data from a {@link DefaultPieDataset}.
 */
public class SuperPieChart extends Chart implements PropertyChangeListener {
		
	protected String[] pulloutFlag;
	protected boolean ThreeDPie = false;
	protected boolean rotateOn = false;
	protected boolean mapPullout= false;
	protected DefaultPieDataset dataset;
	
	protected DefaultListModel lModelPullout;
	protected JList listPulloutRemoved;
	protected int pulloutIndex = -1;
	protected int pulloutLength = 0;
	protected ArrayList<Integer> pulloutList = null;
	protected int pulloutListCursor = 0;	
	int pulloutVarLength;
	 
	protected JButton addButton3 = new JButton(ADD);
	protected JButton removeButton3  = new JButton(REMOVE);
	protected final String ROTATEON	= "ROTATEON";
	protected final String ROTATEOFF	= "ROTATEOFF";
	protected JLabel pulloutLabel = new JLabel("Pullout Flag");   
	protected JToolBar tools3;
	protected  int pulloutMax;
	
	public void init(){

		depLabel = new JLabel("Name");
		indLabel = new JLabel("Value");
		pulloutList = new ArrayList<Integer>();
		
		if(!ThreeDPie)
			mapPullout = true;
		super.init();
		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		pulloutMax = 1;
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
	 showMessageDialog("Piechart doTest get called!");
	
	 int[][] pairs = new int[1][2];
	 pairs[0][0] = 1;   // value
	 pairs[0][1] = 0;   // name
	 chart = chartMaker.getPieChart("Pie Chart", dataTable, pairs,"");	
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
		if (dependentIndex < 0 || independentIndex < 0 || independentLength == 0) {
			showMessageDialog(VARIABLE_MISSING_MESSAGE);
			resetChart();
			return;
			}

		isDemo = false;
		dataset = createDataset(isDemo );	 // not a demo, so get data from the table
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, isDemo ); 
		
		setChart();
		//updateStatus("Chart has been updated, click GRAPH to view it.");
	}

	public ChartPanel getChartPanel(){
		return this.chartPanel;
	}

    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    
    protected DefaultPieDataset createDataset(boolean isDemo) { 
    	
		if (isDemo)
			{
				updateStatus("isDemo==true in "+this.getClass().getName()+" class! return null Dataset, check the code!");
			return null; }
		else{

			try{
			
				setArrayFromTable();
				
			//	System.out.println("SuperPieChart createDataset xyLength="+xyLength);
				
				if (!ThreeDPie){
					setPulloutFromTable();
				}
	
				double[][] x= new double[xyLength][independentVarLength];  //section value
				String[][] y= new String[xyLength][dependentVarLength];   //section name
	
				for (int index=0; index<independentVarLength; index++)
					for (int i = 0; i < xyLength; i++){ 
						//System.out.println("indepValues["+i+"]["+index+"]=:"+indepValues[i][index]+":");
						if (indepValues[i][index]==null || indepValues[i][index]=="null" || indepValues[i][index].length() ==0||indepValues[i][index]=="NaN")
							x[i][index]=0.0;
						else x[i][index] = Double.parseDouble(indepValues[i][index]);
					}
			   
				for (int index=0; index<dependentVarLength; index++)
					for (int i = 0; i < xyLength; i++) 
						y[i][index] = depValues[i][index];
	
			
	            // create the dataset... 
				dataset = new DefaultPieDataset(); 
				for (int i=0; i<xyLength; i++){
					//System.out.println("SuperPieChart adding:"+y[i][0]+","+x[i][0]);
					if(y[i][0]!=null && y[i][0].length()!=0)
						dataset.setValue(y[i][0], x[i][0]);		
				}
	
				return dataset; 
			}catch(NumberFormatException e)
				{
					showMessageDialog("Data format error!");
					return null;}
		}
      } 

    public void setChart(){
    	// update graph
    //	System.out.println("setChart called");
	
    	graphPanel.removeAll();
    	graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

    	//chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

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
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a chart.
     */
    protected JFreeChart createChart(PieDataset dataset) {
        
		JFreeChart chart = ChartFactory.createPieChart(
            chartTitle,  // chart title
            dataset,             // data
            !legendPanelOn,                // include legend
            true,
            false
        );
        TextTitle title = chart.getTitle();
        title.setToolTipText("A title tooltip!");
       
        
        PiePlot plot = (PiePlot) chart.getPlot();
        if (!ThreeDPie){
        	for (int i=0; i<pulloutFlag.length; i++){
        	//System.out.println("SuperPieChart\""+pulloutFlag[i]+"\"");
        		if (pulloutFlag[i].equals("1")){
            		Comparable key = dataset.getKey(i);
            		plot.setExplodePercent(key, 0.30);
            	}
        	}
        }
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
		
        return chart;
}
    
    protected JFreeChart createLegend(PieDataset dataset) {
        
        JFreeChart chart = ChartFactory.createPieChart(
            chartTitle,             // chart title
            dataset,                  // data
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        return chart;
        
    }
    
    protected JFreeChart createLegendChart(JFreeChart origchart) {
       	
       	JFreeChart legendChart = new JFreeChart("", null, new HiddenPlot(),false);
       	
       	legendChart.setBackgroundPaint(Color.white);
           PiePlot plot = (PiePlot)origchart.getPlot();

          /* LegendTitle legendTitle = new LegendTitle(plot, 
                   new ColumnArrangement(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, 0), 
                   new ColumnArrangement(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, 0)); */
           
           LegendTitle legendTitle = new LegendTitle(plot);
           
           legendChart.addLegend(legendTitle); 
           
           return legendChart;
           
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

		System.out.println("From RegCorrAnal:: propertyName =" +propertyName +"!!!");

		if(propertyName.equals("DataUpdate")) {
			//update the local version of the dataTable by outside source
			dataTable = (JTable)(e.getNewValue());
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));
			dataTable.doLayout();

			System.out.println("From RegCorrAnal:: data UPDATED!!!");
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
	    dataset= createDataset(true);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();

        hasExample = true;
		if (!ThreeDPie)
			convertor.dataset2Table(dataset,pulloutFlag);	
		else 
			convertor.dataset2Table(dataset);	

		JTable tempDataTable = convertor.getTable();
		// resetTable();
		resetTableRows(tempDataTable.getRowCount()+1);
		resetTableColumns(tempDataTable.getColumnCount()+1);
		
        for(int i=0;i<tempDataTable.getColumnCount();i++) {
            columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
			//  System.out.println("updateExample tempDataTable["+i+"] = " +tempDataTable.getColumnName(i));
            }
	
		columnModel = dataTable.getColumnModel();
		dataTable.setTableHeader(new EditableHeader(columnModel));

        for(int i=0;i<tempDataTable.getRowCount();i++)
            for(int j=0;j<tempDataTable.getColumnCount();j++) {
                dataTable.setValueAt(tempDataTable.getValueAt(i,j),i,j);
				//System.out.println("setting dataTable :"+tempDataTable.getValueAt(i,j));
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
      
      TableColumnModel columnModel = dataTable.getColumnModel();  
  	columnModel.getColumn(0).setHeaderValue("Name");
  	columnModel.getColumn(1).setHeaderValue("Value");
  	columnModel.getColumn(2).setHeaderValue("pullout flag");
      // this will update the mapping panel     
      resetTableColumns(dataTable.getColumnCount());
  }
  
  public void setMapping(){
	  addButtonDependent();
	  addButtonIndependent();
	  if(mapPullout)
		  addButtonPullout();
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
    	columnModel.getColumn(1).setHeaderValue(yLabel);
    	columnModel.getColumn(2).setHeaderValue("pullout flag");
  		dataTable.setTableHeader(new EditableHeader(columnModel));
    }
    public void initMapPanel() {
        listIndex = new int[dataTable.getColumnCount()];
        for(int j=0;j<listIndex.length;j++)
            listIndex[j]=1;
        bPanel = new JPanel(new BorderLayout());
        mapPanel = new JPanel(new GridLayout(2,5,50, 50));	
        bPanel.add(mapPanel,BorderLayout.CENTER);
		//   bPanel.add(new JPanel(),BorderLayout.NORTH);

        addButton1.addActionListener(this);
        addButton2.addActionListener(this);
        addButton3.addActionListener(this);
        removeButton1.addActionListener(this);
        removeButton2.addActionListener(this);
        removeButton3.addActionListener(this);

        lModelAdded = new DefaultListModel();
        lModelDep = new DefaultListModel();
        lModelIndep = new DefaultListModel();
        lModelPullout = new DefaultListModel();

        int cellWidth = 10;


        listAdded = new JList(lModelAdded);
        listAdded.setSelectedIndex(0);
        listDepRemoved = new JList(lModelDep);
        listIndepRemoved = new JList(lModelIndep);
        listPulloutRemoved = new JList(lModelPullout);

        paintTable(listIndex);
        listAdded.setFixedCellWidth(cellWidth);
        listDepRemoved.setFixedCellWidth(cellWidth);
        listIndepRemoved.setFixedCellWidth(cellWidth);
        listPulloutRemoved.setFixedCellWidth(cellWidth);

        tools1 = new JToolBar(JToolBar.VERTICAL);
        tools2 = new JToolBar(JToolBar.VERTICAL);
        tools3 = new JToolBar(JToolBar.VERTICAL);

       if (mapDep) {
    	   tools1.add(depLabel);
    	   tools1.add(addButton1);
    	   tools1.add(removeButton1);}
       if (mapIndep) {
    	   tools2.add(indLabel);
    	   tools2.add(addButton2);
           tools2.add(removeButton2);
       }
       if (mapPullout) {
    	   tools3.add(pulloutLabel);
    	   tools3.add(addButton3);
           tools3.add(removeButton3);
       }
		tools1.setFloatable(false);
		tools2.setFloatable(false);
		tools3.setFloatable(false);
       
       /*  topPanel.add(listAdded);
       topPanel.add(addButton);
       topPanel.add(listDepRemoved);
       bottomPanel.add(listIndepRemoved);
       bottomPanel.add(addButton2);
       bottomPanel.add(list4); */

        JRadioButton legendPanelOnSwitch;
        JRadioButton legendPanelOffSwitch;
		//
		JPanel choicesPanel = new JPanel();
		choicesPanel.setLayout(new BoxLayout(choicesPanel, BoxLayout.Y_AXIS));
		legendPanelOnSwitch = new JRadioButton("On");
		legendPanelOnSwitch.addActionListener(this);
		legendPanelOnSwitch.setActionCommand(LEGENDON);
		legendPanelOnSwitch.setSelected(false);
		legendPanelOn = false;	

		legendPanelOffSwitch = new JRadioButton("Off");
		legendPanelOffSwitch.addActionListener(this);
		legendPanelOffSwitch.setActionCommand(LEGENDOFF);
		legendPanelOffSwitch.setSelected(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(legendPanelOnSwitch);
		group.add(legendPanelOffSwitch);
		choicesPanel.add(new JLabel("Turn the legend panel:"));
		choicesPanel.add(legendPanelOnSwitch);
		choicesPanel.add(legendPanelOffSwitch);
		choicesPanel.setPreferredSize(new Dimension(200,100));
		
		JRadioButton rotateOnSwitch;
		JRadioButton rotateOffSwitch;
			//
		JPanel rotateChoicesPanel = new JPanel();
		rotateChoicesPanel.setLayout(new BoxLayout(rotateChoicesPanel, BoxLayout.Y_AXIS));
		rotateOnSwitch = new JRadioButton("On");
		rotateOnSwitch.addActionListener(this);
		rotateOnSwitch.setActionCommand(ROTATEON);
		rotateOnSwitch.setSelected(false);
		rotateOn = false;	

		rotateOffSwitch = new JRadioButton("Off");
		rotateOffSwitch.addActionListener(this);
		rotateOffSwitch.setActionCommand(ROTATEOFF);
		rotateOffSwitch.setSelected(true);
			
		ButtonGroup group2 = new ButtonGroup();
		group2.add(rotateOnSwitch);
		group2.add(rotateOffSwitch);
		choicesPanel.add(new JLabel("Turn the rotator:"));
		choicesPanel.add(rotateOnSwitch);
		choicesPanel.add(rotateOffSwitch);
		choicesPanel.setPreferredSize(new Dimension(200,100));

      
        JPanel emptyPanel = new JPanel();
        JPanel emptyPanel2 = new JPanel();
        JPanel emptyPanel3 = new JPanel();
        
        //2X5
        //first line
        mapPanel.add(new JScrollPane(listAdded));
        mapPanel.add(tools1);      
        mapPanel.add(new JScrollPane(listDepRemoved));
	        	
        mapPanel.add(tools3); 
        if (mapPullout)
        	mapPanel.add(new JScrollPane(listPulloutRemoved));
        else  mapPanel.add(emptyPanel);
        //second line--    	    	 
        mapPanel.add(choicesPanel);  
        mapPanel.add(tools2);       
        mapPanel.add(new JScrollPane(listIndepRemoved));
	              	
        mapPanel.add(emptyPanel2);
        mapPanel.add(emptyPanel3);       
    }
    
    public void turnRotateOff(){
    	rotateOn = false;
		if (graphPanel!=null) 
			doChart();
	}
    public void turnRotateOn(){
		rotateOn = true;
		if (graphPanel!=null) 
			doChart();
	}
    
    public void paintTable(int[] lstInd) {
        lModelAdded.clear();
        lModelDep.clear();
        lModelIndep.clear();
        lModelPullout.clear();
        
        for(int i=0;i<lstInd.length;i++) {

            switch(lstInd[i]) {
                case 0:
                    break;
                case 1:
                    lModelAdded.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listAdded.setSelectedIndex(0);

                    break;
                case 2:
                    lModelDep.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listDepRemoved.setSelectedIndex(0);
                    break;
                case 3:
                    lModelIndep.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listIndepRemoved.setSelectedIndex(0);

                    break;
                case 4:
                    lModelPullout.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listPulloutRemoved.setSelectedIndex(0);

                    break;
                default:
                    break;

            }
			//String temp = columnModel.getColumn(i).getHeaderValue().toString().trim();

        }
        listAdded.setSelectedIndex(0);
    }
    
    protected void addButtonPullout() {

        int ct1=-1;
        int sIdx = listAdded.getSelectedIndex(); 
        int idx3 = lModelPullout.getSize();

		//System.out.println("idx3="+idx3+" sIdx="+sIdx+" listIndex.length="+listIndex.length);

        if(sIdx >-1 && idx3 <pulloutMax) {
            for(int i=0;i<listIndex.length;i++) {
                if(listIndex[i] ==1)
                    ct1++;
                if(ct1==sIdx) {
					pulloutIndex=i;
					pulloutList.add(pulloutListCursor, new Integer(i));
                    break;
                }
            }
		   	listIndex[pulloutIndex]=4;
            //System.out.println("addButtonIndependent listIndex = " + listIndex);
			pulloutLength++;
            paintTable(listIndex);
        }
		else 	updateStatus("pulloutMax="+pulloutMax+ " can't add more pullout");
		//			System.out.println("Finish addButtonIndependent, independentIndex = " + independentIndex+"\n");
    }

protected void removeButtonPullout() {
	if (pulloutLength>0) 
		pulloutLength--;
	
	int ct1=-1;
	int idx1 = 0;
	int sIdx = listPulloutRemoved.getSelectedIndex();
	//JOptionPane.showMessageDialog(this, "sIdx = " + sIdx + " idx1 = " + idx1 + " listIndex.length = "+listIndex.length);
	if(sIdx >-1) {
		for(int i=0;i<listIndex.length;i++) {
			if(listIndex[i] ==4)
			ct1++;
			if(ct1==sIdx) {
				idx1=i;
				break;
			}
		}
		listIndex[idx1]=1;
		paintTable(listIndex);
	}
}

    public void actionPerformed(ActionEvent event) {
    	if (event.getSource() == addButton3) {
    		addButtonPullout();
    	}else if (event.getSource() == removeButton3) {
    		removeButtonPullout();
    	}
    	else if (event.getActionCommand().equals(ROTATEON)) {       
            	turnRotateOn();
    	}else if (event.getActionCommand().equals(ROTATEOFF)) {       
    		turnRotateOff();
    	}
    	 super.actionPerformed(event);
     
    }
    
    protected void removeButtonPulloutAll() {
		pulloutLength = 0;
		pulloutIndex = -1;
		/*int ct1=-1;
		int idx1 = 0;
		int sIdx = listIndepRemoved.getSelectedIndex();*/
		for (int i = 0; i < pulloutMax; i++) {
			try {
				removeButtonPullout();
			} catch (Exception e) {
			}			
		}
		
		paintTable(listIndex);
    }

    
    //set pulloutValues, depValues, indepValues 
    protected void setPulloutFromTable(){
		pulloutVarLength = lModelPullout.getSize();
		pulloutFlag = new String[xyLength];
		//pullVar store the column index for pullout
		int pulloutColIndex = 0;	
		
	
		for (int i = 0; i < listIndex.length; i++) {				
			if (listIndex[i]==4)
				pulloutColIndex = i;
		}

		int yLength = 0;
		//resultPanelTextArea.append("\nRESULT:\n" );

		String cellValue = null;
		ArrayList<String> yList = new ArrayList<String>();

		try {
			yLength = dataTable.getRowCount();		
		
			yList = new ArrayList<String>();
			int yyLength =  0;
			for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
						cellValue = ((String)dataTable.getValueAt(k,pulloutColIndex)).trim();					
						yList.add(yyLength , cellValue);
						yyLength ++;
					} catch (Exception e) { // do nothing?					
					}
				}
				
				for (int i = 0; i < yyLength; i++) {
					pulloutFlag[i] = (String)yList.get(i);  //can be null, handle it later
					//System.out.println("depValues["+i+"]["+index2+"]="+depValues[i][index2]);
				}

		}catch (Exception e) {
			showError("Exception In outer catch: " + e );
		}
		
}
    public void resetMappingList() {
		//System.out.println("Chart: resetMappingList get Called");
		dependentIndex = -1;
		pulloutIndex = -1;
		independentIndex = -1;
	    independentList.clear();
	    pulloutList.clear();
		dependentList.clear();
		removeButtonDependentAll();
		removeButtonIndependentAll();
		removeButtonPulloutAll();
	}
    
    public boolean isPullout(int i){
    	if (pulloutFlag[i]!=null && !pulloutFlag[i].equals("null") && !pulloutFlag[i].equals("0") && pulloutFlag[i].length()!=0)
    		return true;
    	else return false;
    }
}
