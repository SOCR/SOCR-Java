/***  ****//****************************************************
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
package edu.ucla.stat.SOCR.analyses.util;

import java.awt.Color;
import java.awt.Graphics;


public class ConfidenceCanvasGeneralLower extends ConfidenceCanvasGeneralBase {

	boolean[] missed;
    /**
     * Constructor for ConfidenceCanvas
     */
    public ConfidenceCanvasGeneralLower(int[] n, int nTrials) {
    	super(n, nTrials);  
    	missed = new boolean[nTrials];
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
        missedCount = 0;
        super.paintComponent(g);
     //   System.out.println("paint lowerPanel");
        
        // get upper and lower bounds of the sample data
        setRange();
       
        r = getSize();
        int xInterval = (r.width - leftGap) / (2 * nTrials);
        this.midHeight = r.height - (topGap + bottomGap);
       
        // both
        drawVscale(5, g);
        	
        // draw a blank graph
        if (sampleData != null) {
        	setParameterOfInterest();
        	getProportion();
            drawData(sampleData, leftGap, midHeight, topGap, xInterval, g);
        
        }
  
     /*   if(chosenIntervalType!=null){
	        //proportion
	        if (chosenIntervalType.equals(IntervalType.proportion_approx)||chosenIntervalType.equals(IntervalType.proportion_wald) || chosenIntervalType.equals(IntervalType.proportion_exact)){
		       // the cut off green line  CDF for lower
		        g.drawLine(leftGap + 20, scaleHeight(scaledProportion), r.width-105, scaleHeight(scaledProportion));
		        g.setColor(Color.black);
		        g.drawString(stringOfInterestLower + decimalFormat.format(proportion), r.width-100, scaleHeight(scaledProportion));
	        } else {
	        	
	        	g.drawLine(leftGap + 20, scaleHeight(parameterOfInterestLower), r.width-105, scaleHeight(parameterOfInterestLower));
		        g.setColor(Color.black);
		        g.drawString("CI_"+stringOfInterestLower+ decimalFormat.format(parameterOfInterestLower), r.width-100, scaleHeight(parameterOfInterestLower));
	        }
        }*/
        
     //   System.out.println("lowerPanel done drawing missedCount="+missedCount);
    }

    public int getMissedCount() {
    	// System.out.println("lowerPabel return missedCount="+missedCount);
        return missedCount;
    }


    private void drawData(double sampleData[][], int leftGap, int
            midHeight, int topGap, int xInterval, Graphics g) {

        int xPos = (8* leftGap + xInterval) / 4;
        int yPos;

        for (int trial = 0; trial < nTrials; trial++) {
            // 1. Draw the Data in upper
        	//g.setColor(Color.blue);
            xPos += xInterval;
            for (int i = 0; i < sampleSizes[trial]; i++) {
            	//System.out.println("sampleData["+trial+"]["+i+"]="+sampleData[trial][i]);
                yPos = scaleHeight(sampleData[trial][i]);
             //  g.drawLine(xPos - 2, yPos, xPos + 2, yPos);
            }

            xPos += xInterval / 2;

            double unscaledCIUpperbound = ciData[trial][0];
            double unscaledCILowerbound = ciData[trial][1];
            
            //System.out.println("unscaledCIUpperbound = ciData[trial][0]="+unscaledCIUpperbound);
            int scaledCIUpperbound = scaleHeight(unscaledCIUpperbound);
            int scaledCILowerbound = scaleHeight(unscaledCILowerbound);
            int x_bar=scaleHeight( xBar[trial]);
          //  System.out.println("xbar="+x_bar);
            if(x_bar<topGap)
            	x_bar=topGap;
            else if(x_bar>topGap + midHeight)
            	x_bar = topGap + midHeight;
            
            // 2. Draw the CI on lower
            g.setColor(Color.red);
            int redPos= xPos-xInterval/2;
            g.drawLine(redPos - 2, scaledCIUpperbound, redPos+2, scaledCIUpperbound);    // Top Line
            g.drawLine(redPos - 2, scaledCILowerbound, redPos+2, scaledCILowerbound); // Bottom Line
            g.drawLine(redPos, scaledCIUpperbound, redPos, scaledCILowerbound); // Main CI Line

            if (isOutsideTheInterval(unscaledCIUpperbound, unscaledCILowerbound))   // CI EXCLUDES the mean=0
            {
                missedCount++;
                missed[trial]=true;
                g.setColor(Color.green);
               // g.fillOval(redPos - MISSED_CIRCLE / 2, x_bar-MISSED_CIRCLE / 2, MISSED_CIRCLE, MISSED_CIRCLE);
         
            } else missed[trial]=false;
       
            // 3. Draw the separating vertical Lines on both canvas
            g.setColor(Color.green);
            g.drawLine(xPos + xInterval / 2, topGap, xPos + xInterval / 2, topGap + midHeight);

        }
          
    }
    
    public boolean[] getMissed() {
        return missed;
    }
    
    //Draws scale from the lowerBound to the upperBound divided into nIntervals
    private void drawVscale(int xpos, Graphics g) {
    	  // only print 2 decimal places.
    	decimalFormat.setMaximumFractionDigits(2);
	        
        g.setColor(Color.black);
        g.drawLine(xpos, topGap, xpos, r.height - bottomGap);     
        
        if(chosenIntervalType!=null){
	        //proportion
	        if (chosenIntervalType.equals(IntervalType.proportion_approx)||chosenIntervalType.equals(IntervalType.proportion_wald) ||chosenIntervalType.equals(IntervalType.proportion_exact)){
		        double intervalSize = (upperBound - lowerBound) / nIntervals;
		        double i = lowerBound;
		      		      
		        if(lowerBound!=upperBound) { // to prevent a infinite  loop
			        while (i <= upperBound) {     
			            g.drawString(percentFormat.format((i-lowerBound)/(upperBound-lowerBound)), xpos+6, scaleHeight(i));
			            i = i + intervalSize;
			        }
		    	}
	        } //mean
	     //   else  if (chosenIntervalType.equals(IntervalType.xbar_sigmaKnown)||chosenIntervalType.equals(IntervalType.xbar_sigmaUnknown)||chosenIntervalType.equals(IntervalType.asy_MLE)){
	        else  if (chosenIntervalType.equals(IntervalType.xbar_sigmaKnown)||chosenIntervalType.equals(IntervalType.xbar_sigmaUnknown)){
	        	double intervalSize = (upperBound - lowerBound) / nIntervals;
	 	        double i = lowerBound;
	 	      
	 	        if(lowerBound!=upperBound) { 
	 	    	   while (i <= upperBound) {
	 	    		   g.drawString(decimalFormat.format(i), xpos+6, scaleHeight(i));
	 	    		   i = i + intervalSize;
			        }
		    	}
	        }
	    	else if (chosenIntervalType.equals(IntervalType.sigma)||chosenIntervalType.equals(IntervalType.sigma2)){ //sigma
	    		 double intervalSize = (upperBound - lowerBound) / nIntervals;
			        double i = lowerBound;
			      
			        
			        if(lowerBound!=upperBound) { // to prevent a infinite  loop
				        while (i <= upperBound) {     
				            g.drawString(decimalFormat.format(i), xpos+6, scaleHeight(i));
				            i = i + intervalSize;
				        }
			    	}
	        }
        }
        else{//intervalType==null
        	double intervalSize = (upperBound - lowerBound) / nIntervals;
        	double i = lowerBound;
        	 if(lowerBound!=upperBound) { // to prevent a infinite  loop
     	        while (i <= upperBound) {
     	         
     	            g.drawString(decimalFormat.format(i), xpos+6, scaleHeight(i));
     	            i = i + intervalSize;
     	        }
         	}
        }

        // System.out.println("General CIcanvas drawVsacle + xpos=" + xpos + " lowerBound=" + lowerBound + "upperBound=" + upperBound + " decimalFormated upperBound" + decimalFormat.format(i));
 
    }
    
}
