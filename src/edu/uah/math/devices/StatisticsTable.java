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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.io.Serializable;
import java.util.Vector;

/**
* This class defines a simple table that displays values from a distribution,
* a data set, or both. The first column shows the names of the variables, and the
* second and third columns the distribution and/or data values. This object is
* actually a JScrollPane that includes a JTable and a table model.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class StatisticsTable extends JScrollPane implements Serializable{
	//Constants
	public final static int BOTH = 0, DATA = 1, DISTRIBUTION = 2, NEITHER = 3;
	//Variables
	private String variableName, distributionName = "Distribution", dataName = "Data";
	private String[] parameterNames;
	private int show = BOTH, numberParameters;
	private double[] dataValues, distributionValues;
	private JTable table;
	private DataModel dataModel;
	private boolean showModelDistribution = true;

	/**
	* This general constructor creates a new statistics table with a specified variable name and array of
	* parameter names.
	* @param v the name of the variable
	* @param p the array of parameter names
	*/
	public StatisticsTable(String v, String[] p){
		variableName = v;
		parameterNames = p;
		numberParameters = parameterNames.length;
		dataValues = new double[numberParameters];
		distributionValues = new double[numberParameters];
		dataModel = new DataModel();
		table = new JTable(dataModel);
		table.setPreferredScrollableViewportSize(new Dimension(100, 20));
		setViewportView(table);
	}

	/**
	* This default constructor creates a new statistics table with name X, and with the parameters mean and SD.
	*/
	public StatisticsTable(){
		this("X", new String[]{"Mean", "SD"});
	}

	/**
	* This method returns the underlying JTable.
	* @return the JTable
	*/
	public JTable getTable(){
		return table;
	}

	/**
	* This method sets the data values.
	* @param d the data values
	*/
	public void setDataValues(double[] d){
		dataValues = d;;
		dataModel.fireTableDataChanged();
	}

	/**
	* This method sets the distribution values.
	* @param d the distribution values
	*/
	public void setDistributionValues(double[] d){
		distributionValues = d;
		dataModel.fireTableDataChanged();
	}

	/**
	* This method sets the column name for the distirbution column.
	* @param s the distribution name
	*/
	public void setDistributionName(String s){
		distributionName = s;
		dataModel.fireTableStructureChanged();
	}

	/**
	* This method sets the column name for the data column.
	* @param s the data name
	*/
	public void setDataName(String s){
		dataName = s;
		dataModel.fireTableStructureChanged();
	}

	/**
	* This method resets the table by resetting the data.
	*/
	public void reset(){
		for (int i = 0; i < numberParameters; i++) dataValues[i] = Double.NaN;
		dataModel.fireTableDataChanged();
	}

	/**
	* This method sets the show paramter.
	* @param i the show parameter
	*/
	public void setShow(int i){
		if (i < 0) i = 0; else if (i > 3) i = 3;
		show = i;
		dataModel.fireTableStructureChanged();
	}

	/**
	* This method sets tool tip for the table. This tip describes the variables.
	* @param s the descriptions
	*/
	public void setDescription(String s){
		TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
		if (headerRenderer instanceof DefaultTableCellRenderer)
			((DefaultTableCellRenderer)headerRenderer).setToolTipText(s);
		table.setToolTipText(s);
		setToolTipText(s);
	}
	
	public void setShowModelDistribution(boolean flag){
		showModelDistribution=flag;
	}

	/**
	* This inner class defines the underlying data model for the table.
	*/
	private class DataModel extends AbstractTableModel{
		/**
		* This method defines the column names. The first column gives the name of the variable
		* the second and third column names are "distribution" and "data", depending on the show variable.
		* @param i the column index
		* @return the column name
		*/
		public String getColumnName(int i){
			if (show == BOTH){
				if (i == 0) return variableName;
				else if (i == 1) return distributionName;
				else return dataName;
			}
			else if (show == DATA){
				if (i == 0) return variableName;
				else return dataName;
			}
			else if (show == DISTRIBUTION){
				if (i == 0) return variableName;
				else return distributionName;
			}
			else return variableName;
		}

		/**
		* This method returns the number of columns, which is 1, 2, or 3 depending on the show variable.
		* @return the column count
		*/
		public int getColumnCount(){
			if (show == BOTH) return 3;
			else if (show == DATA | show == DISTRIBUTION) return 2;
			else return 1;
		}

		/**
		* This method returns the class type of a given column.
		* @param i the column index
		* @return the class type of the column
		*/
		public Class getColumnClass(int i){
			return getValueAt(0, i).getClass();
		}

		/**
		* This method returns the row count, which is the number of parameters.
		* @return the row count
		*/
		public int getRowCount(){
			return numberParameters;
		}

		/**
		* This method returns the object in a specified row and column.
		* @param i the row number
		* @param j the column number
		* @return the object in row i and column j
		*/
		public Object getValueAt(int i, int j){
			if (show == BOTH){
				if (j == 0)	
					return parameterNames[i];
				else if (j == 1 && showModelDistribution) 
					return new Double(distributionValues[i]);
				else if (j==2)
					return new Double(dataValues[i]);
				else return "";
			}
			else if (show == DATA){
				if (j == 0) 
					return parameterNames[i];
				else 	
					return new Double(dataValues[i]);
			}
			else if (show == DISTRIBUTION ){
				if (j == 0) 
					return parameterNames[i];
				else {
					if (showModelDistribution)				
						return new Double(distributionValues[i]);
					else return "";}
			}
			else return parameterNames[i];
		}
	}
}

