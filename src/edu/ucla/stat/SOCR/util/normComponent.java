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

public class normComponent {
       
    //Variables decleration
	private double mean=0;
   	private double stdev=1;
   	private double weight;
   	private edu.ucla.stat.SOCR.distributions.NormalDistribution norm;
        
    public normComponent(double mn, double stv, double wt) {
        updateComponent(mn, stv, wt);
    }
    
    public double getDensity(double x) {
    	return weight* norm.getDensity(x);
    }
    
    public double getMean() {
    	return norm.getMean();
    }
    
    public double getVariance() {
    	return norm.getVariance();
    }

    public double getWeight() {
    	return weight;
    }
    
    public void updateComponent(double mn, double stv, double wt) {
    	mean = mn;
        stdev = stv;
        weight = wt;
        norm = new edu.ucla.stat.SOCR.distributions.NormalDistribution(mean, stdev);
    }
}