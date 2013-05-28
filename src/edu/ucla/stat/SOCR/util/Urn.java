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

/**This class is a container that holds balls.*/
public class Urn extends JPanel{
	private int ballCount;
	private Ball[] ball;
	public final static int WITHOUT_REPLACEMENT = 0, WITH_REPLACEMENT = 1;

	/**This general constructor creates a new urn with a specified number of balls*/
	public Urn(int n){
		setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		ballCount = n;
		//Create the balls and add them to the urn
		ball = new Ball[ballCount];
		for (int i = 0; i < ballCount; i++){
			ball[i] = new Ball();
			add(ball[i]);
		}
	}

	/**This default constructor creates a new urn with balls.*/
	public Urn(){
		this(10);
	}

	/**This method returns the i'th ball.*/
	public Ball getBall(int i){
		if (i < 0) i = 0; else if (i > ballCount - 1) i = ballCount - 1;
		return ball[i];
	}

	/**This method returns the number of balls.*/
	public int getBallCount(){
		return ballCount;
	}

	/**This method draws a sample of size n from the population of size N (numbered from 1 to N).
	The type of sampling is sepcified by t (without	replacement or with replacement).*/
	public void sample(int n, int N, int t){
		int j, u, k;
		int[] pop = new int[N];
		if (t == WITH_REPLACEMENT){
			for (int i = 0; i < n; i ++) ball[i].setValue((int)(Math.random() * N) + 1);
		}
		else{
			for (int i = 0; i < N; i++) pop[i] = i + 1;
			for (int i = 0; i < n; i++){
				j = N - i;
				u = (int)(j * Math.random());
				ball[i].setValue(pop[u]);
				k = pop[j - 1];
				pop[j - 1] = pop[u];
				pop[u] = k;
			}
		}
	}

	/**This method shows a specified number of balls.*/
	public void showBalls(int n){
		if (n < 0) n = 0; else if (n > ballCount) n = ballCount;
		for (int i = 0; i < ballCount; i++) ball[i].setVisible(i < n);
		repaint();
	}
}






