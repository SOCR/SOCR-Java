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

/**This is a specialized graph used in the red-black experiment.*/
public class RedBlackGraph extends Graph{
	int initial, target, current;

	/**This general constructor creates a new red-black graph with a specified initial fortune
	and target fortune.*/
	public RedBlackGraph(int i, int t){
		setParameters(i, t);
		current = initial;
	}

	/**This method sets the parameters: the initial fortune and the target fortune.*/
	public void setParameters(int i, int t){
		initial = i;
		target = t;
		setScale(0, 4, 0, target);
	}

	/**This method paints the graph.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Draw axes
		drawLine(g, 0, 0, 4, 0);
		drawAxis(g, 0, target, 1, 0, VERTICAL);
		g.setColor(Color.red);
		fillBox(g, 0.5, 0, 1.5, initial);
		g.setColor(Color.black);
		fillBox(g, 0.5, initial, 1.5, target);
		drawCurrent();
	}

	/**This method draws the current fortune.*/
	public void drawCurrent(){
		Graphics g = getGraphics();
		g.setColor(Color.red);
		fillBox(g,2.5, 0, 3.5, current);
		g.setColor(Color.black);
		fillBox(g, 2.5, current, 3.5, target);
	}

	/**This method sets the current fortune.*/
	public void setCurrent(int current){
		this.current = current;
	}
}

