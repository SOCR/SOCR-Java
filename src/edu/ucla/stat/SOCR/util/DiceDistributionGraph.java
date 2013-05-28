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
import edu.ucla.stat.SOCR.distributions.*;

// Dice Distribution Graph

public class DiceDistributionGraph extends Graph{
	RandomVariable dieVariable;
	DieDistribution  testDist;
	int barWidth;

	public DiceDistributionGraph(RandomVariable rv, DieDistribution dist){
		super(0,7,0,0.5);
		setMargins(25,20,40,20);
		setSize(200,200);
		setParameters(rv, dist);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);

		// draw x axis
		g.setColor(Color.black);
		drawLine(g, 0, 0, 7, 0);
		for (int i = 1; i < 7; i++){
			drawTick(g, VERTICAL, i, 0, 2, 2);
			drawLabel(g, format((double)i), i, 0, LEFT);
		}
		// draw y axis
		drawLine(g, 0, 0, 0, 0.5);
		for(double y = 0.0; y <= 0.5; y = y + .05) drawTick(g, HORIZONTAL, 0, (int)y, 2, 2);
		drawLabel(g, "0", 0, 0, LEFT);
		drawLabel(g, "0.50", 0, .5, LEFT);
		drawLabel(g, "1.00", 0, 1, LEFT);
		// Draw Test Distribution
		g.setColor(Color.green);
		barWidth = Math.max(1, Math.min(xGraph(.5) - xGraph(0.0) - 1, 5));
		for(int x = 1; x <= 6; x++)	drawBox(g, VERTICAL, x, 0, testDist.getDensity(x), barWidth, 0);
		// Draw Sampling Distribution
		g.setColor(Color.blue);
		for(int x = 1; x <= 6; x++) drawBox(g, VERTICAL, x, 0, dieVariable.getDistribution().getDensity(x), 0, barWidth);
		if (dieVariable.getIntervalData().getDomain().getSize() > 0) drawData();
	}

	public void drawData(){
		Graphics g = getGraphics();
		// Erase Old Data
		g.setColor(Color.white);
		for(int x = 1; x <= 6; x++)	drawBox(g, VERTICAL, x + .1, 0, 1, 0, barWidth);
		// Draw New data
		g.setColor(Color.red);
		for(int x = 1; x <= 6; x++)	drawBox(g, VERTICAL, x + .1, 0, dieVariable.getIntervalData().getFreq(x), 0, barWidth);
	}

	public void setParameters(RandomVariable rv, DieDistribution dist){
		dieVariable = rv;
		testDist = dist;
	}
}
