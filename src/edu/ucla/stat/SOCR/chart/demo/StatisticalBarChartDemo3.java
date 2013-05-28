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
package edu.ucla.stat.SOCR.chart.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.statistics.Statistics;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_Stat_Raw_Vertical;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategoryCellLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create a "statistical" 
 * bar chart using data from a {@link CategoryDataset}.
 */
public class StatisticalBarChartDemo3 extends SuperCategoryChart_Stat_Raw_Vertical implements PropertyChangeListener {


	 
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+" doTest get called!");
		
		 int no_series = dataTable.getRowCount()-1;
		 int no_category = dataTable.getColumnCount()-2;
		 int[][] pairs = new int[no_category][2];
		 for (int i=0; i<no_category; i++){
			 pairs[i][0] = i+1;    //column i store category i
			 pairs[i][1] = 0;    //column 0 stores series name
		 }
		 chart = chartMaker.getCategoryChart("BarStatRaw", chartTitle, "Type", "value", dataTable, no_category, pairs, "");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
	
	
    /**
     * Creates a sample dataset.
     * 
     * @return The dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {
		double mean, stdDev;

		if (isDemo){
		SERIES_COUNT = 3;
      //  CATEGORY_COUNT = 1;
        VALUE_COUNT = 10;
		values_storage = new String[SERIES_COUNT][CATEGORY_COUNT];
   

        DefaultStatisticalCategoryDataset dataset 
            = new DefaultStatisticalCategoryDataset();

        for (int s = 0; s < SERIES_COUNT; s++) {
            for (int c = 0; c < CATEGORY_COUNT; c++) {
                Double[] values = createValueList(0, 20.0, VALUE_COUNT);
				values_storage[s][c]= vs;
				mean = Statistics.calculateMean(values);
				stdDev = Statistics.getStdDev(values);
                dataset.add(mean,stdDev, "Series " + s, "Category " + c);
            }
        }
        return dataset;}
		else return super.createDataset(false);
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  a dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {
        
        // create the chart...
        JFreeChart chart = ChartFactory.createLineChart(
            chartTitle, // chart title
            domainLabel,                         // domain axis label
            rangeLabel,                        // range axis label
            dataset,                        // data
            PlotOrientation.VERTICAL,       // orientation
            !legendPanelOn,                           // include legend
            true,                           // tooltips
            false                           // urls
        );

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        // customise the range axis...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);

        // customise the renderer...
        StatisticalBarRenderer renderer = new StatisticalBarRenderer();
        renderer.setErrorIndicatorPaint(Color.black);
		renderer.setLegendItemLabelGenerator(new SOCRCategoryCellLabelGenerator(dataset, values_storage, SERIES_COUNT, CATEGORY_COUNT));
        plot.setRenderer(renderer);


        // OPTIONAL CUSTOMISATION COMPLETED.
       return chart;
    }
   /* 
    public void initMapPanel() {
    	mapDep = false;   // no series mapping
    	
        listIndex = new int[dataTable.getColumnCount()];
        for(int j=0;j<listIndex.length;j++)
            listIndex[j]=1;
        bPanel = new JPanel(new BorderLayout());
        // topPanel = new JPanel(new FlowLayout());
        //bottomPanel = new JPanel(new FlowLayout());
        mapPanel = new JPanel(new GridLayout(2,3,50, 50));
		//  bPanel.add(new JPanel(),BorderLayout.EAST);
		//   bPanel.add(new JPanel(),BorderLayout.WEST);
		//   bPanel.add(new JPanel(),BorderLayout.SOUTH);
        bPanel.add(mapPanel,BorderLayout.CENTER);
		//   bPanel.add(new JPanel(),BorderLayout.NORTH);

        addButton1.addActionListener(this);
        addButton2.addActionListener(this);
        removeButton1.addActionListener(this);
        removeButton2.addActionListener(this);

        lModel1 = new DefaultListModel();
        lModel2 = new DefaultListModel();
        lModel3 = new DefaultListModel();

        //JLabel depLabel = new JLabel(DEPENDENT);
        //JLabel indLabel = new JLabel(INDEPENDENT);        
		//JLabel varLabel = new JLabel(VARIABLE);

        int cellWidth = 10;


        listAdded = new JList(lModel1);
        listAdded.setSelectedIndex(0);
        listDepRemoved = new JList(lModel2);
        listIndepRemoved = new JList(lModel3);

        paintTable(listIndex);
        listAdded.setFixedCellWidth(cellWidth);
        listDepRemoved.setFixedCellWidth(cellWidth);
        listIndepRemoved.setFixedCellWidth(cellWidth);

        tools1 = new JToolBar(JToolBar.VERTICAL);
        tools2 = new JToolBar(JToolBar.VERTICAL);

       if (mapDep) {
    	   tools1.add(depLabel);
    	   tools1.add(addButton1);
    	   tools1.add(removeButton1);}
  
       if (mapIndep) {
    	   tools2.add(indLabel);
    	   tools2.add(addButton2);
           tools2.add(removeButton2);
       }
		tools1.setFloatable(false);
		tools2.setFloatable(false);
       
      
        JPanel emptyPanel = new JPanel();*/

       /*  topPanel.add(listAdded);
       topPanel.add(addButton);
       topPanel.add(listDepRemoved);
       bottomPanel.add(listIndepRemoved);
       bottomPanel.add(addButton2);
       bottomPanel.add(list4); */

    /*    JRadioButton legendPanelOnSwitch;
        JRadioButton legendPanelOffSwitch;
		//
		JPanel choicesPanel = new JPanel();
		//choicesPanel.setBackground(new Color(100, 200, 100));
		
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
		choicesPanel.setLayout(new BoxLayout(choicesPanel, BoxLayout.Y_AXIS));
		choicesPanel.setAlignmentX(0);
		choicesPanel.add(legendPanelOnSwitch);
		choicesPanel.add(legendPanelOffSwitch);
		choicesPanel.setPreferredSize(new Dimension(200,100));
		
		JComboBox  selectSeries;
		JComboBox  selectCategory;
		selectSeries = new JComboBox();
		selectSeries.setMaximumSize(new Dimension(60,100));		
		for (int i=1; i<=5; i++)
			selectSeries.addItem(String.valueOf(i));
		selectSeries.setSelectedIndex(2); //3
		selectSeries.addActionListener(this);
		
		selectCategory = new JComboBox();
		selectCategory.setMaximumSize(new Dimension(60,100));
		for (int i=1; i<=5; i++)
			selectCategory.addItem(String.valueOf(i));
		selectCategory.setSelectedIndex(1); //2
		selectCategory.addActionListener(this);
		
		JPanel numberChoicesPanel = new JPanel();
		numberChoicesPanel.setPreferredSize(new Dimension(200,100));
		//numberChoicesPanel.setBackground(new Color(100, 100, 100));
		numberChoicesPanel.add(new JLabel(" "));
		numberChoicesPanel.add(new JLabel("Number of Series:"));
		numberChoicesPanel.add(selectSeries);
		//choicesPanel.add(new JLabel("Number of Category:"));
		//choicesPanel.add(selectCategory);
	
		//choicesPanel.add(numberChoicesPanel);
		tools1.add(numberChoicesPanel);
		
        mapPanel.add(new JScrollPane(listAdded));
        mapPanel.add(tools1);
        mapPanel.add(new JScrollPane(listDepRemoved));
       // 
        if (LEGEND_SWITCH)
        	mapPanel.add(choicesPanel);
        else
        	mapPanel.add(emptyPanel);
        mapPanel.add(tools2);
        mapPanel.add(new JScrollPane(listIndepRemoved));*/

   // }
    
  /*  public void actionPerformed(ActionEvent event) {
    	if (event.getActionCommand().equals(LEGENDON)) {       
            	turnLegendPanelOn();          
    	}
    	else if (event.getActionCommand().equals(LEGENDOFF)) {       
    		turnLegendPanelOff();              
    	}
    	else if(event.getSource() == addButton1) {
    		addButtonDependent();
    	}	
    	else if (event.getSource() == removeButton1) {
    		removeButtonDependent();
    	}	
    	else if (event.getSource() == addButton2) {
    		addButtonIndependent();
    	}	
    	else if (event.getSource() == removeButton2) {
    		removeButtonIndependent();
    	}	
    	else if (event.getSource() instanceof JComboBox) {	
    		 JComboBox JCB = (JComboBox) event.getSource();
    		 String JCB_Value = (String) JCB.getSelectedItem();
    		 
    		 	SERIES_COUNT = Integer.parseInt(JCB_Value);
    	}
    		;
     
    }*/
	
    

    /**
     * reset dataTable to default (demo data), and refesh chart
     */
   /*   public void resetExample() {

    	    dataset= createDataset(true);	
    		
    		JFreeChart chart = createChart(dataset);	
    		chartPanel = new ChartPanel(chart, false); 
    		setChart();

            hasExample = true;
    		convertor.valueList2Table_vertical(values_storage, SERIES_COUNT, CATEGORY_COUNT);				
    		JTable tempDataTable = convertor.getTable();
    		//		resetTable();
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
    		//addButtonDependent();
    		int columnCount =dataTable.getColumnCount()-1;
    		for(int i=0; i<columnCount; i++){
    			System.out.println("adding dep="+i);
    			addButtonDependent();
    		}
    		
    		updateStatus(url);
      }*/

      
}

