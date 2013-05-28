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

/**This class models a forest as a rectangular lattice of trees.  Each tree at any time
is in one of three states: green, on fire, or burnt. If a tree is on fire at time t, then
any of the four neighboring green trees can catch fire at time t + 1 with specified probabilities.
Trees catch on fire independently.  A tree on fire at time t is burnt at time t + 1. Once a
tree is burnt, it remains burnt.*/
public class Forest extends JComponent implements MouseListener, MouseMotionListener{
	//Constants
	public final static int GREEN = 0, RED = 1, BLACK = 2;
	public final static int LEFT = 0, RIGHT = 1, DOWN = 2, UP = 3;
	//Variables
	private int forestWidth, forestHeight, rows, columns, treeWidth, xMouse, yMouse, editState;
	private int[][] state;
	private double pLeft = 0.5, pRight = 0.5, pUp = 0.5, pDown = 0.5;
	//Objects
	private Vector<Point> fire = new Vector<Point>(); 
	private Vector<Point> burnt = new Vector<Point>();

	/**This general constructor creates a new forest with a specified number of rows and
	columns and a specified tree width.*/
	public Forest(int c, int r, int w){
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		setParameters(c, r, w);
		setBackground(Color.gray);
	}

	/**This special constructor creates a new forest with a specified number of rows and
	columns, and with default tree width 5.*/
	public Forest(int c, int r){
		this(c, r, 5);
	}

	/**This default constructor creates a new forest with 100 rows, 50 columns and tree
	width 5.*/
	public Forest(){
		this(100, 50);
	}

	/**This method sets the parameters: the number of rows, the number of columns, and the
	tree width.*/
	public void setParameters(int c, int r, int w){
		//Assign the parameters
		treeWidth = w;
		columns = c;
		rows = r;
		//Set overall forest size
		forestWidth = columns * treeWidth;
		forestHeight = columns * treeWidth;
		setSize(forestWidth, forestHeight);
		//Initialize state array
		state = new int[columns][rows];
	}

	/**This method sets the fire spread probabilities.*/
	public void setProbabilities(double pl, double pr, double pd, double pu){
		pLeft = pl;
		pRight = pr;
		pDown = pd;
		pUp = pu;
	}

	/**This method gets the fire spread probabilities.*/
	public double getProbability(int i){
		if (i == LEFT) return pLeft;
		else if (i == RIGHT) return pRight;
		else if (i == DOWN) return pDown;
		else return pUp;
	}

	/**This method sets the tree edit state.  When a tree is clicked on, the state of the
	tree will change to the specified state.*/
	public void setEditState(int newState){
		editState = newState;
	}

	/**This method sets the state of a specified tree.*/
	public void setState(int x, int y, int newState){
		Point p = new Point(x,y);
		Graphics g = getGraphics();
		if (x >= 0 & x < columns & y >= 0 & y < rows){
			int oldState = state[x][y];
			state[x][y] = newState;
			if (newState == RED){
				fire.addElement(p);
				if (oldState == BLACK) burnt.removeElement(p);
				g.setColor(Color.red);
			}
			else if (newState == BLACK){
				burnt.addElement(p);
				if (oldState == RED) fire.removeElement(p);
				g.setColor(Color.black);
			}
			else{
				g.setColor(Color.green);
				if (oldState == RED) fire.removeElement(p);
				if (oldState == BLACK) burnt.removeElement(p);
			}
			g.fillRect(x * treeWidth, y * treeWidth, treeWidth, treeWidth);
		}
	}

	/**This method sets the tree in the center of the forest on fire.*/
	public void setFire(){
		setState(columns / 2, rows / 2, RED);
	}

	/**This method paints the forest.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		Point p;
		g.setColor(Color.green);
		g.fillRect(0, 0, forestWidth, forestHeight);
		g.setColor(Color.red);
		for (int i = 0; i < fire.size(); i++){
			p = (Point) fire.elementAt(i);
			g.fillRect(p.x * treeWidth, p.y * treeWidth, treeWidth, treeWidth);
		}
		g.setColor(Color.black);
		for (int i = 0; i < burnt.size(); i++){
			p = (Point) burnt.elementAt(i);
			g.fillRect(p.x * treeWidth, p.y * treeWidth, treeWidth, treeWidth);
		}
	}

	/**This method updates the forest.*/
	public void update(){
		int x, y, x1, y1;
		Point p, q;
		Graphics g = getGraphics();
		Vector f = (Vector) fire.clone();
		for (int i = 0; i < f.size(); i++){
			p = (Point) f.elementAt(i);
			burnt.addElement(p);
			boolean b = fire.removeElement(p);
			x = p.x; y = p.y;
			state[x][y] = BLACK;
			g.setColor(Color.black);
			g.fillRect(x * treeWidth, y * treeWidth, treeWidth, treeWidth);
			g.setColor(Color.red);
			if (x > 0){
				x1 = x - 1;
				if (state[x1][y] == GREEN & Math.random() < pLeft){
					state[x1][y] = RED;
					fire.addElement(new Point(x1, y));
					g.fillRect(x1 * treeWidth, y * treeWidth, treeWidth, treeWidth);
				}
			}
			if (x < columns - 1){
				x1 = x + 1;
				if (state[x1][y] == GREEN & Math.random() < pRight){
					state[x1][y] = RED;
					fire.addElement(new Point(x1, y));
					g.fillRect(x1 * treeWidth, y * treeWidth, treeWidth, treeWidth);
				}
			}
			if (y > 0){
				y1 = y - 1;
				if (state[x][y1] == GREEN & Math.random() < pUp){
					state[x][y1] = RED;
					fire.addElement(new Point(x, y1));
					g.fillRect(x * treeWidth, y1 * treeWidth, treeWidth, treeWidth);
				}
			}
			if (y < rows - 1){
				y1 = y + 1;
				if (state[x][y1] == GREEN & Math.random() < pDown){
					state[x][y1] = RED;
					fire.addElement(new Point(x, y1));
					g.fillRect(x * treeWidth, y1 * treeWidth, treeWidth, treeWidth);
				}
			}
		}
	}

	/**This method resets the forest to a completely green forest.*/
	public void reset(){
		fire.removeAllElements();
		burnt.removeAllElements();
		state = new int[columns][rows];
		repaint();
	}

	/**This method returns the number of trees that are on fire.*/
	public int treesOnFire(){
		return fire.size();
	}

	/**This method returns the number of burnt trees.*/
	public int treesBurnt(){
		return burnt.size();
	}

	/**This method returns the number of green trees.*/
	public int treesGreen(){
		return rows * columns - fire.size() - burnt.size();
	}

	/**This method gets the x mouse coordinates.*/
	public int getX(){
		return xMouse;
	}

	/**This method gets the y mouse coordinate.*/
	public int getY(){
		return yMouse;
	}

	/**This method handles the mouse click event.The selected tree is set to the
	currently specified state.*/
	public void mouseClicked(MouseEvent event){
		xMouse = event.getX() / treeWidth;
		yMouse = event.getY() / treeWidth;
		setState(xMouse, yMouse, editState);
	}

	/**This method handles the mouse move event. The mouse coordinates are tracked.*/
	public void mouseMoved(MouseEvent event){
		xMouse = event.getX() / treeWidth;
		yMouse = event.getY() / treeWidth;
	}

	//These mouse events are not handles.
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){}
	public void mouseReleased(MouseEvent event){}
	public void mouseDragged(MouseEvent event){}
}