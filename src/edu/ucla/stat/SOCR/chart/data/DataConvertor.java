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

package edu.ucla.stat.SOCR.chart.data;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

import javax.swing.JTable;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

import edu.ucla.stat.SOCR.chart.ChartType;

/**
 * Convert various JFreeChart Dataset to JTable
 * @author jenny Cui
 *
 */
public class DataConvertor {
	/**
	 * @uml.property  name="example" multiplicity="(0 -1)" dimension="2"
	 */
	private String[][] example = new String[1][1];	// the example data
	/**
	 * @uml.property  name="columnNames" multiplicity="(0 -1)" dimension="1"
	 */
	private String[] columnNames = new String[1];

	/**
	 * @uml.property  name="rowNumber"
	 */
	private int rowNumber;
	/**
	 * @uml.property  name="columnNumber"
	 */
	private int columnNumber;
	/**
	 * @uml.property  name="newln"
	 */
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	/**
	 * @uml.property  name="dataTable"
	 * @uml.associationEnd  readOnly="true" multiplicity="(1 1)"
	 */
	private JTable dataTable;
	/**
	 * @uml.property  name="dOT"
	 */
	private final String DOT = ".";
	
	private static final int NULL_EXAMPLE = 0;
	
	private static final int BOOK_EXAMPLE_SLR = 101;
	protected final String DELIMITERS = ",;\t ";
	
	/**
	 * @uml.property  name="dataset"
	 * @uml.associationEnd  readOnly="true" multiplicity="(1 1)"
	 */
	private AbstractDataset dataset;

	public void dataset2Table(AbstractDataset dataset, int type) {
		if (dataset == null){
			if (type == NULL_EXAMPLE){
				
				example = new String[20][2];
				columnNames = new String[2];

				columnNames[0] = "C1";
				columnNames[1] = "C2";

				example[0][0] = ""; example[0][1] = "";
				example[1][0] = ""; example[1][1] = "";
				example[2][0] = ""; example[2][1] = "";
				example[3][0] = ""; example[3][1] = "";
				example[4][0] = ""; example[4][1] = "";
				example[5][0] = ""; example[5][1] = "";
				example[6][0] = ""; example[6][1] = "";
				example[7][0] = ""; example[7][1] = "";
				example[8][0] = ""; example[8][1] = "";
				example[9][0] = ""; example[9][1] = "";

				example[10][0] = ""; example[10][1] = "";
				example[11][0] = ""; example[11][1] = "";
				example[12][0] = ""; example[12][1] = "";
				example[13][0] = ""; example[13][1] = "";
				example[14][0] = ""; example[14][1] = "";

				example[15][0] = ""; example[15][1] = "";
				example[16][0] = ""; example[16][1] = "";
				example[17][0] = ""; example[17][1] = "";
				example[18][0] = ""; example[18][1] = "";
				example[19][0] = ""; example[19][1] = "";
				dataTable = new JTable(example, columnNames);
				return;	
			}	//null_example
			else if (type == ChartType.PIE_CHART){
				example = new String[6][2];
				columnNames = new String[2];

				columnNames[0]="Name";
				columnNames[1]="Value";

				example[0][0] = "One";    example[0][1] = "43.2";
				example[1][0] = "Two";    example[1][1] = "10.0";
				example[2][0] = "Three";  example[2][1] = "27.5";
				example[3][0] = "Four";   example[3][1] = "17.5";
				example[4][0] = "Five";   example[4][1] = "11.0";
				example[5][0] = "Six";    example[5][1] = "19.4";
				
				dataTable = new JTable(example, columnNames);
				return;
			} // pie
			else if (type == ChartType.BAR_XY_CHART || type == ChartType.BAR_CAT_CHART){
				example = new String[5][2];
				columnNames = new String[2];

				columnNames[0]="Category";
				columnNames[1]="Value";

				example[0][0] = "Category1";    example[0][1] = "4.0";
				example[1][0] = "Category2";    example[1][1] = "3.0";
				example[2][0] = "Category3";  example[2][1] = "-2.0";
				example[3][0] = "Category4";   example[3][1] = "3.0";
				example[4][0] = "Category5";   example[4][1] = "6.0";
				
				dataTable = new JTable(example, columnNames);
				return;
			} // bar
		}
	}

	/**
	 * use the data from a DefaultPieDataset to fill a JTable
	 * @param dataset 
	 */
	public void dataset2Table(DefaultPieDataset dataset){
		double value;	
				rowNumber = dataset.getItemCount();
				columnNumber = 2;
				example = new String[rowNumber][columnNumber];
				columnNames = new String[columnNumber];

				columnNames[0]="Name";
				columnNames[1]="Value";

				for (int i=0; i<rowNumber; i++){
					try{
						example[i][0] = (String)(dataset.getKey(i)); }
					catch(Exception e){
						example[i][0] = "nNaN";
					}
					try{
						value = dataset.getValue(i).doubleValue();
						example[i][1] = Double.toString(value);}
					catch(Exception e){
						example[i][1] = "NaN";
					}
				}
				dataTable = new JTable(example, columnNames);
				return;
	}
	
	public void dataset2Table(DefaultPieDataset dataset, String[] pulloutFlag){
		double value;	
				rowNumber = dataset.getItemCount();
				columnNumber = 3;
				example = new String[rowNumber][columnNumber];
				columnNames = new String[columnNumber];

				columnNames[0]="Name";
				columnNames[1]="Value";
				columnNames[2]="Pullout Flag";

				for (int i=0; i<rowNumber; i++){
					try{
						example[i][0] = (String)(dataset.getKey(i)); }
					catch(Exception e){
						example[i][0] = "nNaN";
					}
					try{
						value = dataset.getValue(i).doubleValue();
						example[i][1] = Double.toString(value);}
					catch(Exception e){
						example[i][1] = "NaN";
					}
					example[i][2] = pulloutFlag[i];
				}
				dataTable = new JTable(example, columnNames);
				return;
	}
	
	

	/**
	 * Use the data from a DefaultStatisticalCategoryDataset to fill a JTable
	 * @param dataset
	 */
	public void dataset2Table(DefaultStatisticalCategoryDataset dataset){

		double mean,stdDev;	
		
		int columnCount = dataset.getColumnCount();
		rowNumber = dataset.getRowCount();

		columnNumber = columnCount+1;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		columnNames[0]="Series";
		for (int i=0; i<columnCount; i++){
			columnNames[i+1]=dataset.getColumnKey(i).toString();
		}

		for (int j=0; j<columnCount; j++){

			for (int i=0; i<rowNumber; i++){
				example[i][0] = dataset.getRowKey(i).toString();
				try{
					mean = dataset.getMeanValue(i,j).doubleValue();
					stdDev = dataset.getStdDevValue(i,j).doubleValue();
					example[i][j+1] =  Double.toString(mean)+" , "+Double.toString(stdDev);}
				catch(Exception e){
					example[i][j+1] = "NaN";
				}
			}
		}
		dataTable = new JTable(example, columnNames);
		return;
	}
	
	/**
	 * Use the data from a DefaultStatisticalCategoryDataset to fill a JTable
	 * separate the (mean, std) into 2 columns
	 * @param dataset
	 */
	public void dataset2TableA(DefaultStatisticalCategoryDataset dataset){

		double mean,stdDev;	
		
		int columnCount = dataset.getColumnCount();
		rowNumber = dataset.getRowCount();

		columnNumber = columnCount*2+1;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		columnNames[0]="Series";
		for (int i=0; i<columnCount; i++){
			columnNames[2*i+1]=dataset.getColumnKey(i).toString()+" Mean";
			columnNames[2*i+2]=dataset.getColumnKey(i).toString()+" StdDev";
		}

		for (int j=0; j<columnCount; j++){

			for (int i=0; i<rowNumber; i++){
				example[i][0] = dataset.getRowKey(i).toString();
				try{
					mean = dataset.getMeanValue(i,j).doubleValue();
					stdDev = dataset.getStdDevValue(i,j).doubleValue();
					example[i][j*2+1] =  Double.toString(mean);
					example[i][j*2+2] = Double.toString(stdDev);}
				catch(Exception e){
					example[i][j*2+1] = "NaN";
					example[i][j*2+2] = "NaN";
				}
			}
		}
		dataTable = new JTable(example, columnNames);
		return;
	}

	/**
	 * Use the data from a CategoryDataset to fill a JTable
	 * @param dataset
	 */
	public void dataset2Table(CategoryDataset dataset){

		double value;	
		
		int columnCount = dataset.getColumnCount();
		rowNumber = dataset.getRowCount();

		columnNumber = columnCount+1;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		columnNames[0]="Series";
		for (int i=0; i<columnCount; i++){
			columnNames[i+1]=dataset.getColumnKey(i).toString();
		}

		for (int j=0; j<columnCount; j++){

			for (int i=0; i<rowNumber; i++){
				example[i][0] = dataset.getRowKey(i).toString();
				try{
					value = dataset.getValue(i,j).doubleValue();
					example[i][j+1] =  Double.toString(value);}
				catch(Exception e){
					example[i][j+1] = "NaN";
				}
			}
		}
		dataTable = new JTable(example, columnNames);
		return;
	}
	
	/**
	 * Use the data from a CategoryDataset to fill a JTable
	 * @param dataset
	 */
	public void dataset2Table_vertical(CategoryDataset dataset){

		double value;	
		
		rowNumber = dataset.getColumnCount();
		int columnCount = dataset.getRowCount();

		columnNumber = columnCount+1;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		columnNames[0]="Label";
		for (int i=0; i<columnCount; i++){
			columnNames[i+1]=dataset.getRowKey(i).toString();
		}

		for (int j=0; j<columnCount; j++){

			for (int i=0; i<rowNumber; i++){
				example[i][0] = dataset.getColumnKey(i).toString();
				try{
					value = dataset.getValue(j,i).doubleValue();
					example[i][j+1] =  Double.toString(value);}
				catch(Exception e){
					example[i][j+1] = "NaN";
				}
			}
		}
		dataTable = new JTable(example, columnNames);
		return;
	}

	/**
 * Use the data in the given 2 dimension array to form a JTable
 * @param values_storage
 * @param serieCount
 * @param catCount
 */
	public void valueList2Table(String[][] values_storage, int serieCount, int catCount){

		int columnCount =catCount;
		rowNumber = serieCount;

		columnNumber = columnCount+1;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		columnNames[0]="Series";
		for (int i=0; i<columnCount; i++){
			columnNames[i+1]="Category "+i;
		}


		for (int j=0; j<columnCount; j++){

			for (int i=0; i<rowNumber; i++){
				example[i][0] = "Series "+i;

			example[i][j+1] = values_storage[i][j];
			}
		}
		dataTable = new JTable(example, columnNames);
		return;
	}
	
	public void valueList2Table_vertical(String[][] values_storage, int serieCount, int catCount){

		int columnCount =catCount*serieCount;
		//System.out.println("columnCount="+columnCount);
		//System.out.println(values_storage[0][0]);
		StringTokenizer st = new StringTokenizer(values_storage[0][0],DELIMITERS);
		rowNumber = st.countTokens();

		columnNumber = columnCount;
		
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
			
	//	System.out.println("serieCount="+serieCount);
	//	System.out.println("catCount="+catCount);
		for (int i=0; i<serieCount; i++){
			for (int j=0; j<catCount; j++){
			//columnNames[i*catCount+j]="Series "+ i+":Category "+j;
			columnNames[i*catCount+j]="Series "+ i;
		//	System.out.println("setting "+(i*catCount+j)+" name="+"Seris "+ i+":Category "+j);
			}
		}

		
		for (int j=0; j<catCount; j++){
			for (int i=0; i<serieCount; i++){
				//example[i][j] = "Series "+i;
				String in = values_storage[i][j];
				//System.out.println("in="+in);
				st = new StringTokenizer(in,DELIMITERS);
				
				for (int k=0; k<rowNumber; k++){
					example[k][i*catCount+j]=st.nextToken();
					//System.out.println("setting["+k+"]["+(i*catCount+j)+"]="+example[k][i*catCount+j]);
				}
			}
		}
	//	System.out.println("here");
		dataTable = new JTable(example, columnNames);
		return;
	}

	/**
	 * For QQ, use data from 2 arrays to fill a JTable
	 * @param raw_x
	 * @param raw_y
	 * @param row_count
	 */
	public void XY2Table(String[] raw_x, String[] raw_y, int row_count){

		int columnCount =2;
		rowNumber = row_count;

		columnNumber = columnCount;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		columnNames[0]="X";
		columnNames[1]="Y";

		for (int j=0; j<columnCount; j++){
			for (int i=0; i<rowNumber; i++){
				try{
					example[i][0] = raw_x[i];
				}catch(Exception e){
					example[i][0] ="";
				}
				
				try{
					example[i][1] = raw_y[i];
				}catch(Exception e){
					example[i][1] ="";
				}	
			}
		}
		dataTable = new JTable(example, columnNames);
		return;
	}


	/**
	 * for PowerTrnsformedchart, use data from 2  array to fill a JTable
	 * @param raw_y
	 * @param row_count
	 */
	public void Power2Table(XYDataset ds){

		rowNumber = ds.getItemCount(1);
		
		columnNumber = 2;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		columnNames[0]=(String)ds.getSeriesKey(0);
		columnNames[1]=(String)ds.getSeriesKey(1);
		
		for (int i=0; i<rowNumber; i++){			
			example[i][0] = Double.toString(ds.getYValue(0,i));
			example[i][1] = Double.toString(ds.getYValue(1,i));
		}
		dataTable = new JTable(example, columnNames);
		return;
		
	}
	/**
	 * for QQ, use data from one array to fill a JTable
	 * @param raw_y
	 * @param row_count
	 */
	public void Y2Table(String[] raw_y, int row_count){

		int columnCount =1;
		rowNumber = row_count;

		columnNumber = columnCount;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		columnNames[0]="Data";

		for (int i=0; i<rowNumber; i++){
				example[i][0] = raw_y[i];
			}
		dataTable = new JTable(example, columnNames);
		return;
		
	}
	
	public void multiY2Table(String[][] raw_y, int row_count, int col_count, String[] colName){

		int columnCount =col_count;
		rowNumber = row_count;

		columnNumber = columnCount;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		for(int i=0; i<columnCount; i++)
			columnNames[i]=colName[i];

		for (int j=0; j<columnCount; j++){
			for (int i=0; i<rowNumber; i++){
				example[i][j] = raw_y[j][i];
				//System.out.println("dataConvertor "+example[i][j]);
			}
		}
			
		dataTable = new JTable(example, columnNames);
		return;
		
	}
	
	/**
	 * for QQPower, use data from one array to fill a JTable
	 * @param raw_y
	 * @param row_count
	 */
	public void data2Table(String[] raw_y, double[] transformed, String colname1, String colname2, int row_count){

		int columnCount =2;
		rowNumber = row_count;

		columnNumber = columnCount;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		columnNames[0]=colname1;
		columnNames[1]=colname2;

		for (int i=0; i<rowNumber; i++){
			example[i][0] = raw_y[i];
			example[i][1] = Double.toString(transformed[i]);
		}
		dataTable = new JTable(example, columnNames);
		return;
		
	}
	
	/**
	 * for ScatterPower, use data from XY array to fill a JTable
	 * @param raw_y
	 * @param row_count
	 */
	public void Y2Table(String[] raw_x, String[] raw_y, double[] transformed_x , double[] transformed_y, int row_count){

		int columnCount =4;
		rowNumber = row_count;

		columnNumber = columnCount;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		columnNames[0]="X";
		columnNames[1]="Y";
		columnNames[2]="TransformedX";
		columnNames[3]="TransformedY";

		for (int i=0; i<rowNumber; i++){
				example[i][0] = raw_x[i];
				example[i][1] = raw_y[i];
				example[i][2] = Double.toString(transformed_x[i]);
				example[i][3] = Double.toString(transformed_y[i]);
			}
		dataTable = new JTable(example, columnNames);
		return;
		
	}
/**
 * use data from a TimeSeriesCollection dataset to fill a JTable
 * @param dataset
 */
	public void dataset2Table(TimeSeriesCollection dataset){

		double value;	
		String serieName;
		TimeSeries ts;	
		int itemCount;
		int serieCount = dataset.getSeriesCount();
		for(int i=0; i<serieCount; i++) 		
			{
				itemCount = dataset.getItemCount(i);
				if (itemCount >rowNumber)
					rowNumber = itemCount;
			}

		columnNumber = 2 * serieCount;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		for (int j=0; j<serieCount; j++){
			ts = dataset.getSeries(j);	
			serieName=dataset.getSeriesKey(j).toString();
			if (serieName.length()>0){
				columnNames[j*2]=serieName+":"+ts.getDomainDescription();
				columnNames[j*2+1]=serieName+":"+ts.getRangeDescription();
			}
			else{
				columnNames[j*2]=ts.getDomainDescription();
				columnNames[j*2+1]=ts.getRangeDescription();
			}
		
			for (int i=0; i<rowNumber; i++){
				try{
					example[i][j*2] =  ts.getTimePeriod(i).toString();
				}
				catch(Exception e){
					example[i][j*2] = "NaN";
				}
				try{
					value = ts.getValue(i).doubleValue();
					example[i][j*2+1] = Double.toString(value);}
				catch(Exception e){
					example[i][j*2+1] = "NaN";
				}
			}	
		}
		dataTable = new JTable(example, columnNames);
		return;
	}
	
	/**
	 * use data from a IntervalXYDataset to fill a JTable
	 * @param dataset
	 */
public void YIntervalDataset2Table(IntervalXYDataset dataset){

	   DecimalFormat f=	new DecimalFormat("#0.00");
		double value,y1,y2;
		int itemCount;
		int serieCount = dataset.getSeriesCount();
		for(int i=0; i<serieCount; i++) 		
			{
				itemCount = dataset.getItemCount(i);
				if (itemCount >rowNumber)
					rowNumber = itemCount;
			}

		columnNumber = 2 * serieCount;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		for (int i=0; i<serieCount; i++){
			String s = dataset.getSeriesKey(i).toString();
			if (s.length()>0){
				columnNames[i*2] = s+":X";
				columnNames[i*2+1] = s+":Y";
			}
			else {
				columnNames[i*2] ="X";
				columnNames[i*2+1] ="Y";
			}
		}

		for (int j=0; j<serieCount; j++)
		for (int i=0; i<rowNumber; i++){
			try{
				value = dataset.getXValue(j,i);
				example[i][j*2] =  Double.toString(value);}
			catch(Exception e){
				example[i][j*2] = "NaN";
			}
			try{
				y1= dataset.getStartYValue(j,i);
				y2= dataset.getEndYValue(j,i);
				example[i][j*2+1] = f.format(y1)+" , "+f.format(y2);}
			catch(Exception e){
				example[i][j*2+1] = "NaN";
			}
		}
		dataTable = new JTable(example, columnNames);
		return;
	}
public void YIntervalDataset2TableA(IntervalXYDataset dataset){

	   DecimalFormat f=	new DecimalFormat("#0.00");
		double value,y1,y2;
		int itemCount;
		int serieCount = dataset.getSeriesCount();
		for(int i=0; i<serieCount; i++) 		
			{
				itemCount = dataset.getItemCount(i);
				if (itemCount >rowNumber)
					rowNumber = itemCount;
			}

		columnNumber = 3* serieCount;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		for (int i=0; i<serieCount; i++){
			String s = dataset.getSeriesKey(i).toString();
			if (s.length()>0){
				columnNames[i*3] = s+":X";
				columnNames[i*3+1] = s+":Y lower";
				columnNames[i*3+2] = s+":Y upper";
			}
			else {
				columnNames[i*3] ="X";
				columnNames[i*3+1] ="Y lower";
				columnNames[i*3+2] ="Y upper";
			}
		}

		for (int j=0; j<serieCount; j++)
		for (int i=0; i<rowNumber; i++){
			try{
				value = dataset.getXValue(j,i);
				example[i][j*3] =  Double.toString(value);}
			catch(Exception e){
				example[i][j*3] = "NaN";
			}
			try{
				y1= dataset.getStartYValue(j,i);
				y2= dataset.getEndYValue(j,i);
				example[i][j*3+1] = f.format(y1);
				example[i][j*3+2] = f.format(y2);
				}
			catch(Exception e){
				example[i][j*3+1] = "NaN";
				example[i][j*3+2] = "NaN";
			}
		}
		dataTable = new JTable(example, columnNames);
		return;
	}
/**
 * use data from a IntervalXYDataset to fill a JTable
 * @param dataset
 */
public void dataset2Table(IntervalXYDataset dataset){

		double value;
		int itemCount;
		int serieCount = dataset.getSeriesCount();
		for(int i=0; i<serieCount; i++) 	   
			{
				itemCount = dataset.getItemCount(i);
				if (itemCount >rowNumber)
					rowNumber = itemCount;
			}

		columnNumber = 2 * serieCount;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		for (int i=0; i<serieCount; i++){
			columnNames[i*2]=dataset.getSeriesKey(i).toString();
			columnNames[i*2+1]="Y"+i;
		}

		for (int j=0; j<serieCount; j++)
		for (int i=0; i<rowNumber; i++){
			try{
				value = dataset.getXValue(j,i);
				example[i][j*2] =  Double.toString(value);}
			catch(Exception e){
				example[i][j*2] = "NaN";
			}
			try{
				value= dataset.getYValue(j,i);
				example[i][j*2+1] = Double.toString(value);}
			catch(Exception e){
				example[i][j*2+1] = "NaN";
			}
		}
		dataTable = new JTable(example, columnNames);
		return;
	}
	
/**
 * use data from a XYDataset to fill a JTable
 * @param dataset
 */
public void dataset2Table(XYDataset dataset){

		double value;
		int itemCount;
		int serieCount = dataset.getSeriesCount();
		for(int i=0; i<serieCount; i++) 		
			{
				itemCount = dataset.getItemCount(i);
				if (itemCount >rowNumber)
					rowNumber = itemCount;
			}

		columnNumber = 2 * serieCount;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		for (int i=0; i<serieCount; i++){
			String s = dataset.getSeriesKey(i).toString();
			if (s.length()>0){
				columnNames[i*2]=s+":X";
				columnNames[i*2+1]=s+":Y";
			}
			else {
				columnNames[i*2]="X";
				columnNames[i*2+1]="Y";
			}
		}

		for (int j=0; j<serieCount; j++)
		for (int i=0; i<rowNumber; i++){
			try{
				value = dataset.getXValue(j,i);
				example[i][j*2] =  Double.toString(value);}
			catch(Exception e){
				example[i][j*2] = "NaN";
			}
			try{
				value= dataset.getYValue(j,i);
				example[i][j*2+1] = Double.toString(value);}
			catch(Exception e){
				example[i][j*2+1] = "NaN";
			}
		}
		dataTable = new JTable(example, columnNames);
		return;
	}

/**
 * use data from a XYZDataset to fill a JTable
 * @param dataset
 */
public void dataset2Table(XYZDataset dataset){

		double value;
		int itemCount;
		int serieCount = dataset.getSeriesCount();
		for(int i=0; i<serieCount; i++) 		
			{
				itemCount = dataset.getItemCount(i);
				if (itemCount >rowNumber)
					rowNumber = itemCount;
			}

		columnNumber = 3 * serieCount;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		for (int i=0; i<serieCount; i++){
			String s = dataset.getSeriesKey(i).toString();
			if (s.length()>0){
				columnNames[i*3] = s+":X";
				columnNames[i*3+1] = s+":Y";
				columnNames[i*3+2] = s+":Z";
			}
			else {
				columnNames[i*3] = "X";
				columnNames[i*3+1] = "Y";
				columnNames[i*3+2] = "Z";
			}
		}

		// JFreeChart XYZDataset data order is y,x,z, so we switch the first 2 here
		for (int j=0; j<serieCount; j++)
		for (int i=0; i<rowNumber; i++){
			try{
				value = dataset.getYValue(j,i);
				example[i][j*3+1] =  Double.toString(value);}
			catch(Exception e){
				example[i][j*3+1] = "NaN";
			}
			try{
				value= dataset.getXValue(j,i);
				example[i][j*3] = Double.toString(value);}
			catch(Exception e){
				example[i][j*3] = "NaN";
			}
			try{
				value= dataset.getZValue(j,i);
				example[i][j*3+2] = Double.toString(value);}
			catch(Exception e){
				example[i][j*3+2] = "NaN";
			}
		}
		dataTable = new JTable(example, columnNames);
		return;
	}

/**
 * use data from a ValueDataset to fill a JTable
 * @param dataset
 */
public void dataset2Table(ValueDataset dataset){

		double value= dataset.getValue().doubleValue();

		columnNumber = 1;
		rowNumber =1;
		example = new String[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		
		columnNames[0]="Value";


		for (int j=0; j<columnNumber; j++)
		for (int i=0; i<rowNumber; i++){
			  example[i][j] = Double.toString(value);
		}
		dataTable = new JTable(example, columnNames);
		return;
	}

	/**
	 * 
	 * @param mean
	 * @param stdDev
	 */
	public void normalDataset2Table(double mean, double stdDev){

		columnNames = new String[2];
		example = new String[1][2];
	
		columnNames[0]= "mean";
		columnNames[1]= "stdDev";
		example[0][0] = Double.toString(mean);
		example[0][1] = Double.toString(stdDev);
	
		dataTable = new JTable(example, columnNames);
		return;
	}

	/** returns a JTable object containing the Example Data */
	
	public JTable getTable() {
		return dataTable;
	 }

    public AbstractDataset getDataset(){
		return dataset;
	}
}
