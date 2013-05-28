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
package edu.ucla.stat.SOCR.experiments.util;

import edu.ucla.stat.SOCR.core.Distribution;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class ConfidenceCanvasGeneralBase extends JPanel {

    Dimension r;
    int nTrials;
    int n;
    // LAYOUT PARAMETERS
    int topGap = 10;
    int bottomGap = 10;
    int leftGap = 25;

    int midHeight;
    int missedCount = 0;
    int nIntervals = 8;


    double[] cvlist = {0.0, 1.28, 1.645, 1.96, 2.576, 3.29, 3.89};
    double[] halfAlpha = {0.25, 0.1, 0.05, 0.025, 0.005, 0.0005, 0.00005};

    int cvIndex;
    double cv;
    static final int MISSED_CIRCLE = 10;    // Significant Elipses are reported at CI that

    double[][] ciData;
    double[][] sampleData;
    double[] xBar;
    double upperBound = 200.0; // max value in sample data
    double lowerBound = -200.0;// min value in sample data
   
    
    double parameterOfInterestUpper = 0.0;
    double parameterOfInterestUpper2 = 0.0; // for proportion, left cutoff
    double parameterOfInterestLower = 0.0;
 
    String stringOfInterestLower= "";
    String stringOfInterestUpper= "";
    double scaledLeftCDF= 0.0,  scaledRightCDF= 0.0;;
    protected double proportion;
    double scaledProportion= 0.0;
    
    protected Distribution chosenDistribution;
    IntervalType chosenIntervalType;
    protected double leftCutOffValue, rightCutOffValue;  
    protected double leftCDFValue, rightCDFValue;  
    protected double dist_meanValue, ci_meanValue;
    protected double dist_SDValue, ci_VarValue;
    
    DecimalFormat decimalFormat = new DecimalFormat();
    NumberFormat percentFormat = NumberFormat.getPercentInstance();

    /**
     * Constructor for ConfidenceCanvas
     */
    public ConfidenceCanvasGeneralBase(int n, int nTrials) {
    	setSampleSizeAndNTrials(n, nTrials);       
    }
    
    public void setSampleSizeAndNTrials(int _n, int _nTrials) {
        if (_n >= 1) this.n = _n;
        	else this.n = 1;
        if (_nTrials >= 1) this.nTrials = _nTrials;
        	else this.nTrials = 1;
    }
    
/*
   public Dimension getPreferredSize() {
        return new Dimension(150, 125);
    }

    public Dimension getMinimumSize() {
        return new Dimension(30, 25);
    }*/

 
    public int getMissedCount() {
        return missedCount;
    }
    
 /*   protected boolean isOutsideTheInterval(double unscaledCIUpperbound, double unscaledCILowerbound) {
        boolean outsideInterval = false;
        if (intervalType.equals(IntervalType.sigma)) {
            if (parameterOfInterest < unscaledCILowerbound) {
                outsideInterval = true;
            }
        } else {
            if (parameterOfInterest > unscaledCIUpperbound || parameterOfInterest < unscaledCILowerbound) {
                outsideInterval = true;
            }
        }
        return outsideInterval;
    }*/
    
    protected boolean isOutsideTheInterval(double scaledCIUpperbound, double scaledCILowerbound) {
        boolean outsideInterval = false;
        if (chosenIntervalType.equals(IntervalType.sigma)||chosenIntervalType.equals(IntervalType.sigma2)) {
            if (ci_VarValue > scaledCIUpperbound || ci_VarValue < scaledCILowerbound) {
                outsideInterval = true;
            }
        } else  if (chosenIntervalType.equals(IntervalType.proportion_approx) || chosenIntervalType.equals(IntervalType.proportion_wald) ||chosenIntervalType.equals(IntervalType.proportion_exact)){
          
        	if (scaledProportion > scaledCIUpperbound || scaledProportion < scaledCILowerbound) {
                outsideInterval = true;
            }
        }else{
        	if (ci_meanValue > scaledCIUpperbound || ci_meanValue < scaledCILowerbound) {
                outsideInterval = true;
            }
        }
        return outsideInterval;
    }
    /*
    protected boolean isOutsideTheInterval(double unscaledCIUpperbound, double unscaledCILowerbound) {
        boolean outsideInterval = false;
        if (intervalType.equals(IntervalType.sigma)) {
            if (CDF < unscaledCILowerbound) {
                outsideInterval = true;
            }
        } else {
            if (CDF > unscaledCIUpperbound || CDF < unscaledCILowerbound) {
                outsideInterval = true;
            }
        }
        return outsideInterval;
    }*/
    
    protected double getProportion() {

    /*	if (chosenDistribution.getType()==edu.ucla.stat.SOCR.core.Distribution.DISCRETE) {
    		this.leftCDF = chosenDistribution.getCDF(leftCutOffValue-0.6);
    		this.rightCDF = chosenDistribution.getCDF(rightCutOffValue); 
    	} else {
    		this.leftCDF =chosenDistribution.getCDF(leftCutOffValue); 
    		this.rightCDF = chosenDistribution.getCDF(rightCutOffValue); 
    	}*/

        proportion = 1-rightCDFValue-leftCDFValue;
        this.scaledProportion = proportion*(upperBound-lowerBound)+lowerBound; 
        return proportion;
    }
  
    
    ////--------------
  /*  protected double getParameterOfInterest() {
        parameterOfInterest = distribution.getMean();
        if (intervalType.equals(IntervalType.sigma)) {
            parameterOfInterest = Math.sqrt(distribution.getVariance());
        } else if (intervalType.equals(IntervalType.proportion_approx) || intervalType.equals(IntervalType.proportion_exact)) {
            parameterOfInterest = proportionValue;
        }
        return parameterOfInterest;
    }*/
    
    protected void setParameterOfInterest() {
        parameterOfInterestUpper = dist_meanValue;
        parameterOfInterestLower = ci_meanValue;
        stringOfInterestLower = "Mean:";
        stringOfInterestUpper = "Mean:";
        if (chosenIntervalType.equals(IntervalType.sigma)||chosenIntervalType.equals(IntervalType.sigma2)) {
           // parameterOfInterestUpper = dist_SDValue;
            parameterOfInterestLower = ci_VarValue;
            stringOfInterestLower = "Var: ";
            stringOfInterestUpper = "Var: ";
        } else if (chosenIntervalType.equals(IntervalType.proportion_approx) || chosenIntervalType.equals(IntervalType.proportion_wald) || chosenIntervalType.equals(IntervalType.proportion_exact)) {
            parameterOfInterestUpper = leftCutOffValue;
            parameterOfInterestUpper2 = rightCutOffValue;
            getProportion();
            stringOfInterestUpper = "CutOff:";
            stringOfInterestLower = "CDF:";
        }else if (chosenIntervalType.equals(IntervalType.asy_MLE)){
        	 parameterOfInterestUpper = dist_meanValue;
             parameterOfInterestLower = ci_meanValue;
             stringOfInterestUpper = "Mean:";
             stringOfInterestLower = "Lambda:";
        }
    }
    
    protected static List convertTo1DArray(double[][] twoDArray) {
        List oneDArrayList = new ArrayList();
        for (int i = 0; i < twoDArray.length; i++) {
            for (int j = 0; j < twoDArray[i].length; j++) {
                oneDArrayList.add(new Double(twoDArray[i][j]));
            }
        }
        return oneDArrayList;
    }

    public static double getMaxValueFrom2DArray(double[][] dataArray) {
        Double maximum = (Double) Collections.max(convertTo1DArray(dataArray));
        return maximum;
    }

    public static double getMinValueFrom2DArray(double[][] dataArray) {
        Double minimum = (Double) Collections.min(convertTo1DArray(dataArray));
        return minimum;
    }

    protected int scaleHeight(double x) {
        double dataRange = upperBound - lowerBound;
        int sh = (int) Math.round((double) midHeight * (upperBound - x) / dataRange) + topGap;
        return sh;
    }

    protected void setRange(){
		if (sampleData != null && chosenDistribution!=null) {
	        // get upper and lower bounds of the sample data
	    	double lb_sampleData = getMinValueFrom2DArray(sampleData);
	        double ub_sampleData = getMaxValueFrom2DArray(sampleData);
	        double ub_ciData = getMaxValueFrom2DArray(ciData);
	        double lb_ciData = getMinValueFrom2DArray(ciData);
	        lowerBound = Math.min(lb_sampleData, lb_ciData);
	        upperBound = Math.max(ub_sampleData, ub_ciData);
	        
	     /*   lowerBound = chosenDistribution.inverseCDF(.01);
	    	upperBound = chosenDistribution.inverseCDF(.99);*/
		}
    }

    /**
     * clear clears canvas and resets parameters
     *
     * @param n
     * @param nTrials
     */
    public void clear(int n, int nTrials, Distribution dist) {
        setSampleSizeAndNTrials(n, nTrials);
        setDistribution(dist);
        setParameterOfInterest(); //set stringOfInterestLower etc
        this.sampleData = null;
        this.ciData = null;
        this.xBar= null;
        repaint();
    }


    /* modified by rahul gidwani */

    public void update(int cvIndex, double[][] ciData, double[][] sampleData, double[] xBar) {
        this.cvIndex = cvIndex;
        this.ciData = ciData;
        this.sampleData = sampleData;
        this.xBar= xBar;
     //   paintComponent(this.getGraphics());
    }

    public void setIntervalType(IntervalType type) {
        this.chosenIntervalType = type;
    }

    public void setDistribution(Distribution dist) {
    //	System.out.println("Canvas setDistribution get called reset mean etc");
        this.chosenDistribution = dist;
        this.dist_meanValue = dist.getMean();
        this.parameterOfInterestUpper = dist_meanValue;
        this.dist_SDValue = dist.getSD();
    }

    public void setCutOffValue(double left, double right) {
        this.leftCutOffValue = left;
        this.rightCutOffValue = right;
        setParameterOfInterest();
    }
    
    public void setCDFValue(double left_cdf, double right_cdf) {      
    	this.leftCDFValue = left_cdf;
    	this.rightCDFValue = right_cdf;
    	setParameterOfInterest();
    }
    
    public void setCIMeanValue(double value) {
    	//System.out.println("setting ci_mean="+value);
        this.ci_meanValue = value;
    }
    
    public void setCIVarValue(double value) {
        this.ci_VarValue =value;
    }
}
