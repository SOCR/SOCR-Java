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

/**This class models a ball that has a specified color and label*/
public class Ball extends JComponent{
	private int value, size;
	private Color color;
	private Font font;

	/**This general constructor creates a new ball with a specified value, size, and
	color.*/
	public Ball(int x, int s, Color c){
		setSize(s);
		setValue(x);
		setColor(c);
	}

	/**This special constructor creates a new ball with a specified value and size, and with
	default ball color red.*/
	public Ball(int x, int s){
		this(x, s, Color.red);
	}

	/**This special constructor creates a new ball with a specified value, the default color, and
	with the default size of 24 pixels.*/
	public Ball(int x){
		this(x, 24);
	}

	/**This default constructor creates a new ball with value 0, size 24 pixels, and color red.*/
	public Ball(){
		this(0);
	}

	/**This method paints the ball.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
 		g.setFont(font);
		String label = String.valueOf(value);
		int x = g.getFontMetrics(font).stringWidth(label);
		int y = g.getFontMetrics(font).getAscent();
		g.setColor(color);
		g.fillOval(2, 2, size - 4, size - 4);
		g.setColor(Color.black);
		g.drawOval(2, 2, size - 4, size - 4);
		g.drawString(label, size / 2 - x / 2, size / 2 + y / 2);
	}

	/**This method sets the ball to a specified value*/
	public void setValue(int x){
		value = x;
	}

	/**This method gets the value of the ball*/
	public int getValue(){
		return value;
	}

	/**This method sets the color of the ball.*/
	public void setColor(Color c){
		color = c;
	}

	/**This method gets the ball color.*/
	public Color getColor(){
		return color;
	}

	/**This method sets the size of the ball.*/
	public void setSize(int s){
		if (s < 1) s = 1;
		size = s;
		font = new Font("SansSerif", Font.PLAIN, (size - 4) / 2);
		super.setSize(s, s);
	}

	/**This method specifies the minimum size*/
	public Dimension getMinimumSize(){
		return new Dimension(size, size);
	}

	/**This method specifies the preferred size*/
	public Dimension getPreferredSize(){
		return new Dimension(size, size);
	}
}

