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
import javax.swing.*;

/**This class is a simple model of the classical Galton board*/
public class GaltonBoard extends JComponent{
	//Variables
	private int width, rows, row, column;
	//The spacing between pegs and the radius of a peg
	private int space = 12; int radius = 6;
	private int[] path; //The path through the Galton board
	private String bitString, subset; //The corresponding bit string and subset

	/**This general constructor creates a new Galton board with a specified
	number of rows, spacing, and radius*/
	public GaltonBoard(int n, int d, int r){
		//Correct invalid parameters
		if (n < 1) n = 1;
		if (d <= 0) d = 12;
		if (r <= 0) r = d / 2; else if (r > d) r = d;
		rows = n;
		space = d;
		radius = r;
		setBackground(Color.white);
		path = new int[rows];
 	}

	/**This special constructor creates a new Galton board with a specified
	number of rows and with space 12 and radius 6*/
	public GaltonBoard(int n){
		this(n, 12, 6);
	}

	/**This method draws the Galton board*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		width = getSize().width;
		g.setColor(Color.blue);
		for(int i = 0; i <= rows; i++){
			for(int j = 0; j <= i; j++){
				g.fillOval(width / 2 + (2 * j - i) * space, (i + 1) * space, radius, radius);
			}
		}
		//Draw Path
		drawBall(Color.red, 0, 0);
		int sum = 0;
		for (int j = 0; j < row; j++){
			sum = sum + path[j];
			drawBall(Color.red, j + 1, sum);
		}
	}

	/**This method moves the ball to a specified position on the next row:
	right if i = 1 and left if i = 0*/
	public void moveBall(int i){
		if (row < rows){
			path[row] = i;
			row++;
			column = column + i;
			drawBall(Color.red, row, column);
			bitString = bitString + i;
			if (i == 1){
				if (subset.equals("")) subset = subset + row;
				else subset = subset + ", " + row;
			}
		}
	}

	/**This method draws the ball in a specified color at a specified
	location*/
	public void drawBall(Color c, int k, int i){
		Graphics g = getGraphics();
		g.setColor(c);
		g.fillOval(width / 2 + (2 * i - k) * space, (k + 1) * space - radius, radius, radius);
	}

	/**This method specifies the preferred size of the Galton board*/
	public Dimension getPreferredSize(){
		return new Dimension(2 * (rows + 2) * space, (rows + 2) * space);
	}

	/**This method specifies the minimum size of the Galton board*/
	public Dimension getMinimumSize(){
		return new Dimension(2 * (rows + 2) * space, (rows + 2) * space);
	}

	/**This method resets the Galton board, including the path, bit string,
	and subset*/
	public void reset(){
		row = 0;
		column = 0;
		path = new int[rows];
		repaint();
		bitString = "";
		subset = "";
	}

	/**This method returns the row posiiton of the ball*/
	public int getRow(){
		return row;
	}

	/**This method returns the column position of the ball*/
	public int getColumn(){
		return column;
	}

	/**This method returns the number of rows*/
	public int getRows(){
	return rows;
	}

	/**This method returns the bit string that corresponds to the path of
	the ball through the Galton board*/
	public String getBitString(){
		return bitString;
	}

	/**This method returns the subset that corresponds to the path of the ball
	through the Galton board*/
	public String getSubset(){
		if (row == 0) return "";
		else if (subset.equals("")) return "empty";
		else return "{" + subset + "}";
	}

	/**This method sets the path of the ball through the Galton board*/
	public void setPath(int[] p){
		path = p;
		row = path.length;
	}

	/**This method sets the number of rows in the Galton board*/
	public void setRows(int n){
		rows = n;
		path = new int[rows];
	}
}

