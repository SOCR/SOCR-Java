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
import edu.ucla.stat.SOCR.distributions.DiscreteUniformDistribution;
import edu.ucla.stat.SOCR.distributions.NormalDistribution;
import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import edu.ucla.stat.SOCR.distributions.FisherDistribution;


public class BootStrap {
	public static final int defaultSampleSize = 1000;
	 private double[][] sampleData;
	 private Distribution distribution;
	 private int nTrials;
	 private int sampleSize;
	 private int cvIndex;
	 
	 double[][] bootStrapData;
	 int[][] bootStrapDataIndex;
	 private int bootStrapSize = defaultSampleSize ;
	 
	 public BootStrap(){

	 }
	 public BootStrap(int n){
		 bootStrapSize=n;
	 }
	 
	 public double[][] getBootStrapData(double[] sampleData, int sampleSize){
		 //System.out.println("BootStrap size="+bootStrapSize);
		 bootStrapData = new double[bootStrapSize][sampleSize];
		 bootStrapDataIndex = new int[bootStrapSize][sampleSize];
		 
		 DiscreteUniformDistribution dist = new DiscreteUniformDistribution(1, sampleSize, 1);
		 for(int j=0; j<sampleSize; j++ ){
			 bootStrapData[0][j]= sampleData[j];
			 bootStrapDataIndex[0][j] =j;
		 }
		 
		 for (int i=1; i<bootStrapSize; i++)
			 for(int j=0; j<sampleSize; j++ ){
				 	bootStrapDataIndex[i][j]= (int)dist.simulate()-1;
				 	bootStrapData[i][j]= sampleData[bootStrapDataIndex[i][j]];
				 	
				 	// System.out.println(bootStrapData[i][j] +" index"+ bootStrapDataIndex[i][j]);
			 }
		 
		/* double[] data = new double[bootStrapSize*sampleSize];
		 int k =0;
		 for (int i=0; i<bootStrapSize; i++)
    		 for (int j=0; j<sampleSize; j++){
    			 data[k]=bootStrapData[i][j];
    			// if (k%100==0)
    			//	System.out.println("data["+k+"]"+ data[k]);
    			 k++;
    		 }
    		  return data;
				 */
		return bootStrapData;
	 }
	 
	 public int[][] getBootStrapDataIndex(){
		 return bootStrapDataIndex;
	 }
	 
	 // i, bootStrapSize, j, sampleSize
	 public int getBootStrapDataIndex(int i, int j){
		 return bootStrapDataIndex[i][j];
	 }
	 
	 public int[] getBootStrapDataIndex(int i){
		 return bootStrapDataIndex[i];
	 }
	 public void setBootStrapSize(int n){
		
		 bootStrapSize=n;
		// System.out.println("BootStrap setsize="+bootStrapSize);
	 }
	 
	 public int getBootStrapSize(){
		 return bootStrapSize;
	 }
}
 