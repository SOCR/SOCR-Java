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

package edu.uah.math.devices;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import edu.uah.math.distributions.Functions;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.Serializable;
import java.text.DecimalFormat;

/**
* This class is a special dialog box for entering the event-probabilities and x-values for each Outcome 
* (parameter-vectors) for the SOCR edu.ucla.stat.SOCR.distributions.MultiNomialDistribution distribution. 
* 
* @author Ivo Dinov
* @version October 20, 2009
*/

public class SOCR_MultiNomialDistributionParametersDialog extends javax.swing.JDialog 
		implements ActionListener, WindowListener, Serializable {
	
	private int size;
	private boolean ok = false;
	protected JButton okButton = new JButton("OK");
	protected JButton cancelButton = new JButton("Cancel");
	protected JButton resetButton = new JButton("Reset");

	private DataModel dataModel;
	private JTable table;
	private JScrollPane pane;
	private DecimalFormat decimalFormat = new DecimalFormat();

	private JToolBar buttonBar = new JToolBar("MultiNomial Distribution Parameter Setter");

	protected double[] probValues;
	protected int[] xValues;
	protected String[] columnNames, rowNames;
	
  	/**
  	* This general constructor creates a new modal probability dialog with a specified
  	* frame owner, a specified title, and specified probabilities and values.
   	* @param o the owner
  	* @param t the tile
  	* @param p the 2D array of parameters (event-probabilities and x-values)
  	* @param columnNames the array of labels that specify the names of the columns (Outcome1, Outcome2, ..., OutcomeK)
  	* @param rowNames the array of labels that specify the name of the 2 rows of parameters (event-probabilities and x-values)
  	* 
  	* Dialog Format
  	* =================================================
  	* 			Outcome1	Outcome2  	...	....	OutcomeK
  	* row1		p1			p2			... ....	pK
  	* row2		x1			x2			... ....	xK
   	* =================================================
  	*/
  	public SOCR_MultiNomialDistributionParametersDialog(Frame o, String t, double[] p, int[] x,
  			String[] columnNames, String[] rowNames){
		super(o, t, true);
		setComponents(p, x, columnNames, rowNames);
	}


	/**
	* This method sets up the parameter dialog components, including the
	* OK and Cancel buttons, the p-values and x-values fields, and the parameter String labels.
  	* @param p the 2D array of parameters row=(p, x) x column=(Outcome1, Outcome2, ..., OutcomeK)
  	* @param _columnNames the array of labels that specify the names of the columns (Outcome1, Outcome2, ..., OutcomeK)
  	* @param _rowNames the array of labels that specify the names of the rows (p-values, x-values) for each Outcome
	*/
	private void setComponents(double[] p, int[] x, String[] _columnNames, String[] _rowNames){
		//Event listeners
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		resetButton.addActionListener(this);
		addWindowListener(this);
		
		//Arrays
		probValues = p;
		xValues = x;
		columnNames = _columnNames;
		rowNames = _rowNames;
		
		//Table
		dataModel = new DataModel();
		table = new JTable(dataModel);
		pane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(40 * probValues.length, 50));
		table.setCellSelectionEnabled(true);
		
		//Button Panel
		buttonBar.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonBar.add(okButton);
		buttonBar.add(cancelButton);
		buttonBar.add(resetButton);

		//Dialog
 		getContentPane().add(pane, BorderLayout.CENTER);
		getContentPane().add(buttonBar, BorderLayout.SOUTH);
		pack();
	}
	
	/**
	* This method handles the events associated with selecting a button for a special
	* distribution.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == okButton){
			double totalProb = 0;
			for (int i = 0; i < size; i++){
				try{	// get/set p-values and x-values
					probValues[i] = ((Double)table.getValueAt(0, i)).doubleValue(); // Probabilities
					xValues[i] = ((Integer)table.getValueAt(1, i)).intValue(); // X-values
					totalProb += probValues[i];
				}
				catch(NumberFormatException x){
					probValues[i] = 0;
					xValues[i] = 1;
				}
			}
			
				// Ensure that the Weights add up to 1!
			if (totalProb<=0) totalProb = 1.0;
			for (int i = 0; i < size; i++)
				probValues[i] /= totalProb;
			
			ok = true;
			dispose();
		}
		else if (e.getSource() == cancelButton){
			ok = false;
			dispose();
		}
		else if (e.getSource() == resetButton){
			ok = false;
			for (int i = 0; i < size; i++){
				probValues[i] = 1.0/(probValues.length);
				xValues[i] = 0;
			}
			dispose();
		}
		repaint();
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
	* This method returns the first row (probabilities)
	* @return probabilities (double array of probabilities).
	*/
	public double[] getProbabilities(){
		return probValues;
	}

	/**
	* This method returns the number of outcomes
	* @return k number Multinomial of outcomes
	*/
	public int getOutcomeCount(){
		return probValues.length;
	}

	/**
	* This method returns the Sum of X-values
	* @return n Total Sum of X-values; n=x[0]+x[1]+...+x[k]
	*/
	public int getN(){
		int sum=0;
		for (int i=0; i< xValues.length; i++) sum += xValues[i];
		return sum;
	}

	/**
	* This method returns the Sum of X-values
	* @return n Total Sum of X-values; n=x[0]+x[1]+...+x[k]
	*/
	public int getSumXvalues(){
		return getN();
	}

	/**
	* This method returns the second row (X-values)
	* @return X-valuess (double array of X-values).
	*/
	public int[] getXvalues(){
		return xValues;
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
	* the Mixture Distribution Parameter Dialog.
	*/
	private class DataModel extends AbstractTableModel{

		/**
		* This method returns the column names for the table.
		* @return the array of column names.
		*/
		public String getColumnName(int i){
			return columnNames[i];
		}

		/**
		* This method returns the row names for the table.
		* @return the array of column names.
		*/
		public String getRowName(int i){
			return rowNames[i];
		}

		/**
		* This method returns the number of columns, which is the size of
		* the probability array.
		* @return the column count (number of Outcomes in the Multinomial distribution).
		*/
		public int getColumnCount(){
			return probValues.length;
		}

		/**
		* This method returns the number of rows, in this case 3.
		* @return the row count (P-values, X-values).
		*/
		public int getRowCount(){
			return (2);
		}

		/**
		* This method returns the class of a specified column.
		* @param i the row index
		* @param j the column index
		* @return the class of the table-cell corresponding to the index-pait (i,j).
		*/
		public Class getColumnClass(int i, int j){
			return getValueAt(i, j).getClass();
		}

		/**
		* This method returns the contents of a cell with a given row and column number.
		* @param i the row number
		* @param j the column number
		* @return the object in cell (i,j)
		*/
		public Object getValueAt(int i, int j){
			if (i==0) return decimalFormat.format(probValues[j]);
			else if (i==1) return decimalFormat.format(xValues[j]);
			else return new String("Only p-Values (row=0) and x-Values (row=1) are returned!!!");
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
			if (i==0) probValues[j] = Double.valueOf(v.toString()).doubleValue();
			else if (i==1) xValues[j] = Integer.valueOf(v.toString()).intValue();
			else System.err.println("Only p-Values (row=0) and x-Values (row=1) are allowed to be set!!!");
			fireTableCellUpdated(i, j);
		}
	}
}