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
/*  ConfidenceCanvas.java display for confidence applet
* @author Ivo Dinov
* @version 1.0 Feb. 19 2004
*/
package edu.ucla.stat.SOCR.experiments.util;

import java.awt.*;
import java.applet.*;
import javax.swing.*;
import edu.ucla.stat.SOCR.distributions.NormalDistribution;
import edu.ucla.stat.SOCR.distributions.StudentDistribution;

public class ConfidenceCanvasSimple extends JPanel {
	
	Dimension r; 
	int nTrials;
	int n;
	double[][] normalData;
	double[] xBar;
	double[] s;
	// LAYOUT PARAMETERS
	int topGap = 5;
	int bottomGap = 20;
	int leftGap = 25;
	
	int midHeight;
      int missedCount = 0;

	double[] cvlist = {0.0, 1.28, 1.645, 1.96,  2.576, 3.29, 3.89};
	double[] halfAlpha = {0.25, 0.1, 0.05, 0.025, 0.005, 0.0005, 0.00005};

	int cvIndex;
	double cv;
	int ellipse_w=10, ellipse_h=10;	// Significant Elipses are reported at CI that
							// exclude the mean=0 parameter

	/**
	 * Constructor for ConfidenceCanvas
	 *
	 */
	public ConfidenceCanvasSimple(int n, int nTrials) {
		setSampleSize_NumberExperiments(n, nTrials);
        //Dimension dim = new Dimension(20, 10);
        //setMaximumSize(dim);
        //Border border = new Border();
        //setSize(2, 2);
        //System.out.print(getSize().width);
	}

	public Dimension getPreferredSize(){
		//return new Dimension( 300, 250);
                return new Dimension( 150, 125);
		}

	public Dimension getMinimumSize(){
		//return new Dimension( 200, 200);
        return new Dimension( 30, 25);
                
	}
	
	public void setSampleSize_NumberExperiments(int _n, int _nTrials)
	{	if (_n>=1) this.n=_n;
		else this.n=1;
		if (_nTrials>=1) this.nTrials = _nTrials;
		else this.nTrials = 1;
	}


	/**
	* paint method for ConfidenceCanvas
	*/
	public void paintComponent(Graphics g){
			missedCount =0;
			super.paintComponent(g);

			r=getSize();
                       
			int xInterval = (r.width-leftGap)/(2*nTrials);

			this.midHeight = r.height-(topGap+bottomGap);
			g.setColor(Color.black);
			g.drawLine(leftGap+8,midHeight+topGap,
					r.width,midHeight+topGap);
			g.drawLine(leftGap,topGap, r.width,topGap);
			g.setColor(Color.magenta);
			g.drawLine(leftGap+8, topGap + midHeight/2,
					 r.width, topGap+midHeight/2);
			drawVscale( 5, r.height-bottomGap, topGap, g);
			if (normalData != null) {
				drawData(normalData, leftGap, midHeight, topGap,xInterval, g);
			
			}
	}
	

	public int getMissedCount() {
        return missedCount;
    }
        
        
    private void drawData(double[][] normalData, int leftGap , int 	
					midHeight, int topGap, int xInterval, Graphics g){
			
    	int xPos = (8*leftGap+xInterval)/8;
				
		int yPos=0;
			
			//double tAlpha= cvlist[cvIndex]; // OLD SCHEMA: 19 df 
			//NormalDistribution NDist = new NormalDistribution(0,1);
			//double tAlpha= NDist.inverseCDF(halfAlpha[cvIndex]);
			// Because we sample from KNOWN N(0,1) distribution, we should use the Z-scores
			// If we did not know the SD then sampling form StudentDistribution is more appropriate.
			// StudentDistribution SDist = new StudentDistribution(n-1);
			// double tAlpha= SDist.inverseCDF(halfAlpha[cvIndex]);

		StudentDistribution SDist = new StudentDistribution(n-1);
		double tAlpha= SDist.inverseCDF(halfAlpha[cvIndex]);

		for (int trial=0; trial < nTrials; trial++) {
				
				// 1. Draw the Data
				g.setColor(Color.blue);
				xPos += xInterval;
				for (int i =0; i< n; i++) {
				yPos = scaleHeight(normalData[trial][i]);
				g.drawLine(xPos-2, yPos, xPos+2,yPos);
					}
				xPos += xInterval/2;
				
				int xBarHeight= scaleHeight(xBar[trial]);
				int ciTop = scaleHeight(xBar[trial] + tAlpha*s[trial]/Math.sqrt(n) );
				int ciBottom = scaleHeight(xBar[trial] - tAlpha*s[trial]/Math.sqrt(n) );

				// 2. Draw the CI
				g.setColor(Color.red);
				g.drawLine(xPos-2,ciTop,xPos+2,ciTop);	// Top Line
				g.drawLine(xPos-2, xBarHeight, xPos+2, xBarHeight); //Middle Line
				g.drawLine(xPos-2, ciBottom, xPos+2, ciBottom); // Bottom Line
				g.drawLine(xPos, ciTop, xPos, ciBottom); // Main CI Line
				
				if ( (xBar[trial]+tAlpha*s[trial]/Math.sqrt(n))*
					(xBar[trial]-tAlpha*s[trial]/Math.sqrt(n)) > 0)   // CI EXCLUDES the mean=0
				{	
                    missedCount++;
                    g.setColor(Color.green);
					g.fillOval(xPos-ellipse_w/2, ciBottom-ellipse_h/2, ellipse_w, ellipse_h);
				}

				// 3. Draw the separating vertical Lines
				g.setColor(Color.green);
				g.drawLine(xPos+xInterval/2, topGap, xPos+xInterval/2, topGap+midHeight);
		}
    }

	private int scaleHeight( double x){
		int sh;
		sh =(int)(topGap-midHeight*(x-3)/6);
		return sh;
	}

	/*
	 * Draws scale of -3.0 to 3.0
	 */	
	private void drawVscale( int xpos, int bottomY, int topY ,Graphics g) {
		// int scaleHeight = bottomY - topY;
		System.out.println("Simple drawVsacle + xpos="+xpos+" bottomY="+bottomY+ " topY="+topY);
		g.setColor(Color.black);
		g.drawLine(xpos, bottomY, xpos, topY);
		g.drawLine(xpos-3, bottomY, xpos+3, bottomY);
		g.drawString( "-3.0", xpos+6, bottomY+4);
		g.drawLine(xpos-3, topY, xpos+3, topY);
		g.drawString("3.0", xpos+6, topY+4);
		g.drawLine(xpos-3, (topY+bottomY)/2, xpos+3, (topY+bottomY)/2);
		g.drawString( "0.0", xpos+6, (topY+bottomY)/2+4);
		g.drawLine(xpos-3, (topY/6+5*bottomY/6), xpos+3, (topY/6+5*bottomY/6));
		g.drawString("-2.0", xpos+6, (topY/6+5*bottomY/6)+4);
		g.drawLine(xpos-3, (topY/3+2*bottomY/3), xpos+3, (topY/3+2*bottomY/3));
		g.drawString("-1.0", xpos+6, (topY/3+2*bottomY/3)+4);
		g.drawLine(xpos-3, (4*topY/6+2*bottomY/6), xpos+3, (4*topY/6+2*bottomY/6));
		g.drawString("1.0",xpos+6,4*topY/6+2*bottomY/6);
		g.drawLine(xpos-3, (5*topY/6+bottomY/6), xpos+3,(5*topY/6+bottomY/6));
		g.drawString("2.0", xpos+6, 5*topY/6+bottomY/6);
	}
	

	/**
	 * clear clears canvas and resets parameters
	 * @param n
	 * @param nTrials
	*/
	public void clear(int n, int nTrials){
		this.xBar = new double[nTrials];
		this.normalData = new double[nTrials][n];		
		setSampleSize_NumberExperiments(n, nTrials);
		repaint();
	}


	public void update(int cvIndex, double[][] normalData, double[] xBar, double[] s){
		this.normalData = normalData;
		this.xBar = xBar;
		this.s=s;
		this.cvIndex=cvIndex;
		//repaint();
		paintComponent(this.getGraphics());
	}
}	
