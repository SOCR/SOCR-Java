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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.io.Serializable;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.NormalDistribution;

/**
* This class defines a table for displaying a set of quantiles from a
* specified distribution. The table is used in the probability plot experiment. This
* object is actually a JScrollPane that includes a JTable and a data model.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class QuantileTable extends JScrollPane implements Serializable{
	//Variables
	private double[] probabilities;
	private JTable table;
	private DataModel dataModel;
	private Distribution distribution;

	/**
	* This general constructor creates a new quantile table corresponding to a
	* given distribution and a given array of probabilities. Ordinarily, the
	* probabilities are given in increasing order.
	* @param d the variable name
	* @param p the size
	*/
	public QuantileTable(Distribution d, double[] p){
		distribution = d;
		probabilities = p;
		dataModel = new DataModel();
		table = new JTable(dataModel);
		table.setPreferredScrollableViewportSize(new Dimension(100, 75));
		setViewportView(table);
	}

	/**
	* This general constructor creates a new quantile table corresponding to a
	* given distribution and a uniform set of probabilities.
	* @param d the variable name
	* @param n the number of quantiles
	*/
	public QuantileTable(Distribution d, int n){
		distribution = d;
		probabilities = new double[n];
		for (int i = 0; i < n; i++) probabilities[i] = (double)(i + 1) / (n + 1);
		dataModel = new DataModel();
		table = new JTable(dataModel);
		table.setPreferredScrollableViewportSize(new Dimension(100, 75));
		setViewportView(table);
	}

	/**
	* This default constructor create a new quantile table corresponding to
	* the standard normal distribution and size 10.
	*/
	public QuantileTable(){
		this(new NormalDistribution(), 10);
	}

	/**
	* This method returns the underlying JTable.
	* @return the JTable
	*/
	public JTable getTable(){
		return table;
	}

	/**
	* This method sets the probabilities.
	* @param p the array of probabilities
	*/
	public void setProbabilities(double[] p){
		probabilities = p;
		dataModel.fireTableStructureChanged();
	}

	/**
	* This method returns the array of probabilites.
	* @return the array of probabilities
	*/
	public double[] getProbabilities(){
		return probabilities;
	}

	/**
	* This method sets the probabilities to be uniform probabilities
	* corresponding to a given number.
	* @param n the number of probabilities and quantiles
	*/
	public void setCount(int n){
		probabilities = new double[n];
		for (int i = 0; i < n; i++) probabilities[i] = (double)(i + 1) / (n + 1);
		dataModel.fireTableStructureChanged();
	}

	/**
	* This method sets the distribution.
	* @param d the distribution
	*/
	public void setDistribution(Distribution d){
		distribution = d;
		dataModel.fireTableDataChanged();
	}

	/**
	* This method returns the distribution
	* @return the probability distribution
	*/
	public Distribution getDistribution(){
		return distribution;
	}

	/**
	* This method returns the array of quantiles.
	* @return the array of quantiles
	*/
	public double[] getQuantiles(){
		int n = probabilities.length;
		double[] quantiles = new double[n];
		for (int i = 0; i < n; i++) quantiles[i] = distribution.getQuantile(probabilities[i]);
		return quantiles;
	}

	/**
	* This method returns the quantile corresponding to a given index.
	* @param i the index
	* return the quantile corresponding to that index
	*/
	public double getQuantile(int i){
		if (i < 0) i = 0; else if (i > probabilities.length - 1) i = probabilities.length - 1;
		return distribution.getQuantile(probabilities[i]);
	}

	/**
	* This inner class defines the underlying data model for the sample table.
	*/
	private class DataModel extends AbstractTableModel{
		/**
		* This method defines the names of the columns. The first column holds the
		* numbers of the quantiles, the second column the probabilites, and the third
		* column the quantiles.
		* @param i the column index
		* @return the column name
		*/
		public String getColumnName(int i){
			if (i == 0) return "Index";
			else if (i == 1) return "Probability";
			else return "Quantile";
		}

		/**
		* This method returns the number of columns, which is always 3.
		* @return the column count
		*/
		public int getColumnCount(){
			return 3;
		}

		/**
		* This method returns the class type of the objects in a specified column.
		* @param i the column index
		* @return the class type of the column
		*/
		public Class getColumnClass(int i){
			return getValueAt(0, i).getClass();
		}

		/**
		* This method returns the row count. The numbe of rows is the length
		* of the probability array.
		* @return the row count
		*/
		public int getRowCount(){
			return probabilities.length;
		}

		/**
		* This method returns the object in a specified cell of the table.
		* @param i the row index
		* @param j the column index
		* @return the object in the specified cell
		*/
		public Object getValueAt(int i, int j){
			if (j == 0) return new Integer(i + 1);
			else if (j == 1) return new Double(probabilities[i]);
			else return new Double(distribution.getQuantile(probabilities[i]));
		}
	}
}

