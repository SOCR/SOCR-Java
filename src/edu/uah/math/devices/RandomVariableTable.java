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
import java.awt.Dimension;
import java.text.DecimalFormat;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.io.Serializable;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.IntervalData;

/**
* This class defines a basic table for displaying the distribution and moments
* and the empirical distribution and moments for a specified random variable. The table
* is actually a JScrollPane that includes a JTable and an underlying table model.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class RandomVariableTable extends JScrollPane implements Serializable{
	//Constants
	public final static int NONE = 0, MSD = 1;
	//Variables
	private int statisticsType = 1, values, type;
	//Objects
	private DecimalFormat decimalFormat = new DecimalFormat();
	private JTable table;
	private RandomVariable randomVariable;
	private DataModel dataModel;
	private boolean showModelDistribution = true;

	/**
	* This general constructor creates a new random variable table with a
	* specified random variable.
	* @param rv the random variable
	*/
	public RandomVariableTable(RandomVariable rv){
		randomVariable = rv;
		dataModel = new DataModel();
		table = new JTable(dataModel);
		table.setPreferredScrollableViewportSize(new Dimension(50, 50));
		setViewportView(table);
		decimalFormat.setMaximumFractionDigits(5);
		setToolTipText();
	}

	/**
	* This special constructor creates a new random variable table with
	* a normall distributed random variable.
	*/
	public RandomVariableTable(){
		this(new RandomVariable());
	}

	/**
	* This method sets the tool tip text, for the table and for the scroll pane,
	* to the name of the random variable.
	*/
	private void setToolTipText(){
		String tip = randomVariable.getName() + " distribution";
		setToolTipText(tip);
		table.setToolTipText(tip);
	}

	/**
	* This method sets the random variable.
	* @param rv the random variable
	*/
	public void setRandomVariable(RandomVariable rv){
		randomVariable = rv;
		reset();
	}

	/**
	* This method returns the random variable.
	* @return the random variable
	*/
	public RandomVariable getRandomVariable(){
		return randomVariable;
	}

	/**
	* This method specifies the statistics that are to be displayed in the table
	* (none or mean, standard deviation).
	* @param t the type of moments to show
	*/
	public void setStatisticsType(int t){
		statisticsType = t;
	}

	public void setShowModelDistribution(boolean flag){
		showModelDistribution=flag;
	}
	
	/**
	* This method returns the type variable for the type of statistics that are
	* displayed.
	* @return the statistics type
	*/
	public int getStatisticsType(){
		return statisticsType;
	}

	/**
	* This method returns the underlying JTable.
	* @return the JTable
	*/
	public JTable getTable(){
		return table;
	}

	/**
	* This method rests the table. The name of the random variable and the structure
	* of the table may have changed.
	*/
	public void reset(){
		setToolTipText();
		dataModel.fireTableStructureChanged();
	}


	/**
	* This innder class defines the underlying data model for the table.
	*/
	private class DataModel extends AbstractTableModel{
		/**
		* This method defines the column names. The first column contains the values
		* of the random variable, the second column contains the distribution density
		* values and the third column contains the values of the data density.
		* @param i the index
		* @return the column name of the column
		*/
		public String getColumnName(int i){
			if (i == 0) return randomVariable.getName();
			else if (i == 1) return "Distribution";
			else return "Data";
		}

		/**
		* This method returns the number of columns, which is always 3.
		* @return the column count
		*/
		public int getColumnCount(){
			return 3;
		}

		/**
		This method returns the class type of a specified column.
		* @param i the column index
		* @return the class type of the column
		*/
		public Class getColumnClass(int i){
			return getValueAt(0, i).getClass();
		}

		/**
		* This method returns the row count, which is the size of the underlying domain.
		* @return the row count
		*/
		public int getRowCount(){
			int n = randomVariable.getDistribution().getDomain().getSize();
			if (statisticsType == MSD) return n + 2;
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
			IntervalData data = randomVariable.getIntervalData();
			Distribution distribution = randomVariable.getDistribution();
			Domain domain = distribution.getDomain();
			int n = domain.getSize(), m = data.getSize();
			if (i < n){
				if (j == 0 ) 
					s = decimalFormat.format(domain.getValue(i));
				else if (j == 1 && showModelDistribution) 
					s = decimalFormat.format(distribution.getDensity(domain.getValue(i)));
				else if (j == 2 && m > 0){
					if (domain.getType() == Domain.DISCRETE) s = decimalFormat.format(data.getRelFreq(domain.getValue(i)));
					else s = decimalFormat.format(data.getDensity(domain.getValue(i)));
				}
			}
			if (statisticsType == MSD){
				if (i == n){
					if (j == 0 ) s = "Mean";
					else if (j == 1 && showModelDistribution) s = decimalFormat.format(distribution.getMean());
					else if (j == 2 && m > 0) s = decimalFormat.format(data.getMean());
				}
				else if (i == n + 1){
					if (j == 0) s = "SD";
					else if (j == 1 && showModelDistribution) s = decimalFormat.format(distribution.getSD());
					else if (j == 2 && m > 0) s = decimalFormat.format(data.getSD());
				}
			}
			return s;
		}
	}
}

