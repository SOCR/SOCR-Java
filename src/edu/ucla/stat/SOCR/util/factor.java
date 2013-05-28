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

public class factor {
    private double[] factorValues; 
    private int elementsCount=0;
    private int factorCount = 0;
    private int[] factorLevels; 
   // private int[] factorLevelsCount;
    public  factor() {
    factorValues = new double[1];
    factorValues[0] = 0;
    setFactors();
    }
    
    public factor(double[] factorVals) {
    elementsCount = factorVals.length;
    factorValues = new double[elementsCount];
    System.arraycopy(factorVals, 0, factorValues, 0, factorVals.length);
    setFactors();
    
    }
    
    public void setFactors(double[] factorVals) {
      factorValues = new double[elementsCount];
    System.arraycopy(factorVals, 0, factorValues, 0, factorVals.length);
    setFactors();
    
    }
    
    
    private void setFactors() {
       
        int count=1;
        int clData =0;
        int[] distinctElements = new int[elementsCount];
        
        distinctElements[0] = (int)factorValues[0];
       factorValues[0] = 0;
        
        
        //  Double.parseDouble(table.getValueAt(0, column).toString());
        for(int i=1;i<elementsCount;i++) {
            // clData[i] = table.getValueAt(i, column);
            clData =  (int)factorValues[i];
            
            //Double.parseDouble(table.getValueAt(i, column).toString());
            int flag =0;
            for(int j=0; j < count; j++) {
                if(clData == distinctElements[j]) {
                    factorValues[i] = j;
                    flag = 1;
                    break;
                    
                }
                
            }
            if(flag ==0) {
                distinctElements[count] = (int)factorValues[i];
                factorValues[i] = count;
                //Double.parseDouble(table.getValueAt(i, column).toString());
                count++;
            }
            
        }
        
        factorCount = count;        
        factorLevels = new int[count];
        System.arraycopy(distinctElements, 0, factorLevels, 0, count);
        
            
        
        
    }
    
     public double[] meanFac(double[] dependant) {
     double[] returnMeans = new double[factorCount];
     double[][] sumCount = new double[factorCount][2];
     for(int i=0;i<dependant.length ;i++) {
     sumCount[(int)factorValues[i]][0] = sumCount[(int)factorValues[i]][0] + dependant[i];
     sumCount[(int)factorValues[i]][1]++;
     
     }
     
     for(int i=0;i<factorCount;i++)
         returnMeans[i] = sumCount[i][0]/sumCount[i][1];
     
     return returnMeans;
     }
     
    
    public double getFactorValues(int i) {
    return factorValues[i];
    
    }
    
    public int getElementsCount() {
    return elementsCount;
    }
    
    public int getFactorCount() {
    return factorCount;
    }
    
    public int getFactorLevels(int i) {
    return factorLevels[i];
    }
    
    
    

}

