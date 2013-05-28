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
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComponent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.io.Serializable;
import java.util.Vector;

/**
* This class defines a basic table for displaying the results of a random experiment. The
* results are the values of the random variables of interest for the experiment. These values
* are stored in a vector of arrays where each array contains the values of the variables..
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class RecordTable extends JScrollPane implements Serializable{
	//Objects
	private JTable table;
	private DataModel dataModel;
	private String[] variableNames;
	private Vector<double[]> data = new Vector<double[]>();

	/**
	* This general constructor creates a new record table with a specified array of variable
	* names. The underlying JTable and data model are created and the preferred size specified.
	* @param s the array of variable names
	*/
	public RecordTable(String[] s){
		variableNames = s;
		dataModel = new DataModel();
		table = new JTable(dataModel);
        table.setPreferredScrollableViewportSize(new Dimension(50,50));
        table.setColumnSelectionAllowed(true);
        
		setViewportView(table);
	}

	/**
	* This default constructor creates a new record table with one variable called Run.
	*/
	public RecordTable(){
		this(new String[]{"Run"});
	}

	/**
	* This method sets the string of variable names.
	* @param s the array of variable names
	*/
	public void setVariableNames(String[] s){
		data.removeAllElements();
		variableNames = s;
		dataModel.fireTableStructureChanged();
	}

	/**
	* This method returns the array of variable names.
	* @return the array of variable names
	*/
	public String[] getVariableNames(){
		return variableNames;
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
	* This method returns the underlying JTable.
	* @return the JTable
	*/
	public JTable getTable(){
		return table;
	}

	/**
	* This method adds a record to the table. Each record is a row that gives the values
	* of the random variables for a particular run of the experiment.
	* @param d the array of variable values
	*/
	public void addRecord(double[] d){
		data.addElement(d);
		int n = data.size();
		dataModel.fireTableRowsInserted(n, n);
	}

	/**
	* This method resets the table by resetting the underlying data model. All rows of
	* the table are deleted.
	*/
	public void reset(){
		data.removeAllElements();
		dataModel.fireTableDataChanged();
	}

	/**
	* This method returns the data vector.
	* @return the vector of arrays of values
	*/
	public Vector getData(){
		return data;
	}

	/**
	* This inner class defines the table model for the record table.
	*/
	private class DataModel extends AbstractTableModel{
		/**
		* This method defines the column names, which are the names of the variables.
		* @param i the column index
		* @return the column name
		*/
		public String getColumnName(int i){
			return variableNames[i];
		}

		/**
		* This method returns the number of columns, which is the number of variables.
		* @return the column count
		*/
		public int getColumnCount(){
			return variableNames.length;
		}

		/**
		* This method gets the class type for a specified column.
		* @param i the column index
		* @return the class type of the column
		*/
		public Class getColumnClass(int i){
			return getValueAt(0, i).getClass();
		}

		/**
		* This method gets the number of rows, which is the number of times the
		* underlying experiment has been updated.
		* @return the row count
		*/
		public int getRowCount(){
			return data.size();
		}

		/**
		* This method returns the object in a specified cell of the table. The objects
		* in the cells of this table are encoded as Double objects.
		* @param i the row index
		* @param j the column index
		* @return the object in the specified cell
		*/
		public Object getValueAt(int i, int j){
			double x = ((double[])data.elementAt(i))[j];
			return new Double(x);
		}
	}
}

