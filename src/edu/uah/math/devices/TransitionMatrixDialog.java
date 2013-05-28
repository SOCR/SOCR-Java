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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import edu.uah.math.distributions.Functions;

/**
* This class gives a tansition matrix dialog box that allows the user to specify the
* transition probabilities for a Markov chain.
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class TransitionMatrixDialog extends JDialog implements ActionListener, WindowListener, FocusListener{
	private int size;
	private double[][] probMatrix;
	private boolean ok;
	private JLabel[] valueLabel;
	private JTextField[][] probField;
	//Special transition probabilities
	private JToolBar buttonBar = new JToolBar("Special transition matrices");
	private JButton absorbingButton = new JButton("GR");
	private JButton reflectingButton = new JButton("RW");
	private JButton ehrenfestButton = new JButton("E");
	private JButton geneticsButton = new JButton("G");
	private JButton bernoulliLaplaceButton = new JButton("BL");
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	private JPanel buttonPanel = new JPanel();
	private JPanel distPanel = new JPanel();
	private DecimalFormat decimalFormat = new DecimalFormat("0.000");

	/**
	* This method creates a new Transition Matrix Dialog with a specified frame owner and a
	* specified dimension.
	*/
	public TransitionMatrixDialog(Frame owner, int n){
		super(owner, "Transition Probabilities", true);
		size = n;
		//Absorbing button
		absorbingButton.setIcon(new ImageIcon(getClass().getResource("chain.gif")));
		absorbingButton.setToolTipText("Gambler's ruin chain");
		absorbingButton.addActionListener(this);
		//Reflecting button
		reflectingButton.setIcon(new ImageIcon(getClass().getResource("chain.gif")));
		reflectingButton.setToolTipText("Random walk chain");
		reflectingButton.addActionListener(this);
		//Ehrenfest button
		ehrenfestButton.setIcon(new ImageIcon(getClass().getResource("chain.gif")));
		ehrenfestButton.setToolTipText("Ehrenfest chain");
		ehrenfestButton.addActionListener(this);
		//Genetics button
		geneticsButton.setIcon(new ImageIcon(getClass().getResource("chain.gif")));
		geneticsButton.setToolTipText("Genetics chain");
		geneticsButton.addActionListener(this);
		//Bernoulli-Laplcae button
		bernoulliLaplaceButton.setIcon(new ImageIcon(getClass().getResource("chain.gif")));
		bernoulliLaplaceButton.setToolTipText("Bernoulli-Laplace chain");
		bernoulliLaplaceButton.addActionListener(this);
		//Button bar
		buttonBar.add(absorbingButton);
		buttonBar.add(reflectingButton);
		buttonBar.add(ehrenfestButton);
		buttonBar.add(geneticsButton);
		buttonBar.add(bernoulliLaplaceButton);
		//Event listeners
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		addWindowListener(this);
		//Matrices and arrays
		probMatrix = new double[size][size];
		valueLabel = new JLabel[size];
		probField = new JTextField[size][size];
		for (int i = 0; i < size; i++) valueLabel[i] = new JLabel(String.valueOf(i));
		//Text boxes
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				probField[i][j] = new JTextField("xxxxxxx");
				probField[i][j].addFocusListener(this);
			}
		}
		//Distribution Panel
		distPanel.setLayout(new GridLayout(size + 1, size + 1));
		distPanel.add(new JLabel("P"));
		for (int i = 0; i < size; i++) distPanel.add(valueLabel[i]);
		for(int i = 0; i < size; i++){
			distPanel.add(new JLabel(String.valueOf(i)));
			for (int j= 0; j < size; j++) distPanel.add(probField[i][j]);
		}
		//Button Panel
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		//Dialog
		getContentPane().add(buttonBar, BorderLayout.NORTH);
 		getContentPane().add(distPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		pack();
	}

	/**
	* This method sets the probability of the i, j entry.
	* @param i the row number
	* @param j the column number
	* @param p the probability
	*/
	public void setProbability(int i, int j, double p){
		probMatrix[i][j] = p;
		probField[i][j].setText(decimalFormat.format(p));
	}

	/**
	* This method sets the entire array of probabilities.
	* @param p the array of probabilties
	*/
	public void setProbabilities(double[][] p){
		probMatrix = p;
		for (int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				probField[i][j].setText(decimalFormat.format(probMatrix[i][j]));
	}

	/**
	* This method returns the transition probability matrix.
	* @return the array of probabilities
	*/
	public double[][] getProbabilities(){
		return probMatrix;
	}

	/**
	* This method returns the probability of the i, j entry of the transition probability matrix.
	* @param i the row number
	* @param j the column number
	* @return the probability
	*/
	public double getProbability(int i, int j){
		return probMatrix[i][j];
	}

	/**
	* This method sets a specified label.
	* @param i the index
	* @param s the label
	*/
	public void setLabel(int i, String s){
		valueLabel[i].setText(s);
	}

	/**
	* This method sets the array of labels.
	* @param s the array of labels
	*/
	public void setLabels(String[] s){
		for (int i = 0; i < size; i++) valueLabel[i].setText(s[i]);
	}

	/**
	* This message handles the event generated when the user clicks on a button. If the
	* user clicks on the OK button, the probability matrix is updated, and error checking
	* is used to make sure that the entries form a valid transition matrix.  Thus, the
	* entries must be nonnegative and each row sum must be 1. If the user clicks the Cancel
	* button, no action is taken and the dialog is disposed.
	* @param event the action event
	*/
	public void actionPerformed(ActionEvent event){
		int n = size - 1;
		double x;
		if (event.getSource() == absorbingButton){
			for (int i = 0; i <= n; i++) for (int j = 0; j <= n; j++){
				if ((i == 0 && j == 0) | (i == n && j == n)) setProbability(i, j, 1.0);
				else if ((i < n && j == i - 1) | (i > 0 && j == i + 1)) setProbability(i, j, 0.5);
				else setProbability(i, j, 0.0);
			}
			repaint();
		}
		else if (event.getSource() == reflectingButton){
			for (int i = 0; i <= n; i++) for (int j = 0; j <= n; j++){
				if (i == 0 && j == 1) setProbability(i, j, 1.0);
				else if (i == n && j == n - 1) setProbability(i, j, 1.0);
				else if (j == i - 1 | j == i + 1) setProbability(i, j, 0.5);
				else setProbability(i, j, 0.0);
			}
			repaint();
		}
		else if (event.getSource() == ehrenfestButton){
			for (int i = 0; i <= n; i++) for (int j = 0; j <= n; j++){
				if (j == i - 1) setProbability(i, j, (double)i / n);
				else if (j == i + 1) setProbability(i, j, 1 - (double)i / n);
				else setProbability(i, j, 0.0);
			}
			repaint();
		}
		else if (event.getSource() == geneticsButton){
			for (int i = 0; i <= n; i++) for (int j = 0; j <= n; j++)
				setProbability(i, j, Functions.comb(2 * i, j) * Functions.comb(2 * n - 2 * i, n - j) / Functions.comb(2 * n, n));
			repaint();
		}
		else if (event.getSource() == bernoulliLaplaceButton){
			for (int i = 0; i <= n; i++) for (int j = 0; j <= n; j++){
				x = (double)i;
				if (j == i - 1) setProbability(i, j, (x / n) * (x / n));
				else if (j == i) setProbability(i, j, 2 * (x / n) * (1 - x / n));
				else if (j == i + 1) setProbability(i, j, (1 - x / n) * (1 - x / n));
				else setProbability(i, j, 0.0);
			}
			repaint();
		}
		else if (event.getSource() == okButton){
			for (int i = 0; i < size; i++){
				double sum = 0;
				for(int j = 0; j < size; j++){
					try{
						probMatrix[i][j] = Double.valueOf(probField[i][j].getText()).doubleValue();
					}
					catch(NumberFormatException e){
						probMatrix[i][j] = 0;
					}
					if (probMatrix[i][j] < 0) probMatrix[i][j] = 0;
					sum = sum + probMatrix[i][j];
				}
				if (sum == 0) for (int k = 0; k < size; k++) probMatrix[i][k] = 1.0 / size;
				else for (int k = 0; k < size; k++) probMatrix[i][k] = probMatrix[i][k] / sum;
			}
			ok = true;
			dispose();
		}
		else if (event.getSource() == cancelButton){
			ok = false;
			dispose();
		}
	}

	/**
	* This method returns the flag that specifies whether the OK or Cancel
	* button has been pushed.
	*/
	public boolean isOK(){
		return ok;
	}

	/**
	* This method selects the text in a probability field whenever that field gets
	* the focus.
	* @param e the focus event
	*/
	public void focusGained(FocusEvent e){
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				if (e.getSource() == probField[i][j]) probField[i][j].selectAll();
	}

	/**
	* This method deselects the text when a probability field loses the focus.
	* @param e the focus event
	*/
	public void focusLost(FocusEvent e){
		for (int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				if (e.getSource() == probField[i][j]) probField[i][j].select(0,0);
	}

	/**
	* This method handles the event generated when the user clicks the close button;
	* this is equivalent to the Cancel button and the dialog is disposed.
	* @param e the window event
	*/
	public void windowClosing(WindowEvent e){
		if (e.getWindow() == this){
			ok = false;
			dispose();
		}
	}

	//These methods are not handled.
	public void windowOpened(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowActivated(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowConflicted(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
}
