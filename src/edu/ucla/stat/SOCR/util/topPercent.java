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
import javax.swing.*;
import java.awt.*;
/******************************************
* selects the top x% of an array
*****************************************/


public class topPercent{
   float[] returnArray,ind,tempArray,absVector; 
    public topPercent() {
    }
    
    public topPercent(double[] ipArray,double prct) {
    float[] tpArray = new float[ipArray.length];
    float tpPt = (float) prct;
        for(int i=0;i<ipArray.length;i++)
           tpArray[i] = (float)ipArray[i] ;
    estimatePercent(tpArray,tpPt);
    
    for(int i=0;i<ipArray.length;i++)
           ipArray[i] = (double)tpArray[i] ;
    
    
    }

    public topPercent(float[] tpArray,float tpPt) {
      estimatePercent(tpArray,tpPt);  
    }

    public void estimatePercent(float[] ipArray,float prct) {
        absVector = new float[ipArray.length];
        returnArray = new float[ipArray.length];
        ind = new float[ipArray.length];
       
        for(int i=0;i<ipArray.length;i++) {
            ind[i] = i;
            absVector[i] = Math.abs(ipArray[i]);
        }
        sortAsc(absVector,ipArray, ind);
        
        int cutOff = (int) (ipArray.length*(1-prct/100));
        for(int i=0;i<cutOff;i++)
            ipArray[i] = 0;
        
        resortArray(ipArray,ind);
    
    }
    

    private void sortAsc(float[] abs, float[] ip,float[] id) {
    
         int g,r;
            float c;
            int n = id.length;
            for ( r=0; r <n-1; r++) {
                for ( g=r+1;g<n;g++) {
                    if ( abs[r]  > abs[g] ) {
                        c = abs[r];
                        abs[r] = abs[g];
                        abs[g] = c;
                        
                        c=ip[r];                // these 3 statements swap values
                        ip[r] = ip[g];          // in the 2 cells being compared
                        ip[g] = c;
                        
                        c = id[r];
                        id[r] = id[g];
                        id[g] = c;
                        
                    }
                }
            }
    
    }
    
    private void resortArray(float[] ip,float[] id) {
     tempArray = new float[ip.length];
        for(int i=0;i<ip.length;i++)
            tempArray[(int)id[i]] = ip[i];
    
     
     
     for(int i=0;i<ip.length;i++) {
         ip[i] = tempArray[i];   
         ind[i] = i;
     }
     
    
    }
    
    
    

}

