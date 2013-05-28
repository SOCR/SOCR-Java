/*
Copyright (C) 2001-2007  Ivo Dinov
The SOCR Resource, UCLA

http://www.SOCR.ucla.edu

This program is licensed under a Creative Commons License. Basically, you are free to copy,
distribute, and modify this program, and  to make commercial use of the program.
However you must give proper attribution.
See http://creativecommons.org/licenses/by/2.0/ for more information.

*/
package edu.uah.math.devices;
import java.awt.Color;
import java.awt.Graphics;
//import java.awt.Dimension;
//import java.io.Serializable;

/**
* This class shows the graph of the random walk in the CoinToss LLN Experiment.
* @author Ivo Dinov
* @version February 15, 2007
*/
public class CoinTossLLNGraph extends Graph{
	//Variables
	private int heads, tails, total, index;		// tails=0;   heads=1
	private int [] coinTosses;
	private float p;  							// theoretical probability;
	private float[] proportions, differences; 	// Proportions=heads/total;  differences=2|H-T|/total.
	private Color graphColor = Color.red;
	private Color graphColor1 = Color.green;

	/**
	* This general constructor creates a new CoinToss LLN Graph with specified total
	* (the number of experiments (coin tosses).
	* @param total the total number of trials.
	*/
	public CoinTossLLNGraph(int _total){
		total = _total;
		setPointSize(5);
		setParameters(_total);
		setIndex(0);
		setToolTipText("Coin Toss LLN Graph; Proportions-of-Heads=RED Graph; Differences|H-T|=GREEN Graph");
		
		setScale(0, total, 0, 1);
	}

	/**
	* This defaut constructor creates a new CoinTossLLNGraph with 50 Heads and
	* 30 Tails.
	*/
	public CoinTossLLNGraph(){
		this(50);
	}

	/**
	* This method sets the basic parameter: number of experiments.
	* @param t the total number of experiments.
	*/
	public void setParameters(int t){
		//Correct for invalid parameters
		if (t < 0) {
			total = 0;
			coinTosses = new int[total+2];
			proportions = new float[total+2];
			differences = new float[total+2];
		}
		else {
			//	Initialize arrays and set graph scale
			total=t;
			coinTosses = new int[total+2];
			proportions = new float[total+2];
			differences = new float[total+2];
		}
		setScale(0, total, 0, 1);
		reset();
		repaint();
	}

	/**
	* This method sets the theoretical probability.
	* @param p is the theoretical probability for a Head
	*/
	public void setProbability(float _p){
		if (0>_p || _p>1)  p=(float)0.5;
		else p = _p;
		reset();
		return;
	}

	/**
	* This method gets the theoretical probability.
	* @param p is the theoretical probability for a Head
	*/
	public float getProbability(){
		return p;
	}

	/**
	* This method returns the number of Heads up to index (ind).
	* @return the number of Heads.
	*/
	public int getHeads(int ind){
		int headCount=0;
		for (int i=0; i<=ind; i++) 
			if (coinTosses[i]==1) headCount++;			
		return headCount;
	}

	/**
	* This method returns the Tails up to index (ind).
	* @return the number of Tails.
	*/
	public int getTails(int ind){
		int tailCount=0;
		for (int i=0; i<=ind; i++) 
			if (coinTosses[i]==0) tailCount++;			
		return tailCount;
	}

	/**
	* This method returns the total number of Heads.
	* @return the number of Heads.
	*/
	public int getHeads(){
		int headCount=0;
		for (int i=0; i<=total; i++) 
			if (coinTosses[i]==1) headCount++;			
		return headCount;
	}

	/**
	* This method returns the total number of Tails.
	* @return the number of Tails.
	*/
	public int getTails(){
		int tailCount=0;
		for (int i=0; i<=total; i++) 
			if (coinTosses[i]==0) tailCount++;			
		return tailCount;
	}

	/**
	* This method returns the total number of experiments (H + T).
	* @return the total number of experiments (Heads + Tails).
	*/
	public int getTotal(){
		return total;
	}

	/**
	* This method returns the Current Index.
	* @return the current index.
	*/
	public int getIndex(){
		return index;
	}

	/**
	* This method sets the Current Index.
	* @return the current index.
	*/
	public void setIndex(int i){
		if (i>=0 && i<=total) index=i;
	}

	/**
	* This method sets the total number of experiments to be done.
	* @param t is the Total value.
	* @return.
	*/
	public void setTotal(int _total){
		total=_total;
		setParameters(total);
		return;
	}

	/**
	* This method adds a Head at a specified time index.
	* @param i the index
	* @return void
	*/
	public void addAHead(int i){
		//Correct for invalid parameter
		if (i<0) i=0;
		else if (i>total) i=total;
		coinTosses[i]=1;
		setDifferences(i, (float)((1-p)*getHeads(i)-p*getTails(i)));
		setProportions(i, (float)getHeads(i)/i);
		setIndex(i);
		return;
	}

	/**
	* This method adds a Head at the next index.
	* @return void
	*/
	public void addAHead(){
		index++;
		coinTosses[index]=1;
		setDifferences(index, (float)((1-p)*getHeads(index)-p*getTails(index)));
		setProportions(index, (float)getHeads(index)/index);
		System.err.println("Difference("+index+")="+getDifferences(index));
		System.err.println("Proportion("+index+")="+getProportion(index));
		return;
	}

	/**
	* This method adds a Tails at a specified time index.
	* @param i the index
	* @return void
	*/
	public void addATail(int i){
		//Correct for invalid parameter
		if (i<0) i=0;
		else if (i>total) i=total;
		coinTosses[i]=0;
		setDifferences(i, (float)((1-p)*getHeads(i)-p*getTails(i)));
		setProportions(i, (float)getHeads(i)/i);
		index=i;
		return;
	}

	/**
	* This method adds a Tails at the next index.
	* @return void
	*/
	public void addATail(){
		index++;
		coinTosses[index]=0;
		setDifferences(index, (float)((1-p)*getHeads(index)-p*getTails(index)));
		setProportions(index, (float)getHeads(index)/index);
		System.err.println("Difference("+index+")="+getDifferences(index));
		System.err.println("Proportion("+index+")="+getProportion(index));
		return;
	}

	/**
	* This method computes the Proportions and Difference Statistics up to current index.
	* @param i the index
	* @return void
	*/
	public void computeStats(int i){
		setDifferences(i, (float)((1-p)*getHeads(i)-p*getTails(i)));
		setProportions(i, (float)getHeads(i)/i);
		return;
	}

	/**
	* This method gets the H/(H+T) proportion at a specified time index.
	* @param i the index
	* @return the nthe H/(H+T) proportion at time i.
	*/
	public float getProportion(int i){
		//Correct for invalid parameter
		if (i < 0) i = 0;
		else if (i > total) i = total;
		return proportions[i];
	}

	/**
	* This method sets the 2|H-T|/Total difference at a specified time index.
	* @param i the index
	* @param d the difference value
	* @return void
	*/
	public void setDifferences(int i, float d){
		//Correct for invalid parameter
		if (i<0) i=0;
		else if (i>total) i=total;
		//if (d < 0) differences[i] = 0;
		//else if (d>1) differences[i] = 1;
		else differences[i] = d;
		return;
	}

	/**
	* This method computes the Maximum difference for the sample (for graph normalization)
	* @return double
	*/
	public double maxDiff(){
		double max = 0;
		for (int i=0; i< total; i++)
			if (max<Math.abs(differences[i]))	max = Math.abs(differences[i]);
		return max;
	}

	/**
	* This method sets the Heads Proportion at a specified time index.
	* @param i the index
	* @param p the proportion
	* @return void
	*/
	public void setProportions(int i, float p){
		//Correct for invalid parameter
		if (i<0) i=0;
		else if (i>total) i=total;
		if (p < 0) proportions[i] = 0;
		else if (p>1) proportions[i] = 1;
		else proportions[i] = p;
		return;
	}

	/**
	* This method gets the 2|H-T|/Total difference at a specified time index.
	* @param i the index
	* @return the the 2|H-T|/Total difference at time i.
	*/
	public float getDifferences(int i){
		//Correct for invalid parameter
		if (i < 0) i = 0;
		else if (i > total) i = total;
		return differences[i];
	}

	/**
	* This method draws the graph of the random walk.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Draw axes
		g.setColor(Color.black);
		drawLine(g, 0, 0, total, 0);	// X-axis
		for (int i = 0; i <= total; i++) drawTick(g, i, 0, VERTICAL);
		
		drawLabel(g, "0", 0, 0, LEFT);
		drawLabel(g, format(total), total, 0, ABOVE);	// Perhaps draw this on the LEFT!
		
		drawAxis(g, 0, 1, 1, 0, VERTICAL);
		
			// Draw the second Y-axis (right hand-size)!
		g.setColor(Color.gray);
		//drawAxis(g, 0, 1, 1, total, VERTICAL);
		drawLine(g, total, 0, total, 1);
		drawLabel(g, format(-maxDiff()), total, 0, BELOW);
		drawLabel(g, format(maxDiff()), total, 1, ABOVE);
		
		// Draw Model
		if (showModelDistribution){
			g.setColor(Color.blue);
			drawLine(g, 0, p, total, p);
		}
		
		//Draw data
		for (int i = 0; i < index; i++) {
			g.setColor(graphColor);
			drawLine(g, i, proportions[i], i + 1, proportions[i + 1]);
			//drawLabel(g, "p", 0, proportions[i], LEFT);
			g.setColor(graphColor1);
			drawLine(g, i, p + differences[i]/maxDiff(), i + 1, p + differences[i + 1]/maxDiff());
		}
	}

	/**
	* This method resets the balot graph.
	*/
	public void setAllValues(int [] newValues){
		setParameters(newValues.length);
		int hCounter=0, tCounter=0;
		
		for (int i=0; i<total; i++) {
			coinTosses[i]=newValues[i];
			if (coinTosses[i]==0) { 
				tCounter++;
				addATail(i);
			}
			else {
				hCounter++;
				addAHead(i);
			}
		}
	}

	
	/**
	* This method resets the balot graph.
	*/
	public void reset(){
		for (int i=0; i<=total; i++) coinTosses[index]=-1;
		index=0;
		repaint();
	}

	/**
	* This method sets the color for the graph of the random walk.
	* @param c the graph color
	*/
	public void setGraphColor(Color c){
		graphColor = c;
	}

	/**
	* This method returns the color for the graph of the random walk.
	* @return the graph color
	*/
	public Color getGraphColor(){
		return graphColor;
	}
}
