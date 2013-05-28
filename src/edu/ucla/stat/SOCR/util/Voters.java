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
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**This class models a rectangular array of "voters." Each voter at each time can be in one of a
finite set of states representd as colors.*/
public class Voters extends JComponent implements MouseListener, MouseMotionListener{
	//Constants
	private final static Color[] color = {Color.black, Color.blue, Color.cyan, Color.green,
		Color.magenta, Color.orange, Color.pink, Color.red, Color.white, Color.yellow};
	//Variables
	private int width, height, rows, columns, voterWidth, statesAlive, editState, xMouse, yMouse, states = 10;
	private int stateCount[] = new int[10];
	private int[][] state;
	private boolean newDeath, consensus;
	private double pLeft = 0.25, pRight = 0.25, pUp = 0.25, pDown = 0.25;
	private Vector<Point> changed = new Vector<Point>();

	/**This general constructor creates a new array of voters with a specified number of rows, columns,
	and width.*/
	public Voters(int c, int r, int w){
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		setParameters(c, r, w);
		setBackground(Color.gray);
	}

	/**This special constructor creates a new array of voters with a specified number of rows and
	columns, and with the default size of 20 pixels.*/
	public Voters(int c, int r){
		this(c, r, 20);
	}

	/**This default constructor creates a new array of voters with 5 rows, 10 columns and size
	of 20 pixels.*/
	public Voters(){
		this(10, 5);
	}

	/**This method sets the parameters: the number of rows and columns and the width.*/
	public void setParameters(int c, int r, int w){
		rows = r;
		columns = c;
		voterWidth = w;
		width = columns * voterWidth;
		height = rows * voterWidth;
		setSize(width, height);
		state = new int[columns][rows];
	}

	/**This method sets the probabilities for selecting a neighbor.*/
	public void setProbabilities(double pl, double pr, double pd, double pu){
		pLeft = pl;
		pRight = pr;
		pDown = pd;
		pUp = pu;
	}

	/**This method sets the edit state, the state that a voter will be changed to when a user
	clicks on the voter.*/
	public void setEditState(int editState){
		this.editState = editState;
	}

	/**This method changes the state of a specified voter to a specified new state.*/
	public void setState(int x, int y, int newState){
		int currentState = state[x][y];
		//Update state counts
		stateCount[currentState]--;
		stateCount[newState]++;
		//Change the state
		state[x][y] = newState;
		//Add the voter to the collection of changed voters.
		changed.addElement(new Point(x, y));
		//Determine if a state has died, and if so, update the number of states still alive.
		if (stateCount[currentState] == 0){
			newDeath = true;
			statesAlive--;
			if (statesAlive == 1) consensus = true;
		}
	}

	/**This method returs the state of a specified voter.*/
	public int getState(int x, int y){
		return state[x][y];
	}

	/**This method paints the voter canvas.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		for (int x = 0; x < columns; x++){
			for (int y = 0; y < rows; y++){
				g.setColor(color[state[x][y]]);
				g.fillRect(x * voterWidth, y * voterWidth, voterWidth, voterWidth);
			}
		}
		changed.removeAllElements();
	}

	/**This method updates the voter canvas when voters have changed state.*/
	public void update(){
		int x, y, x1, y1;
		Point p, q;
		Graphics g = getGraphics();
		for (int i = 0; i < changed.size(); i++){
			p = (Point) changed.elementAt(i);
			x = p.x; y = p.y;
			g.setColor(color[state[x][y]]);
			g.fillRect(x * voterWidth, y * voterWidth, voterWidth, voterWidth);
		}
		changed.removeAllElements();
	}

	/**This method resets the voters. Each voter is assigned a random state.*/
	public void reset(){
		int s;
		//Clear the state counts
		for (int i = 0; i < states; i++) stateCount[i] = 0;
		//Assign each voter a random state and determine the new state counts.
		for (int x = 0; x < columns; x++){
			for (int y = 0; y < rows; y++){
				s = (int)(states * Math.random());
				state[x][y] = s;
				stateCount[s]++;
			}
		//Determine how many states exist.
		}
		statesAlive = 0;
		for (int i = 0; i < states; i++) if (stateCount[i] > 0) statesAlive++;
		consensus = (statesAlive == 1);
	}

	/**This method returns the number of voters in a specified state.*/
	public int stateCount(int i){
		return stateCount[i];
	}

	/**This method performs the voter experiment.*/
	public void doExperiment(){
		int x, y, x1, y1, s, s1;
		double r;
		newDeath = false;
		//Select a random vater
		x = (int)(columns * Math.random());
		y = (int)(rows * Math.random());
		//Select a random neighbor of the voter.
		r = Math.random();
		if (r < pLeft){
			x1 = x - 1;
			if (x1 < 0) x1 = columns - 1;
			y1 = y;
		}
		else if (r < pLeft + pRight){
			x1 = x + 1;
			if (x1 >= columns) x1 = 0;
			y1 = y;
		}
		else if (r < pLeft + pRight + pDown){
			x1 = x;
			y1 = y + 1;
			if (y1 >= rows) y1 = 0;
		}
		else {
			x1 = x;
			y1 = y - 1;
			if (y1 < 0) y1 = rows - 1;
		}
		//Change the state of the voter to the state of the neighbor.
		s1 = state[x1][y1];
		setState(x, y, s1);
	}

	/**This method returns the number of states alive.*/
	public int statesAlive(){
		return statesAlive;
	}

	/**This method checks to see if a state has died.*/
	public boolean newDeath(){
		return newDeath;
	}

	/**This method checks to see if the voters are all in the same state.*/
	public boolean consensus(){
		return consensus;
	}

	/**This method returns the x coordinate of the mouse.*/
	public int getX(){
		return xMouse;
	}

	/**This method returns the y coordinate of the mouse.*/
	public int getY(){
		return yMouse;
	}

	/**This method changes the state of the voter clicked on to the current edit state.*/
	public void mouseClicked(MouseEvent event){
		xMouse = event.getX() / voterWidth;
		yMouse = event.getY() / voterWidth;
		setState(xMouse, yMouse, editState);
	}

	/**This method tracks the voter under the mouse cursor.*/
	public void mouseMoved(MouseEvent event){
		xMouse = event.getX() / voterWidth;
		yMouse = event.getY() / voterWidth;
	}

	//These events are not handled
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){}
	public void mouseReleased(MouseEvent event){}
	public void mouseDragged(MouseEvent event){}
}
