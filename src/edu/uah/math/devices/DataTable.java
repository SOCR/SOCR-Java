/*
Copyright (C) 2001-2004  Kyle Siegrist, Dawn Duehring
Department of Mathematical Sciences
University of Alabama in Huntsville

This program is part of Virtual Laboratories in Probability and Statistics,
http://www.math.uah.edu/stat/.

This program is licensed under a Creative Commons License. Basically, you are free to copy,
distribute, and modify this program, and to make commercial use of the program.
However you must give proper attribution.
See http://creativecommons.org/licenses/by/2.0/ for more information.
*/
package edu.uah.math.devices;
import java.awt.Font;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.io.Serializable;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import edu.uah.math.distributions.IntervalData;
import edu.uah.math.distributions.Domain;

/**
* This class models a simple data table.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class DataTable extends JScrollPane implements Serializable{
	//Constants
	public final static int NONE = 0, MSD = 1, IMSD = 2, BOX = 3, MAD = 4, MMM = 5;
	public final static int FREQ = 0, REL_FREQ = 1, DENSITY = 2;
	//Variables
	private int statisticsType = MSD, distributionType = FREQ;
	//Objects
	private DecimalFormat decimalFormat = new DecimalFormat();
	private JTable table;
	private DataModel dataModel;
	private IntervalData data;

	/**
	* This general constructor creates a new data table with a specified interval data set.
	* @param d the interval data set
	*/
	public DataTable(IntervalData d){
		data = d;
		dataModel = new DataModel();
		table = new JTable(dataModel);
		table.setPreferredScrollableViewportSize(new Dimension(50, 50));
		setViewportView(table);
		setToolTipText();
	}

	/**
	* This default constructor creates a new data table with a default interval data set
	* on (0, 1).
	*/
	public DataTable(){
		this(new IntervalData());
	}

	/**
	* This method sets the tool tip text, for the table and the scrollpane, to the name
	* of the data variable.
	*/
	private void setToolTipText(){
		String tip = data.getName() + " distribution";
		setToolTipText(tip);
		table.setToolTipText(tip);
	}

	/**
	* This method sets the interval data set.
	* @param d the interval data set.
	*/
	public void setData(IntervalData d){
		data = d;
		reset();
	}

	/**
	* This method returns the interval data set.
	* @return the data set
	*/
	public IntervalData getData(){
		return data;
	}

	/**
	* This method resets the table. The name and structure of the data set
	* may have changed.
	*/
	public void reset(){
		setToolTipText();
		dataModel.fireTableStructureChanged();
	}

	/**
	* This method specifies what moments to display (1 for mean and standard deviation,
	* 2 for interval mean and standard deviation, 3 for the five number summary,
	* 4 for the median and mean absolute deviation).
	* @param i the statistics type index.
	*/
	public void setStatisticsType(int i){
		statisticsType = i;
		dataModel.fireTableStructureChanged();
	}

	/**
	* This method returns the statistics type. This variable specifies the summary
	* statistics to be displayed.
	* @return the statistics type index
	*/
	public int getStatisticsType(){
		return statisticsType;
	}

	/**
	* This method sets the distribution type (0: frequency, 1: relative frequency, or 2: density).
	* @param i the type of distribution
	*/
	public void setDistributionType(int i){
		distributionType = i;
		dataModel.fireTableDataChanged();
	}

	/**
	* This method returns the distribution type (0: frequency, 1: relative frequency, or 2: density).
	* @return the distribution type
	*/
	public int getDistributionType(){
		return distributionType;
	}

	/**
	* This internal class defines the underlying data model.
	*/
	private class DataModel extends AbstractTableModel{
		/**
		* This method returns the name of a specified column. Column 0 has the name of
		* the data set, and column 1 has the name &quot; data &quot;.
		*/
		public String getColumnName(int i){
			if (i == 0) return data.getName();
			else return "Data";
		}

		/**
		* This method returns the number of columns, which is always 2.
		* @return the column count
		*/
		public int getColumnCount(){
			return 2;
		}

		/**
		* This method returns the class type of a specified column.
		* @param i the column index
		* @return the class type of the column
		*/
		public Class getColumnClass(int i){
			return getValueAt(0, i).getClass();
		}

		/**
		* This method returns the number of rows, which is the size of the data set.
		* @return the row count
		*/
		public int getRowCount(){
			int n = data.getDomain().getSize();
			if (statisticsType == MSD) return n + 2;
			else if (statisticsType == IMSD) return n + 2;
			else if (statisticsType == BOX) return n + 5;
			else if (statisticsType == MAD) return n + 2;
			else if (statisticsType == MMM) return n + 3;
			else return n;
		}

		/**
		* This method returns the object in a specified cell.
		* @param i the row number
		* @param j the column number
		* @return the object in row i and column j
		*/
		public Object getValueAt(int i, int j){
			String s = "";
			Domain domain = data.getDomain();
			int n = domain.getSize(), m = data.getSize();
			if (i < n){
				if (j == 0) s = decimalFormat.format(domain.getValue(i));
				else if (j == 1 && m > 0){
					double x = domain.getValue(i);
					if (distributionType == FREQ) s = decimalFormat.format(data.getFreq(x));
					else if (distributionType == REL_FREQ) s = decimalFormat.format(data.getRelFreq(x));
					else s = decimalFormat.format(data.getDensity(x));
				}
			}
			if (statisticsType == MSD){
				if (i == n){
					if (j == 0) s = "Mean";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getMean());
				}
				else if (i == n + 1){
					if (j == 0) s = "SD";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getSD());
				}
			}
			else if (statisticsType == IMSD){
				if (i == n){
					if (j == 0) s = "Mean";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getIntervalMean());
				}
				else if (i == n + 1){
					if (j == 0) s = "SD";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getIntervalSD());
				}
			}
			else if (statisticsType == BOX){
				if (i == n){
					if (j == 0) s = "Min";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getMinValue());
				}
				else if (i == n + 1){
					if (j == 0) s = "Q1";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getQuartile(1));
				}
				else if (i == n + 2){
					if (j == 0) s = "Median";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getMedian());
				}
				else if (i == n + 3){
					if (j == 0) s = "Q3";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getQuartile(3));
				}
				else if (i == n + 4){
					if (j == 0) s = "Max";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getMaxValue());
				}
			}
			else if (statisticsType == MAD){
				if (i == n){
					if (j == 0) s = "Median";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getMedian());
				}
				else if (i == n + 1){
					if (j == 0) s = "MAD";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getMAD());
				}
			}
			else if (statisticsType == MMM){
				if (i == n){
					if (j == 0) s = "Mean";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getMean());
				}
				else if (i == n + 1){
					if (j == 0) s = "Median";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getMedian());
				}
				else if (i == n + 2){
					if (j == 0) s = "Mode";
					else if (j == 1 && m > 0) s = decimalFormat.format(data.getMode());
				}
			}
			return s;
		}
	}
}


