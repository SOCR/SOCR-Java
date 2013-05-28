
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.ApplicationFrame;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;



	public class SOCRChartAPIDemo extends ApplicationFrame {

	    /**

	     * Creates a new demo.

	     */

	    public SOCRChartAPIDemo(String title) {
	    		super(title);
	     	 JTable dTable = createDataTable();
	         JPanel chartPanel = createDemoPanel(title, dTable);
	        // chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	         setContentPane(chartPanel);

	    }

	   private JPanel createDemoPanel(String title, JTable dTable) {

	        JFreeChart chart = createChart(title, dTable);
	        return new ChartPanel(chart);
	    }

	    
	    private JFreeChart createChart(String title, JTable dataTable){
	    	     JFreeChart chart;
			 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
			
			 int no_series = dataTable.getRowCount()-1;
			 int no_category = dataTable.getColumnCount()-2;
			 int[][] pairs = new int[no_category][2];
			 for (int i=0; i<no_category; i++){
				 pairs[i][0] = i+1;    //column i store category i
				 pairs[i][1] = 0;    //column 0 stores series name
			 }
			 chart = chartMaker.getCategoryChart("Bar", title, "Category", "value", dataTable, no_category, pairs, "3D");	
			 
			 return chart;
	    }

	    private JTable createDataTable(){
	    	JTable dTable;
	    	String[] columnNames = new String[6];
	    	String[][] example = new String[3][6];
	    	
	    	columnNames[0]="Series";
	    	columnNames[1]= "Category 1"; 	
	    	columnNames[2]= "Category 2"; 
	    	columnNames[3]= "Category 3"; 	
	    	columnNames[4]= "Category 4"; 	
	    	columnNames[5]= "Category 5";
	    	
	    
	    	example[0][0]="First";example[0][1]="1.0";example[0][2]="4.0";example[0][3]="3.0";example[0][4]="5.0";example[0][5]="5.0";
	    	example[1][0]="Second";example[1][1]="5.0";example[1][2]="7.0";example[1][3]="6.0";example[1][4]="8.0";example[1][5]="4.0";
	    	example[2][0]="Third";example[2][1]="4.0";example[2][2]="3.0";example[2][3]="2.0";example[2][4]="3.0";example[2][5]="6.0";
	    	
	    	dTable = new JTable(example, columnNames);
	    return dTable;
	    }
	    /**

	     * Starting point for the demonstration application.

	     *

	     * @param args  ignored.

	     */

	    public static void main(String[] args) {

	    	 final SOCRChartAPIDemo demo = new SOCRChartAPIDemo("3D Bar Chart Demo 1");
	        demo.pack();
	        RefineryUtilities.centerFrameOnScreen(demo);
	        demo.setVisible(true);



	    }



	}



