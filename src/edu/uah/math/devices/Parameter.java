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
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.event.*;
import java.text.DecimalFormat;
import java.io.Serializable;

/**
* This class consists of a slider and a label, and is intended for adjusting
* the value of a real parameter. The real parameter is characterized by
* a minimum value, a maximum value, a step size, a current value, a name,
* and a symbol. The name is intended to be descriptive and is used in the tool
* tip. The symbol is used in the label.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Parameter extends JPanel implements ChangeListener, Serializable{
	private double value, min, max, stepSize;
	private int steps, width;
	private JSlider slider = new JSlider();
	private JLabel label = new JLabel();
	private DecimalFormat decimalFormat = new DecimalFormat();
	private String name, symbol;

	/**
	* This general constructor creates a new parameter object for adjusting a parameter.
	* @param a the minimum value of the parameter
	* @param b the maximum value of the parameter
	* @param h the step size of the parameter
	* @param v the initial value of the parameter
	* @param n the name of the parameter
	* @param s the symbol used for the parameter
	* @param w the width dimension of the panel containing the slider and label
	*/
	public Parameter(double a, double b, double h, double v, String n, String s, int w){
		width = w;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		slider.addChangeListener(this);
		slider.setOrientation(JSlider.HORIZONTAL);
		setStrings(n, s);
		add(slider);
		add(label);
		validate();
		setRange(a, b, h, v);
		setValue(v);
	}

	/**
	* This special constructor creates a new parameter object for adjusting a parameter, with default
	* size 175.
	* @param a the minimum value of the parameter
	* @param b the maximum value of the parameter
	* @param h the step size of the parameter
	* @param v the initial value of the parameter
	* @param n the name of the parameter
	* @param s the symbol used for the parameter
	*/
	public Parameter(double a, double b, double h, double v, String n, String s){
		this(a, b, h, v, n, s, 175);
	}

	/**
	* This default constructor creates a new parameter object with min value 0,
	* max value 1, step size 1/10, value 1/2, name "Probability", and symbol "p".
	*/
	public Parameter(){
		this(0, 1, 0.1, 0.5, "Probability", "p");
	}

	/**
	* This method sets the minimum value of the parameter.
	* @param a the minimum value
	*/
	public void setMin(double a){
		setRange(a, max, stepSize, value);
	}

	/**
	* This method sets the maximum value of the parameter.
	* @param b the maximum value
	*/
	public void setMax(double b){
		setRange(min, b, stepSize, value);
	}

	/**
	* This method sets the step size of the parameter.
	* @param w the step size
	*/
	public void setStepSize(double w){
		setRange(min, max, w, value);
	}

	/**
	* This method sets the value of the parameter.
	* @param v the value of the parameter
	*/
	public void setValue(double v){
		//Adjust for invalid parameter value
		if (v < min) v = min; else if (v > max) v = max;
		value = v;
		//Set scrollbar value
		slider.setValue((int)Math.rint((value - min) / stepSize));
		label.setText(symbol + " = " + decimalFormat.format(getValue()));
	}

	/**
	* This method sets the range of the parameter
	* @param a the minimum value of the parameter
	* @param b the maximum value of the parameter
	* @param w the step size of the parameter
	* @param v the value of the parameter
	*/
	public void setRange(double a, double b, double w, double v){
		//Adjust for invalid values
		if (a > b) a = b;
		if (w < 0) w = 1;
		if (v < a) v = a; else if (v > b) v = b;
		min = a; max = b; stepSize = w;
		steps = (int)Math.rint((max - min) / stepSize);
		//Set scrollbar values
		slider.setMinimum(0);
		slider.setMaximum(steps + 1);
		setValue(v);
	}

	/**
	* This method sets the range and strings.
	* @param a the minimum value of the parameter
	* @param b the maximum value of the parameter
	* @param w the step size of the parameter
	* @param v the value of the parameter
	* @param n the name of the parameter
	* @param s the symbol of the parameter
	*/
	public void setProperties(double a, double b, double w, double v, String n, String s){
		setRange(a, b, w, v);
		setStrings(n, s);
		setVisible(true);
	}

	/**
	* This method sets the name and symbol.
	* @param n the name of the parameter
	* @param s the symbol of the parameter
	*/
	public void setStrings(String n, String s){
		name = n;
		symbol = " " + s;
		slider.setToolTipText(name);
		label.setToolTipText(name);
		label.setText(symbol + " = " + decimalFormat.format(getValue()));
	}

	/**
	* This method sets the name.
	* @param n the name of the parameter
	*/
	public void setName(String n){
		setStrings(n, symbol);
	}

	/**
	* This method returns the name
	* @return the name of the parameter
	*/
	public String getName(){
		return name;
	}

	/**
	* This method sets the symbol
	* @param s the symbol of the parameter
	*/
	public void setSymbol(String s){
		setStrings(name, s);
	}

	/**
	* This method returns the symbol
	* @return the symbol of the parameter
	*/
	public String getSymbol(){
		return symbol;
	}

	/**
	* This method sets the width of the component in pixels
	* @param w the width
	*/
	public void setWidth(int w){
		width = w;
	}

	/**
	* This method gets the value of the parameter.
	* @return the value of the parameter
	*/
	public double getValue(){
		value = min + slider.getValue() * stepSize;
		if (value > max) value = max;
		return value;
	}

	/**
	* This method gets the minimum value of the parameter.
	* @return the minimum value of the parameter
	*/
	public double getMin(){
		return min;
	}

	/**
	* This method gets the maximum value of the parameter.
	* @return the maximum value of the parameter
	*/
	public double getMax(){
		return max;
	}

	/**
	* This method gets the step size of the parameter.
	* @return the step size of the parameter
	*/
	public double getStepSize(){
		return stepSize;
	}

	/**
	* This method gets the number of steps.
	* @return the number of steps between the minimum and maximum values
	*/
	public int getSteps(){
		return steps;
	}

	/**
	* This method returns the slider.
	* @return the slider
	*/
	public JSlider getSlider(){
		return slider;
	}

	/**
	* This method returns the label.
	* @return the label
	*/
	public JLabel getLabel(){
		return label;
	}

	/**
	* This method applys the decimal format to be used in the label
	* @param p the decimal pattern.
	*/
	public void applyDecimalPattern(String p){
		decimalFormat.applyPattern(p);
		label.setText(symbol + " = " + decimalFormat.format(getValue()));
	}

	/**
	* This method handles the events associated with changing
	* the scrollbar. The value of the parameter is shown in the label.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == slider)
		label.setText(symbol + " = " + decimalFormat.format(getValue()));
	}

	/**
	* This method specifies that the minimum size should be the same as the preferred size.
	* @return the dimension object corresponding to the preferred size.
	*/
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	/**
	* This method sets the width of the preferred size to the specified value.
	* @return the dimesnion object corresponding to the preferred size.
	*/
	public Dimension getPreferredSize() {
		return new Dimension(width, super.getPreferredSize().height);
	}

	/**
	* The method specifies that the maximum size should be the same as the preferred size.
	* @return the dimension object corresponding to the preferred size.
	*/
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

}
