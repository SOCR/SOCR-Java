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
import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;

/**This class is a graph that shows a prior and posterior beta getDensity*/
public class BetaGraph extends Graph{
	//Variables
	private double left, right, probability, yMax;
	int trials, successes;
	//Objects
	BetaDistribution priorDistribution, posteriorDistribution;

	/**This general constructor creates a new beta graph with specified left and
	right parameters for the prior distribution, specified number of trials and
	probability of success*/
	public BetaGraph(double a, double b, int n, double p){
		setParameters(a, b, n, p);
	}

	/**This default constructor creates a new beta graph with 10 trials,
	probability of success 0.5, and left and right parameters 1 (so the prior
	distribution is uniform).*/
	public BetaGraph(){
		this(1, 1, 10, 0.5);
	}

	/**This method sets the parameters: the left and right parameters for the
	prior distribution, the number of trials, and the probability of success*/
	public void setParameters(double a, double b, int n, double p){
		//Correct for invalid parameters and assign values
		if (a <= 0) a = 1; left = a;
		if (b <= 0) b = 1; right = b;
		if (p < 0) p = 0; else if (p > 1) p = 1; probability = p;
		if (n < 0) n = 0; trials = n;
 		successes = -1;
		//Setup distributions
		priorDistribution = new BetaDistribution(a, b);
		posteriorDistribution = new BetaDistribution(a, b);
		//Initialize parameters
		yMax = 1.3 * priorDistribution.getMaxDensity();
		super.setScale(0, 1, 0, yMax);
		super.setMargins(30, 20, 30, 20);
	}

	/**This method paints the grpah*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		double x1;
		//Draw x axis
		g.setColor(Color.black);
		drawAxis(g, 0, 1, 0.1, 0, HORIZONTAL);
		drawAxis(g, 0, yMax, 0.1 * yMax, 0, VERTICAL);
		//Draw prior getDensity
		g.setColor(Color.blue);
		drawTick(g, probability, 0, 6, 6, VERTICAL);
		drawLabel(g, "p", probability, 0, BELOW);
		for (double x = 0; x < 1; x = x + 0.01){
			x1 = x + 0.01;
			drawLine(g, x, priorDistribution.getDensity(x), x1, priorDistribution.getDensity(x1));
		}
		//Draw posterior data
		if (successes >= 0){
			g.setColor(Color.red);
			for (double x = 0; x < 1; x = x + 0.01){
				x1 = x + 0.01;
				drawLine(g, x, posteriorDistribution.getDensity(x), x1, posteriorDistribution.getDensity(x1));
			}
		}

	}

	/**This method sets the number of successes to a specified value*/
	public void setValue(int k){
		if (k < 0) k = 0; else if (k > trials) k = trials;
		successes = k;
		posteriorDistribution.setParameters(left + successes, right + trials - successes);
		yMax = 1.3 * Math.max(priorDistribution.getMaxDensity(), posteriorDistribution.getMaxDensity());
		super.setScale(0, 1, 0, yMax);
	}

	/**This method specifies the minimum size*/
	public Dimension getMinimumSize(){
		return new Dimension(100, 100);
	}

	/**This method specifies the preferred size*/
	public Dimension getPreferredSize(){
		return new Dimension(200, 200);
	}
}
