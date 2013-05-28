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
import java.awt.Color;
import java.io.Serializable;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.IntervalData;

/**
* This class models a Markov chain with states represented by ball objects.  The number of
* states, the transition matrix, and the initial state can be specified.
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class UrnChain extends Urn implements Serializable{
	//Variables
	private int initialState = 0, state = 0, time = 0;
	private double[][] probabilities;
	private IntervalData data;
	private String name = "X";
	private Color defaultColor = new Color(0, 180, 0), stateColor = Color.red;

	/**
	* This general constructor creates a new Markov chain with a specified number of states,
	* a specified transition matrix, and a specified name.
	* @param n the number of states
	* @param p the transition matrix.
	*/
	public UrnChain(int n, double[][] p, String s){
		super(n, 26);
		setToolTipText("Urn Chain");
		setProbabilities(p);
		setName(s);
	}

	/**
	* This special constructor creates a new Markov chain with a specified number of states,
	* a specified transition matrix, and the default name "X".
	* @param n the number of states.
	* @param p the transition matrix
	*/
	public UrnChain(int n, double[][]p){
		this(n, p, "X");
	}


	/*
	* This special constructor creates a new Markov chain with a specified number of states,
	* the uniform transition matrix, and the default name "X".
	* @param n the number of states
	*/
	public UrnChain(int n){
		super(n, 26);
		setToolTipText("Urn Chain");
		setProbabilities();
		setName("X");
	}

	/*
	* This defalut constructor creates a new Markov chain with a 5 states,
	* the uniform transition matrix, and the default name "X".
	*/
	public UrnChain(){
		this(5);
	}


	/**
	* This method computes the next state to be visited,
	* according to the probabilities in the transition matrix.
	*/
	public void move(){
		int oldState = state,  index = 0;
		double sum = probabilities[oldState][index], t = Math.random();
		while (sum < t){
			index = index + 1;
			sum = sum +  probabilities[oldState][index];
		}
		state = index;
		data.setValue(state);
		time++;
		getBall(oldState).setBallColor(defaultColor);
		getBall(state).setBallColor(stateColor);

	}

	/**
	* This method return the interval data.
	* @return the interval data
	*/
	public IntervalData getData(){
		return data;
	}

	/**
	* This method sets up the Markov chain as an array of ball objects.  The number of states
	* is specified and the probabilities are set to be uniform.
	* @param n the number of states
	*/
	public void setBallCount(int n){
		Ball ball;
		super.setBallCount(n);
		for (int i = 0; i < n; i++){
			ball = getBall(i);
			ball.setValue(i);
			ball.setDrawn(true);
			ball.setToolTipText("State " + i);
		}
		data = new IntervalData(new Domain(0, getBallCount() - 1, 1, Domain.DISCRETE), name);
		setProbabilities();
		setInitialState(0);
		reset();
		validate();
	}

	/**
	* This method returns the current state
	* @return the current state
	*/
	public int getState(){
		return state;
	}

	/**
	* This method sets the current state.
	* @param i the current state
	*/
	public void setState(int i){
		getBall(state).setBallColor(defaultColor);
		state = i;
		getBall(state).setBallColor(stateColor);
	}

	/**
	* This method sets the initial state.
	* @param i the initial state
	*/
	public void setInitialState(int i){
		initialState = i;
	}

	/**
	* This method returns the initial state.
	* @return the initial state
	*/
	public int getInitialState(){
		return initialState;
	}

	/**
	* This method returns the current time.
	* @return the discrete time parameter
	*/
	public int getTime(){
		return time;
	}

	/**
	* This method returns the transition matrix.
	* @return the transition matrix
	*/
	public double[][] getProbabilities(){
	    return probabilities;
	}

	/**
	* This method sets the probabilities.
	* @param p the transition matrix
	*/
	public void setProbabilities(double[][] p){
		probabilities = p;
	}

	/** This method sets the probabilitites in each row of the transition matrix
	* to be uniform.  The number of states, i.e. the dimension of the matrix,
	* is specified.
	*/
	public void setProbabilities(){
		// create a new transition matrix of the of dimension n
		int ballCount = getBallCount();
		probabilities = new double[ballCount][ballCount];
		for(int i = 0; i < ballCount; i++)
			for(int j = 0; j < ballCount; j++) probabilities[i][j] = 1.0 / ballCount;
    }

	/**
	* This method resets the interval data.
	*/
	public void reset(){
		data.reset();
		time = 0;
		setBallColor(defaultColor);
		setState(initialState);
		repaint();
	}

	/**
	* This method sets the name of the chain.
	* @param n the name
	*/
	public void setName(String n){
		name = n;
		data.setName(n);
	}

	/**
	* This method returns the name of the chain.
	* @return the name
	*/
	public String getName(){
		return name;
	}

	/**
	* This method sets the default color, the color of all states except the current state.
	* @param c the default color
	*/
	public void setDefaultColor(Color c){
		defaultColor = c;
		repaint();
	}

	/**
	* This method gets the default color, the color of all states except the current state.
	* @return the default color
	*/
	public Color getDefaultColor(){
		return defaultColor;
	}

	/**
	* This method sets the state color, the color of the current state.
	* @param c the state color
	*/
	public void setStateColor(Color c){
		stateColor = c;
		repaint();
	}

	/**
	* This method gets the state color, the color of the current state.
	* @return the state color
	*/
	public Color getStateColor(){
		return stateColor;
	}

}