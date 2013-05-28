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
import java.awt.Color;
import java.awt.Graphics;

/**This class shows the graph of the random walk in the ballot experiment*/
public class BallotGraph extends Graph{
	//Variables
	private int winnerCount, loserCount, totalCount;
	private int[] value;
	private boolean ballotEvent, data;

	/**This general constructor creates a new ballot graph with specified values of
	the winner count and loser count*/
	public BallotGraph(int a, int b){
		setPointSize(5);
		setParameters(a, b);
	}

	/**This defaut constructor creates a new ballot graphs with winner count 10 and
	lose count 5*/
	public BallotGraph(){
		this(10, 5);
	}

	/**This method sets the basic parameters: the winner count and loser count*/
	public void setParameters(int a, int b){
		//Correct for invalid parameters
		if (a < 1) a = 1;
		if (b < 0) b = 0;
		else if (b >= a) b = a - 1;
		//Assign parameters
		winnerCount = a;
		loserCount = b;
		totalCount = winnerCount + loserCount;
		//Initialize array and set graph scale
		value = new int[totalCount + 1];
		setScale(0, totalCount, -loserCount, winnerCount);
	}

	/**This method returns the winner count*/
	public int getWinnerCount(){
		return winnerCount;
	}

	/**This method returns the loser count*/
	public int getLoserCount(){
		return loserCount;
	}

	/**This method returns the total count*/
	public int getTotalCount(){
		return totalCount;
	}

	/**This method compute the values of the random walk*/
	public void walk(){
		int rightSteps = winnerCount, leftSteps = loserCount, step;
		ballotEvent = true;
		value[0] = 0;
		for (int i = 1; i <= totalCount; i++){
			if (Math.random() < (double) rightSteps / (rightSteps + leftSteps)){
				step = 1;
				rightSteps = rightSteps - 1;
			}
			else{
				step = -1;
				leftSteps = leftSteps - 1;
			}
			value[i] = value[i - 1] + step;
			if (value[i] <= 0) ballotEvent = false;
		}
		data = true;
	}

	/**This method gets the value of the random walk at a specified index*/
	public int getValue(int i){
		//Correct for invalid parameter
		if (i < 0) i = 0;
		else if (i > totalCount) i = totalCount;
		return value[i];
	}

	/**This method gets the state of the ballot event*/
	public boolean ballotEvent(){
		return ballotEvent;
	}

	/**This method gets the probability of the ballot event*/
	public double getProbability(){
		return (double)(winnerCount - loserCount) / (winnerCount + loserCount);
	}

	/**This method draws the graph of the random walk*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Draw axes
		g.setColor(Color.black);
		drawLine(g, 0, 0, totalCount, 0);
		for (int i = 0; i <= totalCount; i++) drawTick(g, i, 0, VERTICAL);
		drawLabel(g, "0", 0, 0, LEFT);
		drawLabel(g, format(totalCount), totalCount, 0, RIGHT);
		drawAxis(g, -loserCount, winnerCount, 1, 0, VERTICAL);
		g.setColor(Color.gray);
		drawAxis(g, -loserCount, winnerCount, 1, totalCount, VERTICAL);
		//Draw data
		if (data){
			g.setColor(Color.red);
			for (int i = 0; i < totalCount; i++) drawLine(g, i, value[i], i + 1, value[i + 1]);
			drawPoint(g, totalCount, winnerCount - loserCount);
		}
	}

	/**This method resets the balot graph*/
	public void reset(){
		data = false;
		value = new int[totalCount + 1];
		repaint();
	}
}






