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

/**
* This class simple table for displaying the values from a statistical sample. This
* object is actually a JScrollPane that includes a JTable and a data model.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class SampleTable extends JScrollPane implements Serializable{
	//Variables
	private int sampleSize;
	private JTable table;
	private DataModel dataModel;
	private double[] data;
	private String name;

	/**
	* This general constructor creates a new sample table corresponding to a
	* variable with a specified name.
	* @param s the variable name
	*/
	public SampleTable(String s){
		name = s;
		data = new double[0];
		dataModel = new DataModel();
		table = new JTable(dataModel);
		table.setPreferredScrollableViewportSize(new Dimension(50, 50));
		setViewportView(table);
		setDescription("Random sample from the distribution of " + s);
	}

	/**
	* This default constructor create s new sample table with variable name x.
	*/
	public SampleTable(){
		this("X");
	}

	/**
	* This method returns the underlying JTable.
	* @return the JTable
	*/
	public JTable getTable(){
		return table;
	}

	/**
	* This method sets the data. The data consist of an array of double values.
	* @param d the array of data values
	*/
	public void setData(double[] d){
		data = d;
		dataModel.fireTableDataChanged();
	}

	public void setData(int[] a){
		int n = a.length;
		double[] d = new double[n];
		for (int i = 0; i < n; i++) d[i] = (double)a[i];
		setData(d);
	}

	/**
	* This method resets the table by resetting the underlying data model and then
	* repainting.
	*/
	public void reset(){
		data = new double[0];
		dataModel.fireTableDataChanged();
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

	/**
	* This inner class defines the underlying data model for the sample table.
	*/
	private class DataModel extends AbstractTableModel{
		/**
		* This method defines the names of the columns. The first column holds the
		* number of the data point and the second column the actual data values.
		* @param i the column index
		* @return the column name
		*/
		public String getColumnName(int i){
			if (i == 0) return "Index";
			else return name;
		}

		/**
		* This method returns the number of columns, which is always 2.
		* @return the column count
		*/
		public int getColumnCount(){
			return 2;
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
		* This method returns the row count. The numbe of rows is 0 after the table
		* has been reset and is the size of the data set otherwise.
		* @return the row count
		*/
		public int getRowCount(){
			return data.length;
		}

		/**
		* This method returns the object in a specified cell of the table.
		* @param i the row index
		* @param j the column index
		* @return the object in the specified cell
		*/
		public Object getValueAt(int i, int j){
			if (j == 0) return new Integer(i + 1);
			else return new Double(data[i]);
		}
	}
}

