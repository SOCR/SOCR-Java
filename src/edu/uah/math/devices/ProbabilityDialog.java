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
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.Frame;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.io.Serializable;
import edu.uah.math.distributions.Functions;

/**
* This class gives is a probability dialog box that allows the user to specify a
* probability distribution on a finite set of values.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class ProbabilityDialog extends JDialog
	implements ActionListener, WindowListener, Serializable{
	private int size;
	private double[] probabilities;
	private String[] labels;
	private boolean ok = false;
	protected JButton okButton = new JButton("OK");
	protected JButton cancelButton = new JButton("Cancel");
	private JPanel buttonBar = new JPanel();
	private DataModel dataModel;
	private JTable table;
	private JScrollPane pane;
	private DecimalFormat decimalFormat = new DecimalFormat();

  	/**
  	* This general constructor creates a new modal probability dialog with a specified
  	* frame owner, a specified title, and specified probabilities and values.
   	* @param o the owner
  	* @param t the tile
  	* @param p the array of probabilities
  	* @param s the array of labels that specify the values
  	*/
  	public ProbabilityDialog(Frame o, String t, double[] p, String[] s){
		super(o, t, true);
		setComponents(p, s);
	}

	/**
	* This special constructor creates a new probability dialog with a specified frame
	* owner, a specified title, and specified probabilities. The values are 1, 2, ..., n.
	*/
	public ProbabilityDialog(Frame o, String t, double[] p){
		super(o, t, true);
		setComponents(p);
	}

	/**
	* This method sets up the probability dialog components, including the
	* OK and Cancel buttons, the probability text fields, and the value labels.
	* @param p the array of probabilities
	* @param s the array of labels that specify the values.
	*/
	private void setComponents(double[] p, String[] s){
		//Event listeners
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		addWindowListener(this);
		//Arrays
		probabilities = p;
		labels = s;
		//Table
		dataModel = new DataModel();
		table = new JTable(dataModel);
		pane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(80 * probabilities.length, 50));		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setCellSelectionEnabled(true);
		
		//Button Panel
		buttonBar.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonBar.add(okButton);
		buttonBar.add(cancelButton);
		//Dialog
 		getContentPane().add(pane, BorderLayout.CENTER);
		getContentPane().add(buttonBar, BorderLayout.SOUTH);
		pack();
	}

	private void setComponents(double[] p){
		labels = new String[p.length];
		for (int i = 0; i < p.length; i++) labels[i] = String.valueOf(i + 1);
		setComponents(p, labels);
	}

	/**
	* This method sets the array of probabilities.
	* @param p the array of probabilities
	*/
	public void setProbabilities(double[] p){
		probabilities = Functions.getProbabilities(p);
		dataModel.fireTableDataChanged();
	}

	/**
	* This method sets the probability of a specified value.
	* @param i the index of the value
	* @param p the probability of the value
	*/
	public void setProbabilities(int i, double p){
		i = Functions.getIndex(i, size);
		probabilities[i] = p;
		setProbabilities(probabilities);
	}


	/**
	* This method returns all of the probabilities.
	* @return the array of probabilities
	*/
	public double[] getProbabilities(){
		return probabilities;
	}

	/**
	* This method returns the probability of value i.
	* @param i the index
	* @return the probability of the value with the given index.
	*/
	public double getProbabilities(int i){
		i = Functions.getIndex(i, size);
		return probabilities[i];
	}

	/**
	* This message handles the event generated when the user clicks on a button.
	* If the OK button is chosen, the probabilities in the text fields are assigned
	* to the probability array. The exception handling ensures that the probabilities
	* are nonnegative and sum to 1. The dialog is disposed. If the cancel button
	* is chosen, the dialog is disposed with no other action taken.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == okButton){
			for (int i = 0; i < size; i++){
				try{
					probabilities[i] = ((Double)table.getValueAt(0, i)).doubleValue();
				}
				catch(NumberFormatException x){
					probabilities[i] = 0;
				}
			}
			probabilities = Functions.getProbabilities(probabilities);
			ok = true;
			dispose();
		}
		else if (e.getSource() == cancelButton){
			ok = false;
			dispose();
		}
	}

	/**
	* This method returns a flag that specifies whether the OK or Cancel
	* buttons have been pushed.
	* @return true if the OK button was selected.
	*/
	public boolean isOK(){
		return ok;
	}

	/**
	* This method handles the event generated when the user clicks the close button.
	* This is equivalent to the Cancel button and the dialog is disposed.
	* @param e the window event
	*/
	public void windowClosing(WindowEvent e){
		if (e.getWindow() == this){
			ok = false;
			dispose();
		}
	}

	//These methods are not handled.*/
	public void windowOpened(WindowEvent event){}
	public void windowClosed(WindowEvent event){}
	public void windowActivated(WindowEvent event){}
	public void windowDeactivated(WindowEvent event){}
	public void windowConflicted(WindowEvent event){}
	public void windowIconified(WindowEvent event){}
	public void windowDeiconified(WindowEvent event){}

	/**
	* This inner class defines the table model associated with
	* the probability dialog.
	*/
	private class DataModel extends AbstractTableModel{

		/**
		* This method returns the column names for the table.
		* @return the array of column names.
		*/
		public String getColumnName(int i){
			return labels[i];
		}

		/**
		* This method returns the number of columns, which is the size of
		* the probability array.
		* @return the column count.
		*/
		public int getColumnCount(){
			return probabilities.length;
		}

		/**
		* This method returns the class of a specified column.
		* @param i the index
		* @return the class of the column corresponding to the index.
		*/
		public Class getColumnClass(int i){
			return getValueAt(0, i).getClass();
		}

		/**
		* This method returns the number of rows, which is simply 1.
		* @return the number of rows.
		*/
		public int getRowCount(){
			return 1;
		}

		/**
		* This method returns the contents of a cell with a given row and column number.
		* @param i the row number
		* @param j the column number
		* @return the object in cell (i,j)
		*/
		public Object getValueAt(int i, int j){
			return decimalFormat.format(probabilities[j]);
		}

		/**
		* This method specifies that the cells are editable.
		* @return true
		*/
		public boolean isCellEditable(int row, int col){
			return true;
		}

		/**
		* This method sets the value of a specified cell.
		* @param i the row number
		* @param j the column number
		* @param v the value of the cell
		*/
		public void setValueAt(Object v, int i, int j) {
			probabilities[j] = Double.valueOf(v.toString()).doubleValue();
			fireTableCellUpdated(i, j);
		}
	}
}
