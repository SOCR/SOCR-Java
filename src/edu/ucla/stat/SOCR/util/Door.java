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

/**This class defines a door that can be opened or closed. When closed, the
door shows a label; when opened the door shows an image and a label*/
public class Door extends JComponent{
	//Variables
	private boolean opened = false;
	//Objects
	private Image image;
	private String label;
	private Color doorColor, openColor, labelColor;
 	private static Font font = new Font("TimesRoman", Font.PLAIN, 12);

	/**This general constructor creates a new door with specified colors*/
	public Door(Color dc, Color oc, Color lc){
		doorColor = dc; openColor = oc; labelColor = lc;
	}

	/**This default constructor creates a new door with default colors:
	door color yellow, open color white, label color red*/
	public Door(){
		this(Color.yellow, Color.white, Color.red);
	}

	/**This method opens the door to display a specified image and caption*/
	public void open(Image i, String s){
		label = s;
		image = i;
		opened = true;
		repaint();
	}

	/**This method closes the door to display a specified caption*/
	public void close(String s){
		label = s;
		opened = false;
		repaint();
	}

	/**This method paints the door*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		g.setFont(font);
		int w = getSize().width, h = getSize().height,
			y = g.getFontMetrics(font).getHeight();
		//Draw the rectangle
		if (opened) g.setColor(openColor); else g.setColor(doorColor);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.black);
		g.drawRect(0, 0, w, h);
		g.setColor(labelColor);
		//Draw the label
		g.drawString(label, 5, y);
		//If opened, draw the image
		if (opened){
			try { int a = image.getWidth(this), b = image.getHeight(this);
				g.drawImage(image, (w - a) / 2, (h - b) / 2, this);
			}
			catch (Exception e) {; }
		}
		else{ //Draw door knob
			g.setColor(Color.black);
			g.fillOval(w - 16, h / 2 - 6, 12, 12);
		}
	}

	/**This method tests to see if the door is opened*/
	public boolean isOpened(){
		return opened;
	}

	/**This method specifies the minimum size*/
	public Dimension getMinimumSize(){
		return new Dimension(100, 100);
	}

	/**This method specifies the preferred size*/
	public Dimension getPreferredSize(){
		return new Dimension(100, 100);
	}
}
