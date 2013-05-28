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
package edu.ucla.stat.SOCR.util;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;

/**This class gives is a probability dialog box that allows the user to specify a
probability distribution on a finite set of values.*/
public class ProbabilityDialog extends JDialog implements ActionListener, WindowListener, FocusListener{
	private int size;
	private double[] prob;
	private boolean ok;
	private JLabel[] valueJLabel;
	private JTextField[] probField;
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	private JPanel buttonBar = new JPanel();
	private JPanel distPanel = new JPanel();
	private DecimalFormat decimalFormat = new DecimalFormat("0.000");

  	/**This method creates a new About Dialog with a specified frame owner and a specified message.*/
  	public ProbabilityDialog(Frame owner, String name, int n){
		super(owner, name, true);
		size = n;
		//Event listeners
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		addWindowListener(this);
		//Arrays
		prob = new double[6];
		valueJLabel = new JLabel[size];
		probField = new JTextField[size];
		for (int i = 0; i < size; i++){
			valueJLabel[i] = new JLabel(String.valueOf(i));
			probField[i] = new JTextField("xxxx");
			probField[i].addFocusListener(this);
		}
		//Distribution Panel
		distPanel.setLayout(new GridLayout(2, size, 1, 1));
		for (int i = 0; i < size; i++) distPanel.add(valueJLabel[i]);
		for (int i = 0; i < size; i++) distPanel.add(probField[i]);
		//Button Panel
		buttonBar.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonBar.add(okButton);
		buttonBar.add(cancelButton);
		//Dialog
 		getContentPane().add(distPanel, BorderLayout.CENTER);
		getContentPane().add(buttonBar, BorderLayout.SOUTH);
		pack();
	}

	/**This method sets the probability of value i.*/
	public void setProbability(int i, double p){
		prob[i] = p;
		probField[i].setText(decimalFormat.format(p));
	}

	/**This method sets the probabilities.*/
	public void setProbabilities(double[] p){
		prob = p;
		for (int i = 0; i < size; i++) probField[i].setText(decimalFormat.format(prob[i]));
	}

	/**This method returns the probabilities.*/
	public double[] getProbabilities(){
		return prob;
	}

	/**This method returns the probability of value i*/
	public double getProbability(int i){
		return prob[i];
	}

	/**This method sets the labels.*/
	public void setLabel(int i, String s){
		valueJLabel[i].setText(s);
	}

        /**This method sets the labels.*/
        public void setLabels(String[] s){
                for (int i = 0; i < s.length; i++)
			valueJLabel[i].setText(s[i]);
        }


	/**This message handles the event generated when the user clicks on the OK button;
	the dialog is disposed.*/
	public void actionPerformed(ActionEvent event){
		if (event.getSource() == okButton){
			double sum = 0;
			for (int i = 0; i < size; i++){
				try{
					prob[i] = Double.valueOf(probField[i].getText()).doubleValue();
				}
				catch(NumberFormatException e){
					prob[i] = 0;
				}
				if (prob[i] < 0) prob[i] = 0;
				sum = sum + prob[i];
			}
			if (sum == 0) for (int i = 0; i < size; i++) prob[i] = 1.0 / size;
			else if (sum > 1) for (int i = 0; i < size; i++) prob[i] = prob[i] / sum;
			ok = true;
			dispose();
		}
		else if (event.getSource() == cancelButton){
			ok = false;
			dispose();
		}
	}

	/**This method returns the flag that specifies whether the OK or Cancel
	buttons have been pushed.*/
	public boolean isOK(){
		return ok;
	}

	/**This method selects the text in a probability field whenever that field gets
	the foucs. */
	public void focusGained(FocusEvent e){
		for(int i = 0; i < size; i++) if (e.getSource() == probField[i]) probField[i].selectAll();
	}

	public void focusLost(FocusEvent e){
		for (int i = 0; i < size; i++) if (e.getSource() == probField[i]) probField[i].select(0,0);
	}

	/**This method handles the event generated when the user clicks the close button;
	this is equivalent to the Cancel button and the dialog is disposed.*/
	public void windowClosing(WindowEvent event){
		if (event.getWindow() == this){
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
}
