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
/* Author - Dushyanth Krishnamurthy April 14th 2004
This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 2 of the License, or (at your option)
any later version.

This program is distributed in the hope that it will be useful, but without
any warranty; without even the implied warranty of merchantability or
fitness for a particular purpose. See the GNU General Public License for
more details. http://www.gnu.org/copyleft/gpl.html */

package edu.ucla.stat.SOCR.util;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class Anova {
    int FACTORS = 1;
    public int reps;
    public static final int A=1,B=2,C=3,AB=4,BC=5,AC=6,ABC=7;
    double[] response;
    private double[] dummyFactor;
    public factor factorA,factorB,factorC;
    private int[][][] cells;
    private double grandMean=0;
    // private int[] factorLevelsCount;
    
    
    public  Anova() {
    
    
    }
    
    //one way anova
    public Anova(double[] resp, double[] factor1) {
    FACTORS=1;
    factorA = new factor(factor1);
    
    //factorA.setFactors(factor1);
    //factorB.setFactors(factor2);
    dummyFactor  = new double[factorA.getElementsCount()];
    //factorC.setFactors(dummyFactor);
    factorB = new factor(dummyFactor);
    factorC = new factor(dummyFactor);
    response = new double[resp.length];
    System.arraycopy(resp, 0, response, 0, resp.length);
    setFactorCounts();
    setGrandMean();
    authenticateFactors();
    }
    
    //two way anova
    public Anova(double[] resp, double[] factor1, double[] factor2) {
    FACTORS=2;
    factorA = new factor(factor1);
    factorB = new factor(factor2);
    //factorA.setFactors(factor1);
    //factorB.setFactors(factor2);
    dummyFactor  = new double[factorA.getElementsCount()];
    //factorC.setFactors(dummyFactor);
    factorC = new factor(dummyFactor);
    response = new double[resp.length];
    System.arraycopy(resp, 0, response, 0, resp.length);
    setFactorCounts();
    setGrandMean();
    authenticateFactors();
    }
    
    // three way anova
    public Anova(double[] resp, double[] factor1, double[] factor2, double[] factor3) {
    FACTORS = 3;
    factorA = new factor(factor1);
    factorB = new factor(factor2);
    factorC = new factor(factor3);
    factorA.setFactors(factor1);
    factorB.setFactors(factor2);
    factorC.setFactors(factor3);
    response = new double[resp.length];
    System.arraycopy(resp, 0, response, 0, resp.length);
    setFactorCounts();
    setGrandMean();
    authenticateFactors();
    }
    
   
    public void setFactorCounts() {
    cells = new int[factorA.getFactorCount() ][factorB.getFactorCount()][factorC.getFactorCount()];
    
        for (int i=0;i< response.length;i++)
        cells[(int)factorA.getFactorValues(i)][(int)factorB.getFactorValues(i)][(int)factorC.getFactorValues(i)]++;
        
    
    }
    
    
    public boolean authenticateFactors() {
    // checks whether there are any missing factors
        
        
        if(response.length<factorA.getFactorCount()*factorB.getFactorCount()*factorC.getFactorCount())
            return false;
        int flag = 0 ;
        reps = cells[0][0][0];
        for(int i=0; i< factorA.getFactorCount();i++ ) {
            for(int j = 0; j< factorB.getFactorCount();j++ ){
                for(int k=0; k<factorC.getFactorCount();k++) {
                if(cells[i][j][k] == 0) {
                    flag=1;
                    break;                
                    }
               if(cells[i][j][k] != reps) {
                    flag=1;
                    break;                
                    }
               
                }
            }  
        }
        
        if(flag ==1)
            return false;
        
       return true;
    
    }
    
    public double SSI(int fac) {
        double[] meanVec;
        double returnSS=0;
        switch(fac) {
               case A:
                   meanVec = factorA.meanFac(response);
                   for(int i=0;i<factorA.getFactorCount();i++)
                   returnSS = returnSS + Math.pow(meanVec[i]-grandMean,2);
                   returnSS = returnSS*factorB.getFactorCount()*factorC.getFactorCount()*reps;
                   break;
               case B:
                   meanVec = factorB.meanFac(response);
                   for(int i=0;i<factorB.getFactorCount();i++)
                   returnSS = returnSS + Math.pow(meanVec[i]-grandMean,2);
                   returnSS = returnSS*factorA.getFactorCount()*factorC.getFactorCount()*reps;
                   break;
               case C:
                   meanVec = factorC.meanFac(response);
                   for(int i=0;i<factorC.getFactorCount();i++)
                   returnSS = returnSS + Math.pow(meanVec[i]-grandMean,2);
                   returnSS = returnSS*factorB.getFactorCount()*factorA.getFactorCount()*reps;
                    break;
                   
               default:
                   meanVec = factorA.meanFac(response);
                   for(int i=0;i<factorA.getFactorCount();i++)
                   returnSS = returnSS + Math.pow(meanVec[i]-grandMean,2);
                   returnSS = returnSS*factorB.getFactorCount()*factorB.getFactorCount()*reps;
                   break;
    
           }
           
        
    return returnSS;
    }
    
    public double SSIJ(int fac) {
        double returnSS=0;   
        double[] meanVecI,meanVecJ,meanVecK;
        double[][] meanVecIJ, meanVecJK, meanVecIK;
        
        switch(fac) {
            case AB:
            meanVecI = factorA.meanFac(response);
            meanVecJ = factorB.meanFac(response);
            meanVecIJ = IJMean(factorA,factorB, response);
            for(int i=0;i<factorA.getFactorCount();i++)
                for(int j=0;j<factorB.getFactorCount();j++)
                returnSS = returnSS + Math.pow(meanVecIJ[i][j] - meanVecI[i] - meanVecJ[j] + grandMean ,2);              
                returnSS = returnSS*factorC.getFactorCount()*reps;
            break;
            
            case BC:
            meanVecJ = factorB.meanFac(response);
            meanVecK = factorC.meanFac(response);
            meanVecJK = IJMean(factorB,factorC, response);
            for(int i=0;i<factorB.getFactorCount();i++)
                for(int j=0;j<factorC.getFactorCount();j++)
                returnSS = returnSS + Math.pow(meanVecJK[i][j] - meanVecJ[i] - meanVecK[j] + grandMean ,2);              
                returnSS = returnSS*factorA.getFactorCount()*reps;
            break;
            
            case AC:
            meanVecI = factorA.meanFac(response);
            meanVecK = factorC.meanFac(response);
            meanVecIK = IJMean(factorA,factorC, response);
            for(int i=0;i<factorA.getFactorCount();i++)
                for(int j=0;j<factorC.getFactorCount();j++)
                returnSS = returnSS + Math.pow(meanVecIK[i][j] - meanVecI[i] - meanVecK[j] + grandMean ,2);              
                returnSS = returnSS*factorB.getFactorCount()*reps;    
                
            break;
            
            default:
            meanVecI = factorA.meanFac(response);
            meanVecJ = factorB.meanFac(response);
            meanVecIJ = IJMean(factorA,factorB, response);
            for(int i=0;i<factorA.getFactorCount();i++)
                for(int j=0;j<factorB.getFactorCount();j++)
                returnSS = returnSS + Math.pow(meanVecIJ[i][j] - meanVecI[i] - meanVecJ[j] + grandMean ,2);              
                returnSS = returnSS*factorC.getFactorCount()*reps;
            break;
                
            
            
             
        }
    
    
        
     return returnSS;  
    }
    
    public double SSIJK() {
    double meanSum = 0;
    double[] meanI,meanJ,meanK;
    double[][] meanIJ,meanIK,meanJK;
    double[][][] meanijk;
    meanI = factorA.meanFac(this.response); 
    meanJ = factorB.meanFac(this.response);
    meanK = factorC.meanFac(this.response);
    meanIJ = IJMean(factorA,factorB, response);
    meanJK = IJMean(factorB,factorC, response);
    meanIK = IJMean(factorA,factorC, response);
    meanijk = meanIJK();
    double sum = 0;
    for(int i=0;i<response.length;i++) {
        sum = meanijk[(int)factorA.getFactorValues(i)][(int)factorB.getFactorValues(i)][(int)factorC.getFactorValues(i)] - meanIJ[(int)factorA.getFactorValues(i)][(int)factorB.getFactorValues(i)] - meanJK[(int)factorB.getFactorValues(i)][(int)factorC.getFactorValues(i)] - meanIK[(int)factorA.getFactorValues(i)][(int)factorC.getFactorValues(i)] + meanI[(int)factorA.getFactorValues(i)] + meanJ[(int)factorB.getFactorValues(i)] + meanK[(int)factorC.getFactorValues(i)] -  grandMean;
       meanSum = meanSum + Math.pow(sum,2);
    }
    return meanSum;
    }
    
    public double SSE() {
     double meanSum=0;
     double[] meanI, meanJ;
        switch(FACTORS) {
       
        case 1:
        meanSum = 0;
        meanI = this.factorA.meanFac(this.response);    
           
        for(int i=0;i<response.length;i++)
              meanSum = meanSum + Math.pow(response[i] - meanI[(int)factorA.getFactorValues(i)],2);  
         
            break;
        case 2:
        meanSum = 0;
        meanI = this.factorA.meanFac(response);   
        meanJ = this.factorB.meanFac(response);
        for(int i=0;i<response.length;i++)
            meanSum = meanSum + Math.pow(response[i] - meanI[(int)factorA.getFactorValues(i)]  - meanJ[(int)factorB.getFactorValues(i)] + grandMean,2);
            break;
        
        case 3:
        meanSum = 0;        
        if(reps>1) {
        double[][][] meanijk = meanIJK();
        
        for(int i=0;i<response.length;i++)
            meanSum = meanSum + Math.pow(response[i] - meanijk[(int)factorA.getFactorValues(i)][(int)factorB.getFactorValues(i)][(int)factorC.getFactorValues(i)],2);
        
        }else {
        meanSum = SSIJK();
        }
        
            break;
                
        
        default:
            break;
            
        
    
    }
    
    
        
        
    return meanSum;
    }
    
    public double[][][] meanIJK() {
    double[][][] sumIJK = new double[factorA.getFactorCount() ][factorB.getFactorCount()][factorC.getFactorCount()];
    double[][][] meanVal = new double[factorA.getFactorCount() ][factorB.getFactorCount()][factorC.getFactorCount()];
        for (int i=0;i< response.length;i++)
        sumIJK[(int)factorA.getFactorValues(i)][(int)factorB.getFactorValues(i)][(int)factorC.getFactorValues(i)] = sumIJK[(int)factorA.getFactorValues(i)][(int)factorB.getFactorValues(i)][(int)factorC.getFactorValues(i)] + response[i];
    
    for(int i=0;i< factorA.getFactorCount();i++)
       for(int j=0;j< factorB.getFactorCount();j++)
           for(int k=0;k< factorC.getFactorCount();k++)
              sumIJK[i][j][k] =   sumIJK[i][j][k]/reps;
    
    return meanVal;
    }
    
    
    public void setGrandMean() {
    double sum = 0;
    
    for(int i=0;i< response.length;i++)
    sum = sum + response[i];
    
    grandMean = sum/response.length;
    }
    
    public double[][] IJMean(factor Atemp, factor Btemp, double[] dependantTemp) {
    //estimates the ijth means if the factors Atemp and Btemo
    //This function is called by SSIJ()
    double[][][] factorMeanSum = new double[Atemp.getFactorCount()][Btemp.getFactorCount()][2];
    double[][] factorMean = new double[Atemp.getFactorCount()][Btemp.getFactorCount()];
    for(int i=0;i<dependantTemp.length;i++) {
        factorMeanSum[(int)Atemp.getFactorValues(i)][(int)Btemp.getFactorValues(i)][0] = factorMeanSum[(int)Atemp.getFactorValues(i)][(int)Btemp.getFactorValues(i)][0] +dependantTemp[i];
        factorMeanSum[(int)Atemp.getFactorValues(i)][(int)Btemp.getFactorValues(i)][1]++; 

    }
    
        for(int i=0;i<Atemp.getFactorCount();i++)   
            for(int j=0;j<Btemp.getFactorCount();j++)
               factorMean[i][j] = factorMeanSum[i][j][0]/factorMeanSum[i][j][1];
           
       return factorMean;  
    }
    
    public int getFACTORS() {
    return FACTORS;
    }
    
    
   
    
    }
    
    
    
    




