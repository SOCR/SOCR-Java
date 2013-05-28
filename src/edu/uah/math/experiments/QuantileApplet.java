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
package edu.uah.math.experiments;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Component;
import java.awt.Color;
import java.io.Serializable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import javax.swing.JApplet;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.GammaDistribution;
import edu.uah.math.distributions.BetaDistribution;
import edu.uah.math.distributions.ChiSquareDistribution;
import edu.uah.math.distributions.WeibullDistribution;
import edu.uah.math.distributions.StudentDistribution;
import edu.uah.math.distributions.LogisticDistribution;
import edu.uah.math.distributions.LogNormalDistribution;
import edu.uah.math.distributions.FisherDistribution;
import edu.uah.math.distributions.ParetoDistribution;
import edu.uah.math.distributions.ExtremeValueDistribution;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.QuantileGraph;

/**
* This applet displays the density function or cumulative distribution function
* for a variety of distributions. The user can specify a value and obtain the
* corresponding quantile, or can specify a quantile and obtain the corresponding value.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class QuantileApplet extends JApplet implements ActionListener, ChangeListener, ItemListener, Serializable{
	//Variables
	private int distributionType = 0;
	private double quantile = 0.0, prob = 0.5;
	//Objects
	private DecimalFormat decimalFormat = new DecimalFormat();
	private JButton aboutButton = new JButton();
	private JTextField quantileField = new JTextField(5);
	private JTextField probField = new JTextField(5);
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private JPanel fieldPanel = new JPanel();
	private JLabel quantileLabel = new JLabel("Quantile");
	private JLabel probLabel = new JLabel("Prob");
	private JComboBox distributionChoice = new JComboBox();
	private JComboBox typeChoice = new JComboBox();
	private Parameter parameter1Scroll = new Parameter(-5, 5, 0.5, 0, "Mean", "\u03bc");
	private Parameter parameter2Scroll = new Parameter(0.5, 5, 0.5, 1, "Standard deviation", "\u03c3");
	private Distribution distribution = new NormalDistribution(0, 1);
	private QuantileGraph quantileGraph = new QuantileGraph(distribution);
	private Frame frame;

	/**
	* This method initializes the applet, including the sliders for the parameters
	* of the distribution, the about button, the choice box for the type of
	* graph, the choice box for the type of distribution, the toolbar, and
	* the quantile and probability fields.
	*/
	public void init(){
		setName("Quantile JApplet");
		//Parameters
		parameter1Scroll.applyDecimalPattern("0.0");
		parameter1Scroll.getSlider().addChangeListener(this);
		parameter2Scroll.applyDecimalPattern("0.0");
		parameter2Scroll.getSlider().addChangeListener(this);
		//Button
		aboutButton.addActionListener(this);
		aboutButton.setToolTipText("Information");
		aboutButton.setIcon(new ImageIcon(getClass().getResource("about.gif")));
		//Initialize objects
		setBackground(Color.lightGray);
		this.getContentPane().setLayout(new BorderLayout());
		//Type Choice
		typeChoice.addItemListener(this);
		typeChoice.setToolTipText("Graph Type");
		typeChoice.addItem("PDF");
		typeChoice.addItem("CDF");
		//Distribution Choice
		distributionChoice.addItemListener(this);
		distributionChoice.setToolTipText("Distribution");
		distributionChoice.addItem("Normal");
		distributionChoice.addItem("Gamma");
		distributionChoice.addItem("Chi-square");
		distributionChoice.addItem("Student");
		distributionChoice.addItem("F");
		distributionChoice.addItem("Beta");
		distributionChoice.addItem("Weibull");
		distributionChoice.addItem("Pareto");
		distributionChoice.addItem("Logistic");
		distributionChoice.addItem("Lognormal");
		distributionChoice.addItem("Extreme Value");
		//toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(typeChoice);
		toolBar.add(distributionChoice);
		toolBar.add(parameter1Scroll);
		toolBar.add(parameter2Scroll);
		toolBar.add(aboutButton);
		//Field Panel
		quantileField.addActionListener(this);
		probField.addActionListener(this);
		fieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		fieldPanel.setToolTipText("Change either value and press Enter to update");
		fieldPanel.add(quantileLabel);
		fieldPanel.add(quantileField);
		fieldPanel.add(probLabel);
		fieldPanel.add(probField);
		probField.setText("0.5000");
		quantileField.setText("0.000");
		//Panels
		this.getContentPane().add("North", toolBar);
		this.getContentPane().add("South", fieldPanel);
		this.getContentPane().add("Center", quantileGraph);
	}


	/**
	* This method returns basic information about the applet, including
	* copyright information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return "\nCopyright (C) 2002, Kyle Siegrist, Dawn Duehring.\n\n"
			+ "This program is free software; you can redistribute it and/or modify it under the terms \n"
			+ "of the GNU General Public License as published by the Free Software Foundation. \n"
			+ "This program is distributed in the hope that it will be useful, but without any warranty;\n"
			+ "without even the implied warranty of merchantability or fitness for a particular purpose.\n\n"
			+ "This applet displays the value (quantile) x  and the cumulative probability p = F(x) for a\n"
			+ "variety of distributions. The following distributions can be chosen from the list box:\n"
			+ "normal, gamma, chi-square, student t, F, beta, Weibull, Pareto, logistic, lognormal,\n"
			+ "The quantile x and probability p are shown graphically in terms of either the probability\n"
			+ "density function or the cumulative distribution function; the type of graph can be chosen\n"
			+ "from a list box. The parameters of the chosen distribution can be varied with sliders.\n"
			+ "Enter a new value of x in the quantile field or a new value of p in the probability field\n"
			+ "and then press Enter to update the display.";
	}

	/**
	* This method handles the events associated with changes in the text fields
	* (the quantile field and the probability field), and handles events associated
	* with the about button.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == quantileField){
			quantile = Double.valueOf(quantileField.getText()).doubleValue();
			prob = distribution.getCDF(quantile);
			probField.setText(format(prob));
			quantileGraph.setQuantile(quantile);
			quantileGraph.repaint();
		}
		else if (e.getSource() == probField){
			prob = Double.valueOf(probField.getText()).doubleValue();
			quantile = distribution.getQuantile(prob);
			quantileField.setText(format(quantile));
			quantileGraph.setQuantile(quantile);
			quantileGraph.repaint();
		}
		else if (e.getSource() == aboutButton){
			JOptionPane.showMessageDialog(frame, getName() + getAppletInfo(), "About " + getName(), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	* This method handles the choice events associated with changes in the type of graph
	* (density or quantile) and changes in the distribution type.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == typeChoice) quantileGraph.setFunctionType(typeChoice.getSelectedIndex() + 1);
		else if (e.getSource() == distributionChoice){
			distributionType = distributionChoice.getSelectedIndex();
			switch(distributionType){
			case 0:	 //Normal
				parameter1Scroll.setProperties(-20, 20, 0.5, 0, "Mean", "\u03bc");
				parameter2Scroll.setProperties(0.5, 20, 0.5, 1, "Standard deviation", "\u03c3");
				break;
			case 1:	 //Gamma
				parameter1Scroll.setProperties(1, 20, 0.5, 1, "Shape", "k");
				parameter2Scroll.setProperties(0.5, 20, 0.5, 1, "Scale", "b");
				break;
			case 2:	 //Chi-square
				parameter1Scroll.setProperties(1, 100, 1, 2, "Degrees of freedom", "n");
				parameter2Scroll.setVisible(false);
				break;
			case 3:	 //Student
				parameter1Scroll.setProperties(1, 100, 1, 3, "Degrees of freedom", "n");
				parameter2Scroll.setVisible(false);
				break;
			case 4:	 //Fisher
				parameter1Scroll.setProperties(1, 100, 1, 5, "Numerator degrees of freedom", "n");
				parameter2Scroll.setProperties(1, 100, 1, 5, "Denominator degrees of freedom", "m");
				break;
			case 5:	 //Beta
				parameter1Scroll.setProperties(0.5, 20, 0.5, 1, "Left parameter", "a");
				parameter2Scroll.setProperties(0.5, 20, 0.5, 1, "Right parameter", "b");
				break;
			case 6:   //Weibull
				parameter1Scroll.setProperties(1, 20, 0.5, 1, "Shape", "k");
				parameter2Scroll.setProperties(0.5, 20, 0.5, 1, "Scale", "b");
				break;
			case 7:   //Pareto
				parameter1Scroll.setProperties(1, 20, 1, 1, "Shape", "a");
				parameter2Scroll.setVisible(false);
				break;
			case 8:   //Logistic
				parameter1Scroll.setVisible(false);
				parameter2Scroll.setVisible(false);
				break;
			case 9:	 //Lognormal
				parameter1Scroll.setProperties(-2, 2, 1, 0, "paarameter", "\u03bc");
				parameter2Scroll.setProperties(0.5, 2, 1, 0.5, "parameter", "\u03c3");
				break;
			case 10: //Extreme Value
				parameter1Scroll.setVisible(false);
				parameter2Scroll.setVisible(false);
				break;
			}
			setDistribution();
		}
	}

	/**
	* This method handles events associated with changes in the distribution parameters.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == parameter1Scroll.getSlider()) setDistribution();
		else if (e.getSource() == parameter2Scroll.getSlider()) setDistribution();
	}

	/**
	* This method sets the distribution when the parameters change.
	*/
	public void setDistribution(){
		double a, k, b, mu, sigma;
		int n, m;
		switch(distributionType){
		case 0:	 //Normal
			mu = parameter1Scroll.getValue();
			sigma = parameter2Scroll.getValue();
			distribution = new NormalDistribution(mu, sigma);
			break;
		case 1:	 //Gamma
			k = parameter1Scroll.getValue();
			b = parameter2Scroll.getValue();
			distribution = new GammaDistribution(k, b);
			break;
		case 2:	 //Chi-square
			n = (int)parameter1Scroll.getValue();
			distribution = new ChiSquareDistribution(n);
			break;
		case 3:	 //Student
			n = (int)parameter1Scroll.getValue();
			distribution = new StudentDistribution(n);
			break;
		case 4:	 //Fisher
			m = (int)parameter1Scroll.getValue();
			n = (int)parameter2Scroll.getValue();
			distribution = new FisherDistribution(m, n);
			break;
		case 5:	 //Beta
			a = parameter1Scroll.getValue();
			b = parameter2Scroll.getValue();
			distribution = new BetaDistribution(a, b);
			break;
		case 6:		//Weibull
			k = parameter1Scroll.getValue();
			b = parameter2Scroll.getValue();
			distribution = new WeibullDistribution(k, b);
			break;
		case 7:   //Pareto
			a = parameter1Scroll.getValue();
			distribution = new ParetoDistribution(a);
			break;
		case 8:   //Logistic
			distribution = new LogisticDistribution();
			break;
		case 9:   //Lognormal
			mu = parameter1Scroll.getValue();
			sigma = parameter2Scroll.getValue();
			break;
		case 10:  //Extreme Value
			distribution = new ExtremeValueDistribution();
			break;
		}
		quantile = distribution.getQuantile(prob);
		quantileGraph.setDistribution(distribution);
		quantileGraph.setQuantile(quantile);
		quantileGraph.repaint();
		quantileField.setText(format(quantile));
		probField.setText(format(prob));
	}

	/**
	* This method formats numbers.
	* @param x the number to be formatted.
	* @return the number formatted as a string
	*/
	public String format(double x){
		if (Double.isInfinite(x) & x < 0) return "-inf";
		else if (Double.isInfinite(x) & x > 0) return "inf";
		else if (Double.isNaN(x)) return "*";
		else return decimalFormat.format(x);
	}

	/**
	* This method gets the frame associated with a given component.
	* @ param c the component
	* @return the frame containing the component
	*/
	private static Frame getFrame(Component c){
		Frame frame = null;
		while((c = c.getParent()) != null){
			if (c instanceof Frame) frame = (Frame)c;
		}
		return frame;
	}
}

