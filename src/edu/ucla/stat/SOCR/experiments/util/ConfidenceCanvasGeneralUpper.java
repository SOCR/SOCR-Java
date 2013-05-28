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
 It's Online, Therefore, It Exists!
 ***************************************************/

/*  ConfidenceCanvas.java display for confidence applet
* @author Ivo Dinov
* @version 1.0 Feb. 19 2004
*/
package edu.ucla.stat.SOCR.experiments.util;

import edu.ucla.stat.SOCR.core.Distribution;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class ConfidenceCanvasGeneralUpper extends ConfidenceCanvasGeneralBase {

    /**
     * Constructor for ConfidenceCanvas
     */
    public ConfidenceCanvasGeneralUpper(int n, int nTrials) {
        super(n, nTrials);  
    }

    /*
    public Dimension getPreferredSize() {
         return new Dimension(150, 125);
     }

     public Dimension getMinimumSize() {
         return new Dimension(30, 25);
     }*/
    
    /**
     * paint method for ConfidenceCanvas
     */
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
      
    	 // get upper and lower bounds of the sample data
        setRange();

        r = getSize();
        int xInterval = (r.width - leftGap) / (2 * nTrials);
        this.midHeight = r.height - (topGap + bottomGap);
  
        //both
        drawVscale(5, g);
        
        // draw a blank graph 
        if (sampleData != null ) {
            drawData(sampleData, leftGap, midHeight, topGap, xInterval, g);
            setParameterOfInterest();
        }
       
        //green line for parameterOfInterest for upper
        if (chosenIntervalType!=null && ((!chosenIntervalType.equals(IntervalType.sigma)) && !chosenIntervalType.equals(IntervalType.sigma2)))
        	g.drawLine(leftGap + 20, scaleHeight(parameterOfInterestUpper), r.width-105, scaleHeight(parameterOfInterestUpper));
    	if (chosenIntervalType!=null && (chosenIntervalType.equals(IntervalType.proportion_approx) || chosenIntervalType.equals(IntervalType.proportion_wald)||chosenIntervalType.equals(IntervalType.proportion_exact)))
    		g.drawLine(leftGap + 20, scaleHeight(parameterOfInterestUpper2), r.width-105, scaleHeight(parameterOfInterestUpper2));
     
    	g.setColor(Color.black);
    	
        if ((chosenIntervalType!=null && (chosenIntervalType.equals(IntervalType.proportion_approx) || chosenIntervalType.equals(IntervalType.proportion_wald)||chosenIntervalType.equals(IntervalType.proportion_exact)))){
        	g.drawString("L"+stringOfInterestUpper+ decimalFormat.format(parameterOfInterestUpper), r.width-100, scaleHeight(parameterOfInterestUpper));
        	g.drawString("R"+stringOfInterestUpper+ decimalFormat.format(parameterOfInterestUpper2), r.width-100, scaleHeight(parameterOfInterestUpper2));
        }
        else if (chosenIntervalType!=null && ((!chosenIntervalType.equals(IntervalType.sigma)) && !chosenIntervalType.equals(IntervalType.sigma2)))
        	g.drawString("Dist_"+stringOfInterestUpper+ decimalFormat.format(parameterOfInterestUpper), r.width-100, scaleHeight(parameterOfInterestUpper));
    }

    private void drawData(double sampleData[][], int leftGap, int
            midHeight, int topGap, int xInterval, Graphics g) {

        int xPos = (8 * leftGap + xInterval) / 4;
        int yPos;


        for (int trial = 0; trial < nTrials; trial++) {

            // 1. Draw the Data
        	// upper
        	g.setColor(Color.blue);
            xPos += xInterval;
            for (int i = 0; i < n; i++) {
            	
                yPos = scaleHeight(sampleData[trial][i]);
               // System.out.println(sampleData[trial][i]);
                g.drawLine(xPos - 2, yPos, xPos + 2, yPos);
            } 
         //   System.out.println();

            xPos += xInterval / 2;


            double unscaledCIUpperbound = ciData[trial][0];
            double unscaledCILowerbound = ciData[trial][1];
            int scaledCIUpperbound = scaleHeight(unscaledCIUpperbound);
            int scaledCILowerbound = scaleHeight(unscaledCILowerbound);

            // 2. Draw the CI on lower
            /* 
            lowerG.setColor(Color.red);
            lowerG.drawLine(xPos - 2, scaledCIUpperbound, xPos + 2, scaledCIUpperbound);    // Top Line
            lowerG.drawLine(xPos - 2, scaledCILowerbound, xPos + 2, scaledCILowerbound); // Bottom Line
            lowerG.drawLine(xPos, scaledCIUpperbound, xPos, scaledCILowerbound); // Main CI Line

            if (isOutsideTheInterval(unscaledCIUpperbound, unscaledCILowerbound))   // CI EXCLUDES the mean=0
            {
                missedCount++;
                lowerG.setColor(Color.green);
                lowerG.fillOval(xPos - MISSED_CIRCLE / 2, (scaledCILowerbound - scaledCIUpperbound) / 2 + scaledCIUpperbound, MISSED_CIRCLE, MISSED_CIRCLE);
            }*/

            // 3. Draw the separating vertical Lines on both canvas
            g.setColor(Color.green);
            g.drawLine(xPos + xInterval / 2, topGap, xPos + xInterval / 2, topGap + midHeight);
             
        }
    }

    //Draws scale from the lowerBound to the upperBound divided into nIntervals
    private void drawVscale(int xpos, Graphics g) {
        g.setColor(Color.black);
        g.drawLine(xpos, topGap, xpos, r.height - bottomGap);

        double intervalSize = (upperBound - lowerBound) / nIntervals;
        double i = lowerBound;
        
        if(lowerBound!=upperBound) { // to prevent a infinite  loop
	        while (i <= upperBound) {
	            // only print 2 decimal places.
	            decimalFormat.setMaximumFractionDigits(2);
	            g.drawString(decimalFormat.format(i), xpos+6, scaleHeight(i));
	            i = i + intervalSize;
	        }
    	}

        // System.out.println("General CIcanvas drawVsacle + xpos=" + xpos + " lowerBound=" + lowerBound + "upperBound=" + upperBound + " decimalFormated upperBound" + decimalFormat.format(i));
    }

}
