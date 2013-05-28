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
import java.awt.Graphics;
import java.awt.Dimension;
import java.io.Serializable;

/**
* This class shows the graph of the random walk in the ballot experiment.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BallotGraph extends Graph{
	//Variables
	private int winnerTotal, loserTotal, voteTotal;
	private int[] winnerLead;
	private boolean winnerAlwaysAhead, votesCounted;
	private Color graphColor = Color.red;

	/**
	* This general constructor creates a new ballot graph with specified values of
	* the winner total (the number of votes that the winner receives) and loser total
	* (the number of votes that the loser receives).
	* @param a the winner count.
	* @param b the loser count.
	*/
	public BallotGraph(int a, int b){
		setPointSize(5);
		setParameters(a, b);
		setToolTipText("Winner lead graph");
	}

	/**
	* This defaut constructor creates a new ballot graphs with winner total 50 and
	* lose count 30. In this default constructor, the votes are counted.
	*/
	public BallotGraph(){
		this(50, 30);
	}

	/**
	* This method sets the basic parameters: the winner count and loser count.
	* @param a the winner count
	* @param b the loser count.
	*/
	public void setParameters(int a, int b){
		//Correct for invalid parameters
		if (a < 1) a = 1;
		if (b < 0) b = 0;
		else if (b >= a) b = a - 1;
		//Assign parameters
		winnerTotal = a;
		loserTotal = b;
		voteTotal = winnerTotal + loserTotal;
		//Initialize array and set graph scale
		winnerLead = new int[voteTotal + 1];
		setScale(0, voteTotal, -loserTotal, winnerTotal);
	}

	/**
	* This method sets the total number of votes that the winner receives.
	* @param a the winner total
	*/
	public void setWinnerTotal(int a){
		setParameters(a, loserTotal);
	}

	/**
	* This method returns the total number of votes that the winner receives.
	* @return the winner total.
	*/
	public int getWinnerTotal(){
		return winnerTotal;
	}

	/**
	* This method sets the total number of votes that the loser receives.
	* @param b the loser total
	*/
	public void setLoserTotal(int b){
		setParameters(winnerTotal, b);
	}

	/**
	* This method returns the total number of votes that the loser receives.
	*@return the loser count.
	*/
	public int getLoserTotal(){
		return loserTotal;
	}

	/**
	* This method returns the total number of votes.
	* @return the vote total.
	*/
	public int getVoteTotal(){
		return voteTotal;
	}

	/**
	* This method randomly counts the votes.
	*/
	public void countVotes(){
		int winnerVotesLeft = winnerTotal, loserVotesLeft = loserTotal, vote;
		winnerAlwaysAhead = true;
		winnerLead[0] = 0;
		for (int i = 1; i <= voteTotal; i++){
			if (Math.random() < (double) winnerVotesLeft / (winnerVotesLeft + loserVotesLeft)){
				vote = 1;
				winnerVotesLeft = winnerVotesLeft - 1;
			}
			else{
				vote = -1;
				loserVotesLeft = loserVotesLeft - 1;
			}
			winnerLead[i] = winnerLead[i - 1] + vote;
			if (winnerLead[i] <= 0) winnerAlwaysAhead = false;
		}
		votesCounted = true;
	}

	/**
	* This method gets the winner lead at a specified time index.
	* @param i the index
	* @return the numer of votes that the winner is ahead after i votes are counted.
	*/
	public int getWinnerLead(int i){
		//Correct for invalid parameter
		if (i < 0) i = 0;
		else if (i > voteTotal) i = voteTotal;
		return winnerLead[i];
	}

	/**
	* This method gets the array of winner leads.
	* @return the array that gives the number of votes that the winner is ahead
	*/
	public int[] getWinnerLead(){
		return winnerLead;
	}

	/**
	* This method gets the state of the ballot event (true or false).
	* @return true if the winner is always ahead in the vote count
	*/
	public boolean isWinnerAlwaysAhead(){
		return winnerAlwaysAhead;
	}

	/**
	* This method gets the state of the votes counted variable
	* @return true if the votes have been counted.
	*/
	public boolean isVotesCounted(){
		return votesCounted;
	}

	/**
	* This method gets the probability of the ballot event.
	* @return the probability of the ballot event.
	*/
	public double getProbability(){
		return (double)(winnerTotal - loserTotal) / (winnerTotal + loserTotal);
	}

	/**
	* This method draws the graph of the random walk.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Draw axes
		g.setColor(Color.black);
		drawLine(g, 0, 0, voteTotal, 0);
		for (int i = 0; i <= voteTotal; i++) drawTick(g, i, 0, VERTICAL);
		drawLabel(g, "0", 0, 0, LEFT);
		drawLabel(g, format(voteTotal), voteTotal, 0, RIGHT);
		drawAxis(g, -loserTotal, winnerTotal, 1, 0, VERTICAL);
		g.setColor(Color.gray);
		drawAxis(g, -loserTotal, winnerTotal, 1, voteTotal, VERTICAL);
		//Draw data
		if (votesCounted){
			g.setColor(graphColor);
			for (int i = 0; i < voteTotal; i++) drawLine(g, i, winnerLead[i], i + 1, winnerLead[i + 1]);
			drawPoint(g, voteTotal, winnerTotal - loserTotal);
		}
	}

	/**
	* This method resets the balot graph.
	*/
	public void reset(){
		votesCounted = false;
		winnerLead = new int[voteTotal + 1];
		repaint();
	}

	/**
	* This method sets the color for the graph of the random walk (the
	* winners lead as a function of the number of votes counted).
	* @param c the graph color
	*/
	public void setGraphColor(Color c){
		graphColor = c;
	}

	/**
	* This method returns the color for the graph of the random walk (the
	* winners lead as a function of the number of votes counted).
	* @return the graph color
	*/
	public Color getGraphColor(){
		return graphColor;
	}
}






