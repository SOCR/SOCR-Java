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
* This class is a special dialog box for entering (mean,SD, weight) parameters for the SOCR Mixture of Normals
* distribution. The user can specify three parameters: (mean,SD, weight) for each mixture component
* 
* @author Ivo Dinov
* @version October 20, 2008
*/

public class SOCR_MixtureDistributionParametersDialog extends javax.swing.JDialog 
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

	private JToolBar buttonBar = new JToolBar("Mixture Distribution Parameter Setter");

	protected double[][] parameters;
	protected String[] columnNames, rowNames;
	
  	/**
  	* This general constructor creates a new modal probability dialog with a specified
  	* frame owner, a specified title, and specified probabilities and values.
   	* @param o the owner
  	* @param t the tile
  	* @param p the 2D array of parameters (mean, SD, weight)
  	* @param columnNames the array of labels that specify the names of the columns (Dist1, Dist2, ..., DistN)
  	* @param rowNames the array of labels that specify the names of the 3 rows (mean, SD, weight)
  	* 
  	* Dialog Format
  	* =================================================
  	* 			Component1	Component2	...	....	ComponentN
  	* row1		mean1		mean2		... ...		meanN
  	* row2		SD1			SD2			... ...		SDN
  	* row3		Weight1		Weight2		...	...		WeightN
  	* =================================================
  	*/
  	public SOCR_MixtureDistributionParametersDialog(Frame o, String t, double[][] p, 
  			String[] columnNames, String[] rowNames){
		super(o, t, true);
		setComponents(p, columnNames, rowNames);
	}


	/**
	* This method sets up the parameter dialog components, including the
	* OK and Cancel buttons, the mean,SD,weight fields, and the parameter String labels.
  	* @param p the 2D array of parameters row=(mean, SD, weight) x column=(Dist1, Dist2, ..., DistN)
  	* @param _columnNames the array of labels that specify the names of the columns (Dist1, ..., DistN)
  	* @param _rowNames the array of labels that specify the names of the rows (mean, SD, weight)
	*/
	private void setComponents(double[][] p, String[] _columnNames, String[] _rowNames){
		//Event listeners
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		resetButton.addActionListener(this);
		addWindowListener(this);
		
		//Arrays
		parameters = p;
		columnNames = _columnNames;
		rowNames = _rowNames;
		
		//Table
		dataModel = new DataModel();
		table = new JTable(dataModel);
		pane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(40 * parameters[0].length, 50));
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
			double totalWeight = 0;
			for (int i = 0; i < size; i++){
				try{	// get/set mean, SD, weight
					parameters[0][i] = ((Double)table.getValueAt(0, i)).doubleValue();
					parameters[1][i] = ((Double)table.getValueAt(1, i)).doubleValue();
					parameters[2][i] = ((Double)table.getValueAt(2, i)).doubleValue();
					totalWeight += parameters[2][i];
				}
				catch(NumberFormatException x){
					parameters[0][i] = 0;
					parameters[1][i] = 1;
					parameters[2][i] = 1.0/(parameters[0].length);
				}
			}
			
				// Ensure that the Weights add up to 1!
			if (totalWeight<=0) totalWeight = 1.0;
			for (int i = 0; i < size; i++)
				parameters[2][i] /= totalWeight;
			
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
				parameters[0][i] = 0;
				parameters[1][i] = 1;
				parameters[2][i] = 1.0/(parameters[0].length);
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
	* This method returns the first row (of means for all Distributions in the mixture)
	* @return means (double array of means.
	*/
	public double[] getMeans(){
		return parameters[0];
	}

	/**
	* This method returns the second row (of Standard Deviations for all Distributions in the mixture)
	* @return SDs (double array of standard deviations).
	*/
	public double[] getSDs(){
		return parameters[1];
	}

	/**
	* This method returns the third row (of weights for all Distributions in the mixture)
	* @return weights (double array of weights).
	*/
	public double[] getWeights(){
		return parameters[2];
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
		* @return the column count (number of distributions in the mixture).
		*/
		public int getColumnCount(){
			return parameters[0].length;
		}

		/**
		* This method returns the number of rows, in this case 3.
		* @return the row count (mean, SD, weight).
		*/
		public int getRowCount(){
			return parameters.length;
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
			return decimalFormat.format(parameters[i][j]);
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
			parameters[i][j] = Double.valueOf(v.toString()).doubleValue();
			fireTableCellUpdated(i, j);
		}
	}
}