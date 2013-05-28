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

package edu.ucla.stat.SOCR.experiments.util;

import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.core.ValueSetter;
import edu.ucla.stat.SOCR.distributions.ChiSquareDistribution;
import edu.ucla.stat.SOCR.distributions.NormalDistribution;
import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import edu.ucla.stat.SOCR.distributions.FisherDistribution;

/**
Rahul Gidwani - UCLA Statistics
 */

public enum SimulationResampleType{

    xbar_sigmaKnown {
    	
        public String toString() {
        	//System.out.println("IntervalType toString bootStrap"+bootStrap);
        	if(bootStrap)
        		return "Mean - BootStrap";
        	else
        		return "Mean ";
        }

       
    },


  /*  xbar_sigmaUnknown {
    	
    	
        public String toString() {
        	if(bootStrap)
        		return "";
        	else
        		return "Mean - Population Variance Unknown";
        }

      
    },*/

    proportion_wald {
       
        public String toString() {
        	if(bootStrap)
        		return "Proportion - BootStrap";
        	else
        		return "Proportion - Normal Approximation";
        }

    
    },


  /*  proportion_approx {
    	
        public String toString() {
	       if(bootStrap)
	    	    return "";
	       else return "Proportion - Quadratic";
        }

     

    },
    
    proportion_exact {
    	
        public String toString() {
        	if(bootStrap)
	    	    return "";
        	else 
        		return "Proportion - Exact";
        }
 
      
    },*/
    
    sigma {
    
        public String toString() {
        	if(bootStrap)
	    	    return "Variance - BootStrap";
        	else 
        		return "Variance ";
        }


    };
    
  /*  sigma2 {
    	
        public String toString() {
        	if(bootStrap)
	    	    return "";
        	else
        	return "Variance - Asymptotically Distribution Free";
        }

       
    },*/
    


    boolean bootStrap = false;
    public abstract String toString();
   
    
}
